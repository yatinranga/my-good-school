package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class ActivityPerformedController {

	@Autowired
	ActivityPerformedService activityPerformedService;

	@PostMapping(value = "api/student/activities")
	public ActivityPerformedResponse saveActivity(@ModelAttribute ActivityPerformedRequest request) {
		return activityPerformedService.saveActivity(request);
	}

	@PostMapping(value = "api/student/{actCid}/submit")
	public ActivityPerformedResponse submitActivity(@PathVariable("actCid") String activityPerformedCid) {
		return activityPerformedService.submitActivity(activityPerformedCid);
	}

	@DeleteMapping(value = "api/student/activity/{activityPerformedId}")
	public SuccessResponse deleteActivityOfStudent(@PathVariable("activityPerformedId") String activityPerformedCid) {
		return activityPerformedService.deleteActivityOfStudent(activityPerformedCid);
	}

	@PostMapping(value = "api/coach/save")
	public ActivityPerformedResponse saveActivityByCoach(
			@ModelAttribute /* Change it to ModelAttribute */ ActivityPerformedRequest request) {
		return activityPerformedService.saveActivityByCoach(request);
	}

	@PostMapping(value = "api/coach/{actCid}/submit")
	public ActivityPerformedResponse reviewByCoach(@PathVariable("activityPerformedId") String activityPerformedCid) {
		return activityPerformedService.submitActivityByCoach(activityPerformedCid);
	}

	@GetMapping(value = "api/coach/activities")
	public List<ActivityPerformedResponse> getAllPendingActivitiesByCoach(@RequestParam("coachId") String coachCid ,@RequestParam("status") String status) {
		return activityPerformedService.getAllActivitiesAssignedToCoachforReview(coachCid , status);
	}

	@GetMapping(value = "api/coach/activities/filterByClass")
	public List<ActivityPerformedResponse> getAllPendingActivitiesByClass(@RequestParam("coachId") String coachCid,
			@RequestParam("classId") String gradeCid) {
		return activityPerformedService.getAllPendingActivitiesByClass(coachCid, gradeCid);
	}

	@GetMapping(value = "api/coach/activities/filterByService")
	public List<ActivityPerformedResponse> getAllPendingActivitiesByActivity(@RequestParam("coachId") String coachCid,
			@RequestParam("activityId") String activityCid) {
		return activityPerformedService.getAllPendingActivitiesByService(coachCid, activityCid);
	}

	@GetMapping(value = "api/coach/activities/filterByClassAndService")
	public List<ActivityPerformedResponse> getAllPendingActivitiesByClassAndActivity(
			@RequestParam("coachId") String coachCid, @RequestParam("classId") String gradeCid,
			@RequestParam("activityId") String activityCid) {
		return activityPerformedService.getAllPendingActivitiesByClassAndService(coachCid, gradeCid, activityCid);
	}

	@GetMapping(value = "api/student/activities")
	public List<ActivityPerformedResponse> getAllActivitiesOfStudentByStatus(
			@RequestParam(value = "status", required = false) String status,
			@RequestParam("studentId") String studentCid, @RequestParam(value = "page", required = false ,defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false ,defaultValue = "10") Integer pageSize) {
		return activityPerformedService.getAllActivitiesOfStudentByStatus(status, studentCid, page, pageSize);
	}

	@GetMapping(value = "api/student/activities/filterByFourS")
	public List<ActivityPerformedResponse> filterActivitiesByFourS(@RequestParam("studentId") String studentCid,
			@RequestParam("fourS") String fourS, @RequestParam("status") String status) {
		return activityPerformedService.findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(studentCid,
				fourS, status);
	}

	@GetMapping(value = "api/student/activities/filterByFocusArea")
	public List<ActivityPerformedResponse> filterActivitiesByFocusArea(@RequestParam("studentId") String studentCid,
			@RequestParam("focusArea") String focusAreaCid, @RequestParam("status") String activityStatus) {
		return activityPerformedService.findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(
				studentCid, focusAreaCid, activityStatus);
	}

	@GetMapping(value = "api/students/activities/filterByCoach")
	public List<ActivityPerformedResponse> filterActivitiesByCoach(@RequestParam("studentId") String studentCid,
			@RequestParam("coachId") String teacherCid, @RequestParam("status") String activityStatus) {
		return activityPerformedService.findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(studentCid,
				teacherCid, activityStatus);
	}

	@GetMapping(value = "api/students/activities/filterByPsdArea")
	public List<ActivityPerformedResponse> filterActivitiesByPsdArea(@RequestParam("studentId") String studentCid,
			@RequestParam("psdArea") String psdArea, @RequestParam("status") String activityStatus) {
		return activityPerformedService.findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(
				studentCid, psdArea, activityStatus);
	}

	@GetMapping(value = "api/students/activities/filterByYear")
	public List<ActivityPerformedResponse> filterActivitiesByYearPerformed(@RequestParam("year") String year,
			@RequestParam("studentId") String studentId) {
		return activityPerformedService.filterActivityByYearPerformed(year, studentId);
	}

}
