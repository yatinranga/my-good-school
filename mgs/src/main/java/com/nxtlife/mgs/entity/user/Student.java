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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.Certificate;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.entity.school.StudentSchoolGrade;

@Entity
@DynamicUpdate(true)
@SuppressWarnings("serial")
public class Student  extends BaseEntity {

	@NotNull
	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	@NotNull
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	@Column(unique = true)
	private String username;

	private Date dob;

	private String imageUrl;

	@Email
	@Column(unique = true, nullable = false)
	private String email;

	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Mobile no. should contain only digit")
	@Column(unique = true)
	private String mobileNumber;

	private String gender;

	private Date subscriptionEndDate;

	private Date sessionStartDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	User user;

//	@NotNull
	@ManyToOne
	private School school;

	@NotNull
	@ManyToOne
	private Grade grade;

//	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "student")
//	private List<Award> awards;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "student")
	private List<ActivityPerformed> activities;

	@ManyToMany(mappedBy = "students", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Guardian> guardians;

	/*
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "student") private List<Guardian> guardians;
	 */

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "student")
	private List<StudentSchoolGrade> studentSchoolGrades;
	
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "student")
	List<Certificate> certificates;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "student")
//	private List<AwardActivityPerformed> StudentAwards;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "student")
	private List<StudentClub> studentClubs;
	

	public Date getSessionStartDate() {
		return sessionStartDate;
	}

	public void setSessionStartDate(Date sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
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
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

//	public List<Award> getAwards() {
//		return awards;
//	}
//
//	public void setAwards(List<Award> awards) {
//		this.awards = awards;
//	}

	public List<ActivityPerformed> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityPerformed> activities) {
		this.activities = activities;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Guardian> getGuardians() {
		return guardians;
	}

	public void setGuardians(List<Guardian> guardians) {
		this.guardians = guardians;
	}


	public List<StudentSchoolGrade> getStudentSchoolGrades() {
		return studentSchoolGrades;
	}

	public void setStudentSchoolGrades(List<StudentSchoolGrade> studentSchoolGrades) {
		this.studentSchoolGrades = studentSchoolGrades;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}

	public List<StudentClub> getStudentClubs() {
		return studentClubs;
	}

	public void setStudentClubs(List<StudentClub> studentClubs) {
		this.studentClubs = studentClubs;
	}

	public Student(@NotNull String name, @NotNull String cid, @NotNull String username, Date dob, String imageUrl,
			@NotNull String email, String mobileNumber, Boolean active, String gender, Date subscriptionEndDate,
			User user, @NotNull School school, Grade grade, List<ActivityPerformed> activities,
			List<Guardian> guardians) {
		this.name = name;
		this.cid = cid;
		this.username = username;
		this.dob = dob;
		this.imageUrl = imageUrl;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.setActive(active);
		this.gender = gender;
		this.subscriptionEndDate = subscriptionEndDate;
		this.user = user;
		this.school = school;
		this.grade = grade;
		this.activities = activities;
		this.guardians = guardians;
	}

	public Student() {

	}

}
