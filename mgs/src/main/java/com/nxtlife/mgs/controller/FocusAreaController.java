package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.FocusAreaService;
import com.nxtlife.mgs.view.FocusAreaRequestResponse;

@RestController
@RequestMapping("/")
public class FocusAreaController {
	@Autowired
	private FocusAreaService focusAreaService;

	@GetMapping(value = "focusAreas")
	public List<FocusAreaRequestResponse> getAllFocusAreas() {
		return focusAreaService.getAllFocusAreas();
	}

	@GetMapping("school/focusAreas")
	public List<FocusAreaRequestResponse> getAllFocusAreas(
			@RequestParam(value = "schoolId", required = false) String schoolCid) {
		return focusAreaService.getAllFocusAreasBySchool(schoolCid);
	}

	@PostMapping(value = "api/focusAreas")
	public FocusAreaRequestResponse saveFocusArea(@RequestBody FocusAreaRequestResponse request) {
		return focusAreaService.save(request);
	}
}
