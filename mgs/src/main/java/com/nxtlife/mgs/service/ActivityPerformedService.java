package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;

public interface ActivityPerformedService {

	List<FileResponse> getAllFilesOfActivity(String activityCId);

	File saveMediaForActivityPerformed(FileRequest fileRequest, String category, ActivityPerformed activityPerformed);

	ActivityPerformedResponse saveActivity(ActivityPerformedRequest request);

	List<ActivityPerformedResponse> getAllSavedActivitiesOfStudent(String studentCid);

	List<ActivityPerformedResponse> getAllSubmittedActivityOfStudent(String studentCid);

	List<ActivityPerformedResponse> getAllReviewedActivityOfStudent(String studentCid);

	ActivityPerformedResponse submitActivity(String activityPerformedCid);

	List<ActivityPerformedResponse> getAllActivitiesOfStudentByStatus(String status, String studentCid);

	List<ActivityPerformedResponse> findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(String studentCid,
			String teacherCid, String activityStatus);

	List<ActivityPerformedResponse> findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(
			String studentCid, String psdArea, String activityStatus);

	List<ActivityPerformedResponse> findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(
			String studentCid, String focusAreaCid, String activityStatus);

	List<ActivityPerformedResponse> findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(String studentCid,
			String fourS, String activityStatus);

	List<ActivityPerformedResponse> filterActivityByYearPerformed(String year, String studentCid);

}
