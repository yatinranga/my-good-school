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
@RequestMapping("/")

public class TeacherController {

	@Autowired
	TeacherService teacherService;

//	@RequestMapping(value = "importTeachers", method = RequestMethod.POST)
//	public List<TeacherResponse> uploadTeachersFromExcel(@RequestParam("file") MultipartFile file) {
//		return teacherService.uploadTeachersFromExcel(file, false);
//	}
//
//	@RequestMapping(value = "importCoaches", method = RequestMethod.POST)
//	public List<TeacherResponse> uploadCoachesFromExcel(@RequestParam("file") MultipartFile file,
//			@RequestParam Integer rowLimit) {
//		return teacherService.uploadTeachersFromExcel(file, true);
//	}

	@PostMapping("api/teachers/")
	public TeacherResponse saveTeacher(@RequestBody TeacherRequest teacherRequest) {
		return teacherService.saveClassTeacher(teacherRequest);
	}
	
	@PostMapping("teachers/signUp")
	public TeacherResponse signUp(@RequestBody TeacherRequest teacherRequest) {
		if(teacherRequest.getIsCoach())
			return teacherService.saveCoach(teacherRequest);
		return teacherService.saveClassTeacher(teacherRequest);
	}
	
	@GetMapping("teachers/all")
	public List<TeacherResponse> getAllTeachers(){
		return teacherService.getAllTeachers();
	}
	
	@GetMapping(value = "teachers/classTeachers")
	public List<TeacherResponse> getAllClassTeachers(){
		return teacherService.getAllClassTeachers();
	} 
	
	
	@GetMapping(value = "api/teachers/{cId}")
	public TeacherResponse getTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findByCId(cId);
	}
	
	@GetMapping(value = "api/teachers/classTeacher/{cId}")
	public TeacherResponse getClassTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findClassTeacherByCId(cId);
	}
	
}
