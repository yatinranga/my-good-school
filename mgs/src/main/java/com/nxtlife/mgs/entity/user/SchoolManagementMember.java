package com.nxtlife.mgs.entity.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.School;

@Entity
public class SchoolManagementMember extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String username;
	
	@NotNull
	@Column(unique = true)
	private String cid;
	
	private Date dob;
	
	private String imageUrl;
	
	@NotNull
	@Column(unique = true)
	private String email;
	
	@Column(unique = true)
	private String mobileNumber;
	
	private Boolean active;
	
	private String gender;
	
	private String designation;
	
	@NotNull
	@ManyToOne
	private School school;
	
	@OneToOne
	User user;

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

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public SchoolManagementMember(@NotNull String name, @NotNull String username, @NotNull String cid, Date dob,
			String imageUrl, @NotNull String email, String mobileNumber, Boolean active, String gender,
			String designation, @NotNull School school, User user) {
		super();
		this.name = name;
		this.username = username;
		this.cid = cid;
		this.dob = dob;
		this.imageUrl = imageUrl;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.active = active;
		this.gender = gender;
		this.designation = designation;
		this.school = school;
		this.user = user;
	}

	public SchoolManagementMember() {
		
	}
	

}
