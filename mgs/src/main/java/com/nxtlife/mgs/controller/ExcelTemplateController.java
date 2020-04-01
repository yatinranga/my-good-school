package com.nxtlife.mgs.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.nxtlife.mgs.service.LFINService;
import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.service.TeacherService;

@RestController
@RequestMapping("/")
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
	
	@Autowired
	LFINService lFINService;

	@GetMapping("template/export")
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
	
	@Transactional
	@RequestMapping(value = "/api/template/bulkUpload", method = RequestMethod.POST)
	public ResponseEntity<?> uploadTeachersFromExcel(@RequestParam("file") MultipartFile file , @RequestParam("type") String type ,@RequestParam(required = false ,value = "schoolId") String schoolCid) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");
		
		if(type.equalsIgnoreCase("TEACHER"))
			 return teacherService.uploadTeachersFromExcel(file, false,schoolCid);
		else if(type.equalsIgnoreCase("COACH"))
			 return teacherService.uploadTeachersFromExcel(file, true,schoolCid);
		else if(type.equalsIgnoreCase("STUDENT"))
			 return studentService.uploadStudentsFromExcel(file,schoolCid);
		else if(type.equalsIgnoreCase("SCHOOL"))
			 return schoolService.uploadSchoolsFromExcel(file);
		else if(type.equalsIgnoreCase("GRADE"))
			 return gradeService.uploadGradesFromExcel(file,schoolCid);
		else if(type.equalsIgnoreCase("FOCUS AREA"))
			return focusAreaService.uploadFocusAreasFromExcel(file);
		else if(type.equalsIgnoreCase("ACTIVITY"))
			return activityService.uploadActivityFromExcel(file ,schoolCid);
		else if(type.equalsIgnoreCase("MANAGEMENT"))
			return teacherService.uploadManagementFromExcel(file, schoolCid);
		else if(type.equalsIgnoreCase("LFIN"))
			return lFINService.uploadLFINFromExcel(file);
		else
			 return new ResponseEntity<String>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
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
