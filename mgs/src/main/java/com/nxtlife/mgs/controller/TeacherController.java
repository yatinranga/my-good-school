package com.nxtlife.mgs.controller;

import java.util.List;

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

import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.ClubMembershipResponse;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/")

public class TeacherController {

	@Autowired
	TeacherService teacherService;
	
	@Autowired
	ActivityService activityService;

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

	@PostMapping("api/teacher/")
	public TeacherResponse saveTeacher(@RequestBody TeacherRequest teacherRequest) {
		return teacherService.saveClassTeacher(teacherRequest);
	}

	@PostMapping("teacher/signUp")
	public TeacherResponse signUp(@RequestBody TeacherRequest teacherRequest) {
		if (teacherRequest.getIsCoach())
			return teacherService.saveCoach(teacherRequest);
		return teacherService.saveClassTeacher(teacherRequest);
	}

	@PutMapping("api/teacher/update/{cid}")
	public TeacherResponse update(@RequestBody TeacherRequest request, @PathVariable String cid) {
		return teacherService.update(request, cid);
	}
	
	@PutMapping("api/teacher/profilePic")
	public TeacherResponse setProfilePic(@RequestParam("profilePic") MultipartFile file) {
		return teacherService.setProfilePic(file);
	}
	
	@DeleteMapping("api/teacher/{cid}")
	public SuccessResponse delete(@PathVariable String cid) {
		return teacherService.delete(cid);
	}

	@GetMapping("api/teachers")
	public List<TeacherResponse> getAllTeachers(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		return teacherService.getAllTeachers(pageNo, pageSize);
	}
	
	@GetMapping(value = "api/teachers/{schoolId}")
	public List<TeacherResponse> getAllTeachersBySchool(@PathVariable("schoolId") String schoolId){
		return teacherService.getAllTeachersOfSchool(schoolId);
	}

	@GetMapping(value = "q" +
			"" +
			"classTeachers")
	public List<TeacherResponse> getAllClassTeachers() {
		return teacherService.getAllClassTeachers();
	}

	@GetMapping(value = "api/teacher/{cId}")
	public TeacherResponse getTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findByCId(cId);
	}

	@GetMapping(value = "api/teacher/classTeacher/{cId}")
	public TeacherResponse getClassTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findClassTeacherByCId(cId);
	}

	@GetMapping(value = "api/managment/{schoolCid}")
	public List<TeacherResponse> getAllManagmentBySchool(@PathVariable("schoolCid") String schoolCid) {
		return teacherService.getAllManagmentBySchool(schoolCid);
	}
	
	@GetMapping(value = "api/teacher/club/members")
	public List<ClubMembershipResponse> getMembershipDetails(){
		return teacherService.getMembershipDetails();
	}
	
	@GetMapping(value = "api/teacher/club/{clubId}/members")
	public List<ClubMembershipResponse> getMembershipDetailsbyClub(@PathVariable("clubId") String clubId){
		return teacherService.getMembershipDetailsbyClub(clubId);
	}
	
	@GetMapping(value = "api/teacher/clubs")
	public List<ActivityRequestResponse> getAllClubsOfTeacher(){
		return activityService.getAllClubsOfTeacher();
	}
	
	@PutMapping(value = "api/teacher/club")
	public ClubMembershipResponse updateStatus(@RequestParam(name = "studentId")  String studentId,@RequestParam(name = "activityId") String activityId , @RequestParam(name="verified",defaultValue="true") Boolean verified) {
		return teacherService.updateStatus(studentId, activityId, verified);
	}

}
