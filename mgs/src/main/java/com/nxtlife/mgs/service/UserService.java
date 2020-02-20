package com.nxtlife.mgs.service;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
<<<<<<< HEAD
import com.nxtlife.mgs.view.Mail;
import com.nxtlife.mgs.view.MailRequest;
=======
>>>>>>> 9858c188d65babdb27dca64399198b4b1b4bbaf5
import com.nxtlife.mgs.view.PasswordRequest;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.UserResponse;

public interface UserService {

	User createStudentUser(Student student);

	User createParentUser(Guardian guardian);
	
	public User createTeacherUser(Teacher teacher) ;

	UserResponse getLoggedInUser();
	
	void sendLoginCredentialsByGmailApi(MailRequest request);

	void sendLoginCredentialsBySMTP(Mail request);

	SuccessResponse changePassword(PasswordRequest request);

	SuccessResponse forgotPassword(String username);

	SuccessResponse changePassword(PasswordRequest request);

	SuccessResponse forgotPassword(String username);

//	User createSchoolUser(School school);
	
}
