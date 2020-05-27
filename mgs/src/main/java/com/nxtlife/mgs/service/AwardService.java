package com.nxtlife.mgs.service;

import java.util.List;
import java.util.Set;

import com.nxtlife.mgs.filtering.filter.AwardFilter;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.PropertyCount;

public interface AwardService {

	AwardResponse createAward(AwardRequest request);
	
	public List<AwardResponse> findAllByStudent(String studentId);
	
	public List<AwardResponse> findAllByStudent(AwardFilter awardFilter ,String studentId) ;
	
	public List<AwardResponse> findAllByManagement(String teacherCid);
	
	public List<AwardResponse> findAllByManagement(AwardFilter awardFilter ,String teacherCid) ;
	
	public AwardResponse updateStatus(String awardId, Boolean isVerified);

	List<PropertyCount> getCount(String studentCid, String status, String type);

	Set<String> getAllAwardTypes();

	Set<String> getAwardCriterias();
	
}
