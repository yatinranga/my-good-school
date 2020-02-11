package com.nxtlife.mgs.view.user;

import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.TeacherRequest;

public class UserRequest {

	public String username;
	public String password;
	public String type;
	public String email;
	public String contactNumber;
	
	public StudentRequest student;
	
	public TeacherRequest teacher;
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public StudentRequest getStudent() {
		return student;
	}

	public void setStudent(StudentRequest student) {
		this.student = student;
	}

	public User toEntity(User user) {
	    user =user==null? new User():user;
		user.setContactNo(contactNumber);
		user.setEmail(email);
		if(UserType.matches(type)) {
			throw new ValidationException("Type not found");
		}
		if(type.equalsIgnoreCase(UserType.Student.name())) {
			user.setStudent(student.toEntity());
		}
		else if(type.equalsIgnoreCase(UserType.Teacher.name())) {
			user.setTeacher(teacher.toEntity());
		}
		
		
		return user;
	}
	
	public User toEntity() {
		return toEntity(null);
	}
}
