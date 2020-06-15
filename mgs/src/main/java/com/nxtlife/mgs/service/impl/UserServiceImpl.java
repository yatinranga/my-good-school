package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nxtlife.mgs.entity.common.RoleAuthorityKey;
import com.nxtlife.mgs.entity.common.UserRoleKey;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Authority;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.RoleAuthority;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.entity.user.UserRole;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.AuthorityRepository;
import com.nxtlife.mgs.jpa.RoleAuthorityRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.jpa.UserRoleRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.MailService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.Mail;
import com.nxtlife.mgs.view.MailRequest;
import com.nxtlife.mgs.view.PasswordRequest;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.UserRequest;
import com.nxtlife.mgs.view.user.UserResponse;
import com.nxtlife.mgs.view.user.security.AuthorityResponse;
import com.nxtlife.mgs.view.user.security.RoleResponse;
import com.sun.mail.smtp.SMTPSendFailedException;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseService implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private NotificationServiceImpl notificationService;

	@Autowired
	private MailService mailService;

	@Value("${spring.mail.username}")
	private String emailUsername;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private RoleAuthorityRepository roleAuthorityRepository;

	@Autowired
	private HttpServletRequest httpServletRequest;

	private static PasswordEncoder userPasswordEncoder = new BCryptPasswordEncoder();

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

	// private static Logger logger =
	// LoggerFactory.getLogger(UserService.class);

	// @PostConstruct
	// public void init() {
	//
	// // Iterate through the authority list and add it to the database!
	//
	// Set<Authority> authorityList = new HashSet<Authority>();
	//
	// Field[] fields = MgsAuth.Authorities.class.getDeclaredFields();
	// for (Field f : fields) {
	// if (Modifier.isStatic(f.getModifiers()) &&
	// Modifier.isFinal(f.getModifiers())) {
	// logger.info("Found authority {} ", f.getName());
	// Authority a = authorityRepository.getOneByName(f.getName());
	//
	// if (a == null) {
	// a = new Authority();
	// a.setName(f.getName());
	// a.setDescription(f.getName());
	// a = authorityRepository.save(a);
	//
	// }
	// authorityList.add(a);
	// }
	// }
	//
	// // attach it to tech admin role:)
	// Role role = roleRepository.getOneByName("Admin");
	// if (role == null) {
	// role = new Role();
	// role.setName("Admin");
	//// role.setCid(Utils.generateRandomAlphaNumString(8));
	// }
	// role.setAuthorities(authorityList);
	// role.setActive(true);
	// roleRepository.save(role);
	//
	// logger.info("attached Authorities to admin.");
	//
	// User user = userRepository.findByUserName("mainAdmin");
	//
	// if (user == null) {
	// user = new User();
	// // user.setUserName("Admin0001");
	// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	// String encodedPassword = encoder.encode("root");
	// user.setPassword(encodedPassword);
	// user.setCid(Utils.generateRandomAlphaNumString(8));
	// user.setContactNumber("8860571043");
	// user.setEmail("admin@gmail.com");
	// user.setUsername("mainAdmin");
	//// user.setUserType(UserType.Admin);
	// }
	// user.setRoleForUser(role);
	// user.setActive(true);
	// userRepository.save(user);
	// }

	// @Override
	// public User createUserForEntity(Object entity) {
	// User user = new User();
	// user.setActive(true);
	//// user.setRegisterType(RegisterType.MANUALLY);
	// setPasswordAndCidForUser(user);
	// Role defaultRole = null;
	//
	// if(entity instanceof Student) {
	// if (userRepository.countByUsernameAndActiveTrue(((Student)
	// entity).getUsername()) > 0) {
	// throw new ValidationException("This username is already registered");
	// }
	//
	// if ((((Student)entity).getMobileNumber() != null &&
	// (userRepository.countByContactNumber(((Student)entity).getMobileNumber()))
	// > 0)
	// || (((Student)entity).getEmail() != null &&
	// userRepository.countByEmail(((Student)entity).getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for Student [%s] is already registered
	// for some other Student",
	// ((Student)entity).getMobileNumber(), ((Student)entity).getEmail(),
	// ((Student)entity).getName()));
	// }
	//
	//// user.setUserType(UserType.Student);
	// user.setUsername(((Student) entity).getUsername());
	//
	// if (((Student) entity).getSubscriptionEndDate() != null)
	// if (((Student) entity).getSubscriptionEndDate()
	// .after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
	//// user.setIsPaid(true);
	//
	//
	// defaultRole = roleRepository.getOneByName("Student");
	// if (defaultRole == null)
	// throw new ValidationException("Role Student does not exist");
	//
	//// user.setStudent(((Student) entity));
	// user.setContactNumber(((Student) entity).getMobileNumber());
	// user.setEmail(((Student) entity).getEmail());
	//
	// }else if(entity instanceof Guardian) {
	//
	// if
	// (userRepository.countByUsernameAndActiveTrue(((Guardian)entity).getUsername())
	// > 0) {
	// throw new ValidationException("This username is already registered");
	// }
	//
	// if ((((Guardian)entity).getMobileNumber() != null &&
	// (userRepository.countByContactNumber(((Guardian)entity).getMobileNumber()))
	// > 0)
	// || (((Guardian)entity).getEmail() != null &&
	// userRepository.countByEmail(((Guardian)entity).getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for guardian [%s] is already registered
	// for some other guardian",
	// ((Guardian)entity).getMobileNumber(), ((Guardian)entity).getEmail(),
	// ((Guardian)entity).getName()));
	// }
	//
	//// user.setUserType(UserType.Parent);
	// user.setUsername(((Guardian)entity).getUsername());
	//
	// if (((Guardian)entity).getStudents() != null &&
	// !((Guardian)entity).getStudents().isEmpty())
	// for (Student s : ((Guardian)entity).getStudents()) {
	//
	// if (s.getSubscriptionEndDate() != null) {
	// if (s.getSubscriptionEndDate().after(
	// Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
	// {
	//// user.setIsPaid(true);
	// break;
	// }
	// }
	// }
	//
	// defaultRole = roleRepository.getOneByName("Guardian");
	// if (defaultRole == null)
	// throw new ValidationException("Role Guardian does not exist");
	//
	//// user.setGuardian(((Guardian)entity));
	// user.setContactNumber(((Guardian)entity).getMobileNumber());
	// user.setEmail(((Guardian)entity).getEmail());
	//
	// }else if(entity instanceof Teacher) {
	//
	// if
	// (userRepository.countByUsernameAndActiveTrue(((Teacher)entity).getUsername())
	// > 0) {
	// throw new ValidationException("This username is already registered");
	// }
	// if ((((Teacher)entity).getMobileNumber() != null &&
	// (userRepository.countByContactNumber(((Teacher)entity).getMobileNumber()))
	// > 0)
	// || (((Teacher)entity).getEmail() != null &&
	// userRepository.countByEmail(((Teacher)entity).getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for Teacher [%s] is already registered
	// for some other teacher",
	// ((Teacher)entity).getMobileNumber(), ((Teacher)entity).getEmail(),
	// ((Teacher)entity).getName()));
	// }
	//
	//// user.setUserType(UserType.Teacher);
	// user.setUsername(((Teacher)entity).getUsername());
	// defaultRole = roleRepository.getOneByName("Teacher");
	// if (defaultRole == null)
	// throw new ValidationException("Role Teacher does not exist");
	//
	// if (((Teacher)entity).getIsCoach() != null &&
	// ((Teacher)entity).getIsCoach() == true) {
	// defaultRole = roleRepository.getOneByName("Coach");
	// if (defaultRole == null)
	// throw new ValidationException("Role Coach does not exist");
	// }
	//// user.setTeacher(((Teacher)entity));
	// user.setContactNumber(((Teacher)entity).getMobileNumber());
	// user.setEmail(((Teacher)entity).getEmail());
	//
	// }else if(entity instanceof School) {
	//
	// if
	// (userRepository.countByUsernameAndActiveTrue(((School)entity).getUsername())
	// > 0) {
	// throw new ValidationException("This username is already registered");
	// }
	// if ((((School)entity).getContactNumber() != null &&
	// (userRepository.countByContactNumber(((School)entity).getContactNumber()))
	// > 0)
	// || (((School)entity).getEmail() != null &&
	// userRepository.countByEmail(((School)entity).getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for School [%s] is already registered
	// for some other school",
	// ((School)entity).getContactNumber(), ((School)entity).getEmail(),
	// ((School)entity).getName()));
	// }
	//
	//// user.setUserType(UserType.School);
	// user.setUsername(((School)entity).getUsername());
	// defaultRole = roleRepository.getOneByName("School");
	// if (defaultRole == null)
	// throw new ValidationException("Role School does not exist");
	//
	// user.setSchool(((School)entity));
	// user.setEmail(((School)entity).getEmail());
	// user.setContactNumber(((School)entity).getContactNumber());
	//
	// }else if(entity instanceof LFIN){
	// if
	// (userRepository.countByUsernameAndActiveTrue(((LFIN)entity).getUsername())
	// > 0) {
	// throw new ValidationException("This username is already registered");
	// }
	// if ((((LFIN)entity).getContactNumber() != null &&
	// (userRepository.countByContactNumber(((LFIN)entity).getContactNumber()))
	// > 0)
	// || (((LFIN)entity).getEmail() != null &&
	// userRepository.countByEmail(((LFIN)entity).getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for LFIN [%s] is already registered for
	// some other LFIN",
	// ((LFIN)entity).getContactNumber(), ((LFIN)entity).getEmail(),
	// ((LFIN)entity).getName()));
	// }
	//
	//// user.setUserType(UserType.LFIN);
	// user.setUsername(((LFIN)entity).getUsername());
	// if (!roleRepository.existsByName("Lfin"))
	// throw new ValidationException("Role Lfin does not exist");
	// defaultRole = new Role("Lfin");
	// user.setUserRoles(Arrays.asList(new UserRole(new
	// UserRoleKey(defaultRole.getId(), user.getId()), defaultRole, user)));
	//// user.setLfin(((LFIN)entity));
	// user.setEmail(((LFIN)entity).getEmail());
	// user.setContactNumber(((LFIN)entity).getContactNumber());
	//
	// }else {
	// throw new ValidationException(String.format("User for this entity type
	// (%s) cannot be created.", entity.getClass().getSimpleName()));
	// }
	//
	//
	//// user.setRoleForUser(defaultRole);
	// return user;
	// }

	// @Override
	// public User createSchoolUser(School school) {
	// if (userRepository.countByUserNameAndActiveTrue(school.getUsername()) >
	// 0) {
	// throw new ValidationException("This username is already registered");
	// }
	// if ((school.getContactNumber() != null &&
	// (userRepository.countByMobileNo(school.getContactNumber())) > 0)
	// || (school.getEmail() != null &&
	// userRepository.countByEmail(school.getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for School [%s] is already registered
	// for some other school",
	// school.getContactNumber(), school.getEmail(), school.getName()));
	// }
	// User user = new User();
	// user.setActive(true);
	// user.setUserType(UserType.School);
	// user.setRegisterType(RegisterType.MANUALLY);
	// user.setUserName(school.getUsername());
	// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	// String password = Utils.generateRandomAlphaNumString(10);
	// String encodedPassword = encoder.encode(password);
	// user.setPassword(encodedPassword);
	// user.setRawPassword(password);
	// System.out.println(String.format("Password : %s ANd EncodedPassword :
	// %s", user.getRawPassword() , user.getPassword()) );
	// user.setCid(Utils.generateRandomAlphaNumString(8));
	//
	// // user.setIsPaid(true);
	//
	// Role defaultRole = roleRepository.getOneByName("School");
	// if (defaultRole == null)
	// throw new ValidationException("Role School does not exist");
	//
	// user.setRoleForUser(defaultRole);
	// user.setSchool(school);
	// user.setEmail(school.getEmail());
	// user.setMobileNo(school.getContactNumber());
	// return user;
	// }
	//
	//
	// @Override
	// public User createStudentUser(Student student) {
	//
	// if (userRepository.countByUserNameAndActiveTrue(student.getUsername()) >
	// 0) {
	// throw new ValidationException("This username is already registered");
	// }
	// User user = new User();
	// user.setActive(true);
	// user.setUserType(UserType.Student);
	// user.setRegisterType(RegisterType.MANUALLY);
	// user.setUserName(student.getUsername());
	// // setting encrypted password
	// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	// String password = Utils.generateRandomAlphaNumString(10);
	// String encodedPassword = encoder.encode(password);
	// user.setPassword(encodedPassword);
	// user.setRawPassword(password);
	// System.out.println(String.format("Password : %s ANd EncodedPassword :
	// %s", user.getRawPassword() , user.getPassword()) );
	// user.setCid(Utils.generateRandomAlphaNumString(8));
	//
	// if (student.getSubscriptionEndDate() != null) {
	// if (student.getSubscriptionEndDate()
	// .after(Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
	// user.setIsPaid(true);
	// }
	// Role defaultRole = roleRepository.getOneByName("Student");
	// if (defaultRole == null)
	// throw new ValidationException("Role Student does not exist");
	//
	// user.setRoleForUser(defaultRole);
	// user.setStudent(student);
	// user.setMobileNo(student.getMobileNumber());
	// user.setEmail(student.getEmail());
	// // return userRepository.save(user);
	// return user;
	// }
	//
	// @Override
	// public User createTeacherUser(Teacher teacher) {
	// if (userRepository.countByUserNameAndActiveTrue(teacher.getUsername()) >
	// 0) {
	// throw new ValidationException("This username is already registered");
	// }
	// if ((teacher.getMobileNumber() != null &&
	// (userRepository.countByMobileNo(teacher.getMobileNumber())) > 0)
	// || (teacher.getEmail() != null &&
	// userRepository.countByEmail(teacher.getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for Teacher [%s] is already registered
	// for some other teacher",
	// teacher.getMobileNumber(), teacher.getEmail(), teacher.getName()));
	// }
	// User user = new User();
	// user.setActive(true);
	// user.setUserType(UserType.Teacher);
	// user.setRegisterType(RegisterType.MANUALLY); // setting it to manual may
	// be required to passed as an argument
	// user.setUserName(teacher.getUsername());
	//
	// // setting encrypted password
	// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	// String password = Utils.generateRandomAlphaNumString(10);
	// String encodedPassword = encoder.encode(password);
	// user.setPassword(encodedPassword);
	// user.setRawPassword(password);
	// System.out.println(String.format("Password : %s ANd EncodedPassword :
	// %s", user.getRawPassword() , user.getPassword()) );
	//
	// Role defaultRole = roleRepository.getOneByName("Teacher");
	// if (defaultRole == null)
	// throw new ValidationException("Role Teacher does not exist");
	//
	// if (teacher.getIsCoach() != null && teacher.getIsCoach() == true) {
	// defaultRole = roleRepository.getOneByName("Coach");
	// if (defaultRole == null)
	// throw new ValidationException("Role Coach does not exist");
	// }
	// user.setRoleForUser(defaultRole);
	// user.setTeacher(teacher);
	// user.setMobileNo(teacher.getMobileNumber());
	// user.setEmail(teacher.getEmail());
	// // return userRepository.save(user);
	// return user;
	// }
	//
	//
	// @Override
	// public User createParentUser(Guardian guardian) {
	// if (userRepository.countByUserNameAndActiveTrue(guardian.getUsername()) >
	// 0) {
	// throw new ValidationException("This username is already registered");
	// }
	//
	// if ((guardian.getMobileNumber() != null &&
	// (userRepository.countByMobileNo(guardian.getMobileNumber())) > 0)
	// || (guardian.getEmail() != null &&
	// userRepository.countByEmail(guardian.getEmail()) > 0)) {
	// throw new ValidationException(String.format(
	// "mobile number [%s] or email [%s] for guardian [%s] is already registered
	// for some other guardian",
	// guardian.getMobileNumber(), guardian.getEmail(), guardian.getName()));
	// }
	//
	// User user = new User();
	// user.setActive(true);
	// user.setUserType(UserType.Parent);
	// user.setRegisterType(RegisterType.MANUALLY);
	// user.setUserName(guardian.getUsername());
	//
	// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	// String password = Utils.generateRandomAlphaNumString(10);
	// String encodedPassword = encoder.encode(password);
	// user.setPassword(encodedPassword);
	// user.setRawPassword(password);
	// System.out.println(String.format("Password : %s ANd EncodedPassword :
	// %s", user.getRawPassword() , user.getPassword()) );
	// // try {
	// user.setCid(Utils.generateRandomAlphaNumString(8));
	//
	// if (guardian.getStudents() != null && !guardian.getStudents().isEmpty())
	// for (Student s : guardian.getStudents()) {
	//
	// if (s.getSubscriptionEndDate() != null) {
	// if (s.getSubscriptionEndDate().after(
	// Date.from(java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())))
	// {
	// user.setIsPaid(true);
	// break;
	// }
	// }
	// }
	//
	// Role defaultRole = roleRepository.getOneByName("Guardian");
	// if (defaultRole == null)
	// throw new ValidationException("Role Guardian does not exist");
	//
	// user.setRoleForUser(defaultRole);
	// user.setGuardian(guardian);
	// user.setMobileNo(guardian.getMobileNumber());
	// user.setEmail(guardian.getEmail());
	// // return userRepository.save(user);
	// return user;
	//
	// }

	// public SuccessResponse changePassword(PasswordRequest request) {
	//
	// request.checkPassword();
	//
	// User user = getUser();
	//
	// if (user == null)
	// throw new ValidationException("No user found.");
	// user = userRepository.findByIdAndActiveTrue(user.getId());
	//
	// String encodedPassword = user.getPassword();
	//
	// if (encodedPassword == null) {
	// throw new NotFoundException(
	// String.format("Old Password of user having [id-%s] not set. ",
	// user.getCid()));
	// }
	//
	// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	//
	// if (!encoder.matches(request.getOldPassword(), encodedPassword)) {
	// throw new ValidationException(String.format("old password[%s] is
	// incorrect", request.getOldPassword()));
	// }
	// user.setPassword(encoder.encode(request.getPassword()));
	// user = userRepository.save(user);
	//
	// return new SuccessResponse(HttpStatus.OK.value(), "password changed
	// successfully");
	// }

	// @Override
	// public SuccessResponse forgotPassword(String username) {
	//
	//// if (!username.matches("^[@A-Za-z0-9_]")) {
	//// throw new ValidationException(String.format("incorrect username [%s]",
	// username));
	//// }
	// if(username == null)
	// throw new ValidationException("username cannot be null.");
	//
	// User user = userRepository.findByUserNameAndActiveTrue(username);
	//
	// if (user == null) {
	// throw new NotFoundException(String.format("No user found having username
	// : [%s] ", username));
	// }
	//
	// if (user.getEmail() == null && user.getContactNumber() == null) {
	// throw new ValidationException("User email/contact not registered with
	// us");
	// }
	//
	// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	//
	// String generatedPassword = Utils.generateRandomAlphaNumString(8);
	// user.setPassword(encoder.encode(generatedPassword));
	// user = userRepository.save(user);
	// Boolean emailFlag =false;
	// if (user.getEmail() != null) {
	// Mail mail = usernamePasswordSendContentBuilder(user.getUsername(),
	// generatedPassword, emailUsername, user.getEmail());
	// mail.setMailSubject("Password reset successful.");
	// emailFlag = sendLoginCredentialsBySMTP(mail);
	// }
	// if(!emailFlag || user.getEmail() == null)
	// throw new ValidationException("Password could not be sent because email
	// may not be registered or email is wrong.");
	// return new SuccessResponse(HttpStatus.OK.value(),
	// "New generated password has been sent to your email and/or contact
	// number");
	//
	// }

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
	public Boolean sendLoginCredentialsBySMTP(Mail request) throws SMTPSendFailedException{
		if (request.getMailTo() == null || request.getMailFrom() == null || request.getMailSubject() == null)
			throw new ValidationException("Please provide to , from and subject if not provided already.");
		if (request.getMailContent() == null)
			throw new ValidationException("Please provide email content to send.");
		try {
			mailService.sendEmail(request);
		} catch (UnsupportedEncodingException | MessagingException e) {
			logger.info(String.format("Email not sent successfuly to email address %s , email might be incorrect. ",
					request.getMailTo()));
			return false;
			// throw new ValidationException(String.format("Something went wrong
			// unable to send email to (%s)", request.getMailTo()));
		}
		logger.info(String.format("Email sent successfuly to email address %s ", request.getMailTo()));
		System.out.println(String.format("Email sent successfuly to email address %s ", request.getMailTo()));

		return true;
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

	@PostConstruct
	public void init() {
		School school;
		if ((school = schoolRepository.findByNameAndActiveTrue("MyGoodSchool")) == null) {
			school = new School();
			school.setName("MyGoodSchool");
			school.setUsername(
					String.format("%s%s", school.getName().toLowerCase().substring(0, 3), Utils.generateRandomNumString(8)));
			school.setEmail("mygoodschool@gmail.com");
			school.setCid(Utils.generateRandomAlphaNumString(8));
			school.setActive(true);
			schoolRepository.save(school);
		}

		Role role;
		if ((role = roleRepository.findBySchoolIdAndName(school.getId(), "MainAdmin")) == null) {
			role = new Role("MainAdmin");
			role.setSchool(school);
		}
		if (userRepository.findByUsernameAndSchoolId("mgsadmin", school.getId()) == null) {
			User user = new User("mgsadmin", userPasswordEncoder.encode("12345"), null);
			user.setName("Mgs Admin");
			user.setCid(Utils.generateRandomAlphaNumString(8));
			user.setSchool(school);
			user.setUserRoles(Arrays.asList(new UserRole(new UserRoleKey(role.getId(), user.getId()), role, user)));
			userRepository.save(user);
			List<Authority> authorities = authorityRepository.findAll();
			for (Authority authority : authorities) {
				roleAuthorityRepository.save(
						new RoleAuthority(new RoleAuthorityKey(role.getId(), authority.getId()), role, authority));
			}
		}

	}

	@Override
	public User createUser(String name, String contactNumber, String email, Long schoolId) {
		User user = new User();
		user.setActive(true);
		user.setName(name);
		user.setUsername(String.format("%s%s", name.substring(0, 3), Utils.generateRandomNumString(8)));
		if (email != null && userRepository.existsByEmail(email))
			throw new ValidationException(String.format("User with email (%s) already exists.", email));
		user.setEmail(email);
		if (contactNumber != null && userRepository.existsByContactNumber(contactNumber))
			throw new ValidationException(
					String.format("User with contact number (%s) already exists.", contactNumber));
		user.setContactNumber(contactNumber);
		user.setCid(Utils.generateRandomAlphaNumString(6));
		if (schoolId != null) {
			School school = new School();
			school.setId(schoolId);
			user.setSchool(school);
		}
		setPasswordAndCidForUser(user);
		return user;
	}

	private void setPasswordAndCidForUser(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = Utils.generateRandomAlphaNumString(10);
		String encodedPassword = encoder.encode(password);
		user.setPassword(encodedPassword);
		user.setRawPassword(password);
		System.out.println(
				String.format("Password : %s ANd EncodedPassword : %s", user.getRawPassword(), user.getPassword()));
		user.setCid(Utils.generateRandomAlphaNumString(8));
	}

	// @Override
	// public UserDetails loadUserByUsername(String s) throws
	// UsernameNotFoundException {
	// User user = userRepository.findByUserName(s);
	// user.setUserId(user.getId());
	// return user;
	// }

	@Override
	@Transactional
	public UserResponse getLoggedInUserResponse() {
		User user = getUser();
		if (user == null)
			throw new ValidationException("No user found.");
		user = userRepository.findByIdAndActiveTrue(user.getId());
		return UserResponse.get(user);

	}

	/**
	 * this method used to validate role ids that these role ids exist in
	 * database or not for an organization
	 * 
	 * @param requestRoleIds
	 * @param schoolId
	 */
	private void validateRoleIds(Set<Long> requestRoleIds, String schoolId) {
		List<Long> roleIds = roleRepository.getAllIdsByActive(true);
		if (!roleIds.containsAll(requestRoleIds)) {
			requestRoleIds.removeAll(roleIds);
			throw new ValidationException(String.format("Some of the roles (%s) are not valid or not active",
					StringUtils.arrayToCommaDelimitedString(requestRoleIds.toArray())));

		}
	}

	private void validateRoleIds(Set<Long> requestRoleIds, Long schoolId) {
		List<Long> roleIds = roleRepository.getAllIdsByActive(true);
		if (!roleIds.containsAll(requestRoleIds)) {
			requestRoleIds.removeAll(roleIds);
			throw new ValidationException(String.format("Some of the roles (%s) are not valid or not active",
					StringUtils.arrayToCommaDelimitedString(requestRoleIds.toArray())));

		}
	}

	/**
	 * this method used to validate user request like username already exist or
	 * not
	 * 
	 * @param request
	 * @param schoolId
	 */
	@SuppressWarnings("unused")
	private void validate(UserRequest request, String schoolId) {
		if (userRepository.existsByUsernameAndSchoolCid(request.getUsername(), schoolId)) {
			throw new ValidationException(String.format("This user (%s) already exist", request.getUsername()));
		} else if (userRepository.findIdBySchoolCidAndContactNumberAndActive(schoolId, request.getContactNumber(),
				true) != null) {
			throw new ValidationException(String.format("This user's contactNo (%s) already exists",
					request.getContactNumber() == null ? "NA" : request.getContactNumber()));
		} else if (userRepository.findIdBySchoolCidAndEmailAndActive(schoolId, request.getEmail(), true) != null) {
			throw new ValidationException(String.format("This user's email (%s) already exists", request.getEmail()));
		}
		validateRoleIds(request.getRoleIds(), schoolId);
	}

	private void validate(UserRequest request, Long schoolId) {
		if (userRepository.existsByUsernameAndSchoolId(request.getUsername(), schoolId)) {
			throw new ValidationException(String.format("This user (%s) already exist", request.getUsername()));
		} else if (userRepository.findIdBySchoolIdAndContactNumberAndActive(schoolId, request.getContactNumber(),
				true) != null) {
			throw new ValidationException(String.format("This user's contactNo (%s) already exists",
					request.getContactNumber() == null ? "NA" : request.getContactNumber()));
		} else if (userRepository.findIdBySchoolIdAndEmailAndActive(schoolId, request.getEmail(), true) != null) {
			throw new ValidationException(String.format("This user's email (%s) already exists", request.getEmail()));
		}
		validateRoleIds(request.getRoleIds(), schoolId);
	}

	/**
	 * this method used to fetch user response using user entity and roleIds
	 * 
	 * @param user
	 * @param roleIds
	 * @return {@link UserResponse}
	 */
	private UserResponse fetch(User user, Set<Long> roleIds) {
		UserResponse response = UserResponse.get(user);
		response.setRoles(new HashSet<>());
		RoleResponse roleResponse;
		for (Long roleId : roleIds) {
			roleResponse = roleRepository.findResponseById(roleId);
			roleResponse.setAuthorities(authorityRepository.findByAuthorityRolesRoleId(roleId));
			response.getRoles().add(roleResponse);
		}
		return response;
	}

	@Override
	@Secured(AuthorityUtils.USER_CREATE)
	public UserResponse save(UserRequest request) {
		Long schoolId = null;
		if(getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin")))
			schoolId = schoolRepository.findIdByCid(request.getSchoolId());
		else
			schoolId = getUser().gettSchoolId();
		
		validate(request, schoolId);
		User user = request.toEntity();
		user.settSchoolId(schoolId);
		user.setCid(Utils.generateRandomAlphaNumString(8));
		user.setPassword(userPasswordEncoder.encode("12345"));
		user = userRepository.save(user);
		for (Long roleId : request.getRoleIds()) {
			userRoleRepository.save(user.getId(), roleId);
		}
		return fetch(user, request.getRoleIds());
	}

	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public List<UserResponse> findAll() {
		Long schoolId = getUser().gettSchoolId();
		List<UserResponse> userResponseList = userRepository.findBySchoolId(schoolId);
		Set<Long> roleIds = roleRepository.findIdsBySchoolIdAandActive(schoolId, true);
		Map<Long, List<AuthorityResponse>> roleAuthoritiesMap = new HashMap<>();
		for (Long roleId : roleIds) {
			roleAuthoritiesMap.put(roleId, authorityRepository.findByAuthorityRolesRoleId(roleId));
		}
		for (UserResponse user : userResponseList) {
			user.setRoles(roleRepository.findByRoleUsersUserCid(user.getId()));
			for (RoleResponse role : user.getRoles()) {
				role.setAuthorities(roleAuthoritiesMap.get(role.getId()));
			}
		}
		return userResponseList;
	}
	
//	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public List<UserResponse> findAllByRoleId(Long roleId) {
		Long schoolId = getUser().gettSchoolId();
		List<UserResponse> userResponseList = userRepository.findBySchoolIdAndIdIn(schoolId , userRoleRepository.findUserIdsByRoleId(roleId));
		Set<Long> roleIds = roleRepository.findIdsBySchoolIdAandActive(schoolId, true);
		Map<Long, List<AuthorityResponse>> roleAuthoritiesMap = new HashMap<>();
		for (Long rId : roleIds) {
			roleAuthoritiesMap.put(rId, authorityRepository.findByAuthorityRolesRoleId(rId));
		}
		for (UserResponse user : userResponseList) {
			user.setRoles(roleRepository.findByRoleUsersUserCid(user.getId()));
			for (RoleResponse role : user.getRoles()) {
				role.setAuthorities(roleAuthoritiesMap.get(role.getId()));
			}
		}
		return userResponseList;
	}

	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public UserResponse findById(String id) {
		Long schoolId = getUser().gettSchoolId();
		UserResponse user = userRepository.findResponseByCid(id);
		if (user == null) {
			throw new NotFoundException(String.format("User(%s) not found", id));
		}
		if (!user.getSchoolId().equals(schoolId)) {
			throw new AccessDeniedException("You aren't login with correct user to fetch this details");
		}
		user.setRoles(roleRepository.findByRoleUsersUserCid(user.getId()));
		for (RoleResponse role : user.getRoles()) {
			role.setAuthorities(authorityRepository.findByAuthorityRolesRoleId(role.getId()));
		}
		return user;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("username(%s) not found", username));
		} else if (!user.getActive()) {
			throw new ValidationException("User is deactivated. Please ask admin to activate again to access.");
		}
		user.setUserId(user.getId());
		Set<Authority> authorities = new HashSet<>();
		Set<Role> roles = new HashSet<>();
		for (UserRole userRole : user.getUserRoles()) {
			for (RoleAuthority roleAuthority : userRole.getRole().getRoleAuthorities()) {
				authorities.add(roleAuthority.getAuthority());
				roles.add(roleAuthority.getRole());
			}
		}
		if (user.getSchool() != null)
			user.settSchoolId(user.getSchool().getId());
		user.setAuthorities(authorities);
		user.setRoles(roles);
		return user;
	}

	@Override
	public UserResponse findByAuthentication() {
		User user = getUser();
		if (user == null) {
			throw new NotFoundException("User not found");
		}
		UserResponse response = UserResponse.get(user);
		response.setSchool(schoolRepository.findResponseById(user.gettSchoolId()));
		return response;
	}

	@Override
	@Secured(AuthorityUtils.USER_UPDATE)
	public UserResponse update(String userId, UserRequest request) {
		Long schoolId = getUser().gettSchoolId();
		Boolean requestBody = false;
		if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
			validateRoleIds(request.getRoleIds(), schoolId);
			requestBody = true;
		}
		if (request.getContactNumber() == null && request.getEmail() == null && request.getName() == null
				&& !requestBody) {
			throw new ValidationException("Request body is not valid");
		}
		Long id;
		User user = userRepository.findByCid(userId);
		if (user == null) {
			throw new NotFoundException(String.format("User (%s) not found", userId));
		}
		if (request.getContactNumber() != null) {
			if (request.getContactNumber().length() != 10 || !Pattern.matches("^[0-9]*$", request.getContactNumber())) {
				throw new ValidationException("Contact number value is not correct");
			}
			id = userRepository.findIdBySchoolIdAndContactNumberAndActive(schoolId, request.getContactNumber(), true);
			if (id != null) {
				throw new ValidationException(
						String.format("This contact number (%s) already exists", request.getContactNumber()));
			}
			user.setContactNumber(request.getContactNumber());
		}
		if (request.getEmail() != null) {
			id = userRepository.findIdBySchoolIdAndEmailAndActive(schoolId, request.getEmail(), true);
			if (id != null) {
				throw new ValidationException(String.format("This email (%s) already exists", request.getEmail()));
			}
			user.setEmail(request.getEmail());
		}
		if (request.getName() != null) {
			user.setName(request.getName());
		}
		userRepository.save(user);
		Set<Long> roleIds = userRoleRepository.findRoleIdsByUserCid(userId);
		if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
			Set<Long> requestedRoleIds = new HashSet<>(request.getRoleIds());
			Set<Long> newRoles = request.getRoleIds();
			newRoles.removeAll(roleIds);
			for (Long roleId : newRoles) {
				userRoleRepository.save(user.getId(), roleId);
			}
			roleIds.removeAll(requestedRoleIds);
			for (Long roleId : roleIds) {
				userRoleRepository.delete(userRepository.findIdByCid(userId), roleId);
			}
		}
		return fetch(user, request.getRoleIds() == null ? roleIds : request.getRoleIds());
	}

	@Override
	public SuccessResponse forgotPassword(String username) {
		if (!username.matches("^[@A-Za-z0-9_]{3,20}$")) {
			throw new ValidationException(String.format("Incorrect username [%s]", username));
		}
		String client = httpServletRequest.getHeader("clientId");
		if (client == null) {
			throw new ValidationException("Client header not found");
		}
		Long schoolId = schoolRepository.findIdByName(client);
		if (schoolId == null) {
			throw new NotFoundException(String.format("Organization(%s) not found", client));
		}
		Map<String, String> response = userRepository.findEmailAndContactByUsernameAndSchoolId(username, schoolId);
		if (response == null) {
			throw new NotFoundException(String.format("User[username-%s] not found", username));
		}
		if (response.get("email") == null && response.get("contactNo") == null) {
			throw new ValidationException("User email/contact not register with us");
		}
		String generatedPassword = UUID.randomUUID().toString().substring(0, 6);
		logger.info("Password {} has been sent to email {}/contact {}", generatedPassword, response.get("email"),
				response.get("contactNo"));
		userRepository.setGeneratedPassword(username, schoolId, userPasswordEncoder.encode(generatedPassword));
		return new SuccessResponse(HttpStatus.OK.value(),
				"New generated password has been sent to your email and contact number");
	}

	@Override
	public SuccessResponse matchGeneratedPassword(String username, String generatedPassword) {
		String client = httpServletRequest.getHeader("clientId");
		if (client == null) {
			throw new ValidationException("Client header not found");
		}
		Long organizationId = schoolRepository.findIdByName(client);
		if (organizationId == null) {
			throw new NotFoundException(String.format("Organization(%s) not found", client));
		}
		String encodedGeneratedPassword = userRepository.findGeneratedPasswordByUsernameAndSchoolId(username,
				organizationId);
		if (encodedGeneratedPassword == null) {
			throw new NotFoundException(String.format("User[username-%s] not found or password already set", username));
		}
		if (!userPasswordEncoder.matches(generatedPassword, encodedGeneratedPassword)) {
			throw new ValidationException(String.format("Sent generated password[%s] incorrect", generatedPassword));
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Generated password matched");
	}

	@Override
	public SuccessResponse changePasswordByGeneratedPassword(PasswordRequest request) {
		request.checkGeneratedPassword();
		String client = httpServletRequest.getHeader("clientId");
		if (client == null) {
			throw new ValidationException("Client header not found");
		}
		Long organizationId = schoolRepository.findIdByName(client);
		if (organizationId == null) {
			throw new NotFoundException(String.format("Organization(%s) not found", client));
		}
		SuccessResponse response = matchGeneratedPassword(request.getUsername(), request.getGeneratedPassword());
		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new ValidationException("Generated password didn't match");
		}
		int rows = userRepository.setPassword(request.getUsername(), organizationId,
				userPasswordEncoder.encode(request.getPassword()));
		if (rows == 0) {
			throw new ValidationException("No row updated");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully");
	}

	@Override
	public SuccessResponse changePassword(PasswordRequest request) {
		request.checkPassword();
		String encodedPassword = userRepository.findPasswordById(getUserId());
		if (encodedPassword == null) {
			throw new NotFoundException(String.format("User[id-%s] not found or password already set", getUserId()));
		}
		if (!userPasswordEncoder.matches(request.getOldPassword(), encodedPassword)) {
			throw new ValidationException(String.format("Old password[%s] incorrect", request.getOldPassword()));
		}
		int rows = userRepository.setPassword(getUserId(), userPasswordEncoder.encode(request.getPassword()));
		if (rows == 0) {
			throw new ValidationException("No row updated");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully");
	}

	@Override
	@Secured(AuthorityUtils.USER_UPDATE)
	public SuccessResponse activate(String id) {
		if (!userRepository.existsByCid(id)) {
			throw new NotFoundException(String.format("User (%s) not found", id));
		}
		if (userRoleRepository.findRoleIdsByUserCidAndRoleActive(id, false).size() > 0) {
			throw new ValidationException("Some of the roles are not active for this user");
		}
		int rows = userRepository.activate(id, getUserId(), new Date());
		if (rows > 0) {
			logger.info("User {} activated successfully", id);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "User activated successfully");
	}

	@Override
	@Secured(AuthorityUtils.USER_DELETE)
	public SuccessResponse delete(String id) {
		if (!userRepository.existsByCid(id)) {
			throw new NotFoundException(String.format("User (%s) not found", id));
		}
		int rows = userRepository.delete(id, getUserId(), new Date());
		if (rows > 0) {
			logger.info("User {} deleted successfully", id);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "User deleted successfully");
	}
}
