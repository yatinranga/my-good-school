package com.nxtlife.mgs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/api/teacher/")
public class TeacherController {

	@Autowired
	TeacherService teacherService;

	@PostMapping("/save")
	public TeacherResponse addTeacher(@RequestBody @Valid TeacherRequest request) {
		return teacherService.addTeacher(request);
	}

}
