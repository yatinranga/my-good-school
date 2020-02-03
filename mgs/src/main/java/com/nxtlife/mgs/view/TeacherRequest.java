package com.nxtlife.mgs.view;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.UniqueElements;

import com.nxtlife.mgs.entity.activity.ActivityOffered;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.User;

public class TeacherRequest {

	@NotNull(message = "name can't be null")
	@UniqueElements
	private String name;

	@NotNull(message = "cId can't be null")
	@UniqueElements
	private String cId;

	private String username;

	private String gender;

	private Date dob;

	private String imageUrl;

	@NotNull(message = "email can't be null")
	@UniqueElements
	private String email;

	private String qualification;

	private Boolean isClassTeacher;

	private Boolean isCoach;

	private Boolean isPrincipal;

	private User user;

	private School school;

	private List<ActivityPerformed> activitiesAssigned;

	private List<Grade> grades;

	private ActivityOffered activityOffered;

	private List<Award> awards;

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Boolean getIsClassTeacher() {
		return isClassTeacher;
	}

	public void setIsClassTeacher(Boolean isClassTeacher) {
		this.isClassTeacher = isClassTeacher;
	}

	public Boolean getIsCoach() {
		return isCoach;
	}

	public void setIsCoach(Boolean isCoach) {
		this.isCoach = isCoach;
	}

	public Boolean getIsPrincipal() {
		return isPrincipal;
	}

	public void setIsPrincipal(Boolean isPrincipal) {
		this.isPrincipal = isPrincipal;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<ActivityPerformed> getActivitiesAssigned() {
		return activitiesAssigned;
	}

	public void setActivitiesAssigned(List<ActivityPerformed> activitiesAssigned) {
		this.activitiesAssigned = activitiesAssigned;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public ActivityOffered getActivityOffered() {
		return activityOffered;
	}

	public void setActivityOffered(ActivityOffered activityOffered) {
		this.activityOffered = activityOffered;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

}
