package com.nxtlife.mgs.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.SuccessResponse;

public interface ActivityService {

	List<ActivityRequestResponse> getAllOfferedActivities(Integer pageNo, Integer pageSize);

	List<ActivityRequestResponse> getAllOfferedActivitiesBySchool(String schoolCid);

	ResponseEntity<?> uploadActivityFromExcel(MultipartFile file ,String schoolCid);

	ActivityRequestResponse saveActivity(ActivityRequestResponse request);

	SuccessResponse deleteActivityByCid(String cid ,String schoolCid , Boolean forAll);

	List<ActivityRequestResponse> getAllGeneralActivities();

	Map<String, Object> getAvailableFilters();

	List<ActivityRequestResponse> getAllClubsOfStudent(String studentCid);

//	List<ActivityRequestResponse> getAllClubsOfTeacher(String teacherCid);

	List<ActivityRequestResponse> getAllClubsOfTeacher(String teacherCid, String schoolCid);

	List<ActivityRequestResponse> getAllOfferedActivitiesBySchoolAsPerGrade(String schoolCid);

	ActivityRequestResponse getById(String cid);

}
