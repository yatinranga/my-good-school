package com.nxtlife.mgs.service;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.view.Mail;
import com.nxtlife.mgs.view.MailRequest;
import com.nxtlife.mgs.view.user.UserResponse;

public interface UserService {

	User createStudentUser(Student student);

	User createParentUser(Guardian guardian);
	
	public User createTeacherUser(Teacher teacher) ;

	UserResponse getLoggedInUser();
	
	void sendLoginCredentialsByGmailApi(MailRequest request);

	void sendLoginCredentialsBySMTP(Mail request);

//	User createSchoolUser(School school);
	
}
