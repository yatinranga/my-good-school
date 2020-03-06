package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.SuccessResponse;

public interface SchoolService {

	ResponseEntity<?> uploadSchoolsFromExcel(MultipartFile file);

	SchoolResponse save(SchoolRequest request);

	SchoolResponse findById(Long id);

	SchoolResponse findByCid(String cId);

	List<SchoolResponse> getAllSchools();

	SuccessResponse delete(String cid);

	SchoolResponse update(SchoolRequest request, String cid);

}
