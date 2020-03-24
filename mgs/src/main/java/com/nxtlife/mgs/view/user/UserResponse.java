package com.nxtlife.mgs.view.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.TeacherResponse;

@JsonInclude(value = Include.NON_ABSENT)
public class UserResponse {

//	private Long id;
	private String id;
	private String userName;
	private String contactNumber;
	private String email;
	private String userType;
	private String roleId;
	private StudentResponse student;
	private TeacherResponse teacher;
	private SchoolResponse school;
	private String roleName;

	public String getUserName() {
		return userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public StudentResponse getStudent() {
		return student;
	}

	public void setStudent(StudentResponse student) {
		this.student = student;
	}

	public TeacherResponse getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherResponse teacher) {
		this.teacher = teacher;
	}

	public SchoolResponse getSchool() {
		return school;
	}

	public void setSchool(SchoolResponse school) {
		this.school = school;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public UserResponse(User user) {
//		this.id = user.getId();
		this.id = user.getCid();
		this.email = user.getEmail();
		if (user.getRoleForUser() != null) {
			this.roleId = user.getRoleForUser().getCid();
			this.roleName = user.getRoleForUser().getName();
		}
		this.userName = user.getUserName();
		if (user.getUserType() != null)
			this.userType = user.getUserType().toString();
		this.contactNumber = user.getContactNumber();
		if (user.getStudent() != null) {
			this.student = new StudentResponse(user.getStudent(), true);
		}
		if (user.getSchool() != null)
			this.school = new SchoolResponse(user.getSchool());
		if (user.getTeacher() != null) {
			this.teacher = new TeacherResponse(user.getTeacher(), true);
		}

	}

	public UserResponse() {

	}
}
