package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/")
public class CoachController {

	@Autowired
	TeacherService teacherService;
	
	@GetMapping("coaches/{schoolId}/{activityId}")
	public List<TeacherResponse> getAllCoachesBySchoolCidAndActivityCid(@PathVariable("schoolId") String schoolCid,@PathVariable("activityId") String activityCid){
		return teacherService.findCoachesBySchoolCidAndActivityCid(schoolCid, activityCid);
	}

	@GetMapping(value = "coaches")
	public List<TeacherResponse> getAllCoaches(){
		return teacherService.getAllCoaches();
	} 
	
	@GetMapping(value = "coaches/{schoolId}")
	public List<TeacherResponse> getAllCoachesBySchool(@PathVariable("schoolId") String schoolId){
		return teacherService.getAllCoaches();
	} 
	
	@GetMapping(value = "api/coaches/{cId}")
	public TeacherResponse getCoachByCId(@PathVariable("cId") String cId) {
		return teacherService.findCoachByCId(cId);
	}
	
	@PostMapping()
	public TeacherResponse saveCoach(@RequestBody TeacherRequest teacherRequest) {
			return teacherService.saveCoach(teacherRequest);
	}
	
	
}
