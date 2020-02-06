package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.ActivityResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

public interface TeacherService {

	List<TeacherResponse> uploadTeachersFromExcel(MultipartFile file,  Boolean isCoach);

	TeacherResponse save(TeacherRequest request);

	TeacherResponse saveClassTeacher(TeacherRequest request);

	TeacherResponse saveCoach(TeacherRequest request);

	TeacherResponse findByCId(String cId);

	TeacherResponse findById(Long id);
	
	TeacherResponse findCoachByCId(String cId);

	TeacherResponse findCoachById(Long id);
	
	TeacherResponse findClassTeacherByCId(String cId);

	TeacherResponse findClassTeacherById(Long id);

	List<ActivityResponse> findAllActivitiesByCoachId(Long id);

	List<ActivityResponse> findAllActivitiesByCoachCId(String cId);
	
	List<TeacherResponse> getAllTeachers();
	
	List<TeacherResponse> getAllCoaches();
	
	List<TeacherResponse> getAllClassTeachers();
	
	List<TeacherResponse> getAllTeachersOfSchool(String schoolCid);
	
	List<TeacherResponse> getAllClassTeachersOfSchool(String schoolCid);
	
	List<TeacherResponse> getAllCoachesOfSchool(String schoolCid);

	List<TeacherResponse> findCoachesBySchoolAndActivityName(String schoolCid, String activityName);
}
