package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.user.Teacher;

@JsonInclude(value = Include.NON_ABSENT)
public class TeacherResponse {

	private String id;
	private String name;
	private String username;
	private String userId;
	private String gender;
	private String mobileNumber;
	private String email;
	private String dob;
	private String qualification;
	private List<GradeResponse> grades;
	private List<String> activities;
	private String schoolName;
	private Boolean active;
	private String designation;
	private Boolean isManagmentMember;
	private Boolean isCoach;

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
	
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	
	public List<GradeResponse> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeResponse> grades) {
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Boolean getIsManagmentMember() {
		return isManagmentMember;
	}

	public void setIsManagmentMember(Boolean isManagmentMember) {
		this.isManagmentMember = isManagmentMember;
	}

	public Boolean getIsCoach() {
		return isCoach;
	}

	public void setIsCoach(Boolean isCoach) {
		this.isCoach = isCoach;
	}

	public TeacherResponse(Teacher teacher) {
		this.id = teacher.getcId();

		if (teacher.getUser() != null)
			this.userId = teacher.getUser().getCid();

		this.username = teacher.getUsername();
		this.name = teacher.getName();
		this.gender = teacher.getGender();
		this.mobileNumber = teacher.getMobileNumber();
		this.email = teacher.getEmail();
		this.dob = teacher.getDob().toString();
		this.qualification = teacher.getQualification();
		this.active = teacher.getActive();
		this.isManagmentMember = teacher.getIsManagmentMember();
		this.designation = teacher.getDesignation();
		this.isCoach = teacher.getIsCoach();
		if (teacher.getSchool() != null)
			this.schoolName = teacher.getSchool().getName();
		if (teacher.getGrades() != null && !teacher.getGrades().isEmpty()) {
//			if (grades == null)
				grades = new ArrayList<GradeResponse>();
			for (Grade grade : teacher.getGrades()) {
				grades.add(new GradeResponse(grade));
			}
		}

		// change database for activity and then get all activity names and add to
		// member list in the view

		if (teacher.getActivities() != null && !teacher.getActivities().isEmpty()) {
			if (activities == null)
				activities = new ArrayList<String>();
			for (Activity activity : teacher.getActivities()) {
				activities.add(String.format("%s", activity.getName()));
			}
		}
	}
	
	public TeacherResponse(Teacher teacher , Boolean teacherResponseForUser) {
		this.id = teacher.getcId();

		if(teacher.getUser() != null)
		  this.userId = teacher.getUser().getCid();
	
		this.username = teacher.getUsername();
		this.name = teacher.getName();
		this.gender = teacher.getGender();
		this.mobileNumber = teacher.getMobileNumber();
		this.email = teacher.getEmail();
		this.dob = teacher.getDob().toString();
		this.qualification = teacher.getQualification();
		this.active = teacher.getActive();
		if (teacher.getSchool() != null)
			this.schoolName = teacher.getSchool().getName();
		

	}

}
