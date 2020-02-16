package com.nxtlife.mgs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;

public interface StudentService extends ExcelUtil {

	Map<String,List<Object>> uploadStudentsFromExcel(MultipartFile file , String schoolCid);

	StudentResponse save(StudentRequest request);

	List<StudentResponse> findByName(String name);

	StudentResponse findByid(Long id);

	StudentResponse findByCId(String cId);

	StudentResponse findByMobileNumber(String mobileNumber);

	StudentResponse findByUsername(String username);

	List<StudentResponse> getAll();
	
	List<StudentResponse> getAllBySchoolCid(String schoolCid);
	
//	List<StudentResponse> getAllBySchoolCid(String schoolCid);

//	ActivityPerformedResponse saveActivity(ActivityPerformedRequest request);

}
