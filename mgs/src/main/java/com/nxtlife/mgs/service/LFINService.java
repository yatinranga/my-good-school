package com.nxtlife.mgs.service;

import com.nxtlife.mgs.entity.user.LFIN;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.LFINRequestResponse;

public interface LFINService {

	LFIN save(LFINRequestResponse request);
	
	public AwardResponse updateActivityStatus(String awardId, Boolean isVerified);
}
