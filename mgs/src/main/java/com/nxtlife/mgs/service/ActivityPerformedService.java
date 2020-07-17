package com.nxtlife.mgs.service;

import java.util.List;
import java.util.Set;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.filtering.filter.ActivityPerformedFilter;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;
import com.nxtlife.mgs.view.PropertyCount;
import com.nxtlife.mgs.view.SuccessResponse;

public interface ActivityPerformedService {

	List<FileResponse> getAllFilesOfActivity(String activityCId);

	File saveMediaForActivityPerformed(FileRequest fileRequest, String category, ActivityPerformed activityPerformed);

	ActivityPerformedResponse saveActivity(ActivityPerformedRequest request);

	List<ActivityPerformedResponse> getAllSavedActivitiesOfStudent(String studentCid, Integer page, Integer pageSize);

	ActivityPerformedResponse submitActivity(String activityPerformedCid);

	List<ActivityPerformedResponse> getAllActivitiesOfStudentByStatus(String status, String studentCid, Integer page,
			Integer pageSize);

	List<ActivityPerformedResponse> findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(String studentCid,
			String teacherCid, String activityStatus);

	List<ActivityPerformedResponse> findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(
			String studentCid, String psdArea, String activityStatus);

	List<ActivityPerformedResponse> findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(
			String studentCid, String focusAreaCid, String activityStatus);

	List<ActivityPerformedResponse> findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(String studentCid,
			String fourS, String activityStatus);

	List<ActivityPerformedResponse> filterActivityByYearPerformed(String year, String studentCid);

	ActivityPerformedResponse saveActivityByCoach(ActivityPerformedRequest request);

	ActivityPerformedResponse submitActivityByCoach(String activityPerformedCid);

	List<ActivityPerformedResponse> getAllActivitiesAssignedToCoachforReview(String coachCid, String status);

	List<ActivityPerformedResponse> getAllPendingActivitiesByClass(String coachCid, String gradeCid);

	List<ActivityPerformedResponse> getAllPendingActivitiesByService(String coachCid, String activityCid);

	List<ActivityPerformedResponse> getAllPendingActivitiesByClassAndService(String coachCid, String gradeCid,
			String activityCid);

	List<ActivityPerformedResponse> getAllActivityOfStudentByActivityId(String studentCid, String activityCid,
			String activityStatus);

	SuccessResponse deleteActivityOfStudent(String activityPerformedCid);

	List<ActivityPerformedResponse> getAllPerformedActivitiesOfStudent(String studentCid, Integer page,
			Integer pageSize);

	List<ActivityPerformedResponse> getAllSubmittedActivityOfStudent(String studentCid, Integer page, Integer pageSize);

	List<ActivityPerformedResponse> getAllReviewedActivityOfStudent(String studentCid, Integer page, Integer pageSize);

	List<ActivityPerformedResponse> filter(ActivityPerformedFilter filterRequest);

	List<PropertyCount> getCount(String studentCid, String status, String type);

	Set<ActivityPerformedResponse> getAllActivityPerformedForCoordinator(String schoolCid, String gradeId,
			String clubId, String status);

}
