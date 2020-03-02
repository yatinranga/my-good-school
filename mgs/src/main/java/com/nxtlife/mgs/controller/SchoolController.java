package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.Mail;
import com.nxtlife.mgs.view.MailRequest;
import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class SchoolController {

	@Autowired
	SchoolService schoolService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping(consumes = {"multipart/form-data"},value = "api/schools")
	public SchoolResponse save(@ModelAttribute SchoolRequest schoolRequest) {
		return schoolService.save(schoolRequest);
	}
	
	@GetMapping(value="api/schools/{id}")
	public SchoolResponse getByCid(@PathVariable("id") String cid) {
		return schoolService.findByCid(cid);
	}
	
	@GetMapping("schools")
	public List<SchoolResponse> getAll(){
//		MailRequest request = new MailRequest("Test email", "testing email sending api", null, "laxmi.ssj4@gmail.com", "vtsefkon@gmail.com");
//		userService.sendLoginCredentials(request);
//		Mail mail = new Mail();
//		mail.setMailFrom("laxminath@nxtlifetechnologies.com");
//        mail.setMailTo("laxmi.ssj4@gmail.com");
//        mail.setMailSubject("Spring Boot - Email Example");
//        mail.setMailContent("Learn How to send Email using Spring Boot!!!\n\nThanks\nwww.technicalkeeda.com");
//        userService.sendLoginCredentialsBySMTP(mail);

		return schoolService.getAllSchools();
	}
	
	@DeleteMapping(value = "api/schools/{id}")
	public SuccessResponse delete(@PathVariable("id") String cid) {
		return schoolService.delete(cid);
	}
	
}
