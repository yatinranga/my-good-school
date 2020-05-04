package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.ClubMembershipResponse;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

public interface TeacherService {

	ResponseEntity<?> uploadTeachersFromExcel(MultipartFile file, Boolean isCoach, String schoolCid);

	TeacherResponse save(TeacherRequest request);

	TeacherResponse saveClassTeacher(TeacherRequest request);

	TeacherResponse saveCoach(TeacherRequest request);

	TeacherResponse findByCId(String cId);

	TeacherResponse findById(Long id);

	TeacherResponse findCoachByCId(String cId);

	TeacherResponse findCoachById(Long id);

	TeacherResponse findClassTeacherByCId(String cId);

	TeacherResponse findClassTeacherById(Long id);

	List<ActivityRequestResponse> findAllActivitiesByCoachId(Long id);

	List<ActivityRequestResponse> findAllActivitiesByCoachCId(String cId);

	List<TeacherResponse> getAllTeachers(Integer pageNo, Integer pageSize);

	List<TeacherResponse> getAllCoaches();

	List<TeacherResponse> getAllClassTeachers();

	List<TeacherResponse> getAllTeachersOfSchool(String schoolCid);

	List<TeacherResponse> getAllClassTeachersOfSchool(String schoolCid);

	List<TeacherResponse> getAllCoachesOfSchool(String schoolCid);

	List<TeacherResponse> findCoachesBySchoolAndActivityName(String schoolCid, String activityName);

	List<TeacherResponse> findCoachesBySchoolCidAndActivityCid(String schoolCid, String activityCid);

	TeacherResponse update(TeacherRequest request, String cid);

	SuccessResponse delete(String cid);

	List<TeacherResponse> getAllManagmentBySchool(String schoolCid);

	ResponseEntity<?> uploadManagementFromExcel(MultipartFile file, String schoolCid);

	TeacherResponse setProfilePic(MultipartFile file);

	TeacherResponse addOrRemoveActivitiesToTeachers(TeacherRequest request);

	List<ClubMembershipResponse> getMembershipDetails();

	ClubMembershipResponse updateStatus(String studentId, String activityId, Boolean isVerified);

}
