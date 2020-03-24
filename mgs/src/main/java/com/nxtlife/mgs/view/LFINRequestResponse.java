package com.nxtlife.mgs.view;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.user.LFIN;
import com.nxtlife.mgs.util.DateUtil;

@JsonInclude(value = Include.NON_ABSENT)
public class LFINRequestResponse {

	private String id;

	private String dob;

	private String name;

	@Email
	@NotEmpty(message = "Email cannot be null or empty.")
	private String email;

	private String contactNumber;

	private String gender;

	private String userId;
	
	private String username ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public LFIN toEntity() {
		return toEntity(null);
	}

	public LFIN toEntity(LFIN lfin) {
		lfin = lfin == null ? new LFIN() : lfin;
		lfin.setName(this.name);
		lfin.setEmail(this.email);
		lfin.setContactNumber(this.contactNumber);
		lfin.setGender(this.gender);
		lfin.setDob(DateUtil.convertStringToDate(this.dob));
		return lfin;
	}

	public LFINRequestResponse() {
	}
	
	public LFINRequestResponse(LFIN lfin) {
		this.name = lfin.getName();
		this.id = lfin.getCid();
		this.email = lfin.getEmail();
		this.contactNumber = lfin.getContactNumber();
		if(lfin.getDob()!=null)
			   this.dob = DateUtil.formatDate(lfin.getDob());
		this.gender = lfin.getGender();
		if(lfin.getUser()!=null)
			this.userId = lfin.getUser().getCid();
		this.username = lfin.getUsername();
	}
}
