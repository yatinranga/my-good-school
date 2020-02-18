package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.FocusAreaRequestResponse;

public interface FocusAreaService {

	FocusAreaRequestResponse save(FocusAreaRequestResponse request);

	List<FocusAreaRequestResponse> uploadFocusAreasFromExcel(MultipartFile file);

	List<FocusAreaRequestResponse> getAllFocusAreasBySchool(String schoolCid);

	List<FocusAreaRequestResponse> getAllFocusAreas();

}
