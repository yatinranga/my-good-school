package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;

public interface StudentService extends ExcelUtil{

	List<StudentResponse> uploadStudentsFromExcel(MultipartFile file, Integer rowLimit);

	StudentResponse save(StudentRequest request);

	List<StudentResponse> findByName(String name);

	StudentResponse findByCId(String cId);

	StudentResponse findByMobileNumber(String mobileNumber);

	StudentResponse findByUsername(String username);

	List<StudentResponse> getAll();

//	ActivityPerformedResponse saveActivity(ActivityPerformedRequest request);

	ActivityPerformedResponse saveActivity(ActivityPerformedRequest request, ActivityStatus activityStatus);

}
