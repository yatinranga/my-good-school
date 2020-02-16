package com.nxtlife.mgs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;

@RestController
@RequestMapping("/api/activities")
public class ActivityPerformedController {

	@Autowired
	ActivityPerformedService activityPerformedService;
	
	@PostMapping(value = "/save")
	public ActivityPerformedResponse saveActivity(@ModelAttribute ActivityPerformedRequest request) {
		return activityPerformedService.saveActivity(request);
	}
	
	@PostMapping(value = "/{actCid}/submit")
	public ActivityPerformedResponse submitActivity(@PathVariable("actCid") String activityPerformedCid) {
		return activityPerformedService.submitActivity(activityPerformedCid);
	}
	
	
}
