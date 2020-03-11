package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.filtering.filter.AwardFilter;
import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.PropertyCount;

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

	@PutMapping("/teacher/award/{awardId}")
	public AwardResponse updateStatus(@PathVariable String awardId, @RequestParam(name="verified",defaultValue="true") Boolean verified){
		return awardService.updateStatus(awardId, verified);
	}
	
	@PostMapping("/student/awards")
	private List<AwardResponse> getAwardsByStudent(@RequestBody AwardFilter filter){
		return awardService.findAllByStudent(filter);
	}
	
	@PostMapping("/teacher/awards")
	private List<AwardResponse> getAwardsByManagement(@RequestBody AwardFilter filter){
		return awardService.findAllByManagement(filter);
	}
	
	@GetMapping(value = "/student/{studentId}/awards/count" )
	public List<PropertyCount> getCount(@PathVariable("studentId") String studentCid ,@RequestParam(name = "status" , required = false ,defaultValue = "Reviewed") String status ,@RequestParam("type") String type){
		return awardService.getCount(studentCid , status,type);
	}


}
