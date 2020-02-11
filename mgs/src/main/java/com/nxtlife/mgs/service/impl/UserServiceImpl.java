package com.nxtlife.mgs.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.RegisterType;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.user.UserRequest;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
//	@Autowired
//	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Utils utils;
	
	@PostConstruct
	public void init() {
		Role role = roleRepository.getOneByName("Admin");
		if (role == null) {
		role = new Role();
		try {
			role.setCid(utils.generateRandomAlphaNumString(8));
	        }catch(ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
	        	role.setCid(utils.generateRandomAlphaNumString(8));
	        }
		
		role.setName("Admin");
		}
		//Logic for authorities missing
		roleRepository.save(role);
		if(userRepository.findByUserName("mainAdmin")==null) {
		User user = new User();
		user.setRoleForUser(role);
//		user.setUserName("Admin0001");
		user.setPassword("root");
		try {
			user.setCid(utils.generateRandomAlphaNumString(8));
	        }catch(ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
	        	user.setCid(utils.generateRandomAlphaNumString(8));
	        }
		user.setActive(true);
		user.setContactNo("8860571043");
		user.setEmail("admin@gmail.com");
		user.setUserName("mainAdmin");
		userRepository.save(user);
		}
	}
	@Override
	public User createStudentUser(Student student) {
		
			
			if (userRepository.countByUserName(student.getUsername()) > 0) {
			   throw new ValidationException("This username is already registered");
			}
			User user = new User();
			user.setActive(true);
			user.setUserType(UserType.Student);
			user.setRegisterType(RegisterType.MANUALLY);
			user.setUserName(student.getUsername());
			// later change it to encrypted password 
//			user.setPassword(bCryptPasswordEncoder.encode(student.getUsername())); //Setting username as password
			user.setPassword(student.getUsername());
			try {
		        user.setCid(utils.generateRandomAlphaNumString(8));
		        }catch(ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
		        	user.setCid(utils.generateRandomAlphaNumString(8));
		        }
		
			if(student.getSubscriptionEndDate()!=null) {
				if(student.getSubscriptionEndDate().after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
					user.setIsPaid(true);
			}
			Role defaultRole = roleRepository.getOneByName("Student");
			if(defaultRole==null)
				throw new ValidationException("Role Student does not exist");
			
			user.setRoleForUser(defaultRole);
			user.setStudent(student);
			return userRepository.save(user);
			}
	
	@Override
	public User createTeacherUser(Teacher teacher) {
		if (userRepository.countByUserName(teacher.getUsername()) > 0) {
			   throw new ValidationException("This username is already registered");
			}
			User user = new User();
			user.setActive(true);
			user.setUserType(UserType.Teacher);
			user.setRegisterType(RegisterType.MANUALLY); //setting it to manual may be required to passed as an argument
			user.setUserName(teacher.getUsername());
			// later change it to encrypted password 
//			user.setPassword(bCryptPasswordEncoder.encode(student.getUsername())); //Setting username as password
			user.setPassword(teacher.getUsername());
			try {
		        user.setCid(utils.generateRandomAlphaNumString(8));
		        }catch(ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
		        	user.setCid(utils.generateRandomAlphaNumString(8));
		        }
		
//			if(teacher.getSubscriptionEndDate()!=null) {
//				if(teacher.getSubscriptionEndDate().after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
//					user.setIsPaid(true);
//			}
			Role defaultRole = roleRepository.getOneByName("Teacher");
			if(defaultRole==null)
				throw new ValidationException("Role Teacher does not exist");
			
			List<Role> defaultRoleList = new ArrayList<>();
			defaultRoleList.add(defaultRole); 
			
			if(teacher.getIsCoach()!=null && teacher.getIsCoach()==true)
			{
				defaultRole = roleRepository.getOneByName("Coach");
				if(defaultRole==null)
					throw new ValidationException("Role Coach does not exist");
				user.setRoleForUser(defaultRole);
			}
			
			user.setTeacher(teacher);
			return userRepository.save(user);
	}
   
	
	 @Override
	    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
	        User user = userRepository.findByUserName(s);

	        return user;
	    }
	
	/*
	 * @Override public User createParentUser(Guardian guardian) { if
	 * (userRepository.countByUsername(guardian.getUsername()) > 0) { throw new
	 * ValidationException("This username is already registered"); } User user = new
	 * User(); user.setActive(true); user.setUserType(UserType.Parent);
	 * user.setRegisterType(RegisterType.MANUALLY);
	 * user.setUserName(guardian.getUsername()); //later change it to encrypted
	 * password //
	 * user.setPassword(bCryptPasswordEncoder.encode(guardian.getUsername()));
	 * //Setting username as password user.setPassword(guardian.getUsername()); try
	 * { user.setCid(utils.generateRandomAlphaNumString(8));
	 * }catch(ConstraintViolationException |
	 * javax.validation.ConstraintViolationException ce) {
	 * user.setCid(utils.generateRandomAlphaNumString(8)); }
	 * 
	 * // if(student.getSubscriptionEndDate()!=null) { //
	 * if(student.getSubscriptionEndDate().after(Date.from(java.time.LocalDateTime.
	 * now().atZone(ZoneId.systemDefault()).toInstant()))) // user.setIsPaid(true);
	 * // } Role defaultRole = roleRepository.getOneByName("Guardian");
	 * if(defaultRole==null) throw new
	 * ValidationException("Role Parent does not exist");
	 * 
	 * List<Role> defaultRoleList = new ArrayList<>();
	 * defaultRoleList.add(defaultRole);
	 * 
	 * if (!defaultRoleList.isEmpty()) { user.setRoles(defaultRoleList); }
	 * user.setGuardian(guardian); return userRepository.save(user);
	 * 
	 * }
	 * 
	 * @Override public User createSchoolUser(School school) { if
	 * (userRepository.countByUsername(school.getUsername()) > 0) { throw new
	 * ValidationException("This username is already registered"); } User user = new
	 * User(); user.setActive(true); user.setUserType(UserType.School);
	 * user.setRegisterType(RegisterType.MANUALLY);
	 * user.setUserName(school.getUsername()); //later change it to encrypted
	 * password //
	 * user.setPassword(bCryptPasswordEncoder.encode(school.getUsername()));
	 * //Setting username as password user.setPassword(school.getUsername()); try {
	 * user.setCid(utils.generateRandomAlphaNumString(8));
	 * }catch(ConstraintViolationException |
	 * javax.validation.ConstraintViolationException ce) {
	 * user.setCid(utils.generateRandomAlphaNumString(8)); }
	 * 
	 * 
	 * // user.setIsPaid(true);
	 * 
	 * Role defaultRole = roleRepository.getOneByName("School");
	 * if(defaultRole==null) throw new
	 * ValidationException("Role Parent does not exist");
	 * 
	 * List<Role> defaultRoleList = new ArrayList<>();
	 * defaultRoleList.add(defaultRole);
	 * 
	 * if (!defaultRoleList.isEmpty()) { user.setRoles(defaultRoleList); }
	 * user.setSchool(school); return userRepository.save(user); }
	 */
}
