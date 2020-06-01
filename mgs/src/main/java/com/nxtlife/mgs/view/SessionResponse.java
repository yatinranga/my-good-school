package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.session.Event;
import com.nxtlife.mgs.enums.SessionStatus;
import com.nxtlife.mgs.util.DateUtil;

@JsonInclude(content = Include.NON_ABSENT)
public class SessionResponse {

	private String id;

	private String startDay;

	private String endDay;

	private Long number;

	private String startDate;

	private String endDate;

	private String title;

	private ActivityRequestResponse club;

	private List<GradeResponse> grades;

	private String teacherId;

	private String teacherName;

	private String status;

	private String description;

	private List<FileResponse> fileResponses;

	private List<GroupResponseBy<SessionResponse>> sessions;

	public String getId() {
		return id;
	}

	public String getStartDay() {
		return startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public Long getNumber() {
		return number;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getTitle() {
		return title;
	}

	public ActivityRequestResponse getClub() {
		return club;
	}

	public List<GradeResponse> getGrades() {
		return grades;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public String getStatus() {
		return status;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setClub(ActivityRequestResponse club) {
		this.club = club;
	}

	public void setGrades(List<GradeResponse> grades) {
		this.grades = grades;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GroupResponseBy<SessionResponse>> getSessions() {
		return sessions;
	}

	public void setSessions(List<GroupResponseBy<SessionResponse>> sessions) {
		this.sessions = sessions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FileResponse> getFileResponses() {
		return fileResponses;
	}

	public void setFileResponses(List<FileResponse> fileResponses) {
		this.fileResponses = fileResponses;
	}

	public SessionResponse() {

	}

	public SessionResponse(Event session) {
		this.id = session.getCid();
		this.title = session.getTitle();
		this.number = session.getNumber();
		if (session.getStartDate() != null)
			this.startDate = DateUtil.formatDate(session.getStartDate());
		if (session.getEndDate() != null)
			this.endDate = DateUtil.formatDate(session.getEndDate());
		Date now = LocalDateTime.now().toDate();
		if (session.getStartDate() != null && session.getEndDate() != null) {

			startDay = LocalDateTime.fromDateFields(session.getStartDate()).dayOfWeek().getAsShortText();
			endDay = LocalDateTime.fromDateFields(session.getEndDate()).dayOfWeek().getAsShortText();

			if (session.getStartDate().before(now))
				if (session.getEndDate().before(now))
					status = SessionStatus.ended.toString();
				else
					status = SessionStatus.running.toString();
			else if (session.getStartDate().compareTo(now) == 0)
				status = SessionStatus.running.toString();
			else
				status = SessionStatus.upcoming.toString();
		}
		if (session.getTeacher() != null) {
			this.teacherId = session.getTeacher().getCid();
			this.teacherName = session.getTeacher().getName();
		}
		if (session.getClub() != null)
			this.club = new ActivityRequestResponse(session.getClub());
		this.description = session.getDescription();
		if(session.getFiles() != null)
			this.fileResponses = session.getFiles().stream().filter(f -> f.getActive()).distinct().map(FileResponse::new).collect(Collectors.toList());
		if(session.getGrades() != null)
			this.grades = session.getGrades().stream().map(GradeResponse::new).distinct().collect(Collectors.toList());
	}
}
