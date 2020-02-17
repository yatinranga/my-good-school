package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;

public interface StudentService extends ExcelUtil {

	List<StudentResponse> uploadStudentsFromExcel(MultipartFile file, String schoolCid);

	StudentResponse save(StudentRequest request);

	List<StudentResponse> findByName(String name);

	StudentResponse findByid(Long id);

	StudentResponse findByCId(String cId);

	StudentResponse findByMobileNumber(String mobileNumber);

	StudentResponse findByUsername(String username);

	List<StudentResponse> getAll();

	List<StudentResponse> getAllBySchoolCid(String schoolCid);

	StudentResponse update(StudentRequest request, String cid);

	SuccessResponse delete(StudentRequest request, String cid);

//	List<StudentResponse> getAllBySchoolCid(String schoolCid);

//	ActivityPerformedResponse saveActivity(ActivityPerformedRequest request);

}
