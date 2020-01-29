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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;

@Entity
public class Student extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cId;
	
	@NotNull
	@Column(unique = true)
	private String username;
	
	private Date dob;
	
	private String imageUrl;
	
	@NotNull
	@Column(unique = true)
	private String email;
	
	@Column(unique = true)
	private String mobileNumber;
	
	private Boolean active = false;
	
	private String gender;
	
	private Date subscriptionEndDate;
	
	@OneToOne
	@JoinColumn(name="user_id")
	User user;
	
//	@NotNull
	@ManyToOne
	private School school;
	
	@ManyToOne
	private Grade grade;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "student")
	private List<Award> awards;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "student")
	private List<ActivityPerformed> activities;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy="student")
	private List<Guardian> guardians ; 

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public List<ActivityPerformed> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityPerformed> activities) {
		this.activities = activities;
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

	public List<Guardian> getGuardians() {
		return guardians;
	}

	public void setGuardians(List<Guardian> guardians) {
		this.guardians = guardians;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student(@NotNull String name, @NotNull String cId, @NotNull String username, Date dob, String imageUrl,
			@NotNull String email, String mobileNumber, Boolean active, String gender, Date subscriptionEndDate,
			User user, @NotNull School school, Grade grade, List<Award> awards, List<ActivityPerformed> activities,
			List<Guardian> guardians) {
		super();
		this.name = name;
		this.cId = cId;
		this.username = username;
		this.dob = dob;
		this.imageUrl = imageUrl;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.active = active;
		this.gender = gender;
		this.subscriptionEndDate = subscriptionEndDate;
		this.user = user;
		this.school = school;
		this.grade = grade;
		this.awards = awards;
		this.activities = activities;
		this.guardians = guardians;
	}

	public Student() {
		
	}
	
}