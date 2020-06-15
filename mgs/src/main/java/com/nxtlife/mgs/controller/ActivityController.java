package com.nxtlife.mgs.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@PostMapping(value = "api/activitiesOffered")
	public ActivityRequestResponse saveActivity(@Validated @RequestBody ActivityRequestResponse request) {
		return activityService.saveActivity(request);
	}

//	@GetMapping(value = "activitiesOffered")
//	public List<ActivityRequestResponse> getAllActivities(@RequestParam(defaultValue = "0") Integer pageNo,
//			@RequestParam(defaultValue = "10") Integer pageSize) {
//		return activityService.getAllOfferedActivities(pageNo, pageSize);
//	}

	@GetMapping(value = { "activitiesOffered/{schoolCid}", "activitiesOffered" })
	public List<ActivityRequestResponse> getAllActivitiesOfSchoolAsPerGrade(
			@PathVariable(name = "schoolCid", required = false) String schoolCid) {
		return activityService.getAllOfferedActivitiesBySchoolAsPerGrade(schoolCid);
//		return activityService.getAllOfferedActivitiesBySchool(schoolCid);
	}
	
	@GetMapping("activities")
	public List<ActivityRequestResponse> getAllActivitiesOfSchool(@RequestParam(value = "schoolId" ,required = false) String schoolCid){
		return activityService.getAllOfferedActivitiesBySchool(schoolCid);
	}

	@GetMapping("generalActivities")
	public List<ActivityRequestResponse> getAllGeneralActivities() {
		return activityService.getAllGeneralActivities();
	}
	
	@GetMapping(value = "/filters")
	public Map<String , Object> getAvailableFilters(){	
		return activityService.getAvailableFilters();
	}

	@DeleteMapping("api/activitiesOffered/{cid}")
	public SuccessResponse deleteActivityByCid(@PathVariable("cid") String cid ,@RequestParam(value = "schoolId" ,required = false) String schoolCid ,@RequestParam(value = "forAll" ,defaultValue = "false") Boolean forAll) {
		return activityService.deleteActivityByCid(cid,schoolCid,forAll);
	}

//	@GetMapping(value = "/coaches")
//	public List<TeacherResponse> getCoachesBySchoolAndActivityCid(@RequestParam("schoolId") String schoolCid ,@RequestParam("activityId") String activityCid){
//		return teacherService.findCoachesBySchoolCidAndActivityCid(schoolCid, activityCid);
//	}

//	@RequestMapping(value = "/api/activitiesOffered/importActivities", method = RequestMethod.POST)
//	public List<ActivityRequestResponse> uploadActivitiesFromExcel(@RequestParam("file") MultipartFile file) {
//		return activityService.uploadActivityFromExcel(file);
//	}

}
