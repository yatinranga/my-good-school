package com.nxtlife.mgs.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

public interface CommonService {

	ResponseEntity<?> downloadFile(String filePath) ;

}
