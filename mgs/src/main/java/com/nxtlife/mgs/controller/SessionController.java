package com.nxtlife.mgs.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.nxtlife.mgs.filtering.filter.SessionFilter;
import com.nxtlife.mgs.service.SessionService;
import com.nxtlife.mgs.view.SessionRequest;
import com.nxtlife.mgs.view.SessionResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@RestController
@RequestMapping("/")
public class SessionController {
	@Autowired
	private SessionService sessionService;

	@PostMapping(value = "api/session")
	public SessionResponse createSession(@ModelAttribute @Validated SessionRequest request) {
		return sessionService.createSession(request);
	}

	@GetMapping(value = "api/sessions")
	public List<SessionResponse> getSessions(@RequestBody SessionFilter filter){
		return sessionService.getSessions(filter);
	}

	//api/student/sessions and api/student/sessions/club/{clubId}
	
	@GetMapping(value = "api/student/sessions/club/{clubId}")
	public SessionResponse getStudentSessionsOfClubBy(@PathVariable("clubId") String clubId,@RequestParam(name = "sessionFetch",required = false) String sessionFetch, @RequestParam(value = "teacherId" ,required = false) String teacherId, @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		return sessionService.getStudentSessionsOfClubBy(clubId, sessionFetch,teacherId, page, pageSize);
	}
	
	@GetMapping(value = "api/student/sessions")
	public SessionResponse getStudentSessionsOfClubsBy(@RequestParam(name = "sessionFetch",required = false) String sessionFetch, @RequestParam(value = "teacherId" ,required = false) String teacherId ,@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		return sessionService.getStudentSessionsOfClubsBy(sessionFetch,teacherId, page, pageSize);
	}
	
	@GetMapping(value = "api/teacher/sessions/club/{clubId}")
	public  SessionResponse getTeacherSessionsOfClubBy(@PathVariable("clubId") String clubId, @RequestParam(name = "sessionFetch",required = false) String sessionFetch,@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		return sessionService.getTeacherSessionsOfClubBy(clubId, sessionFetch, page, pageSize);
	}
	
	@GetMapping(value = "api/teacher/sessions")
	public SessionResponse getTeacherSessionsOfClubsBy(@RequestParam(name = "sessionFetch",required = false) String sessionFetch,@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
		return sessionService.getTeacherSessionsOfClubsBy(sessionFetch, page, pageSize);
	}
	
	@PostMapping(value = "api/session/update")
	public SessionResponse updateSession(@ModelAttribute SessionRequest request) {
		return sessionService.updateSession(request);
	}
	
	@DeleteMapping(value = "api/session/{sessionId}")
	public SuccessResponse deleteSession(@PathVariable("sessionId") String sessionId) {
		return sessionService.deleteSession(sessionId);
	}
}
