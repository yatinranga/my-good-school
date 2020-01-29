package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.user.Teacher;

public class TeacherResponse {
	
	private String id;
	private String name;
	private String username;
	private String userId;
	private String gender;
	private String mobileNumber;
	private String email;
	private Date dob;
	private String qualification;
	private List<String> grades;
	private List<String> activities;
	private String schoolName;
	private Boolean active;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public List<String> getGrades() {
		return grades;
	}
	public void setGrades(List<String> grades) {
		this.grades = grades;
	}
	public List<String> getActivities() {
		return activities;
	}
	public void setActivities(List<String> activities) {
		this.activities = activities;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public TeacherResponse(Teacher teacher) {
		this.id = teacher.getcId();
		if(teacher.getUser() != null)
		  this.userId = teacher.getUser().getcId();
		this.username = teacher.getUsername();
		this.name = teacher.getName();
		this.gender = teacher.getGender();
		this.mobileNumber = teacher.getMobileNumber();
		this.email = teacher.getEmail();
		this.dob = teacher.getDob();
		this.qualification = teacher.getQualification();
		this.active = teacher.getActive();
		if(teacher.getSchool() != null)
		   this.schoolName = teacher.getSchool().getName();   
		if(teacher.getGrades() != null && !teacher.getGrades().isEmpty())
		{
			if(grades == null)
				grades = new ArrayList<String>();
			for(Grade grade : teacher.getGrades() ) {
				grades.add(String.format("%s %s", grade.getName() , grade.getSection()));
			}
		}
		
		// change database for activity and then get all activity names and add to member list in the view
	}

}
