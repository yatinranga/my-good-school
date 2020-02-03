package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@RequestMapping("/api/teachers")
public class TeacherController {

	@Autowired
	TeacherService teacherService;

	@RequestMapping(value = "importTeachers", method = RequestMethod.POST)
	public List<TeacherResponse> uploadStudentsFromExcel(@RequestParam("file") MultipartFile file,
			@RequestParam Integer rowLimit) {
		return teacherService.uploadTeachersFromExcel(file, rowLimit, false);
	}

	@RequestMapping(value = "importCoaches", method = RequestMethod.POST)
	public List<TeacherResponse> uploadCoachesFromExcel(@RequestParam("file") MultipartFile file,
			@RequestParam Integer rowLimit) {
		return teacherService.uploadTeachersFromExcel(file, rowLimit, true);
	}

	@PostMapping()
	public TeacherResponse saveTeacher(@RequestBody TeacherRequest teacherRequest) {
		if (teacherRequest.getIsCoach())
			return teacherService.saveCoach(teacherRequest);

		return teacherService.saveClassTeacher(teacherRequest);
	}
}
