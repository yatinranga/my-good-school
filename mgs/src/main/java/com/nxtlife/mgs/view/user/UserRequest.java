package com.nxtlife.mgs.view.user;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.view.Request;

public class UserRequest extends Request {

	private String id;

	@NotNull(message = "User's name can't be null")
	private String name;

	@NotNull(message = "User's name can't be null")
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	private String username;

	@NotEmpty(message = "User's email can't be empty")
	@Email(message = "Email pattern isn't correct")
	private String email;

	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Contact no should contain only digit")
	private String contactNumber;

	@NotEmpty(message = "Role ids can't be null or empty")
	private Set<Long> roleIds;

	private String schoolId;

	private String gender;

//	private StudentRequest student;

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getId() {
		return id;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public Set<Long> getRoleIds() {
		return roleIds;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public String getGender() {
		return gender;
	}

	public User toEntity() {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setContactNumber(contactNumber);
		user.setUsername(username);
		return user;
	}
}
