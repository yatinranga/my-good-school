package com.nxtlife.mgs.view;

import java.util.Date;

import com.nxtlife.mgs.entity.user.Student;

public class StudentResponse {

	private String id;
	private String name;
	private String username;
	private String userId;
	private String gender;
	private String email;
	private String section;
	private String grade;
	private String schoolId;
	private String mobileNumber;
	private Date subscriptionEndDate;
	private Boolean active;
	private Date dob;
	

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public StudentResponse(Student student) {
		this.id = student.getCid();
		this.name = student.getName();
		this.email = student.getEmail();
		this.gender = student.getGender();
		this.mobileNumber = student.getMobileNumber();
		if(student.getUser() != null)
		   this.userId = student.getUser().getCid();
		this.username = student.getUsername();
		this.active = student.getActive();
		this.dob = student.getDob();
		if (student.getGrade() != null) {
			this.grade = student.getGrade().getName();
			this.section = student.getGrade().getSection();
		}
		if (student.getSchool() != null)
			this.schoolId = student.getSchool().getCid();
		this.subscriptionEndDate = student.getSubscriptionEndDate();
	}

	public StudentResponse() {

	}

}
