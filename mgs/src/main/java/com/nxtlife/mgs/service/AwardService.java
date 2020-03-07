package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;

public interface AwardService {

	AwardResponse createAward(AwardRequest request);
	
	public List<AwardResponse> findAllByStudent();
	
	public List<AwardResponse> findAllByManagement();
	
}
