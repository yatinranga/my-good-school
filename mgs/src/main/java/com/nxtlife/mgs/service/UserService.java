package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.view.Mail;
import com.nxtlife.mgs.view.MailRequest;

import com.nxtlife.mgs.view.PasswordRequest;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.UserRequest;
import com.nxtlife.mgs.view.user.UserResponse;
import com.sun.mail.smtp.SMTPSendFailedException;

public interface UserService {

//	User createStudentUser(Student student);
//
//	User createParentUser(Guardian guardian);
//	
//	User createTeacherUser(Teacher teacher) ;
//	
//	User createSchoolUser(School school);

	UserResponse getLoggedInUserResponse();
	
	void sendLoginCredentialsByGmailApi(MailRequest request);

	Boolean sendLoginCredentialsBySMTP(Mail request) throws SMTPSendFailedException;

	SuccessResponse changePassword(PasswordRequest request);

	SuccessResponse forgotPassword(String username);

	Mail usernamePasswordSendContentBuilder(String username, String password, String mailFrom, String mailTo);

//	User createUserForEntity(Object entity);

	User createUser(String name, String contactNumber, String email, Long schoolId);

	UserResponse save(UserRequest request);

	List<UserResponse> findAll();

	UserResponse findById(String id);

	UserResponse findByAuthentication();

	UserResponse update(String userId, UserRequest request);

	SuccessResponse matchGeneratedPassword(String username, String generatedPassword);

	SuccessResponse changePasswordByGeneratedPassword(PasswordRequest request);

	SuccessResponse activate(String id);

	SuccessResponse delete(String id);
	
}
