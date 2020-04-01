package com.nxtlife.mgs.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.LFINRequestResponse;

public interface LFINService {

	LFINRequestResponse save(LFINRequestResponse request);
	
	public ActivityRequestResponse updateActivityStatus(String awardId, Boolean isVerified);

	ResponseEntity<?> uploadLFINFromExcel(MultipartFile file);
}
