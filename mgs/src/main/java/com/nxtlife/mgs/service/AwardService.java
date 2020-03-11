package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.filtering.filter.AwardFilter;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.PropertyCount;

public interface AwardService {

	AwardResponse createAward(AwardRequest request);
	
	public List<AwardResponse> findAllByStudent();
	
	public List<AwardResponse> findAllByStudent(AwardFilter awardFilter) ;
	
	public List<AwardResponse> findAllByManagement();
	
	public List<AwardResponse> findAllByManagement(AwardFilter awardFilter) ;
	
	public AwardResponse updateStatus(String awardId, Boolean isVerified);

	List<PropertyCount> getCount(String studentCid, String status, String type);
	
}
