package com.nxtlife.mgs.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.CommonService;
import com.nxtlife.mgs.service.FileStorageService;

@Service
public class CommonServiceImpl extends BaseService implements CommonService {

	private static Logger logger = LoggerFactory.getLogger(CommonService.class);

	@Autowired
	private FileStorageService<MultipartFile> fileStorageService;

	// @Override
	public ResponseEntity<Resource> downloadFile(String filePath) {
		Resource resource = null;
		try {
			resource = fileStorageService.loadFileAsResource(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Couldn't able to convert file {} into resource", filePath);
		}
		if (resource == null)
			throw new ValidationException(String.format("File with path (%s) not found.", filePath));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + resource.getFilename());

		return ResponseEntity.ok().headers(headers).body(resource);

	}
}
