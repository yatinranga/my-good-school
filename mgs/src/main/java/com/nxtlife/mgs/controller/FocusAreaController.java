package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.service.FocusAreaService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.FocusAreaRequestResponse;

@RestController
@RequestMapping("/api/focusAreas")
public class FocusAreaController {
	@Autowired
	FocusAreaService focusAreaService;

	@RequestMapping(value = "importFocusAreas", method = RequestMethod.POST)
	public List<FocusAreaRequestResponse> uploadFocusAreasFromExcel(@RequestParam("file") MultipartFile file) {
		return focusAreaService.uploadFocusAreasFromExcel(file);
	}

	@PostMapping()
	public FocusAreaRequestResponse saveFocusArea(@RequestBody FocusAreaRequestResponse request) {
		return focusAreaService.save(request);
	}
}
