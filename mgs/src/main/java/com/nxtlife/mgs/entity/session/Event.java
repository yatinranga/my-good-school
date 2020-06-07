package com.nxtlife.mgs.entity.session;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.SessionStatus;
import com.nxtlife.mgs.util.DateUtil;

@SuppressWarnings("serial")
@Entity
@Table(name = "event",uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id","startDate","club_id"})})
public class Event extends BaseEntity{
	
	@NotNull
	@Column(unique = true)
	private String cid;
	
	private Long number;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	private String title;
	
//	@Enumerated(EnumType.STRING)
//	private SessionStatus status;
	
	@NotNull
	@OneToOne
	private Activity club;
	
	@NotNull
	@ManyToMany
	@JoinTable(name = "session_grade", joinColumns = { @JoinColumn(name = "session_id") }, inverseJoinColumns = {
			@JoinColumn(name = "grade_id") },uniqueConstraints = {@UniqueConstraint(columnNames = {"session_id","grade_id"})})
	private List<Grade> grades ;
	
	@NotNull
	@ManyToOne
	private Teacher teacher ;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
	private List<File> files;
	
	@Column(columnDefinition = "TEXT ")
	private String description;
	
	@Transient
	private LocalDate startLocalDate;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Activity getClub() {
		return club;
	}

	public void setClub(Activity club) {
		this.club = club;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public LocalDate getStartLocalDate(TimeZone zone) {
		zone = zone == null ? DateUtil.defaultTimeZone : zone;
		return DateUtil.convertToLocalDate(this.startDate ,zone);
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Event() {
	}

	public Event(String cid, Long number, Date startDate, Date endDate, String title,
			Boolean active, Activity club, List<Grade> grades, Teacher teacher) {
		this.cid = cid;
		this.number = number;
		this.startDate = startDate;
		this.endDate = endDate;
		this.title = title;
		this.setActive(active);
		this.club = club;
		this.grades = grades;
		this.teacher = teacher;
	}
	
}
