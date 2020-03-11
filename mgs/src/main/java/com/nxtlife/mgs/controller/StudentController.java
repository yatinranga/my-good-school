package com.nxtlife.mgs.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.glassfish.jersey.server.validation.ValidationError;
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
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.StudentService;
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

	// @RequestMapping(value = "importStudents", method = RequestMethod.POST)
	// public List<StudentResponse>
	// uploadStudentsFromExcel(@RequestParam("file") MultipartFile file) {
	// return studentService.uploadStudentsFromExcel(file);
	// }

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
	public List<StudentResponse> getAll(@RequestParam(name = "schoolId", required = false) String schoolId,
			@RequestParam(name = "gradeId", required = false) String gradeId) {
		if (schoolId == null && gradeId == null) {
			return studentService.getAll();
		}else if(schoolId!=null){
			return studentService.getAllBySchoolCid(schoolId);
		}else if(gradeId!=null){
			return studentService.getAllByGradeId(gradeId);
		}else{
			return new ArrayList<>();
		}
	}

	// @GetMapping("api/students/name/{name}")
	// public List<StudentResponse> findByName(@PathVariable String name) {
	// return studentService.findByName(name);
	// }

	// @GetMapping("/id/{cId}")
	// public StudentResponse findByid(@PathVariable Long id) {
	// return studentService.findByid(id);
	// }

	@GetMapping("api/student/{cId}")
	public StudentResponse findByCId(@PathVariable String cId) {
		return studentService.findByCId(cId);
	}

	@GetMapping("/api/school/{id}/students")
	public List<StudentResponse> getAllBySchoolId(@PathVariable String schoolCid) {
		return studentService.getAllBySchoolCid(schoolCid);
	}

	@GetMapping(value = "api/student/activity/{activityCid}")
	public List<StudentResponse> getAllStudentsBySchoolAndActivityAndCoachAndStatusReviewed(
			@RequestParam("schoolId") String schoolCid, @RequestParam("gradeId") String gradeCid,
			@PathVariable("activityCid") String activityCid, @RequestParam("teacherId") String teacherCid) {
		return studentService.getAllStudentsBySchoolAndActivityAndCoachAndStatusReviewed(schoolCid, gradeCid,
				activityCid, ActivityStatus.Reviewed.toString(), teacherCid);
	}

	@DeleteMapping("api/students/{cid}")
	public SuccessResponse delete(@PathVariable String cid) {
		return studentService.delete(cid);
	}
	
	@PutMapping("api/student/{cid}/profilePic")
	public StudentResponse setProfilePic(@RequestParam("profilePic") MultipartFile file, @PathVariable("cid") String studentCid) {
		return studentService.setProfilePic(file, studentCid);
	}

}
