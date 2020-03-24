package com.nxtlife.mgs.service;

import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.LFINRequestResponse;

public interface LFINService {

	LFINRequestResponse save(LFINRequestResponse request);
	
	public ActivityRequestResponse updateActivityStatus(String awardId, Boolean isVerified);
}
