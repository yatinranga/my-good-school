package com.nxtlife.mgs.view;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.ex.ValidationException;

public class SchoolRequest extends Request{

	@NotNull
	private String name;

	private String username;

	private String id;

	private String address;

	@NotNull
	private String email;

	private String contactNumber;

	private MultipartFile logo;

	private Boolean active;

	private List<String> generalActivities;

	private List<ActivityRequestResponse> newActivities;
	
	private List<GradeRequest> gradeRequests;
	
	private List<ActivityRequestResponse> activities;
	

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

	public List<String> getGeneralActivities() {
		return generalActivities;
	}

	public void setGeneralActivities(List<String> generalActivities) {
		this.generalActivities = generalActivities;
	}

	public List<ActivityRequestResponse> getNewActivities() {
		return newActivities;
	}

	public void setNewActivities(List<ActivityRequestResponse> newActivities) {
		this.newActivities = newActivities;
	}

	public List<GradeRequest> getGradeRequests() {
		return gradeRequests;
	}

	public void setGradeRequests(List<GradeRequest> gradeRequests) {
		this.gradeRequests = gradeRequests;
	}

	public List<ActivityRequestResponse> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityRequestResponse> activities) {
		this.activities = activities;
	}

	public School toEntity(School school) {
		school = school == null ? new School() : school;
		if (this.name != null) {
			if(!isStringOnlyAlphabet(this.name))
				throw new ValidationException(String.format("Name (%s) is in invalid format, it should contain only alphabets.",this.name));
			school.setName(this.name);
		}
		if (this.username != null)
			school.setUsername(this.username);
		if (this.contactNumber != null) {
			validateMobileNumber(this.contactNumber);
			school.setContactNumber(this.contactNumber);
		}
		if (this.email != null) {
			validateEmail(this.email);
			school.setEmail(this.email);
		}
		if (this.address != null)
			school.setAddress(this.address);
		return school;
	}

	public School toEntity() {

		return toEntity(null);
	}
}
