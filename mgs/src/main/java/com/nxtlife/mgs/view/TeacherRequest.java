package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.util.DateUtil;

public class TeacherRequest extends Request {

	private String name;

	private String id;

	private String userId;

	private String username;

	private String dob;

//	private String school;

	private String qualification;

	private Boolean isCoach;

	private Boolean isClassTeacher;

	private Boolean active;

	private String email;

	private String mobileNumber;

	private String gender;

	private List<ActivityRequestResponse> activities;

	private List<String> gradeIds;

	private String schoolId;

	private String designation;

	private Boolean isManagmentMember;
	
	private List<TeacherRequest> teachers;
	
	private String profileBrief;
	
	private Set<String> roles;

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

	public List<ActivityRequestResponse> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityRequestResponse> activities) {
		this.activities = activities;
	}

	public List<String> getGradeIds() {
		return gradeIds;
	}

	public void setGradeIds(List<String> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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

	public List<TeacherRequest> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherRequest> teachers) {
		this.teachers = teachers;
	}

	public String getProfileBrief() {
		return profileBrief;
	}

	public void setProfileBrief(String profileBrief) {
		this.profileBrief = profileBrief;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Teacher toEntity() {
		return toEntity(null);
	}

	public Teacher toEntity(Teacher teacher) {
		teacher = teacher == null ? new Teacher() : teacher;
		if (this.name != null) {
			if(!isStringOnlyAlphabet(this.name))
				throw new ValidationException(String.format("Name (%s) is in invalid format, it should contain only alphabets.",this.name));
			teacher.setName(this.name);
		}

		if (this.username != null)
			teacher.setUsername(this.username);

		if (this.dob != null)
			teacher.setDob(DateUtil.convertStringToDate(this.dob));
		if (this.qualification != null)
			teacher.setQualification(this.qualification);
		/*
		 * if (this.active != null) teacher.setActive(this.active);
		 */
		if (this.gender != null)
			teacher.setGender(this.gender);
		if (this.mobileNumber != null) {
			validateMobileNumber(this.mobileNumber);
			teacher.setMobileNumber(this.mobileNumber);
		}
		if (this.email != null) {
			validateEmail(this.email);
			teacher.setEmail(this.email);
		}
		if (this.isCoach != null)
			teacher.setIsCoach(this.isCoach);
		if (this.isClassTeacher != null)
			teacher.setIsClassTeacher(this.isClassTeacher);

		teacher.setIsManagmentMember(this.isManagmentMember);
		teacher.setDesignation(this.designation);
		
		if(this.profileBrief != null) {
			if(countWords(this.profileBrief) < 20 || countWords(this.profileBrief) >200)
				throw new ValidationException("profile brief can be between 20 to 200 words.");
			teacher.setProfileBrief(this.profileBrief);
		}
		   

		return teacher;
	}

}
