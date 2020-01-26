package com.nxtlife.mgs.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.RegisterType;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.StudentRequest;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
//	@Autowired
//	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Utils utils;
	
	@Override
	public User createStudentUser(Student student) {
		
			
			if (userRepository.countByUsername(student.getUsername()) > 0) {
			   throw new ValidationException("This username is already registered");
			}
			User user = new User();
			user.setActive(true);
			user.setUserType(UserType.Student);
			user.setRegisterType(RegisterType.MANUALLY);
			user.setUsername(student.getUsername());
			// later change it to encrypted password 
//			user.setPasswordHash(bCryptPasswordEncoder.encode(student.getUsername())); //Setting username as password
			user.setPasswordHash(student.getUsername());
			try {
		        user.setcId(utils.generateRandomAlphaNumString(8));
		        }catch(ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
		        	user.setcId(utils.generateRandomAlphaNumString(8));
		        }
		
			if(student.getSubscriptionEndDate()!=null) {
				if(student.getSubscriptionEndDate().after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
					user.setIsPaid(true);
			}
			Role defaultRole = roleRepository.getOneByName("Student");
			if(defaultRole==null)
				throw new ValidationException("Role Student does not exist");
			
			List<Role> defaultRoleList = new ArrayList<>();
			defaultRoleList.add(defaultRole);
			
			if (!defaultRoleList.isEmpty()) {
			user.setRoles(defaultRoleList);
			}
			user.setStudent(student);
			return userRepository.save(user);
			}
	
	@Override
	public User createParentUser(Guardian guardian) {
		if (userRepository.countByUsername(guardian.getUsername()) > 0) {
			   throw new ValidationException("This username is already registered");
			}
			User user = new User();
			user.setActive(true);
			user.setUserType(UserType.Parent);
			user.setRegisterType(RegisterType.MANUALLY);
			user.setUsername(guardian.getUsername());
			//later change it to encrypted password 
//			user.setPasswordHash(bCryptPasswordEncoder.encode(guardian.getUsername())); //Setting username as password
			user.setPasswordHash(guardian.getUsername());
			try {
		        user.setcId(utils.generateRandomAlphaNumString(8));
		        }catch(ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
		        	user.setcId(utils.generateRandomAlphaNumString(8));
		        }
		
//			if(student.getSubscriptionEndDate()!=null) {
//				if(student.getSubscriptionEndDate().after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
//					user.setIsPaid(true);
//			}
			Role defaultRole = roleRepository.getOneByName("Guardian");
			if(defaultRole==null)
				throw new ValidationException("Role Parent does not exist");
			
			List<Role> defaultRoleList = new ArrayList<>();
			defaultRoleList.add(defaultRole);
			
			if (!defaultRoleList.isEmpty()) {
			user.setRoles(defaultRoleList);
			}
			user.setGuardian(guardian);
			return userRepository.save(user);

	}

}
