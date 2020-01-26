package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.view.StudentResponse;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	@Autowired
	StudentService studentService;
	
	@RequestMapping(value="importStudents", method=RequestMethod.POST)
	public List<StudentResponse> uploadStudentsFromExcel(@RequestParam("file") MultipartFile file, @RequestParam Integer rowLimit){
		return studentService.uploadStudentsFromExcel(file, rowLimit);
	}
	
	
}
