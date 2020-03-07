package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;

@RestController
@RequestMapping("/api/")
public class AwardController {

	@Autowired
	AwardService awardService;

	@PostMapping("award")
	public AwardResponse createAward(@RequestBody AwardRequest request) {
		return awardService.createAward(request);
	}
	
	@GetMapping("/student/awards")
	public List<AwardResponse> getAwardsByStudent(){
		return awardService.findAllByStudent();
	}
	
	@GetMapping("/teacher/awards")
	public List<AwardResponse> getAwardsByManagement(){
		return awardService.findAllByManagement();
	}

	// @PostMapping("/assign")
	// public AwardResponse assignAward(@RequestBody AwardRequest request) {
	// return awardService.assignAward(request);
	// }
	//
	// @GetMapping()
	// public List<AwardResponse> getAllAwardsBySchool(@RequestParam("schoolId")
	// String schoolCid) {
	// return awardService.getAllAwardsBySchool(schoolCid);
	// }
	//
	// @GetMapping("/unverified")
	// public List<AwardResponse>
	// getAllUnverifiedAwardsOfSchool(@RequestParam("schoolCid") String
	// schoolCid) {
	// return awardService.getAllUnverifiedAwardsOfSchool(schoolCid);
	// }
	//
	// @GetMapping("/unverified/solo")
	// public List<AwardResponse>
	// getAllSoloUnverifiedAwardsOfSchool(@RequestParam("schoolCid") String
	// schoolCid,
	// @RequestParam("awardCid") String awardCid) {
	// return awardService.getAllSoloUnverifiedAwardsOfSchool(schoolCid,
	// awardCid);
	// }
	//
	// @PostMapping("/verify")
	// public List<AwardResponse> verifyAwards(@RequestBody AwardRequest
	// request) {
	// return awardService.verifyAwards(request);
	// }
	//
	// @GetMapping("/year")
	// public List<AwardResponse>
	// filterAwardByYearPerformed(@RequestParam("year") String year,
	// @RequestParam("studentCid") String studentCid) {
	// return awardService.filterAwardByYearPerformed(year, studentCid);
	// }
	//
	// @GetMapping(value = "api/teacher/awards")
	// public List<AwardResponse> getAllAwardsAssignedByTeacher(AwardRequest
	// request){
	// return awardService.getAllAwardsAssignedByTeacher(request);
	// }

}
