package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/")
public class ActivityController {

	@Autowired
	private ActivityService activityService;
	
	
	@GetMapping(value = "activitiesOffered")
	public List<ActivityRequestResponse> getAllActivities(){
		return activityService.getAllOfferedActivities();
	}
	
	@GetMapping(value = "activitiesOffered/{schoolCid}")
	public List<ActivityRequestResponse> getAllActivitiesOfSchool(@PathVariable("schoolCid") String schoolCid){
		return activityService.getAllOfferedActivitiesBySchool(schoolCid);
	}
	
	@DeleteMapping("api/activitiesOffered/{cid}")
	public SuccessResponse deleteActivityByCid(@PathVariable("cid") String cid) {
		return activityService.deleteActivityByCid(cid);
	}
	
//	@GetMapping(value = "/coaches")
//	public List<TeacherResponse> getCoachesBySchoolAndActivityCid(@RequestParam("schoolId") String schoolCid ,@RequestParam("activityId") String activityCid){
//		return teacherService.findCoachesBySchoolCidAndActivityCid(schoolCid, activityCid);
//	}
	
//	@RequestMapping(value = "/api/activitiesOffered/importActivities", method = RequestMethod.POST)
//	public List<ActivityRequestResponse> uploadActivitiesFromExcel(@RequestParam("file") MultipartFile file) {
//		return activityService.uploadActivityFromExcel(file);
//	}

	@PostMapping(value = "/api/activitiesOffered")
	public ActivityRequestResponse saveActivity(@RequestBody ActivityRequestResponse request) {
		return activityService.saveActivity(request);
	}
}