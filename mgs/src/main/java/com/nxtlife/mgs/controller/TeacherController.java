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
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/api/teachers/")

public class TeacherController {

	@Autowired
	TeacherService teacherService;

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

	@PostMapping()
	public TeacherResponse saveTeacher(@RequestBody TeacherRequest teacherRequest) {
		return teacherService.saveClassTeacher(teacherRequest);
	}

	@PostMapping("signUp")
	public TeacherResponse signUp(@RequestBody TeacherRequest teacherRequest) {
		if (teacherRequest.getIsCoach())
			return teacherService.saveCoach(teacherRequest);
		return teacherService.saveClassTeacher(teacherRequest);
	}

	@PutMapping("update/{cid}")
	public TeacherResponse update(@RequestBody TeacherRequest request, @PathVariable String cid) {
		return teacherService.update(request, cid);
	}

	@GetMapping("/all")
	public List<TeacherResponse> getAllTeachers() {
		return teacherService.getAllTeachers();
	}

	@GetMapping(value = "classTeachers")
	public List<TeacherResponse> getAllClassTeachers() {
		return teacherService.getAllClassTeachers();
	}

	@GetMapping(value = "{cId}")
	public TeacherResponse getTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findByCId(cId);
	}

	@GetMapping(value = "classTeacher/{cId}")
	public TeacherResponse getClassTeacherByCId(@PathVariable("cId") String cId) {
		return teacherService.findClassTeacherByCId(cId);
	}

	@DeleteMapping("{cid}")
	public SuccessResponse delete(@PathVariable String cid) {
		return teacherService.delete(cid);
	}

}
