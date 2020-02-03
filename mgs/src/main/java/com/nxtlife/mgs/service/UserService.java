package com.nxtlife.mgs.service;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.view.StudentRequest;

public interface UserService {

	User createStudentUser(Student student);

	User createParentUser(Guardian guardian);
	
	public User createTeacherUser(Teacher teacher) ;
	
}
