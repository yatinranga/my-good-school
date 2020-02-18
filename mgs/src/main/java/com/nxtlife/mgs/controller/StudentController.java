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
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	@Autowired
	StudentService studentService;

	@Autowired
	ActivityPerformedService activityPerformedService;

//	@RequestMapping(value = "importStudents", method = RequestMethod.POST)
//	public List<StudentResponse> uploadStudentsFromExcel(@RequestParam("file") MultipartFile file) {
//		return studentService.uploadStudentsFromExcel(file);
//	}

	@PostMapping(value = "/signUp")
	public StudentResponse signUpStudent(@RequestBody StudentRequest studentRequest) {
		return studentService.save(studentRequest);
	}

	@PostMapping()
	public StudentResponse saveStudent(@Valid @RequestBody StudentRequest studentRequest) {
		return studentService.save(studentRequest);
	}

	@PutMapping("/update/{cid}")
	public StudentResponse update(@RequestBody StudentRequest request, @PathVariable String cid) {
		return studentService.update(request, cid);
	}

	@GetMapping()
	public List<StudentResponse> getAll() {
		return studentService.getAll();
	}

	@GetMapping("/name/{name}")
	public List<StudentResponse> findByName(@PathVariable String name) {
		return studentService.findByName(name);
	}

//	@GetMapping("{id}")
//	public StudentResponse findByid(@PathVariable Long id) {
//		return studentService.findByid(id);
//	}

	@GetMapping("{cId}")
	public StudentResponse findByCId(@PathVariable String cId) {
		return studentService.findByCId(cId);
	}

	@GetMapping("/contact/{mobileNumber}")
	public StudentResponse findByMobileNumber(@PathVariable String mobileNumber) {
		return studentService.findByMobileNumber(mobileNumber);
	}

	@GetMapping("/username/{username}")
	public StudentResponse findByUsername(@PathVariable String username) {
		return studentService.findByUsername(username);
	}

	@GetMapping(value = "/activities")
	public List<ActivityPerformedResponse> getAllActivitiesOfStudentByStatus(@RequestParam("status") String status,
			@RequestParam("studentId") String studentCid) {
		return activityPerformedService.getAllActivitiesOfStudentByStatus(status, studentCid);
	}
	
	@GetMapping(value = "/activities/filterByFourS")
	public List<ActivityPerformedResponse> filterActivitiesByFourS(@RequestParam("studentId") String studentCid,@RequestParam("fourS") String fourS ,@RequestParam("status") String status){
		return activityPerformedService.findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(studentCid, fourS, status);
	}
	
	@GetMapping(value = "/activities/filterByFocusArea")
	public List<ActivityPerformedResponse> filterActivitiesByFocusArea(@RequestParam("studentId") String studentCid,@RequestParam("focusArea") String focusAreaCid,@RequestParam("status") String activityStatus){
		return activityPerformedService.findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(studentCid, focusAreaCid, activityStatus);
	}
	
	@GetMapping(value = "/activities/filterByCoach")
	public List<ActivityPerformedResponse> filterActivitiesByCoach(@RequestParam("studentId") String studentCid,@RequestParam("coachId") String teacherCid,@RequestParam("status") String activityStatus){
		return activityPerformedService.findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(studentCid, teacherCid, activityStatus);
	}
	
	@GetMapping(value = "/activities/filterByPsdArea")
	public List<ActivityPerformedResponse> filterActivitiesByPsdArea(@RequestParam("studentId") String studentCid,@RequestParam("psdArea") String psdArea,@RequestParam("status") String activityStatus){
		return activityPerformedService.findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(studentCid, psdArea, activityStatus);
	}
	
	@GetMapping(value = "/activities/filterByYear")
	public List<ActivityPerformedResponse> filterActivitiesByYearPerformed(@RequestParam("year") String year ,@RequestParam("studentId") String studentId){
		return activityPerformedService.filterActivityByYearPerformed(year, studentId);
	}
	

	@DeleteMapping("{cid}")
	public SuccessResponse delete(@RequestBody StudentRequest request, @PathVariable String cid) {
		return studentService.delete(request, cid);
	}

}
