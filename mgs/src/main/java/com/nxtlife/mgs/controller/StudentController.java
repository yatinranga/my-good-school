package com.nxtlife.mgs.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class StudentController {

	@Autowired
	StudentService studentService;

	@Autowired
	ActivityPerformedService activityPerformedService;

//	@RequestMapping(value = "importStudents", method = RequestMethod.POST)
//	public List<StudentResponse> uploadStudentsFromExcel(@RequestParam("file") MultipartFile file) {
//		return studentService.uploadStudentsFromExcel(file);
//	}

	@PostMapping(value = "/student/signUp")
	public StudentResponse signUpStudent(@RequestBody StudentRequest studentRequest) {
		return studentService.save(studentRequest);
	}

	@PostMapping("api/student")
	public StudentResponse saveStudent(@Valid @RequestBody StudentRequest studentRequest) {
		return studentService.save(studentRequest);
	}

	@PutMapping("api/student/{cid}")
	public StudentResponse update(@RequestBody StudentRequest request, @PathVariable String cid) {
		return studentService.update(request, cid);
	}

	@GetMapping("api/students")
	public List<StudentResponse> getAll() {
		return studentService.getAll();
	}

//	@GetMapping("api/students/name/{name}")
//	public List<StudentResponse> findByName(@PathVariable String name) {
//		return studentService.findByName(name);
//	}

//	@GetMapping("/id/{cId}")
//	public StudentResponse findByid(@PathVariable Long id) {
//		return studentService.findByid(id);
//	}

	@GetMapping("api/student/{cId}")
	public StudentResponse findByCId(@PathVariable String cId) {
		return studentService.findByCId(cId);
	}

	@GetMapping("/api/school/{id}/students")
	public List<StudentResponse> getAllBySchoolId(@PathVariable String schoolCid) {
		return studentService.getAllBySchoolCid(schoolCid);
	}

	@GetMapping(value = "api/student/{cid}/awards")
	public List<AwardResponse> getAllAwardsOfStudentByActivityId(@PathVariable("cid") String studentCid,
			@RequestParam(name = "activityCid", required = false) String activityCid) {
		return studentService.getAllAwardsOfStudentByActivityId(studentCid, activityCid);
	}

	@DeleteMapping("api/students/{cid}")
	public SuccessResponse delete(@PathVariable String cid) {
		return studentService.delete(cid);
	}

}
