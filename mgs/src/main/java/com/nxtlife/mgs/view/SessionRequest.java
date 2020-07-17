package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import com.nxtlife.mgs.entity.session.Event;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.util.DateUtil;

public class SessionRequest extends Request {

	private String id;

	@NotNull(message = "session number cannot be null or empty")
	private Long number;

	@NotEmpty(message = "Session start date cannot be null or empty")
	private String startDate;

	@NotEmpty(message = "Session end date cannot be null or empty")
	private String endDate;

	private String title;

	@NotEmpty(message = "club id cannot be null or empty")
	private String clubId;

	@NotEmpty(message = "grade ids for session cannot be null or empty.")
	private List<String> gradeIds;

	private String description;

	private List<FileRequest> fileRequests;

//	@NotEmpty(message = "teacher id cannot be null or empty.")
//	private String teacherId;

	public String getId() {
		return id;
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

	public String getClubId() {
		return clubId;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public String getDescription() {
		return description;
	}

	public List<FileRequest> getFileRequests() {
		return fileRequests;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public void setFileRequests(List<FileRequest> fileRequests) {
		this.fileRequests = fileRequests;
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

	public void setClubId(String clubId) {
		this.clubId = clubId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Event toEntity() {
		return toEntity(null);
	}

	public Event toEntity(Event session) {
		session = session == null ? new Event() : session;

		if (this.title != null)
			session.setTitle(this.title);
		if (this.number != null)
			session.setNumber(this.number);
		if (this.description != null)
			session.setDescription(this.description);
		LocalDateTime currentDateTime = LocalDateTime.now(DateTimeZone.forTimeZone(DateUtil.defaultTimeZone));
		if ((session.getStartDate() != null && session.getStartDate().before(currentDateTime.toDate()))
				|| (session.getEndDate() != null && session.getEndDate().before(currentDateTime.toDate())))
			throw new ValidationException("The session has already begun or had end , so it can't be edited now.");

		if ((startDate != null && DateUtil.convertStringToDate(startDate).before(currentDateTime.toDate()))
				|| (endDate != null && DateUtil.convertStringToDate(endDate).before(currentDateTime.toDate())))
			throw new ValidationException("StartDate or endDate cannot be a past date.");

		Date startDate, endDate;
		if (this.startDate == null && this.endDate == null) {

		} else if (this.startDate == null && this.endDate != null) {
			endDate = DateUtil.convertStringToDate(this.endDate);
			if (endDate.before(session.getStartDate()))
				throw new ValidationException("End date cannot be before startDate.");
			session.setEndDate(endDate);
		} else if (this.startDate != null && this.endDate == null) {
			startDate = DateUtil.convertStringToDate(this.startDate);
			if (startDate.after(session.getEndDate()))
				throw new ValidationException("startDate cannot be after endDate.");
			session.setStartDate(startDate);
		} else {
			startDate = DateUtil.convertStringToDate(this.startDate);
			endDate = DateUtil.convertStringToDate(this.endDate);
			if (startDate.after(endDate))
				throw new ValidationException("startDate shall fall before end date.");
			session.setStartDate(startDate);
			session.setEndDate(endDate);
		}

		return session;
	}

}
