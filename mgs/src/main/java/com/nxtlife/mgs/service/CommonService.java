package com.nxtlife.mgs.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface CommonService {

	ResponseEntity<Resource> downloadFile(String filePath) ;

}
