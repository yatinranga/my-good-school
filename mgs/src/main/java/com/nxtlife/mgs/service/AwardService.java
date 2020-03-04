package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.filtering.filter.AwardFilter;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;

public interface AwardService {

	AwardResponse createAward(AwardRequest request);

	List<AwardResponse> assignAward(AwardRequest request);

	List<AwardResponse> getAllAwardsBySchool(String schoolCid);

	List<AwardResponse> getAllUnverifiedAwardsOfSchool(String schoolCid);

	List<AwardResponse> verifyAwards(AwardRequest request);

	List<AwardResponse> filterAwardByYearPerformed(String year, String studentCid);

	List<AwardResponse> getAllSoloUnverifiedAwardsOfSchool(String schoolCid, String awardCid);

	List<AwardResponse> getAllAwardsAssignedByTeacher(AwardRequest request);

	List<AwardResponse> filter(String studentCid, AwardFilter filterRequest);

}
