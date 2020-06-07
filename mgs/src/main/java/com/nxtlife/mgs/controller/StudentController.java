package com.nxtlife.mgs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.glassfish.jersey.server.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.CertificateRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.CertificateRequest;
import com.nxtlife.mgs.view.CertificateResponse;
import com.nxtlife.mgs.view.ClubMembershipResponse;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private ActivityPerformedService activityPerformedService;
	
	@Autowired
	private ActivityService activityService;


	@PostMapping(value = "/student/signUp")
	public StudentResponse signUpStudent(@RequestBody StudentRequest studentRequest) {
		return studentService.save(studentRequest);
	}

	@PostMapping("api/student")
	public StudentResponse saveStudent(@Valid @RequestBody StudentRequest studentRequest) {
		return studentService.save(studentRequest);
	}

	@PutMapping("api/student/{cid}")
	public StudentResponse update(@RequestBody StudentRequest request, @PathVariable(value = "cid",required = false) String cid) {
		return studentService.update(request, cid);
	}

	@GetMapping("api/students")
	public List<StudentResponse> getAll(@RequestParam(name = "schoolId", required = false) String schoolId,
			@RequestParam(name = "gradeId", required = false) String gradeId) {
		return studentService.getAllBySchoolIdOrGradeIdOrBothOrNoneButAll(schoolId, gradeId);
	}


	@GetMapping("api/student/{cId}")
	public StudentResponse findByCId(@PathVariable String cId) {
		return studentService.findByCId(cId);
	}

	@GetMapping("/api/school/{id}/students")//doneGrade
	public List<StudentResponse> getAllBySchoolId(@PathVariable(value = "id" , required = false) String schoolCid) {
		return studentService.getAllBySchoolCid(schoolCid);
	}

	@GetMapping(value = "api/student/activity/{activityCid}")
	public List<StudentResponse> getAllStudentsBySchoolAndActivityAndCoachAndStatusReviewed(
			@RequestParam(value = "schoolId" , required = false) String schoolCid, @RequestParam("gradeId") String gradeCid,
			@PathVariable("activityCid") String activityCid, @RequestParam("teacherId") String teacherCid) {
		return studentService.getAllStudentsBySchoolAndActivityAndCoachAndStatusReviewed(schoolCid, gradeCid,
				activityCid, ApprovalStatus.VERIFIED.toString(), teacherCid);
	}
	
	@GetMapping(value = "api/students/activity")//doneGrade
	public List<StudentResponse> getAllStudentsOfSchoolForParticularActivity(@RequestParam(value = "schoolId" ,required = false) String schoolCid , @RequestParam("activityId") String activityCid ,@RequestParam(value = "teacherId" , required = false) String teacherId ){
		return studentService.getAllStudentsOfSchoolForParticularActivity(schoolCid ,activityCid,teacherId ,ApprovalStatus.VERIFIED.toString());
	}

	@DeleteMapping("api/student/{cid}")
	public SuccessResponse delete(@PathVariable(value = "cid" , required = false) String cid) {
		return studentService.delete(cid);
	}
	
	@PutMapping("api/student/{cid}/profilePic")
	public StudentResponse setProfilePic(@RequestParam("profilePic") MultipartFile file, @PathVariable(value = "cid" ,required = false) String studentCid) {
		return studentService.setProfilePic(file, studentCid);
	}
	
	@PostMapping(value = "api/student/certificate" , consumes = { "multipart/form-data" })
	public CertificateResponse uploadCertificate(@Validated @ModelAttribute CertificateRequest certificateRequest , @RequestParam(value = "studentId" ,required = false) String studentId) {
		return studentService.uploadCertificate(certificateRequest , studentId);
	}
	
	@GetMapping(value = "api/student/certificates" )
	public List<CertificateResponse> getAllCertificatesOfStudent(@RequestParam(value = "studentId" ,required = false) String studentId){
		return studentService.getAllCertificatesOfStudent(studentId);
	}
	
	@GetMapping(value = "api/award/students") //doneGrade
	public Set<StudentResponse> getAllStudentsAndItsActivitiesByAwardCriterion(@RequestParam(value = "schoolId" ,required = false) String schoolCid ,@RequestParam(name = "awardCriterion") String awardCriterion ,@RequestParam(name = "criterionValue") String criterionValue, @RequestParam(name = "gradeId" , required = false) String gradeId,@RequestParam(name = "startDate" , required = false) String startDate ,@RequestParam(name = "endDate" , required = false) String endDate){
		return studentService.getAllStudentsAndItsActivitiesByAwardCriterion(schoolCid,awardCriterion, criterionValue, gradeId,startDate ,endDate);
	}
	
	@PostMapping(value = "api/student/club")
	public ClubMembershipResponse applyForClubMembership(@RequestParam(value = "studentId" , required = false) String studentId ,@RequestParam("activityId") String activityId ,@RequestParam("supervisorId") String supervisorId) {
		return studentService.applyForClubMembership(studentId ,activityId, supervisorId);
	}
	
	@GetMapping(value = "api/student/clubs")
	public List<ActivityRequestResponse> getAllClubsOfStudent(@RequestParam(value = "studentId" , required = false) String studentId){
		return activityService.getAllClubsOfStudent(studentId);
	}
	
	@GetMapping(value = "api/student/club/membershipStatus")
	public List<ClubMembershipResponse> getMembershipDetails(@RequestParam(value = "studentId" , required = false) String studentId){
		return studentService.getMembershipDetails(studentId);
	}

}
