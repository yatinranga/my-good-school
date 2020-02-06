package com.nxtlife.mgs.view;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

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
	
	private MultipartFile logo;
	
	private Boolean active ;

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
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public MultipartFile getLogo() {
		return logo;
	}

	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public School toEntity(School school) {
		school = school==null?new School():school;
		if(this.name!=null)
		  school.setName(this.name);
		if(this.username!=null)
		  school.setUsername(this.username);
		if(this.email!=null)
		  school.setEmail(this.getEmail());
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
