package com.nxtlife.mgs.view;

import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.school.School;

public class SchoolResponse {

    private String name;
	
	private String id;
	
	private String address;

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
	
	public SchoolResponse(School school) {
		this.setName(school.getName());
		this.setAddress(school.getAddress());
		this.setId(school.getCid());
		this.setContactNumber(school.getContactNumber());
		this.setEmail(school.getEmail());
		this.setLogo(school.getLogo());
	}
	
    public SchoolResponse() {
		
	}

}
