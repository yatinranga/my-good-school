package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.util.DateUtil;

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
	private String schoolId;
	private Boolean active;
	private String designation;
	private Boolean isManagmentMember;
	private Boolean isCoach;
	private Boolean isClassTeacher;
	private String imagePath;
	private String profileBrief;
	private String yearOfEnrolment;
	private List<String> activityIds;
	private List<TeacherResponse> teachers;

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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Boolean getIsClassTeacher() {
		return isClassTeacher;
	}

	public void setIsClassTeacher(Boolean isClassTeacher) {
		this.isClassTeacher = isClassTeacher;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getProfileBrief() {
		return profileBrief;
	}

	public void setProfileBrief(String profileBrief) {
		this.profileBrief = profileBrief;
	}

	public String getYearOfEnrolment() {
		return yearOfEnrolment;
	}

	public void setYearOfEnrolment(String yearOfEnrolment) {
		this.yearOfEnrolment = yearOfEnrolment;
	}

	public List<String> getActivityIds() {
		return activityIds;
	}

	public void setActivityIds(List<String> activityIds) {
		this.activityIds = activityIds;
	}

	public List<TeacherResponse> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherResponse> teachers) {
		this.teachers = teachers;
	}

	public TeacherResponse() {
		
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
		if(teacher.getDob()!=null)
		   this.dob = DateUtil.formatDate(teacher.getDob());
		this.qualification = teacher.getQualification();
		this.active = teacher.getActive();
		this.isManagmentMember = teacher.getIsManagmentMember();
		this.designation = teacher.getDesignation();
		this.isCoach = teacher.getIsCoach();
		this.isClassTeacher = teacher.getIsClassTeacher();
		this.imagePath = teacher.getImageUrl();
		this.profileBrief = teacher.getProfileBrief();
		if (teacher.getCreatedDate() != null)
			this.yearOfEnrolment = Integer.toString(teacher.getCreatedDate().getYear());
		
			if (teacher.getSchool() != null) {
				this.schoolName = teacher.getSchool().getName();
				this.schoolId = teacher.getSchool().getCid();
			}
		
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
			if(activityIds == null)
				activityIds = new ArrayList<String>();
			teacher.getActivities().stream().distinct().forEach(act -> {activities.add(act.getName());
			activityIds.add(act.getCid());});
//			for (Activity activity : teacher.getActivities()) {
//				activities.add(String.format("%s", activity.getName()));
//			}
		}
	}

	public TeacherResponse(Teacher teacher, Boolean teacherResponseForUser) {
		this.id = teacher.getcId();

		if (teacher.getUser() != null)
			this.userId = teacher.getUser().getCid();

		this.username = teacher.getUsername();
		this.name = teacher.getName();
		this.gender = teacher.getGender();
		this.mobileNumber = teacher.getMobileNumber();
		this.email = teacher.getEmail();
		this.profileBrief = teacher.getProfileBrief();
		if (teacher.getCreatedDate() != null)
			this.yearOfEnrolment = Integer.toString(teacher.getCreatedDate().getYear());
		
		if(teacher.getDob()!=null)
			   this.dob = DateUtil.formatDate(teacher.getDob());
		
		this.qualification = teacher.getQualification();
		this.active = teacher.getActive();
		if (teacher.getSchool() != null) {
			this.schoolName = teacher.getSchool().getName();
			this.schoolId = teacher.getSchool().getCid();
		}
		
		this.isManagmentMember = teacher.getIsManagmentMember();
		this.isCoach = teacher.getIsCoach();
		this.isClassTeacher = teacher.getIsClassTeacher();
		this.designation = teacher.getDesignation();
		this.imagePath = teacher.getImageUrl();
		
		if (teacher.getActivities() != null && !teacher.getActivities().isEmpty()) {
			if (activities == null)
				activities = new ArrayList<String>();
			if(activityIds == null)
				activityIds = new ArrayList<String>();
			teacher.getActivities().stream().distinct().forEach(act -> {activities.add(act.getName());
			activityIds.add(act.getCid());});
		}
	}

}
