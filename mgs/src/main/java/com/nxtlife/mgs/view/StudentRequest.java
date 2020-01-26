package com.nxtlife.mgs.view;

import java.util.Date;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.user.Student;

public class StudentRequest {

	@NotNull
	private String name;

	private String cId;

	private String userCId;

	@NotNull
	private String schoolCId;

	private String username;

	private Date dob;

	private String imageUrl;

	@NotNull
	private String email;

	private String mobileNumber;

	private String gender;

	private Date subscriptionEndDate;

	private String fathersName;

	private String fathersEmail;

	private String fathersMobileNumber;

	private String mothersName;

	private String mothersEmail;

	private String mothersMobileNumber;

	private Boolean active;

	@NotNull
	private String gradeCId;

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

	public String getUserCId() {
		return userCId;
	}

	public void setUserCId(String userCId) {
		this.userCId = userCId;
	}

	public String getSchoolCId() {
		return schoolCId;
	}

	public void setSchoolCId(String schoolCId) {
		this.schoolCId = schoolCId;
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

	public String getGradeCId() {
		return gradeCId;
	}

	public void setGradeCId(String gradeCId) {
		this.gradeCId = gradeCId;
	}

	public String getFathersName() {
		return fathersName;
	}

	public void setFathersName(String fathersName) {
		this.fathersName = fathersName;
	}

	public String getFathersEmail() {
		return fathersEmail;
	}

	public void setFathersEmail(String fathersEmail) {
		this.fathersEmail = fathersEmail;
	}

	public String getFathersMobileNumber() {
		return fathersMobileNumber;
	}

	public void setFathersMobileNumber(String fathersMobileNumber) {
		this.fathersMobileNumber = fathersMobileNumber;
	}

	public String getMothersName() {
		return mothersName;
	}

	public void setMothersName(String mothersName) {
		this.mothersName = mothersName;
	}

	public String getMothersEmail() {
		return mothersEmail;
	}

	public void setMothersEmail(String mothersEmail) {
		this.mothersEmail = mothersEmail;
	}

	public String getMothersMobileNumber() {
		return mothersMobileNumber;
	}

	public void setMothersMobileNumber(String mothersMobileNumber) {
		this.mothersMobileNumber = mothersMobileNumber;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Student toEntity(Student student) {
		student = student == null ? new Student() : student;
		if (this.name != null)
			student.setName(this.name);
		if (this.username != null)
			student.setUsername(this.username);
		if (this.email != null)
			student.setEmail(this.email);
		if (this.gender != null)
			student.setGender(this.gender);
		if (this.dob != null)
			student.setDob(this.dob);
		if (this.mobileNumber != null)
			student.setMobileNumber(this.mobileNumber);
		if (this.subscriptionEndDate != null)
			student.setSubscriptionEndDate(this.subscriptionEndDate);
		return student;
	}

	public Student toEntity() {
		return this.toEntity(null);
	}

}
