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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.activity.ActivityOffered;
import com.nxtlife.mgs.entity.activity.ActivityOfferedFocusArea;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;

@Entity
public class Teacher extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@NotNull
	@Column(unique = true)
	private String cId;

	@NotNull
	private String name;

	private String username;

	private String gender;

	@NotNull
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

	private Boolean isPrincipal;

	@OneToOne
	@JoinColumn(name = "user_id")
	User user;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teacher")
	private List<ActivityPerformed> activitiesAssigned;

	@ManyToOne
	private School school;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teacher")
	private List<Grade> grades;

//	@ManyToMany(mappedBy = "teachers")
//	private List<ActivityOfferedFocusArea> activityOfferedFocusAreas;

	@ManyToOne
	private ActivityOffered activityOffered;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
	private List<Award> awards;

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

	public Boolean getIsPrincipal() {
		return isPrincipal;
	}

	public void setIsPrincipal(Boolean isPrincipal) {
		this.isPrincipal = isPrincipal;
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
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	public Teacher(@NotNull String cId, @NotNull String name, String username, String gender, @NotNull Date dob,
//			String imageUrl, @NotNull String email, String mobileNumber, Boolean active, String qualification,
//			Boolean isClassTeacher, Boolean isCoach, Boolean isPrincipal, List<ActivityPerformed> activitiesAssigned,
//			School school, List<Grade> grades, List<ActivityOfferedFocusArea> activityOfferedFocusAreas,
//			List<Award> awards) {
//		super();
//		this.cId = cId;
//		this.name = name;
//		this.username = username;
//		this.gender = gender;
//		this.dob = dob;
//		this.imageUrl = imageUrl;
//		this.email = email;
//		this.mobileNumber = mobileNumber;
//		this.active = active;
//		this.qualification = qualification;
//		this.isClassTeacher = isClassTeacher;
//		this.isCoach = isCoach;
//		this.isPrincipal = isPrincipal;
//		this.activitiesAssigned = activitiesAssigned;
//		this.school = school;
//		this.grades = grades;
////		this.activityOfferedFocusAreas = activityOfferedFocusAreas;
//		this.awards = awards;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityOffered getActivityOffered() {
		return activityOffered;
	}

	public void setActivityOffered(ActivityOffered activityOffered) {
		this.activityOffered = activityOffered;
	}

	public Teacher(@NotNull String cId, @NotNull String name, String username, String gender, @NotNull Date dob,
			String imageUrl, @NotNull String email, String mobileNumber, Boolean active, String qualification,
			Boolean isClassTeacher, Boolean isCoach, Boolean isPrincipal, User user,
			List<ActivityPerformed> activitiesAssigned, School school, List<Grade> grades,
			ActivityOffered activityOffered, List<Award> awards) {
		super();
		this.cId = cId;
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
		this.isPrincipal = isPrincipal;
		this.user = user;
		this.activitiesAssigned = activitiesAssigned;
		this.school = school;
		this.grades = grades;
		this.activityOffered = activityOffered;
		this.awards = awards;
	}

	public Teacher() {

	}

}
