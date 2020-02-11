package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.service.GradeService;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.view.GradeRequest;
import com.nxtlife.mgs.view.GradeResponse;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

	@Autowired
	GradeService gradeService;

	@RequestMapping(value = "importGrades", method = RequestMethod.POST)
	public List<GradeResponse> uploadGradesFromExcel(@RequestParam("file") MultipartFile file) {
		return gradeService.uploadGradesFromExcel(file);
	}

	@PostMapping()
	public GradeResponse saveGrade(@RequestBody GradeRequest gradeRequest) {
		return gradeService.save(gradeRequest);
	}
}
