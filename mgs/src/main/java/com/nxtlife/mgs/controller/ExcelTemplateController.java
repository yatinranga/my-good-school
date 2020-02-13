package com.nxtlife.mgs.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.ExcelTemplateService;
import com.nxtlife.mgs.service.FocusAreaService;
import com.nxtlife.mgs.service.GradeService;
import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.FocusAreaRequestResponse;
import com.nxtlife.mgs.view.GradeResponse;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.TeacherResponse;

@RestController
@RequestMapping("/api/template")
public class ExcelTemplateController {

	@Autowired
	ExcelTemplateService excelTemplateService;
	
	@Autowired
	TeacherService teacherService;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	SchoolService schoolService;
	
	@Autowired
	GradeService gradeService;
	
	@Autowired
	FocusAreaService focusAreaService;
	
	@Autowired
	ActivityService activityService;

	@GetMapping("/export")
	public void exportExampleTemplate(@RequestParam String type , HttpServletResponse response) throws IOException {
		if(type.equalsIgnoreCase("STUDENT"))
			exportExampleFile("STUDENT", response);
		else if(type.equalsIgnoreCase("TEACHER"))
			exportExampleFile("TEACHER", response);
		else if(type.equalsIgnoreCase("COACH"))
			exportExampleFile("COACH", response);
		else if(type.equalsIgnoreCase("USER"))
			exportExampleFile("USER", response);
		else if(type.equalsIgnoreCase("SCHOOL"))
			exportExampleFile("SCHOOL", response);
		else if(type.equalsIgnoreCase("MANAGEMENT"))
			exportExampleFile("MANAGEMENT", response);
		else if(type.equalsIgnoreCase("LFIN"))
			exportExampleFile("LFIN", response);
		else if(type.equalsIgnoreCase("GRADE"))
			exportExampleFile("GRADE", response);
		else if(type.equalsIgnoreCase("ACTIVITY"))
			exportExampleFile("ACTIVITY", response);
		else if(type.equalsIgnoreCase("FOCUS AREA"))
			exportExampleFile("FOCUS AREA", response);
		else
			System.out.println("Invalid type.");
		
		
	}
	
	@RequestMapping(value = "bulkUpload", method = RequestMethod.POST)
	public void uploadTeachersFromExcel(@RequestParam("file") MultipartFile file , @RequestParam("type") String type ,@RequestParam(required = false ,value = "schoolId") String schoolCid) {
		if(type.equalsIgnoreCase("TEACHER"))
			 teacherService.uploadTeachersFromExcel(file, false,schoolCid);
		else if(type.equalsIgnoreCase("COACH"))
			 teacherService.uploadTeachersFromExcel(file, true,schoolCid);
		else if(type.equalsIgnoreCase("STUDENT"))
			studentService.uploadStudentsFromExcel(file,schoolCid);
		else if(type.equalsIgnoreCase("SCHOOL"))
			schoolService.uploadSchoolsFromExcel(file);
		else if(type.equalsIgnoreCase("GRADE"))
			gradeService.uploadGradesFromExcel(file);
		else if(type.equalsIgnoreCase("FOCUS AREA"))
			focusAreaService.uploadFocusAreasFromExcel(file);
		else if(type.equalsIgnoreCase("ACTIVITY"))
			activityService.uploadActivityFromExcel(file);
	}


    public void exportExampleFile(String type, HttpServletResponse response) throws IOException {
        File file;
        file = excelTemplateService.exportExampleFile(type);
        if(file== null)
        	 throw new ValidationException("undefined upload type");
        String filename = type.toUpperCase()+".xlsx";
        response.setContentType("text/csv");
        response.setHeader("Content-disposition", String.format("attachment; filename = %s", filename));
        response.setHeader("fileName", filename);
        FileInputStream is = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        IOUtils.copy(is, out);
        out.flush();
        is.close();
        if (!file.delete()) {
            throw new IOException("Could not delete temporary file after processing: " + file);
        }


    }
}
