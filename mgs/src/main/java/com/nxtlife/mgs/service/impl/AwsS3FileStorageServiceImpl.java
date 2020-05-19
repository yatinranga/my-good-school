package com.nxtlife.mgs.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.nxtlife.mgs.config.Aws;
import com.nxtlife.mgs.config.FileStorageProperties;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.UploadException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.service.FileStorageService;

@Aws
// @Profile(value = "default")
@Service
public class AwsS3FileStorageServiceImpl<T> implements FileStorageService<T> {

	private static final Logger logger = LoggerFactory.getLogger(AwsS3FileStorageServiceImpl.class);

	@Value("${mgs.s3.bucket}")
	private String s3Bucket;

	@Value("${mgs.aws.accessKeyId}")
	private String accessKey;

	@Value("${mgs.aws.secretKey}")
	private String secretKey;

	private AmazonS3 s3Client;

	private final Path fileStorageLocation;
	private final String supportedExtension;
	private final String imageSupportedExtension;

	@Autowired
	public AwsS3FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		this.supportedExtension = fileStorageProperties.getSupportedExtension();
		this.imageSupportedExtension = fileStorageProperties.getImageSupportedExtension();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new UploadException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	private String getFileExtension(String fileOriginalName, String category) {
		String fileExtension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
		if (!category.equalsIgnoreCase("image") && !supportedExtension.contains(fileExtension)) {
			throw new ValidationException(String.format("this formate[%s] not supported", fileExtension));
		}
		if (category.equalsIgnoreCase("image") && !imageSupportedExtension.contains(fileExtension)) {
			throw new ValidationException(String.format("this formate[%s] not supported", fileExtension));
		}
		return fileExtension;
	}

	private Resource getResourceFile(String filename) {
		try {
			Path filePath = this.fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new NotFoundException("File not found " + filename);
			}
		} catch (MalformedURLException ex) {
			throw new NotFoundException("File not found " + filename, ex);
		}
	}

	@PostConstruct
	public void init() {
		s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.withRegion("us-east-2").build();
	}

	@Override
	public Resource loadFileAsResource(String filepath) throws IOException {
		logger.info("Fetching {}", filepath);
		String tempFilename = fileStorageLocation.normalize() + UUID.randomUUID().toString()
				+ filepath.substring(filepath.lastIndexOf("."));
		S3Object object;
		object = s3Client.getObject(new GetObjectRequest(s3Bucket, filepath));
		InputStream reader = new BufferedInputStream(object.getObjectContent());
		FileOutputStream out = new FileOutputStream(tempFilename);
		IOUtils.copy(reader, out);
		out.flush();
		reader.close();
		return getResourceFile(tempFilename);
	}

	@Override
	public byte[] loadFileAsByte(String filepath) throws IOException {
		logger.info("Fetching {}", filepath);
		S3Object object;
		object = s3Client.getObject(new GetObjectRequest(s3Bucket, filepath));
		InputStream reader = new BufferedInputStream(object.getObjectContent());
		byte[] imageBytes = org.apache.commons.io.IOUtils.toByteArray(reader);
		reader.close();
		return imageBytes;
	}

	@Override
	public String storeFile(T file, String filename, String filepath, Boolean changeFileName, Boolean image) {
		String tFilename = null;
		String fileExtension = getFileExtension(filename, image ? "image" : "other-document");

		ObjectMetadata objectMetadata = new ObjectMetadata();
		PutObjectRequest putObjectRequest;
		try {
			filename = fileExtension.toUpperCase() + filepath + filename;
			if (changeFileName != null && changeFileName) {
				tFilename = fileExtension.toUpperCase() + filepath + UUID.randomUUID().toString() + fileExtension;
			}
			if (file instanceof MultipartFile) {
				objectMetadata.setContentLength(((MultipartFile) file).getSize());
				putObjectRequest = new PutObjectRequest(s3Bucket, changeFileName ? tFilename : filename,
						((MultipartFile) file).getInputStream(), objectMetadata);
			} else if (file instanceof Resource) {
				objectMetadata.setContentLength(((Resource) file).contentLength());
				putObjectRequest = new PutObjectRequest(s3Bucket, changeFileName ? tFilename : filename,
						((Resource) file).getInputStream(), objectMetadata);
			} else if (file instanceof Document) {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer;
				transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				DOMSource source = new DOMSource((Document) file);
				File tfile = new File(
						fileStorageLocation.normalize().toString() + (changeFileName ? tFilename : filename));
				StreamResult result = new StreamResult(tfile);
				transformer.transform(source, result);
				InputStream is = new FileInputStream(tfile);
				putObjectRequest = new PutObjectRequest(s3Bucket, changeFileName ? tFilename : filename, is,
						objectMetadata);
				is.close();
				tfile.delete();
			} else {
				throw new RuntimeException(
						String.format("For this type of file (%s) implementation not found", file.getClass()));
			}
			s3Client.putObject(putObjectRequest);
			logger.info("Object put in s3 bucket successfully");
			logger.info("1.S3Bucket {}, Filename {}, tFilename {}", s3Bucket, filename, tFilename);
			// URL url = s3Client.getUrl(s3Bucket, changeFileName ? tFilename :
			// filename);
			return changeFileName ? tFilename : filename;
		} catch (Exception ex) {
			logger.error("S3Bucket error : {}", ex.getLocalizedMessage());
			ex.printStackTrace();
			throw new ValidationException("File can't store in s3 bucket");
		}

	}

	@Override
	public boolean isExist(String filename) {
		try {
			s3Client.getObjectMetadata(s3Bucket, filename);
		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				// bucket/key does not exist
				return false;
			} else {
				throw e;
			}
		}
		return true;
	}

	@Override
	public void delete(String filepath) {
		s3Client.deleteObject(new DeleteObjectRequest(s3Bucket, filepath));
	}

}
