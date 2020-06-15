package com.nxtlife.mgs.view;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.user.Guardian;

@JsonInclude(value = Include.NON_ABSENT)
public class GuardianResponse {

	private String name;
	private String username;
	private String email;
	private String mobileNumber;
	private Boolean active;
	private String gender;
	private String relationship;
	private String id;
	private List<String> studentIds;
	private String imageUrl;

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

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(List<String> studentIds) {
		this.studentIds = studentIds;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public GuardianResponse() {
	}

	public GuardianResponse(Guardian guardian) {

		this.name = guardian.getName();
		this.email = guardian.getEmail();
		this.mobileNumber = guardian.getMobileNumber();
		this.relationship = guardian.getRelationship();
		this.id = guardian.getCid();
		if(guardian.getStudents() !=null) {
			this.studentIds = guardian.getStudents().stream().distinct().map(s-> s.getCid()).collect(Collectors.toList());
		}
		this.imageUrl = guardian.getImageUrl();
	}

}
