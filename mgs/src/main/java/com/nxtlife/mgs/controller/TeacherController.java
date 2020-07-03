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
	private TeacherService teacherService;
	
	@Autowired
	private ActivityService activityService;


	@PostMapping(value = {"api/teacher/" ,"teacher/signUp"})
	public TeacherResponse saveTeacher(@RequestBody TeacherRequest teacherRequest) {
		return teacherService.save(teacherRequest);
	}

//	@PostMapping("teacher/signUp")
//	public TeacherResponse signUp(@RequestBody TeacherRequest teacherRequest) {
//		if (teacherRequest.getIsCoach())
//			return teacherService.saveCoach(teacherRequest);
//		return teacherService.saveClassTeacher(teacherRequest);
//	}

	@PutMapping("api/teacher/update/{cid}")
	public TeacherResponse update(@RequestBody TeacherRequest request, @PathVariable(value = "cid" ,required = false) String cid) {
		return teacherService.update(request, cid);
	}
	
	@PutMapping("api/teacher/profilePic")
	public TeacherResponse setProfilePic(@RequestParam("profilePic") MultipartFile file,@RequestParam(value = "cid" ,required = false) String cid) {
		return teacherService.setProfilePic(file,cid);
	}
	
	@DeleteMapping("api/teacher/{cid}")
	public SuccessResponse delete(@PathVariable(value = "cid" ,required = false) String cid) {
		return teacherService.delete(cid);
	}

	@GetMapping("api/teachers")
	public List<TeacherResponse> getAllTeachers(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "20") Integer pageSize) {
		return teacherService.getAllTeachers(pageNo, pageSize);
	}
	
	@GetMapping(value = "api/teachers/{schoolId}")
	public List<TeacherResponse> getAllTeachersBySchool(@PathVariable(value = "schoolId" ,required = false) String schoolId){
		return teacherService.getAllTeachersOfSchool(schoolId);
	}

//	@GetMapping(value = "q" +
//			"" +
//			"classTeachers")
//	public List<TeacherResponse> getAllClassTeachers() {
//		return teacherService.getAllClassTeachers();
//	}

	@GetMapping(value = "api/teacher/{cId}")
	public TeacherResponse getTeacherByCId(@PathVariable(value = "cId" , required = false) String cId) {
		return teacherService.findByCId(cId);
	}

//	@GetMapping(value = "api/teacher/classTeacher/{cId}")
//	public TeacherResponse getClassTeacherByCId(@PathVariable("cId") String cId) {
//		return teacherService.findClassTeacherByCId(cId);
//	}

//	@GetMapping(value = "api/managment/{schoolCid}")
//	public List<TeacherResponse> getAllManagmentBySchool(@PathVariable("schoolCid") String schoolCid) {
//		return teacherService.getAllManagmentBySchool(schoolCid);
//	}
	
	@GetMapping(value = "api/teacher/club/members") //doneGrade //toSend
	public List<ClubMembershipResponse> getMembershipDetails(@RequestParam(value = "teacherId" , required = false) String teacherId ,@RequestParam(value = "schoolId" , required = false) String schoolCid){
		return teacherService.getMembershipDetails(teacherId ,schoolCid);
	}
	
	@GetMapping(value = "api/teacher/club/{clubId}/members") //doneGrade //toSend
	public List<ClubMembershipResponse> getMembershipDetailsbyClub(@PathVariable("clubId") String clubId ,@RequestParam(value = "teacherId" , required = false) String teacherId ,@RequestParam(value = "schoolId" , required = false) String schoolCid){
		return teacherService.getMembershipDetailsbyClub(clubId,teacherId,schoolCid);
	}
	
	@GetMapping(value = "api/teacher/clubs")//doneGrade 
	public List<ActivityRequestResponse> getAllClubsOfTeacher(@RequestParam(value = "teacherId" , required = false) String teacherId ,@RequestParam(value = "schoolId" , required = false) String schoolId){
		return activityService.getAllClubsOfTeacher(teacherId,schoolId);
	}
	
	@PutMapping(value = "api/teacher/club") //doneGrade //toSend
	public ClubMembershipResponse updateStatus(@RequestParam(name = "studentId")  String studentId,@RequestParam(name = "activityId") String activityId , @RequestParam(name="verified",defaultValue="true") Boolean verified ,@RequestParam(value = "teacherId" , required = false) String teacherId) {
		return teacherService.updateStatus(studentId, activityId, verified ,teacherId);
	}

}
