package com.nxtlife.mgs.view;

import com.nxtlife.mgs.entity.user.Guardian;

public class GuardianRequest {

	private String name;
	private String email;
	private String gender;
	private String mobileNumber;
	private String studentId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Guardian toEntity(Guardian guardian) {
		guardian = guardian==null?new Guardian():guardian;
		guardian.setName(name);
		guardian.setEmail(email);
		guardian.setMobileNumber(mobileNumber);
		
		return guardian;
	}
	
	public Guardian toEntity() {
		return toEntity(null);
	}
}
