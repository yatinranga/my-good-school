package com.nxtlife.mgs.view;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.school.School;

public class SchoolRequest {

	@NotNull
	private String name;
	
	private String username;
	
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
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public School toEntity(School school) {
		school = school==null?new School():school;
		if(this.name!=null)
		  school.setName(this.name);
		if(this.username!=null)
		  school.setUsername(this.username);
		if(this.email!=null)
		  school.setEmail(this.getEmail());
		if(this.logo!=null)
		  school.setLogo(this.logo);
		if(this.contactNumber!=null)
		  school.setContactNumber(this.contactNumber);
		if(this.address!=null)
		  school.setAddress(this.address);
		return school;
	}
	
    public School toEntity() {
		
		return this.toEntity(null);
	}
}
