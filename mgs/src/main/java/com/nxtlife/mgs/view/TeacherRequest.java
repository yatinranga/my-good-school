package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;

import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

public class TeacherRequest {

	private String name;
	
	private String id;
	
	private String userId;
	
	private String username;
	
	private Date dob;
	
	private String school;
	
	private String qualification;
	
	private Boolean isCoach;
	
	private Boolean isClassTeacher;
	
	private Boolean isPrincipal;
	
	private Boolean active;
	
	private String email;
	
	private String mobileNumber;
	
	private String gender;
	
	private List<String> activities;
	
	private String gradeId;
	
	private String schoolId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Boolean getIsCoach() {
		return isCoach;
	}

	public void setIsCoach(Boolean isCoach) {
		this.isCoach = isCoach;
	}

	public Boolean getIsClassTeacher() {
		return isClassTeacher;
	}

	public void setIsClassTeacher(Boolean isClassTeacher) {
		this.isClassTeacher = isClassTeacher;
	}

	public Boolean getIsPrincipal() {
		return isPrincipal;
	}

	public void setIsPrincipal(Boolean isPrincipal) {
		this.isPrincipal = isPrincipal;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	public Teacher toEntity(Teacher teacher) {
		teacher = teacher == null ? new Teacher() : teacher;
		if(this.name!= null)
			teacher.setName(this.name);
		if(this.username != null)
			teacher.setUsername(this.username);
		if(this.dob != null)
			teacher.setDob(this.dob);
		if(this.qualification!= null)
			teacher.setQualification(this.qualification);
		if(this.active!=null)
			teacher.setActive(this.active);
		if(this.gender!=null)
			teacher.setGender(this.gender);
		if(this.mobileNumber!=null)
			teacher.setMobileNumber(this.mobileNumber);
		if(this.email!=null)
			teacher.setEmail(this.email);
		if(this.isCoach != null)
			teacher.setIsCoach(this.isCoach);
		if(this.isClassTeacher!=null)
			teacher.setIsClassTeacher(this.isClassTeacher);
		if(this.isPrincipal!=null)
			teacher.setIsPrincipal(this.isPrincipal);
			
		return teacher;
	}
	
	public Teacher toEntity() {
		return toEntity(null);
	}
}
