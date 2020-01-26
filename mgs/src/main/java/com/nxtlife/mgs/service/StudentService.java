package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;

public interface StudentService extends ExcelUtil{

	List<StudentResponse> uploadStudentsFromExcel(MultipartFile file, Integer rowLimit);

	StudentResponse save(StudentRequest request);

}
