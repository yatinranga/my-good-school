package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/")
public class SchoolController {

	@Autowired
	SchoolService schoolService;
	
	@Autowired
	TeacherService teacherService;

	@PostMapping(consumes = { "multipart/form-data" }, value = "school/signUp")
	public SchoolResponse signUp(@ModelAttribute SchoolRequest schoolRequest) {
		return schoolService.save(schoolRequest);
	}

	@PostMapping(consumes = { "multipart/form-data" }, value = "api/school")
	public SchoolResponse save(@ModelAttribute SchoolRequest schoolRequest) {
		return schoolService.save(schoolRequest);
	}

	@GetMapping(value = "api/school/{id}")
	public SchoolResponse getByCid(@PathVariable("id") String cid) {
		return schoolService.findByCid(cid);
	}

	@GetMapping("schools")
	public List<SchoolResponse> getAll() {
		return schoolService.getAllSchools();
	}

	@PostMapping(consumes = { "multipart/form-data" }, value = "api/school/{cid}")
	public SchoolResponse update(@ModelAttribute SchoolRequest request, @PathVariable("cid") String cid) {
		return schoolService.update(request, cid);
	}

	@PutMapping(value = "api/teacher/assignActivity")
	public TeacherResponse addOrRemoveActivitiesToTeachers(@RequestBody TeacherRequest request) {
		return teacherService.addOrRemoveActivitiesToTeachers(request);
	}
	
	@DeleteMapping(value = "api/school/{id}")
	public SuccessResponse delete(@PathVariable("id") String cid) {
		return schoolService.delete(cid);
	}

}
