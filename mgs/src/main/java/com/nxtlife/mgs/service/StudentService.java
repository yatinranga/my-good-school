package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;

public interface StudentService extends ExcelUtil {

	ResponseEntity<?> uploadStudentsFromExcel(MultipartFile file, String schoolCid);

	StudentResponse save(StudentRequest request);

	List<StudentResponse> findByName(String name);

	StudentResponse findByid(Long id);

	StudentResponse findByCId(String cId);

	StudentResponse findByMobileNumber(String mobileNumber);

	StudentResponse findByUsername(String username);

	List<StudentResponse> getAll();

	public List<StudentResponse> getAllBySchoolCid(String schoolCid);
	
	public List<StudentResponse> getAllByGradeId(String gradeCid);

	StudentResponse update(StudentRequest request, String cid);

	SuccessResponse delete(String cid);

	List<StudentResponse> getAllStudentsBySchoolAndActivityAndCoachAndStatusReviewed(String schoolCid, String gradeCid,
			String activityCid, String activityStatus, String teacherCid);

	// List<AwardResponse> getAllAwardsOfStudentByActivityId(String studentCid,
	// String awardCid);

	// List<StudentResponse> getAllBySchoolCid(String schoolCid);

	// ActivityPerformedResponse saveActivity(ActivityPerformedRequest request);

}
