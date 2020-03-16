package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.auth.MgsAuth;
import com.nxtlife.mgs.entity.school.School;
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
import com.nxtlife.mgs.service.MailService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.Utils;

import com.nxtlife.mgs.view.Mail;
import com.nxtlife.mgs.view.MailRequest;

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

	@Autowired
	NotificationServiceImpl notificationService;

	@Autowired
	MailService mailService;
	
	@Value("${spring.mail.username}")
	private String emailUsername;

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
			role.setAuthorities(authorityList);
			role.setActive(true);
			roleRepository.save(role);
		}

		logger.info("attached Authorities to admin.");

		// Role role = roleRepository.getOneByName("Admin");
		// if (role == null) {
		// role = new Role();
		// role.setCid(utils.generateRandomAlphaNumString(8));
		// role.setName("Admin");
		// roleRepository.save(role);
		// }
		// // Logic for authorities missing

		if (userRepository.findByUserName("mainAdmin") == null) {
			User user = new User();
			user.setRoleForUser(role);
			// user.setUserName("Admin0001");
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode("root");
			user.setPassword(encodedPassword);
			user.setCid(utils.generateRandomAlphaNumString(8));
			user.setActive(true);
			user.setMobileNo("8860571043");
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
		user.setRawPassword(password);
		System.out.println(String.format("Password : %s ANd EncodedPassword : %s", user.getRawPassword() , user.getPassword()) );
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
		user.setMobileNo(student.getMobileNumber());
		user.setEmail(student.getEmail());
		// return userRepository.save(user);
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
		String password = utils.generateRandomAlphaNumString(10);
		String encodedPassword = encoder.encode(password);
		user.setPassword(encodedPassword);
		user.setRawPassword(password);
		System.out.println(String.format("Password : %s ANd EncodedPassword : %s", user.getRawPassword() , user.getPassword()) );

		// if(teacher.getSubscriptionEndDate()!=null) {
		// if(teacher.getSubscriptionEndDate().after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
		// user.setIsPaid(true);
		// }
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
		user.setMobileNo(teacher.getMobileNumber());
		user.setEmail(teacher.getEmail());
		// return userRepository.save(user);
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(s);
		user.setUserId(user.getId());
		return user;
	}

	@Override
	public User createParentUser(Guardian guardian) {
		if (userRepository.countByUserNameAndActiveTrue(guardian.getUsername()) > 0) {
			throw new ValidationException("This username is already registered");
		}

		if ((userRepository.countByMobileNo(guardian.getMobileNumber())) > 0
				|| userRepository.countByEmail(guardian.getEmail()) > 0) {
			throw new ValidationException(String.format(
					"mobile number [%s] or email [%s] for guardian [%s] is already registered for some other guardian",
					guardian.getMobileNumber(), guardian.getEmail(), guardian.getName()));
		}

		User user = new User();
		user.setActive(true);
		user.setUserType(UserType.Parent);
		user.setRegisterType(RegisterType.MANUALLY);
		user.setUserName(guardian.getUsername());

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = utils.generateRandomAlphaNumString(10);
		String encodedPassword = encoder.encode(password);
		user.setPassword(encodedPassword);
		user.setRawPassword(password);
		System.out.println(String.format("Password : %s ANd EncodedPassword : %s", user.getRawPassword() , user.getPassword()) );
		// try {
		user.setCid(utils.generateRandomAlphaNumString(8));
		// } catch (ConstraintViolationException |
		// javax.validation.ConstraintViolationException ce) {
		// user.setCid(utils.generateRandomAlphaNumString(8));
		// }
		if (guardian.getStudents() != null && !guardian.getStudents().isEmpty())
			for (Student s : guardian.getStudents()) {

				if (s.getSubscriptionEndDate() != null) {
					if (s.getSubscriptionEndDate().after(
							Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))) {
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
		user.setMobileNo(guardian.getMobileNumber());
		user.setEmail(guardian.getEmail());
		// return userRepository.save(user);
		return user;

	}

	@Override
	@Transactional
	public UserResponse getLoggedInUser() {
		User user = getUser();
		if (user == null)
			throw new ValidationException("No user found.");
		user = userRepository.findByIdAndActiveTrue(user.getId());
		return new UserResponse(user);

	}

	@Override
	public void sendLoginCredentialsByGmailApi(MailRequest request) {
		if (request.getTo() == null || request.getFrom() == null || request.getSubject() == null)
			throw new ValidationException("Please provide to , from and subject if not provided already.");
		if (request.getContent() == null)
			throw new ValidationException("Please provide email content to send.");
		try {
			notificationService.sendEmail(request);
		} catch (MessagingException | IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		logger.info(String.format("Email sent successfuly to email address %s ", request.getTo()));
		System.out.println(String.format("Email sent successfuly to email address %s ", request.getTo()));
	}

	@Override
	@Async
	public Boolean sendLoginCredentialsBySMTP(Mail request) {
		if (request.getMailTo() == null || request.getMailFrom() == null || request.getMailSubject() == null)
			throw new ValidationException("Please provide to , from and subject if not provided already.");
		if (request.getMailContent() == null)
			throw new ValidationException("Please provide email content to send.");
		try {
			mailService.sendEmail(request);
		} catch (UnsupportedEncodingException | MessagingException e) {
			return false;
			//throw new ValidationException(String.format("Something went wrong unable to send email to (%s)", request.getMailTo()));
		}
		logger.info(String.format("Email sent successfuly to email address %s ", request.getMailTo()));
		System.out.println(String.format("Email sent successfuly to email address %s ", request.getMailTo()));
		
		return true;
	}

	public SuccessResponse changePassword(PasswordRequest request) {

		request.checkPassword();

		User user = getUser();

		if (user == null)
			throw new ValidationException("No user found.");
		user = userRepository.findByIdAndActiveTrue(user.getId());
		
		String encodedPassword = user.getPassword();

		if (encodedPassword == null) {
			throw new NotFoundException(
					String.format("Old Password of user having [id-%s] not set. ", user.getCid()));
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (!encoder.matches(request.getOldPassword(), encodedPassword)) {
			throw new ValidationException(String.format("old password[%s] is  incorrect", request.getOldPassword()));
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
			throw new NotFoundException(String.format("No user found having username : [%s] ", username));
		}

		if (user.getEmail() == null && user.getMobileNo() == null) {
			throw new ValidationException("User email/contact not registered with us");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String generatedPassword = utils.generateRandomAlphaNumString(8);
		user.setPassword(encoder.encode(generatedPassword));
		user = userRepository.save(user);
		
		if (user.getEmail() != null) {
			Mail mail = usernamePasswordSendContentBuilder(user.getUsername(),
					generatedPassword, emailUsername, user.getEmail());
			mail.setMailSubject("Password reset successful.");
			sendLoginCredentialsBySMTP(mail);
		}
		return new SuccessResponse(HttpStatus.OK.value(),
				"New generated password has been sent to your email and/or contact number");

	}

	@Override
	public User createSchoolUser(School school) {
		if (userRepository.countByUserNameAndActiveTrue(school.getUsername()) > 0) {
			throw new ValidationException("This username is already registered");
		}
		User user = new User();
		user.setActive(true);
		user.setUserType(UserType.School);
		user.setRegisterType(RegisterType.MANUALLY);
		user.setUserName(school.getUsername());
		user.setEmail(school.getEmail());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = utils.generateRandomAlphaNumString(10);
		String encodedPassword = encoder.encode(password);
		user.setPassword(encodedPassword);
		user.setRawPassword(password);
		System.out.println(String.format("Password : %s ANd EncodedPassword : %s", user.getRawPassword() , user.getPassword()) );
		user.setCid(utils.generateRandomAlphaNumString(8));

		// user.setIsPaid(true);

		Role defaultRole = roleRepository.getOneByName("School");
		if (defaultRole == null)
			throw new ValidationException("Role School does not exist");

		user.setRoleForUser(defaultRole);
		user.setSchool(school);
		user.setMobileNo(school.getContactNumber());
		return user;
	}

	@Override
	public Mail usernamePasswordSendContentBuilder(String username, String password, String mailFrom, String mailTo) {
		String mailContent = String.format(
				"<b> Login Credentials :</b> <br> <b> Username :</b> %s <br> <b> Password :</b> %s <br>", username,
				password);
		Mail mail = new Mail(mailFrom, mailTo, "My Good School Login Credentials", mailContent);
		mail.setHtml(true);
		return mail;
	}

}
