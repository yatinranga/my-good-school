package com.nxtlife.mgs.entity.user;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.activity.Activity;
//import com.nxtlife.mgs.entity.activity.ActivityOffered;
//import com.nxtlife.mgs.entity.activity.ActivityOfferedFocusArea;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.school.TeacherSchoolGrade;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(true)
public class Teacher extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String cid;

	@NotNull
	private String name;

	@Column(unique = true, nullable = false)
	private String username;

	private String gender;

	private Date dob;

	private String imageUrl;

	@NotNull
	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String mobileNumber;

	private Boolean active;

	private String qualification;

	private Boolean isClassTeacher;

	private Boolean isCoach;

	private String designation;

	private Boolean isManagmentMember;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	User user;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teacher")
	private List<ActivityPerformed> activitiesAssigned;

	@ManyToOne
	private School school;

	@ManyToMany()
	@JoinTable(name = "teacher_grade", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {
			@JoinColumn(name = "grade_id") },uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id","grade_id"})})
	private List<Grade> grades;

//	@ManyToMany(mappedBy = "teachers")
//	private List<ActivityOfferedFocusArea> activityOfferedFocusAreas;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
	private List<Award> awards;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
	private List<TeacherSchoolGrade> teacherSchoolGrades;

	@ManyToMany(mappedBy = "teachers")
	private List<Activity> activities;

//	@ManyToMany(mappedBy = "teachers")
//	private List<ActivityOfferedFocusArea> activityOfferedFocusAreas;

//	@ManyToOne
//	ActivityOffered activityOffered; 

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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public List<ActivityPerformed> getActivitiesAssigned() {
		return activitiesAssigned;
	}

	public void setActivitiesAssigned(List<ActivityPerformed> activitiesAssigned) {
		this.activitiesAssigned = activitiesAssigned;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

//	public List<ActivityOfferedFocusArea> getActivityOfferedFocusAreas() {
//		return activityOfferedFocusAreas;
//	}
//
//	public void setActivityOfferedFocusAreas(List<ActivityOfferedFocusArea> activityOfferedFocusAreas) {
//		this.activityOfferedFocusAreas = activityOfferedFocusAreas;
//	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public String getcId() {
		return cid;
	}

	public void setcId(String cId) {
		this.cid = cId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public ActivityOffered getActivityOffered() {
//		return activityOffered;
//	}
//
//	public void setActivityOffered(ActivityOffered activityOffered) {
//		this.activityOffered = activityOffered;
//	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public List<TeacherSchoolGrade> getTeacherSchoolGrades() {
		return teacherSchoolGrades;
	}

	public void setTeacherSchoolGrades(List<TeacherSchoolGrade> teacherSchoolGrades) {
		this.teacherSchoolGrades = teacherSchoolGrades;
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

	public Teacher(@NotNull String cid, @NotNull String name, String username, String gender, @NotNull Date dob,
			String imageUrl, @NotNull String email, String mobileNumber, Boolean active, String qualification,

			Boolean isClassTeacher, Boolean isCoach, User user, List<ActivityPerformed> activitiesAssigned,
			School school, List<Grade> grades, List<Activity> activities, List<Award> awards) {
		this.cid = cid;
		this.name = name;
		this.username = username;
		this.gender = gender;
		this.dob = dob;
		this.imageUrl = imageUrl;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.active = active;
		this.qualification = qualification;
		this.isClassTeacher = isClassTeacher;
		this.isCoach = isCoach;
		this.user = user;
		this.activitiesAssigned = activitiesAssigned;
		this.school = school;
		this.grades = grades;
		this.activities = activities;
		this.awards = awards;
	}

	public Teacher() {

	}

}
