package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.GradeRequest;
import com.nxtlife.mgs.view.GradeResponse;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("api/activitiesOffered")
public class ActivityController {

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private TeacherService teacherService;
	
	@GetMapping(value = "/{schoolCid}")
	public List<ActivityRequestResponse> getAllOfferedActivitiesBySchool(@PathVariable("schoolCid") String schoolCid){
		return activityService.getAllOfferedActivitiesBySchool(schoolCid);
	}
	
	@GetMapping()
	public List<ActivityRequestResponse> getAllActivities(){
		return activityService.getAllOfferedActivities();
	}
	
//	@GetMapping(value = "/coaches")
//	public List<TeacherResponse> getCoachesBySchoolAndActivityCid(@RequestParam("schoolId") String schoolCid ,@RequestParam("activityId") String activityCid){
//		return teacherService.findCoachesBySchoolCidAndActivityCid(schoolCid, activityCid);
//	}
	
	@RequestMapping(value = "importActivities", method = RequestMethod.POST)
	public List<ActivityRequestResponse> uploadGradesFromExcel(@RequestParam("file") MultipartFile file) {
		return activityService.uploadActivityFromExcel(file);
	}

	@PostMapping()
	public ActivityRequestResponse saveActivity(@RequestBody ActivityRequestResponse request) {
		return activityService.saveActivity(request);
	}
}
