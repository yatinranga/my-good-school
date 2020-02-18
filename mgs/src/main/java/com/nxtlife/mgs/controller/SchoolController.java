package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;

@RestController
@RequestMapping("/schools")
public class SchoolController {

	@Autowired
	SchoolService schoolService;
	
	@Autowired
	private ActivityService activityService;
	
	@GetMapping(value = "/{schoolCid}/activitiesOffered")
	public List<ActivityRequestResponse> getAllOfferedActivitiesBySchool(@PathVariable("schoolCid") String schoolCid){
		return activityService.getAllOfferedActivitiesBySchool(schoolCid);
	}

	@PostMapping(consumes = {"multipart/form-data"})
	public SchoolResponse saveSchool(@ModelAttribute SchoolRequest schoolRequest) {
		return schoolService.save(schoolRequest);
	}
	
	@GetMapping(value="/{cid}")
	public SchoolResponse findByCid(@PathVariable("cid") String cid) {
		return schoolService.findByCid(cid);
	}
	
	@GetMapping
	public List<SchoolResponse> getAllSchools(){
		return schoolService.getAllSchools();
	}
	
	
}