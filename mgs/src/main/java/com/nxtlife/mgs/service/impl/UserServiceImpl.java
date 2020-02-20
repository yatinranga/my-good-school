package com.nxtlife.mgs.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.auth.MgsAuth;
import com.nxtlife.mgs.entity.user.Authority;
import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.RegisterType;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.AuthorityRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.PasswordRequest;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.UserResponse;

@Service
public class UserServiceImpl extends BaseService implements UserService, UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	Utils utils;

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@PostConstruct
	public void init() {

		// Iterate through the authority list and add it to the database!

		Set<Authority> authorityList = new HashSet<Authority>();

		Field[] fields = MgsAuth.Authorities.class.getDeclaredFields();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) {
				logger.info("Found authority {} ", f.getName());
				Authority a = authorityRepository.getOneByName(f.getName());

				if (a == null) {
					a = new Authority();
					a.setName(f.getName());
					a.setDescription(f.getName());
					a = authorityRepository.save(a);

				}
				authorityList.add(a);
			}
		}

		// attach it to tech admin role:)
		Role role = roleRepository.getOneByName("Admin");
		if (role == null) {
			role = new Role();
			role.setName("Admin");
			role.setCid(utils.generateRandomAlphaNumString(8));
		}
		role.setAuthorities(authorityList);
		roleRepository.save(role);
		logger.info("attached Authorities to admin.");

//		Role role = roleRepository.getOneByName("Admin");
//		if (role == null) {
//			role = new Role();
//			role.setCid(utils.generateRandomAlphaNumString(8));
//			role.setName("Admin");
//			roleRepository.save(role);
//		}
//		// Logic for authorities missing

		if (userRepository.findByUserName("mainAdmin") == null) {
			User user = new User();
			user.setRoleForUser(role);
//		    user.setUserName("Admin0001");
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode("root");
			user.setPassword(encodedPassword);
			user.setCid(utils.generateRandomAlphaNumString(8));
			user.setActive(true);
			user.setContactNo("8860571043");
			user.setEmail("admin@gmail.com");
			user.setUserName("mainAdmin");
			user.setUserType(UserType.Admin);
			userRepository.save(user);
		}
	}

	@Override
	public User createStudentUser(Student student) {

		if (userRepository.countByUserNameAndActiveTrue(student.getUsername()) > 0) {
			throw new ValidationException("This username is already registered");
		}
		User user = new User();
		user.setActive(true);
		user.setUserType(UserType.Student);
		user.setRegisterType(RegisterType.MANUALLY);
		user.setUserName(student.getUsername());
		// setting encrypted password

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = utils.generateRandomAlphaNumString(10);
		String encodedPassword = encoder.encode(password);

		user.setPassword(encodedPassword);
		System.out.println("Password : " + user.getPassword());
		user.setCid(utils.generateRandomAlphaNumString(8));

		if (student.getSubscriptionEndDate() != null) {
			if (student.getSubscriptionEndDate()
					.after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
				user.setIsPaid(true);
		}
		Role defaultRole = roleRepository.getOneByName("Student");
		if (defaultRole == null)
			throw new ValidationException("Role Student does not exist");

		user.setRoleForUser(defaultRole);
		user.setStudent(student);
		user.setContactNo(student.getMobileNumber());
		user.setEmail(student.getEmail());
//		return userRepository.save(user);
		return user;
	}

	@Override
	public User createTeacherUser(Teacher teacher) {
		if (userRepository.countByUserNameAndActiveTrue(teacher.getUsername()) > 0) {
			throw new ValidationException("This username is already registered");
		}
		User user = new User();
		user.setActive(true);
		user.setUserType(UserType.Teacher);
		user.setRegisterType(RegisterType.MANUALLY); // setting it to manual may be required to passed as an argument
		user.setUserName(teacher.getUsername());

		// setting encrypted password
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(utils.generateRandomAlphaNumString(10));
		user.setPassword(encodedPassword);
		user.setCid(utils.generateRandomAlphaNumString(8));

//			if(teacher.getSubscriptionEndDate()!=null) {
//				if(teacher.getSubscriptionEndDate().after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
//					user.setIsPaid(true);
//			}
		Role defaultRole = roleRepository.getOneByName("Teacher");
		if (defaultRole == null)
			throw new ValidationException("Role Teacher does not exist");

		if (teacher.getIsCoach() != null && teacher.getIsCoach() == true) {
			defaultRole = roleRepository.getOneByName("Coach");
			if (defaultRole == null)
				throw new ValidationException("Role Coach does not exist");
		}
		user.setRoleForUser(defaultRole);
		user.setTeacher(teacher);
		user.setContactNo(teacher.getMobileNumber());
		user.setEmail(teacher.getEmail());
//		return userRepository.save(user);
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(s);

		return user;
	}

	@Override
	public User createParentUser(Guardian guardian) {
		if (userRepository.countByUserNameAndActiveTrue(guardian.getUsername()) > 0) {
			throw new ValidationException("This username is already registered");
		}
		User user = new User();
		user.setActive(true);
		user.setUserType(UserType.Parent);
		user.setRegisterType(RegisterType.MANUALLY);
		user.setUserName(guardian.getUsername());
		// Setting encrypted password
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(utils.generateRandomAlphaNumString(10));

		/*
		 * if(userRepository.findByPassword(encodedPassword)!=null) throw new
		 * ValidationException("Password already exist please choose a different one.");
		 */
		user.setPassword(encodedPassword);
//		try {
		user.setCid(utils.generateRandomAlphaNumString(8));
//		} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
//			user.setCid(utils.generateRandomAlphaNumString(8));
//		}

		for (Student s : guardian.getStudents()) {

			if (s.getSubscriptionEndDate() != null) {
				if (s.getSubscriptionEndDate()
						.after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))) {
					user.setIsPaid(true);
					break;
				}

			}

		}

		Role defaultRole = roleRepository.getOneByName("Guardian");
		if (defaultRole == null)
			throw new ValidationException("Role Guardian does not exist");

		user.setRoleForUser(defaultRole);
		user.setGuardian(guardian);
		user.setContactNo(guardian.getMobileNumber());
		user.setEmail(guardian.getEmail());
//		return userRepository.save(user);
		return user;

	}

	@Override
	public UserResponse getLoggedInUser() {
		User user = getUser();
		if (user == null)
			throw new ValidationException("No user found.");
		return new UserResponse(user);

	}

	@Override
	public SuccessResponse changePassword(PasswordRequest request) {

		request.checkPassword();

		User user = getUser();

		String encodedPassword = user.getPassword();

		if (encodedPassword == null) {
			throw new NotFoundException(
					String.format("User[id-%s] not found or password already exist", user.getCid()));
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (!encoder.matches(request.getOldPassword(), encodedPassword)) {
			throw new ValidationException(String.format("old password[%s] incorrect", request.getOldPassword()));
		}
		user.setPassword(encoder.encode(request.getPassword()));
		user = userRepository.save(user);

		return new SuccessResponse(HttpStatus.OK.value(), "password changed successfully");
	}

	@Override
	public SuccessResponse forgotPassword(String username) {

		if (!username.matches("^[@A-Za-z0-9_]")) {
			throw new ValidationException(String.format("incorrect username [%s]", username));
		}

		User user = userRepository.findByUserNameAndActiveTrue(username);

		if (user == null) {
			throw new NotFoundException(String.format("no user found having username : [%s] ", username));
		}

		if (user.getEmail() == null && user.getContactNo() == null) {
			throw new ValidationException("User email/contact not register with us");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		user.setPassword(encoder.encode(utils.generateRandomAlphaNumString(10)));
		user = userRepository.save(user);

		return new SuccessResponse(HttpStatus.OK.value(),
				"new generated password has been sent to your email and contact number");

	}

//	@Override public User createSchoolUser(School school) { if
//	  (userRepository.countByUsername(school.getUsername()) > 0) { throw new
//	  ValidationException("This username is already registered"); } User user = new
//	  User(); user.setActive(true); user.setUserType(UserType.School);
//	  user.setRegisterType(RegisterType.MANUALLY);
//	  user.setUserName(school.getUsername()); //later change it to encrypted
//	  password //
//	  user.setPassword(bCryptPasswordEncoder.encode(school.getUsername()));
//	  //Setting username as password user.setPassword(school.getUsername()); try {
//	  user.setCid(utils.generateRandomAlphaNumString(8));
//	  }catch(ConstraintViolationException|
//
//	javax.validation.ConstraintViolationException ce)
//	{
//		user.setCid(utils.generateRandomAlphaNumString(8));
//	}
//
//	// user.setIsPaid(true);
//
//	Role defaultRole = roleRepository
//			.getOneByName("School");if(defaultRole==null)throw new ValidationException("Role Parent does not exist");
//
//	List<Role> defaultRoleList = new ArrayList<>();defaultRoleList.add(defaultRole);
//
//	if(!defaultRoleList.isEmpty())
//	{
//		user.setRoles(defaultRoleList);
//	}user.setSchool(school);return userRepository.save(user);
//}

}
