package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.GradeRequest;
import com.nxtlife.mgs.view.GradeResponse;
import com.nxtlife.mgs.view.SchoolResponse;

public interface GradeService {

	List<GradeResponse> uploadGradesFromExcel(MultipartFile file);

	GradeResponse save(GradeRequest request);
	
	GradeResponse findById(Long id);
	
	GradeResponse findByCid(String cId);
}
