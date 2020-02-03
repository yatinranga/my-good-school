package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(value = "importStudents", method = RequestMethod.POST)
	public List<StudentResponse> uploadStudentsFromExcel(@RequestParam("file") MultipartFile file,
			@RequestParam Integer rowLimit) {
		return studentService.uploadStudentsFromExcel(file, rowLimit);
	}

	@GetMapping()
	public List<StudentResponse> getAll() {
		return studentService.getAll();
	}

	@GetMapping("{name}")
	public List<StudentResponse> findByName(@PathVariable String name) {
		return studentService.findByName(name);
	}

	@GetMapping("{id}")
	public StudentResponse findByid(@PathVariable Long id) {
		return studentService.findByid(id);
	}

	@GetMapping("{mobileNumber}")
	public StudentResponse findByMobileNumber(@PathVariable String mobileNumber) {
		return studentService.findByMobileNumber(mobileNumber);
	}

	@GetMapping("{username}")
	public StudentResponse findByUsername(@PathVariable String username) {
		return studentService.findByUsername(username);
	}

}
