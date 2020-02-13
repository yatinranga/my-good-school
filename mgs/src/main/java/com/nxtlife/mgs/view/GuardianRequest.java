package com.nxtlife.mgs.view;

import com.nxtlife.mgs.entity.user.Guardian;

public class GuardianRequest {

	private String id;
	private String name;
	private String email;
	private String gender;
	private String mobileNumber;
	private String relationship;
	private String studentId;
	private String username;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Guardian toEntity(Guardian guardian) {
		guardian = guardian==null?new Guardian():guardian;
		guardian.setName(name);
		guardian.setEmail(email);
		guardian.setMobileNumber(mobileNumber);
		guardian.setRelationship(this.relationship);
		guardian.setUsername(this.username);
		return guardian;
	}
	
	public Guardian toEntity() {
		return toEntity(null);
	}
	
	public GuardianRequest(String name, String email, String gender, String mobileNumber, String relationship) {
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.mobileNumber = mobileNumber;
		this.relationship = relationship;
	}
	
	public GuardianRequest() {
		
	}
	
}
