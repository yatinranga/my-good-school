package com.nxtlife.mgs.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.service.GuardianService;
import com.nxtlife.mgs.view.GuardianRequest;
import com.nxtlife.mgs.view.GuardianResponse;
import com.nxtlife.mgs.view.StudentResponse;

@RestController
@RequestMapping("/")
public class GuardianController {

	@Autowired
	GuardianService guardianService;

	@PostMapping(value = "api/guardian")
	public GuardianResponse save(@Valid @RequestBody(required = true) GuardianRequest request) {
		return guardianService.save(request);
	}

	@PutMapping("api/guardian/profilePic")
	public GuardianResponse setProfilePic(@RequestParam("profilePic") MultipartFile file,
			@RequestParam(value = "id", required = false) String id) {
		return guardianService.setProfilePic(file, id);
	}

	@PutMapping("api/guardian")
	public GuardianResponse update(@RequestParam(value = "id", required = false) String id,
			@RequestBody(required = true) GuardianRequest request) {
		return guardianService.update(id, request);
	}

	@GetMapping("api/guardian/{id}")
	public GuardianResponse getById(@PathVariable("id") String id) {
		return guardianService.getById(id);
	}

	@GetMapping(value = "api/guardian/children")
	public List<StudentResponse> getAllChildrenOfGuardian(
			@RequestParam(value = "id", required = false) String guardianId) {
		return guardianService.getAllChildrenOfGuardian(guardianId);
	}
}
