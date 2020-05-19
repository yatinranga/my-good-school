//package com.nxtlife.mgs.service.impl;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import com.nxtlife.mgs.entity.activity.File;
//import com.nxtlife.mgs.ex.NotFoundException;
//import com.nxtlife.mgs.ex.ValidationException;
//import com.nxtlife.mgs.jpa.FileRepository;
//import com.nxtlife.mgs.service.BaseService;
//import com.nxtlife.mgs.service.FileService;
//import com.nxtlife.mgs.view.FileRequest;
//import com.nxtlife.mgs.view.FileResponse;
//import com.nxtlife.mgs.view.SuccessResponse;
//import com.nxtlife.mgs.store.FileStore;
//
//@Service
//public class FileServiceImpl extends BaseService implements FileService {
//
//	@Autowired
//	private FileStore store;
//
//	@Autowired
//	FileRepository fileRepository;
//
//	@Override
//	public File saveMedia(FileRequest fileRequest, String category) {
//		if (fileRequest == null) {
//			throw new ValidationException("Missing file");
//		}
//
//		if (fileRequest.getFile() == null)
//			throw new ValidationException("Media file missing");
////       String orgFileName = imgReq.getImageFile().getOriginalFilename();
//		fileRequest.setName(fileRequest.getFile().getOriginalFilename());
//		String fileExtn = getFileExtension(fileRequest.getName());
//		fileRequest.setExtension(fileExtn);
//
//		String filename = UUID.randomUUID().toString() + "." + fileExtn;
//		List<String> allFileRepoUrls = new ArrayList<String>();
//		File file = null;
//		try {
//			String fileUrl = store.store(category, filename, fileRequest.getFile().getBytes());
//			if (allFileRepoUrls.contains(fileUrl))
//				throw new ValidationException("File Url already exist.");
//
//			fileRequest.setUrl(fileUrl);
//			file = fileRequest.toEntity();
//			file.setActive(true);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return file;
//	}
//
//	@Override
//	public SuccessResponse deleteFileByCId(String cId) {
//		int i = fileRepository.updateFileSetActiveByCid(false, cId);
//		if (i < 1)
//			throw new ValidationException("File not deleted");
//		return new SuccessResponse(HttpStatus.OK.value(), "Image successfully deleted");
//	}
//
//	@Override
//	public SuccessResponse deleteFileByUrl(String url) {
//		int i = fileRepository.updateFileSetActiveByUrl(false, url);
//		if (i < 1)
//			throw new ValidationException("File not deleted");
//		return new SuccessResponse(HttpStatus.OK.value(), "Image successfully deleted");
//	}
//
//	@Override
//	public FileResponse getFileByUrl(String url) {
//		File file = fileRepository.findByUrlAndActiveTrue(url);
//		if (file == null)
//			throw new NotFoundException("file Not Found.");
//		return new FileResponse(file);
//	}
//
//	@Override
//	public FileResponse getFileByCId(String cId) {
//		File file = fileRepository.findByCidAndActiveTrue(cId);
//		if (file == null)
//			throw new NotFoundException("file Not Found.");
//		return new FileResponse(file);
//	}
//
//	@Override
//	public String getFileExtension(String fileName) {
//		String[] separatedItems = fileName.split("\\.");
//		if (separatedItems.length < 2)
//			throw new ValidationException("Not able to parse file extension from file name.");
//
//		return separatedItems[separatedItems.length - 1];
//
//	}
//
//}
