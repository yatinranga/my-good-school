package com.nxtlife.mgs.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

import com.nxtlife.mgs.filtering.filter.AwardFilter;
import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.PropertyCount;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class AwardController {

	@Autowired
	private AwardService awardService;
	
	@GetMapping("awardCriteria")
	public Set<String> getAwardCriteria(){
		return awardService.getAwardCriterias();
	}

	@PostMapping("api/award")
	public AwardResponse createAward(@RequestBody AwardRequest request) {
		return awardService.createAward(request);
	}
	
	@GetMapping("api/student/awards") //doneGrade 
	public List<AwardResponse> getAwardsByStudent(@RequestParam(value = "studentId" , required = false) String studentCid){
		return awardService.findAllByStudent(studentCid);
	}
	
	@GetMapping("api/teacher/awards") //doneGrade 
	public List<AwardResponse> getAwardsByManagement(@RequestParam(value = "schoolId" , required = false) String schoolCid ,@RequestParam(value = "teacherId" , required = false) String teacherCid){
		return awardService.findAllByManagement(schoolCid ,teacherCid);
	}

	@PutMapping("api/teacher/award/{awardId}") //done //toSend //Coordinator Head
	public AwardResponse updateStatus(@PathVariable String awardId, @RequestParam(name="verified",defaultValue="true") Boolean verified){
		return awardService.updateStatus(awardId, verified);
	}
	
	@PostMapping("api/student/awards")
	private List<AwardResponse> getAwardsByStudent(@RequestBody AwardFilter filter ,@RequestParam(value = "studentId" , required = false) String studentCid){
		return awardService.findAllByStudent(filter ,studentCid);
	}
	
	@PostMapping("api/teacher/awards")
	private List<AwardResponse> getAwardsByManagement(@RequestBody AwardFilter filter ,@RequestParam(value = "teacherId" , required = false) String teacherCid){
		return awardService.findAllByManagement(filter ,teacherCid);
	}
	
	@GetMapping(value = "api/student/{studentId}/awards/count" )
	public List<PropertyCount> getCount(@PathVariable(value = "studentId" ,required = false) String studentCid ,@RequestParam(name = "status" , required = false ,defaultValue = "Reviewed") String status ,@RequestParam("type") String type){
		return awardService.getCount(studentCid , status,type);
	}
	
	@GetMapping(value = "awardTypes")
	public Set<String> getAllAwardTypes(){
		return awardService.getAllAwardTypes();
	}
	
	@DeleteMapping(value = "api/awardType")
	public SuccessResponse deleteAwardType(@RequestParam("name") String name) {
		return awardService.deleteAwardType(name);
	}
	
	@PostMapping("api/awardType")
	public SuccessResponse createAwardType(@RequestParam("name") String name) {
		return awardService.createAwardType(name);
	}

	@GetMapping("api/awards")
	public Collection<AwardResponse> getAllAwardsForGradesUnderMe(@RequestParam(value = "schoolId" ,required = false) String schoolCid){
		return awardService.getAllAwardsForGradesUnderMe(schoolCid);
	}

}
