package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.util.DateUtil;

public class StudentRequest extends Request {

	@NotEmpty(message = "name can't be null/empty")
	private String name;

	private String id;

	private String userId;

	@NotEmpty(message = "schoolId can't be null/empty")
	private String schoolId;

	private String dob;

	private Date sessionStartDate;
	
	private MultipartFile profileImage;

	@NotEmpty
	@Email(message = "email pattern not vaild")
	private String email;

	
	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Mobile no should contain only digit")
	private String mobileNumber;

	private String gender;

	private Date subscriptionEndDate;

	private List<GuardianRequest> guardians;

//	private List<String> guardianIds;

	@NotEmpty(message = "grade id can't be null or empty")
	private String gradeId;
	
	private String username;

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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public MultipartFile getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
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

//	public List<String> getGuardianIds() {
//		return guardianIds;
//	}
//
//	public void setGuardianIds(List<String> guardianIds) {
//		this.guardianIds = guardianIds;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Student toEntity(Student student) {
		student = student == null ? new Student() : student;
		if (this.name != null) {
			if(!isStringOnlyAlphabet(this.name))
				throw new ValidationException(String.format("Name (%s) is in invalid format, it should contain only alphabets.",this.name));
			student.setName(this.name);
		}
		if (this.email != null) {
			validateEmail(this.email);
			student.setEmail(this.email);
		}
		if (this.gender != null)
			student.setGender(this.gender);
		if (this.dob != null) {
			Date dateOfBirth = DateUtil.convertStringToDate(this.dob);
			if(dateOfBirth.before(LocalDateTime.now(DateTimeZone.forTimeZone(DateUtil.defaultTimeZone)).minusYears(21).toDate()))
				throw new ValidationException("Dob should be within 21 years from todays date.");
			student.setDob(dateOfBirth);
			
		}
			
		// student.setDob(DateUtil.getDate(this.dob));
		if (this.mobileNumber != null) {
			validateMobileNumber(this.mobileNumber);
			student.setMobileNumber(this.mobileNumber);
		}
		if (this.subscriptionEndDate != null)
			student.setSubscriptionEndDate(this.subscriptionEndDate);
		if (this.sessionStartDate != null)
			student.setSessionStartDate(this.sessionStartDate);
		if(this.username != null)
		    student.setUsername(this.username);

		return student;
	}

	public Student toEntity() {
		return this.toEntity(null);
	}
	
	public StudentRequest() {
		
	}

	public StudentRequest(@NotEmpty(message = "name can't be null/empty") String name,
			@NotEmpty @Email(message = "email pattern not vaild") String email,
			@Size(min = 10, max = 10) @Pattern(regexp = "^[0-9]*$", message = "Mobile no should contain only digit") String mobileNumber,
			String username ,String gender) {
		this.name = name;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.username = username;
		this.gender = gender;
	}
	
    

}
