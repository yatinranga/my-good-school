package com.nxtlife.mgs.controller;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/api/")
public class CoachController {

	@Autowired
	private TeacherService teacherService;

	@GetMapping(value = { "coaches/{schoolId}/{activityId}", "coaches/activity/{activityId}" }) // doneGrade
	public List<TeacherResponse> getAllCoachesBySchoolCidAndActivityCid(
			@PathVariable(value = "schoolId", required = false) String schoolCid,
			@PathVariable("activityId") @NotNull(message = "Activity Id cannot be null.") String activityCid) {
		return teacherService.findCoachesBySchoolCidAndActivityCid(schoolCid, activityCid);
	}

//	@GetMapping(value = "coaches")
//	public List<TeacherResponse> getAllCoaches(){
//		return teacherService.getAllCoaches();
//	} 

	@GetMapping(value = { "coaches/{schoolId}", "coaches" }) // doneGrade
	public List<TeacherResponse> getAllCoachesBySchool(
			@PathVariable(value = "schoolId", required = false) String schoolId /*
																				 * , @RequestParam(value= "gradeId",
																				 * required = false) String gradeId
																				 */) {
		return teacherService.getAllCoachesOfSchool(schoolId /* ,gradeId */);
	}

	@GetMapping(value = "coach/{cId}")
	public TeacherResponse getCoachByCId(@PathVariable(value = "cId", required = false) String cId) {
		return teacherService.findCoachByCId(cId);
	}

	@GetMapping(value = "coaches/student/activity/{activityId}")
	public Collection<TeacherResponse> getCoachesOfActivityForStudent(@PathVariable("activityId") String activityCid) {
		return teacherService.getCoachesOfActivityForStudent(activityCid);
	}

//	@PostMapping("coaches")
//	public TeacherResponse saveCoach(@RequestBody TeacherRequest teacherRequest) {
//			return teacherService.saveCoach(teacherRequest);
//	}

}
