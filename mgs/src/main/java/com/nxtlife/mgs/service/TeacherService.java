package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.ActivityResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

public interface TeacherService {

	List<TeacherResponse> uploadTeachersFromExcel(MultipartFile file, Integer rowLimit,Boolean isCoach);

	TeacherResponse save(TeacherRequest request);
	
	TeacherResponse saveClassTeacher(TeacherRequest request);
	
	TeacherResponse saveCoach(TeacherRequest request);

	List<TeacherResponse> findCoachesByActivityName(String activityName);

	TeacherResponse findByCId(String cId);

	TeacherResponse findById(Long id);

	List<ActivityResponse> findAllActivitiesByCoachId(Long id);

	List<ActivityResponse> findAllActivitiesByCoachCId(String cId);
}
