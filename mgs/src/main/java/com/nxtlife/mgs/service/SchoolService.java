package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;

public interface SchoolService {

	List<SchoolResponse> uploadSchoolsFromExcel(MultipartFile file);

	SchoolResponse save(SchoolRequest request);
	
	SchoolResponse findById(Long id);
	
	SchoolResponse findByCid(String cId);

	List<SchoolResponse> getAllSchools();

}
