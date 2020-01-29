package com.nxtlife.mgs.view;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.school.School;

public class SchoolRequest {

	@NotNull
	private String name;
	
	private String id;
	
	private String address;
	
	@NotNull
	private String email;
	
	private String contactNumber;
	
	private String logo;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public School toEntity(School school) {
		school = school==null?new School():school;
		
		school.setName(this.name);
		school.setEmail(this.getEmail());
		school.setLogo(this.logo);
		school.setContactNumber(this.contactNumber);
		school.setAddress(this.address);
		return school;
	}
	
    public School toEntity() {
		
		return this.toEntity(null);
	}
}
