package com.nxtlife.mgs.view.user;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.user.Authority;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.user.security.AuthorityResponse;
import com.nxtlife.mgs.view.user.security.RoleResponse;


@JsonInclude(value = Include.NON_ABSENT)
public class UserResponse {

	private Long id;
	
	private String name;

	private Boolean active;
	
	private String username;
	
	private String email;
	
	private String contactNumber;
	
	private String picUrl;
	
	private String schoolId;
	
	private SchoolResponse school;
	
	private Set<RoleResponse> roles;
	
	private Set<AuthorityResponse> authorities;

	public UserResponse(Long id, String name, Boolean active, String username, String email, String contactNumber,
			String picUrl, String schoolCid) {
		super();
		this.id = id;
		this.active = active;
		this.name = name;
		this.username = username;
		this.email = email;
		this.contactNumber = contactNumber;
		this.picUrl = picUrl;
		this.schoolId = schoolCid;
	}

	public static UserResponse get(User user) {
		if (user != null) {
			UserResponse response = new UserResponse(user.getId() == null ? user.getUserId() : user.getId(),
					user.getName(), user.getActive(), user.getUsername(), user.getEmail(), user.getContactNumber(),
					user.getPicUrl(), null);
			if (user.getAuthorities() != null) {
				response.authorities = new HashSet<>();
				for (Authority authority : user.getAuthorities()) {
					response.authorities.add(new AuthorityResponse(authority));
				}
			}
			if (user.getRoles() != null) {
				response.roles = new HashSet<>();
				for (Role role : user.getRoles()) {
					response.roles.add(RoleResponse.get(role));
				}
			}
			return response;
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public SchoolResponse getSchool() {
		return school;
	}

	public void setSchool(SchoolResponse school) {
		this.school = school;
	}

	public Set<RoleResponse> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleResponse> roles) {
		this.roles = roles;
	}

	public Set<AuthorityResponse> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityResponse> authorities) {
		this.authorities = authorities;
	}
}
