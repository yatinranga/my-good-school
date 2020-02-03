package com.nxtlife.mgs.view;

import java.util.Date;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.user.Student;

public class StudentRequest {

	@NotNull
	private String name;

	private String id;

	private String userId;

	
	private String schoolId;

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

	private String gradeId;

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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
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
