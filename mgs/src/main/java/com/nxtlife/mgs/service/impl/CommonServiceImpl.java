package com.nxtlife.mgs.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
	
	@Autowired
	Utils utils;
	
//	@Override
	public ResponseEntity<?> downloadFile(String filePath) {
//		FileInputStream fin;
//		try {
//			fin = new FileInputStream(filePath);
//			System.out.println(filePath);
//			System.out.println(fin);

			// return IO ByteArray(in);
			HttpHeaders headers = new HttpHeaders();
			// headers.add("fileName"," Incidents.xls");

			// set filename in header
			headers.add("Content-Disposition", "attachment; filename= mgs_file_" + utils.generateRandomAlphaNumString(4)  + filePath.substring(filePath.lastIndexOf(".")));
			Resource resource = null;
			try {
				resource = fileStorageService.loadFileAsResource(filePath);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Couldn't able to convert file {} into resource", filePath);
			}

			return ResponseEntity.ok().headers(headers).body(resource);
//		} catch (FileNotFoundException e ) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException("IOError writing file to output stream or file not found");
//		}
	}
}
