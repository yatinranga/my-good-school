package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.util.DateUtil;

public class StudentRequest {

	@NotEmpty(message = "name can't be null/empty")
	private String name;

	private String id;

	private String userId;

	@NotEmpty(message = "schoolId can't be null/empty")
	private String schoolId;

	private String username;

	private String dob;

	private Date sessionStartDate;

	private String imageUrl;

	@NotEmpty
	@Email(message = "email pattern not vaild")
	private String email;

	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Mobile no should contain only digit")
	private String mobileNumber;

	private String gender;

	private Date subscriptionEndDate;

	private List<GuardianRequest> guardians;

	private List<String> guardianIds;

	private Boolean active;

	@NotEmpty(message = "grade id can't be null or empty")
	private String gradeId;

//	private String fathersName;
//
//	private String fathersEmail;
//
//	private String fathersMobileNumber;
//
//	private String mothersName;
//
//	private String mothersEmail;
//
//	private String mothersMobileNumber;

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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getSessionStartDate() {
		return sessionStartDate;
	}

	public void setSessionStartDate(Date sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}

	public List<GuardianRequest> getGuardians() {
		return guardians;
	}

	public void setGuardians(List<GuardianRequest> guardians) {
		this.guardians = guardians;
	}

	public List<String> getGuardianIds() {
		return guardianIds;
	}

	public void setGuardianIds(List<String> guardianIds) {
		this.guardianIds = guardianIds;
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
			student.setDob(DateUtil.convertStringToDate(this.dob));
		// student.setDob(DateUtil.getDate(this.dob));
		if (this.mobileNumber != null)
			student.setMobileNumber(this.mobileNumber);
		if (this.subscriptionEndDate != null)
			student.setSubscriptionEndDate(this.subscriptionEndDate);
		if (this.sessionStartDate != null)
			student.setSessionStartDate(this.sessionStartDate);

		return student;
	}

	public Student toEntity() {
		return this.toEntity(null);
	}

}
