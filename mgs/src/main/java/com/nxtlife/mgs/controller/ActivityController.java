package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@GetMapping(value = "activitiesOffered")
	public List<ActivityRequestResponse> getAllActivities() {
		return activityService.getAllOfferedActivities();
	}

	@GetMapping(value = "activitiesOffered/{schoolCid}")
	public List<ActivityRequestResponse> getAllActivitiesOfSchool(@PathVariable("schoolCid") String schoolCid) {
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
