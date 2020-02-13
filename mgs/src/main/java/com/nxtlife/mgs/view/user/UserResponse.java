package com.nxtlife.mgs.view.user;

import com.nxtlife.mgs.entity.user.User;

public class UserResponse {

	private Long id;
	private String cid;
	private String userName;
	private String contactNumber;
	private String email;
	private String userType;
	private String roleId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public UserResponse(User user) {
		this.id = user.getId();
		this.cid = user.getCid();
		this.email = user.getEmail();
		if(user.getRoleForUser()!=null)
		  this.roleId = user.getRoleForUser().getCid();
		this.userName = user.getUserName();
		if(user.getUserType()!=null)
		  this.userType = user.getUserType().toString();
		this.contactNumber = user.getContactNo();
	}
	
	public UserResponse() {
		
	}
}
