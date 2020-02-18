package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.SuccessResponse;

public interface ActivityService {

	List<ActivityRequestResponse> getAllOfferedActivities();

	List<ActivityRequestResponse> getAllOfferedActivitiesBySchool(String schoolCid);

	List<ActivityRequestResponse> uploadActivityFromExcel(MultipartFile file ,String schoolCid);

	ActivityRequestResponse saveActivity(ActivityRequestResponse request);

	SuccessResponse deleteActivityByCid(String cid);

}
