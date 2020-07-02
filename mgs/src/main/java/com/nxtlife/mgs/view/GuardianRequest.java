package com.nxtlife.mgs.view;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.ex.ValidationException;

public class GuardianRequest extends Request {

	private String id;
	
	@NotNull(message = "Guardian's name can't be null")
	private String name;
	
	@NotEmpty(message = "Guardian's email can't be empty")
	@Email(message = "Email pattern isn't correct")
	private String email;
	
	private String gender;
	
	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Mobile no should contain only digit")
	private String mobileNumber;
	
	@NotNull(message = "Relationship can't be null")
	private String relationship;
	
	@NotEmpty(message = "Student Ids can't be empty.")
	private List<String> studentIds;
	
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
	

	public List<String> getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(List<String> studentIds) {
		this.studentIds = studentIds;
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
		guardian = guardian == null ? new Guardian() : guardian;
		if (this.name != null) {
			if(!isStringOnlyAlphabet(this.name))
				throw new ValidationException(String.format("Name (%s) is in invalid format, it should contain only alphabets.",this.name));
			guardian.setName(this.name);
		}
		if (this.email != null) {
			validateEmail(this.email);
			guardian.setEmail(this.email);
		}
		if (this.mobileNumber != null) {
			validateMobileNumber(this.mobileNumber);
			guardian.setMobileNumber(this.mobileNumber);
		}
		guardian.setRelationship(this.relationship);
		guardian.setGender(this.gender);
		if(this.username != null)
			guardian.setUsername(this.username);
		return guardian;
	}

	public Guardian toEntity() {
		return toEntity(null);
	}

	public GuardianRequest(String name, String email, String gender, String mobileNumber, String relationship ,String username) {
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.mobileNumber = mobileNumber;
		this.relationship = relationship;
		this.username = username == null ? this.username : username;
	}
	
	public GuardianRequest(String name, String email, String mobileNumber , String username, String gender) {
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.mobileNumber = mobileNumber;
		this.username = username;
	}

	public GuardianRequest() {

	}

}
