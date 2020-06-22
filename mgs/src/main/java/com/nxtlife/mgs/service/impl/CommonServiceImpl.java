package com.nxtlife.mgs.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.glassfish.jersey.server.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.CommonService;
import com.nxtlife.mgs.service.FileStorageService;
//import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.util.Utils;

@Service
public class CommonServiceImpl extends BaseService implements CommonService {

	private static Logger logger = LoggerFactory.getLogger(CommonService.class);
	//	@Autowired
	//	FileService fileService;

	@Autowired
	private FileStorageService<MultipartFile> fileStorageService;


	//	@Override
	public ResponseEntity<Resource> downloadFile(String filePath) {
		//		FileInputStream fin;
		//		try {
		//			fin = new FileInputStream(filePath);
		//			System.out.println(filePath);
		//			System.out.println(fin);

		// return IO ByteArray(in);
		// headers.add("fileName"," Incidents.xls");

		Resource resource = null;
		try {
			resource = fileStorageService.loadFileAsResource(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Couldn't able to convert file {} into resource", filePath);
		}
		if(resource == null)
			throw new ValidationException(String.format("File with path (%s) not found.",filePath));

		HttpHeaders headers = new HttpHeaders();
		// set filename in header
//		headers.add("Content-Disposition", "attachment; filename= mgs_file_" + Utils.generateRandomAlphaNumString(4)  + filePath.substring(filePath.lastIndexOf(".")));
		headers.add("Content-Disposition", "attachment; filename="+resource.getFilename());

		return ResponseEntity.ok().headers(headers).body(resource);

		//		} catch (FileNotFoundException e ) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//			throw new RuntimeException("IOError writing file to output stream or file not found");
		//		}
	}
}
