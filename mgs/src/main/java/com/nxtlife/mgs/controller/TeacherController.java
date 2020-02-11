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

import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;


@RestController
@RequestMapping("/api/teachers/")

public class TeacherController {

	@Autowired
	TeacherService teacherService;

	@RequestMapping(value = "importTeachers", method = RequestMethod.POST)
	public List<TeacherResponse> uploadTeachersFromExcel(@RequestParam("file") MultipartFile file) {
		return teacherService.uploadTeachersFromExcel(file, false);
	}

	@RequestMapping(value = "importCoaches", method = RequestMethod.POST)
	public List<TeacherResponse> uploadCoachesFromExcel(@RequestParam("file") MultipartFile file,
			@RequestParam Integer rowLimit) {
		return teacherService.uploadTeachersFromExcel(file, true);
	}

	@PostMapping()
	public TeacherResponse saveTeacher(@RequestBody TeacherRequest teacherRequest) {
		if (teacherRequest.getIsCoach())
			return teacherService.saveCoach(teacherRequest);

		return teacherService.saveClassTeacher(teacherRequest);
	}
	
	@GetMapping(value = "/{schoolId}")
	public List<TeacherResponse> getAllCoachesBySchoolCidAndActivityCid(@PathVariable("schoolId") String schoolCid,@RequestParam("activityId") String activityCid){
		return teacherService.findCoachesBySchoolCidAndActivityCid(schoolCid, activityCid);
	}
	
	@GetMapping()
	public List<TeacherResponse> getAllTeachers(){
		return teacherService.getAllTeachers();
	}
	
	@GetMapping(value = "classTeachers")
	public List<TeacherResponse> getAllClassTeachers(){
		return teacherService.getAllClassTeachers();
	} 
	
	@GetMapping(value = "coaches")
	public List<TeacherResponse> getAllCoaches(){
		return teacherService.getAllCoaches();
	} 
	
	@GetMapping(value = "{cId}")
	public TeacherResponse getTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findByCId(cId);
	}
	
	@GetMapping(value = "classTeacher/{cId}")
	public TeacherResponse getClassTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findClassTeacherByCId(cId);
	}
	
	@GetMapping(value = "coach/{cId}")
	public TeacherResponse getCoachByCId(@PathVariable("cId") String cId) {
		return teacherService.findCoachByCId(cId);
	}
}
