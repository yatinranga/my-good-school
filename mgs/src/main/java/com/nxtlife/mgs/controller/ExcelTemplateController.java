package com.nxtlife.mgs.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.service.ExcelTemplateService;

@RestController
@RequestMapping("/api/template")
public class ExcelTemplateController {

	@Autowired
	ExcelTemplateService excelTemplateService;
	
	@GetMapping("/export/student")
	public void exportExampleStudentFile(HttpServletResponse response) throws IOException {
		exportExampleFile("STUDENT", response);
	}
	
	@GetMapping("/export/teacher")
	public void exportExampleTeacherFile(HttpServletResponse response) throws IOException {
		exportExampleFile("TEACHER", response);
	}
	
	@GetMapping("/export/coach")
	public void exportExampleCoachFile(HttpServletResponse response) throws IOException {
		exportExampleFile("COACH", response);
	}
	
	@GetMapping("/export/school")
	public void exportExampleSchoolFile(HttpServletResponse response) throws IOException {
		exportExampleFile("SCHOOL", response);
	}
	
	@GetMapping("/export/user")
	public void exportExampleUserFile(HttpServletResponse response) throws IOException {
		exportExampleFile("USER", response);
	}
	
	@GetMapping("/export/management")
	public void exportExampleManagementFile(HttpServletResponse response) throws IOException {
		exportExampleFile("MANAGEMENT", response);
	}
	
	@GetMapping("/export/lfin")
	public void exportExampleLfinFile(HttpServletResponse response) throws IOException {
		exportExampleFile("LFIN", response);
	}
	
	@GetMapping("/export/grade")
	public void exportExampleGradeFile(HttpServletResponse response) throws IOException {
		exportExampleFile("GRADE", response);
	}
	
	@GetMapping("/export/activity")
	public void exportExampleActivityFile(HttpServletResponse response) throws IOException {
		exportExampleFile("ACTIVITY", response);
	}
	
	@GetMapping("/export/focusArea")
	public void exportExampleFocusAreaFile(HttpServletResponse response) throws IOException {
		exportExampleFile("FOCUS AREA", response);
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
