package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;

@RestController
@RequestMapping("/")
public class AwardController {

	@Autowired
	AwardService awardService;
	
	@PostMapping("api/awards")
	public AwardResponse createAward(@RequestBody AwardRequest request) {
		return awardService.createAward(request);
	}
	
	@GetMapping("api/awards")
	public List<AwardResponse> getAllAwardsBySchool(@RequestParam("schoolId") String schoolCid){
		return awardService.getAllAwardsBySchool(schoolCid);
	}
}
