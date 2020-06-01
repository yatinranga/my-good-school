package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.TeacherActivityGrade;
import com.nxtlife.mgs.entity.common.StudentActivityId;
import com.nxtlife.mgs.entity.common.UserRoleKey;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.entity.user.UserRole;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentClubRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherActivityGradeRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.jpa.UserRoleRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileStorageService;
//import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.service.UserService;
//import com.nxtlife.mgs.store.FileStore;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.ClubMembershipResponse;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@Service
public class TeacherServiceImpl extends BaseService implements TeacherService {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private GradeRepository gradeRepository;

//	@Autowired
//	private SequenceGeneratorService sequenceGeneratorService;
//
//	@Autowired
//	private SequenceGeneratorRepo sequenceGeneratorRepo;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private ActivityPerformedRepository activityPerformedRepository;

	@Value("${spring.mail.username}")
	private String emailUsername;

	@Autowired
	private FileStorageService<MultipartFile> fileStorageService;

	// @Autowired
	// private FileStore filestore;

	@Autowired
	private StudentClubRepository studentClubRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherActivityGradeRepository teacherActivityGradeRepository;

	// @PreAuthorize("hasRole('SchoolAdmin') or hasRole('MainAdmin') or
	// hasRole('Lfin')")
	@Secured(AuthorityUtils.SCHOOL_STAKEHOLDER_CREATE)
	@Override
	public TeacherResponse save(TeacherRequest request) {

		if (request == null)
			throw new ValidationException("Request can not be null.");
		if ((getUser() == null || getUser().getSchool() == null) && request.getSchoolId() == null)
			throw new ValidationException(String.format("School id cannot be null."));
		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");
		if (request.getName() == null)
			throw new ValidationException("Teacher name can not be null");
		if (request.getRoles() == null || request.getRoles().isEmpty())
			throw new ValidationException("Roles cannot be empty.");

		// int emailCount =
		// teacherRepository.countByEmailAndActiveTrue(request.getEmail());
		if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail()))
			throw new ValidationException(String.format("Email %s already exists", request.getEmail()));

		if (request.getMobileNumber() != null && userRepository.existsByContactNumber(request.getMobileNumber())) {
			throw new ValidationException(
					String.format("Mobile number (%s) already exists", request.getMobileNumber()));
		}
		Long schoolId = null;
		if(getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")))
			schoolId = schoolRepository.findIdByCid(request.getSchoolId());
		else
			schoolId = getUser().gettSchoolId();

		if (!schoolRepository.existsById(schoolId)) {
			throw new ValidationException(String.format("School with id : %s not found.", request.getSchoolId()));
		}

		Teacher teacher = request.toEntity();

		// saving school
		School school = new School();
		school.setId(schoolId);
		teacher.setSchool(school);

		User user = userService.createUser(teacher.getName(), teacher.getMobileNumber(), teacher.getEmail(),
				school.getId());
		Set<String> existingRoles = roleRepository.findNameBySchoolId(school.getId());
		if (request.getRoles() != null && !request.getRoles().isEmpty()) {
			for (String role : request.getRoles()) {
				if (!existingRoles.contains(role))
					throw new ValidationException(String.format("Role (%s) not present in school yet.", role));
			}
			List<Role> roles = roleRepository.findAllBySchoolIdAndNameIn(school.getId(), request.getRoles());
			List<UserRole> userRoles = roles.stream().distinct()
					.map(r -> new UserRole(new UserRoleKey(r.getId(), user.getId()), r, user))
					.collect(Collectors.toList());
			user.setUserRoles(userRoles);
		}
		if (user == null)
			throw new ValidationException("User not created successfully");

		teacher.setUser(user);
		teacher.setUsername(teacher.getUser().getUsername());

		// saving grades

		if (request.getGradeIds() != null && !request.getGradeIds().isEmpty()) {

			List<Grade> repoGradeList = gradeRepository.findAllBySchoolsCidAndActiveTrue(request.getSchoolId());
			List<Grade> finalGradeList = new ArrayList<Grade>();

			for (int i = 0; i < request.getGradeIds().size(); i++) {

				boolean flag = false;

				for (Grade grade : repoGradeList) {

					if (grade.getCid().equals(request.getGradeIds().get(i))) {
						flag = true;
						List<Teacher> teachers = new ArrayList<Teacher>();
						teachers = grade.getTeachers();
						teachers.add(teacher);
						finalGradeList.add(grade);
						break;
					}
				}

				if (flag == false) {
					throw new NotFoundException(
							String.format("grade having id [%s] didn't exist", request.getGradeIds().get(i)));
				}

			}

			teacher.setGrades(finalGradeList);
			teacher.setIsClassTeacher(true);
		}

		teacher.setCid(Utils.generateRandomAlphaNumString(8));

		// User user = userService.createTeacherUser(teacher);
		// User user = userService.createUserForEntity(teacher);

		teacher.setActive(true);

		teacher = teacherRepository.save(teacher);

		if (teacher == null)
			throw new RuntimeException("Something went wrong teacher not saved.");

		// saving activities

		if (request.getActivities() != null && !request.getActivities().isEmpty()) {

			List<Activity> repoActivityList = activityRepository
					.findAllBySchoolsCidAndActiveTrue(request.getSchoolId());

			List<TeacherActivityGrade> finalActivityList = new ArrayList<TeacherActivityGrade>();

			// validating activities present in request are also present in
			// school activity
			// list or not.

			for (int i = 0; i < request.getActivities().size(); i++) {

				boolean flag = false;
				// aptFlag = false;

				for (Activity activity : repoActivityList) {

					if (activity.getCid().equals(request.getActivities().get(i).getId())) {
						flag = true;
						teacher.setIsCoach(true);
						if (request.getActivities().get(i).getGrades() == null
								|| request.getActivities().get(i).getGrades().isEmpty())
							throw new ValidationException("Grades cannot be null or empty.");
						for (String gradeId : request.getActivities().get(i).getGrades()) {
							if (!gradeRepository.existsByCidAndActiveTrue(gradeId))
								throw new ValidationException(String.format("Grade with id (%s) not found.", gradeId));
							TeacherActivityGrade activityGrade = new TeacherActivityGrade(teacher.getId(),
									activity.getId(), gradeRepository.getOneByCid(gradeId).getId());
							finalActivityList.add(activityGrade);
						}

						// for (Teacher apt /* apt = alreadyPresentTeacher */ :
						// activity.getTeachers()) {
						//
						// if (apt.getEmail() == teacher.getEmail()
						// || apt.getMobileNumber() ==
						// teacher.getMobileNumber()) {
						// aptFlag = true;
						// break;
						// }
						// }

						// if (aptFlag == false) {
						// activity.getTeachers().add(teacher);
						// }

						break;
					}
				}

				if (flag == false) {
					throw new NotFoundException(String.format("activity having id [%s] didn't exist",
							request.getActivities().get(i).getId()));
				}
			}
			teacher.setTeacherActivityGrades(
					finalActivityList = teacherActivityGradeRepository.saveAll(finalActivityList));
		}

		Boolean emailFlag = false;

//		if (user.getEmail() != null)
//			try {
//				emailFlag = userService.sendLoginCredentialsBySMTP(userService.usernamePasswordSendContentBuilder(
//						user.getUsername(), user.getRawPassword(), emailUsername, user.getEmail()));
//			} catch (SMTPSendFailedException e) {
//				emailFlag= false;
//			}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("Teacher", new TeacherResponse(teacher));
		String emailMessage = emailFlag ? String.format("Email sent successfully to (%s)", user.getEmail())
				: String.format("Email not sent successfully to (%s) , email address might be wrong.", user.getEmail());
		int emailStatusCode = emailFlag ? 200 : 400;
		response.put("MailResponse", new SuccessResponse(emailStatusCode, emailMessage));
		// return new ResponseEntity<Map<String, Object>>(response,
		// HttpStatus.OK);

		return new TeacherResponse(teacher);
	}

	@Secured(AuthorityUtils.SCHOOL_FACULTY_PROFILE_UPDATE)
	@Override
	public TeacherResponse update(TeacherRequest request, String teacherCid) {

		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head") )) {
			if (teacherCid == null || !teacherRepository.existsByCidAndActiveTrue(teacherCid)) {
				throw new ValidationException("teacherId cannot be null or invalid.");
			}
		}else {
			teacherCid = teacherRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (teacherCid == null) 
				throw new ValidationException("Teacher not found probably userId is not set for student.");
		}
		
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(teacherCid);

		if (teacher == null) {
			throw new NotFoundException(String.format("teacher having id [%s] didn't exist", teacherCid));
		}

		teacher = request.toEntity(teacher);

		if(request.getSchoolId() != null && getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))) {
			Long schoolId = schoolRepository.findIdByCid(request.getSchoolId());
			School school = new School();
			school.setId(schoolId);
			teacher.setSchool(school);
		}

		if (request.getMobileNumber() != null) {

			// if(!userRepository.existsByContactNumberAndCid(request.getMobileNumber(),teacher.getUser().getCid()))
			// {
			if (!userRepository.existsByContactNumberAndCidNot(request.getMobileNumber(), teacher.getUser().getCid())) {
				teacher.setMobileNumber(request.getMobileNumber());
				teacher.getUser().setContactNumber(request.getMobileNumber());
			} else {
				throw new ValidationException(String.format("Mobile Number (%s) already belongs to some other user.",
						request.getMobileNumber()));
			}
			// }
		}

		if (request.getEmail() != null) {
			// if(!userRepository.existsByEmailAndCid(request.getEmail(),teacher.getUser().getCid()))
			// {
			if (!userRepository.existsByEmailAndCidNot(request.getEmail(), teacher.getUser().getCid())) {
				teacher.setEmail(request.getEmail());
				teacher.getUser().setEmail(request.getEmail());
			} else {
				throw new ValidationException(
						String.format("Email (%s) already belongs to some other user.", request.getEmail()));
			}
			// }
		}

		if (request.getActivities() != null && !request.getActivities().isEmpty()) {
			List<ActivityRequestResponse> requestActivityGrades = request.getActivities();
			List<TeacherActivityGrade> teacherActivityGradesOldList = teacher.getTeacherActivityGrades();
			List<TeacherActivityGrade> toDelete = new ArrayList<TeacherActivityGrade>();

			for (int j = 0; j < teacherActivityGradesOldList.size(); j++) {
				final int i = j;
				ActivityRequestResponse activity = requestActivityGrades.stream()
						.filter(act -> teacherActivityGradesOldList.get(i).getActivity().getCid().equals(act.getId()))
						.findFirst().orElse(null);

				if (activity == null) {
					if (toDelete.stream().anyMatch(actGrd -> actGrd.getActivity().getId()
							.equals(teacherActivityGradesOldList.get(i).getActivity().getId())))
						continue;

					if (activityPerformedRepository.existsByTeacherCidAndActivityCidAndActiveTrue(teacher.getCid(),
							teacherActivityGradesOldList.get(j).getActivity().getCid()))
						throw new ValidationException(String.format(
								"You cannot delete the activty : %s as few student has already performed this activity under you.",
								teacherActivityGradesOldList.get(j).getActivity().getName()));
					else
						toDelete.addAll(teacherActivityGradesOldList.stream()
								.filter(old -> old.getActivity().getId()
										.equals(teacherActivityGradesOldList.get(i).getActivity().getId()))
								.collect(Collectors.toList()));
					// teacherActivityGradeRepository.deleteAllByTeacherCidAndActivityCid(teacher.getCid()
					// ,teacherActivityGradesOldList.get(j).getActivity().getCid()
					// );
				} else {
					if (activity.getGrades() == null || activity.getGrades().isEmpty()) {
						ActivityRequestResponse deletedActivity = request.getActivities().stream()
								.filter(act -> act.getId().equals(activity.getId())
										&& (act.getGrades() == null || act.getGrades().isEmpty()) && !act.getVisited())
								.findFirst().orElse(null);
						if (deletedActivity != null) {
							toDelete.addAll(teacherActivityGradesOldList.stream()
									.filter(old -> old.getActivity().getCid().equals(deletedActivity.getId()))
									.collect(Collectors.toList()));
						}
						requestActivityGrades.remove(deletedActivity);
					}
					// requestActivityGrades.removeIf(actGrd ->
					// actGrd.getId().equals(activity.getId()));
					// throw new ValidationException(String.format("Grades
					// cannot be null or empty for activity with id
					// (%s)",activity.getId()));

					if (!activity.getGrades().contains(teacherActivityGradesOldList.get(j).getGrade().getCid()))
						toDelete.add(teacherActivityGradesOldList.get(j));
					else {
						activity.getGrades().remove(teacherActivityGradesOldList.get(i).getGrade().getCid());
						activity.setVisited(true);
					}
					// If(grd ->
					// grd.equals(teacherActivityGradesOldList.get(i).getGrade().getCid()));

				}
			}

			teacherActivityGradesOldList.removeAll(toDelete);
			// now delete from db
			if (!requestActivityGrades.isEmpty()) {
				for (ActivityRequestResponse actGrades : requestActivityGrades) {
					if (!activityRepository.existsByCidAndActiveTrue(actGrades.getId()))
						throw new ValidationException(
								String.format("Activity with id (%s) not found.", actGrades.getId()));
					if (actGrades.getGrades() != null) {
						for (String gradeCid : actGrades.getGrades()) {
							if (!gradeRepository.existsByCidAndActiveTrue(gradeCid))
								throw new ValidationException(String.format("Grade with id (%s) not found.", gradeCid));
							teacherActivityGradesOldList
							.add(teacherActivityGradeRepository.save(new TeacherActivityGrade(teacher.getId(),
									activityRepository.findIdByCidAndActiveTrue(actGrades.getId()),
									gradeRepository.findIdByCidAndActiveTrue(gradeCid))));
						}
					}
				}
			}
			teacherActivityGradeRepository.deleteAll(toDelete);
		}

		// if (request.getActivityIds() != null &&
		// !request.getActivityIds().isEmpty()) {
		// List<String> requestActivityIds = request.getActivityIds();
		// List<Activity> previousActivities = teacher.getActivities();
		// List<Activity> toBeDeletedActivities = new ArrayList<Activity>();
		//
		// for (int i = 0; i < previousActivities.size(); i++) {
		//
		// if (requestActivityIds.contains(previousActivities.get(i).getCid()))
		// {
		// requestActivityIds.remove(previousActivities.get(i).getCid());
		// } else {
		// if
		// (activityPerformedRepository.existsByTeacherCidAndActivityCidAndActiveTrue(teacher.getCid(),previousActivities.get(i).getCid()))
		// {
		// throw new ValidationException(String.format(
		// "You cannot delete the activty : %s as few student has already
		// performed activity %s under you.",
		// previousActivities.get(i).getName(),
		// previousActivities.get(i).getName()));
		// } else {
		// List<Teacher> teachers = previousActivities.get(i).getTeachers();
		// if (teachers != null && !teachers.isEmpty()) {
		// teachers.remove(teacher);
		// previousActivities.get(i).setTeachers(teachers);
		// toBeDeletedActivities.add(previousActivities.get(i));
		// }
		// previousActivities.remove(i--);
		// }
		// }
		// }
		//
		// if (requestActivityIds != null && !requestActivityIds.isEmpty()) {
		// for (String actId : requestActivityIds) {
		// if (!activityRepository.existsByCidAndActiveTrue(actId))
		// throw new ValidationException(String.format("Activity with id (%s)
		// not found .", actId));
		// Activity activity = activityRepository.findByCidAndActiveTrue(actId);
		// List<Teacher> teachers = new ArrayList<Teacher>();
		// teachers = activity.getTeachers();
		// teachers.add(teacher);
		// activity.setTeachers(teachers);
		// previousActivities.add(activity);
		// }
		// }
		// if (previousActivities != null && !previousActivities.isEmpty())
		// teacher.setIsCoach(true);
		// teacher.setActivities(previousActivities);
		// activityRepository.save(toBeDeletedActivities);
		//// teacher.setActivities(activityRepository.save(previousActivities));
		//
		// }

		if (request.getGradeIds() != null && !request.getGradeIds().isEmpty()) {
			List<String> requestGradeIds = request.getGradeIds();
			List<Grade> previousGrades = teacher.getGrades();

			for (int i = 0; i < previousGrades.size(); i++) {

				if (requestGradeIds.contains(previousGrades.get(i).getCid())) {
					requestGradeIds.remove(previousGrades.get(i).getCid());
				} else {
					previousGrades.remove(i--);
				}
			}
			if (requestGradeIds != null && !requestGradeIds.isEmpty())
				for (String gradeId : requestGradeIds) {
					if (!gradeRepository.existsByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(gradeId,
							teacher.getSchool().getCid()))
						throw new ValidationException(String.format("No grade with id (%s) found in your school (%s)",
								gradeId, teacher.getSchool().getName()));
					Grade grade = gradeRepository.findByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(gradeId,
							teacher.getSchool().getCid());
					List<Teacher> teachers = new ArrayList<Teacher>();
					teachers = grade.getTeachers();
					teachers.add(teacher);
					grade.setTeachers(teachers);
					previousGrades.add(grade);
				}
			teacher.setGrades(previousGrades);
		}

		teacher = teacherRepository.save(teacher);

		return new TeacherResponse(teacher);

	}


	@Override
	public TeacherResponse findById(Long id) {
		if (id == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findByIdAndActiveTrue(id);
		if (teacher == null)
			throw new ValidationException(String.format("Teacher having id : %s not found", id));

		return new TeacherResponse(teacher);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_FACULTY_FETCH)
	public TeacherResponse findByCId(String teacherId) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head") )) {
			if (teacherId == null || !teacherRepository.existsByCidAndActiveTrue(teacherId)) {
				throw new ValidationException("teacherId cannot be null or invalid.");
			}
		}else {
			teacherId = teacherRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (teacherId == null) 
				throw new ValidationException("Teacher not found probably userId is not set for student.");
		}

		Teacher teacher = teacherRepository.findByCidAndActiveTrue(teacherId);
		if (teacher == null)
			throw new ValidationException(String.format("Teacher having id : %s not found", teacherId));

		return new TeacherResponse(teacher);
	}

	@Override
	public List<TeacherResponse> findCoachesBySchoolAndActivityName(String schoolCid, String activityName) {
		if (activityName == null)
			throw new ValidationException("Activity name can not be null");
		List<Teacher> teachersList = teacherActivityGradeRepository
				.findAllTeacherByActivityNameAndTeacherSchoolCidActiveTrue(activityName, schoolCid);

		if (teachersList == null || teachersList.isEmpty())
			throw new ValidationException(
					String.format("No coaches found in the school with id (%s) for activity (%s)", activityName));

		return teachersList.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<ActivityRequestResponse> findAllActivitiesByCoachId(Long id) {

		if (id == null)
			throw new ValidationException("Id can not be null");

		List<Activity> activities = teacherActivityGradeRepository.findAllActivityByTeacherIdAndActiveTrue(id);
		if (activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");

		return activities.stream().map(ActivityRequestResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<ActivityRequestResponse> findAllActivitiesByCoachCId(String cId) {
		if (cId == null)
			throw new ValidationException("Id can not be null");
		List<Activity> activities = teacherActivityGradeRepository.findAllActivityByTeacherCidAndActiveTrue(cId);
		if (activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");

		return activities.stream().map(ActivityRequestResponse::new).distinct().collect(Collectors.toList());
	}

	
	@Secured(AuthorityUtils.SCHOOL_FACULTY_FETCH)
	@Override
//	@PreAuthorize("hasRole('MainAdmin') or hasRole('Lfin')")
	
	public List<TeacherResponse> getAllTeachers(Integer pageNo, Integer pageSize) {

		Pageable paging = PageRequest.of(pageNo, pageSize);

		Page<Teacher> teachers;

		teachers = teacherRepository.findAllByActiveTrue(paging);

		if (!teachers.hasContent())
			throw new ValidationException("No teachers found.");

		return teachers.getContent().stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> getAllCoaches() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers = teacherActivityGradeRepository.findAllTeacherByActiveTrue();
		// findAllBySchoolCidAndIsCoachTrue();
		if (teachers == null)
			throw new ValidationException("No coaches found.");
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> getAllClassTeachers() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers = teacherRepository.findAllByIsClassTeacherTrueAndActiveTrue();
		// findAllBySchoolCidAndIsClassTeacherTrue();
		if (teachers == null)
			throw new ValidationException("No class teachers found.");
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_FACULTY_FETCH)
	public TeacherResponse findCoachByCId(String teacherId) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head") )) {
			if (teacherId == null || !teacherRepository.existsByCidAndActiveTrue(teacherId)) {
				throw new ValidationException("teacherId cannot be null or invalid.");
			}
		}else {
			teacherId = teacherRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (teacherId == null) 
				throw new ValidationException("Teacher not found probably userId is not set for student.");
		}
		Teacher teacher = teacherRepository.findByCidAndIsCoachTrueAndActiveTrue(teacherId);
		if (teacher == null)
			throw new ValidationException("No coach found.");
		return new TeacherResponse(teacher);
	}

	@Override
	public TeacherResponse findCoachById(Long id) {
		if (id == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findByIdAndIsCoachTrueAndActiveTrue(id);
		if (teacher == null)
			throw new ValidationException("No coach found.");
		return new TeacherResponse(teacher);
	}

	@Override
	public TeacherResponse findClassTeacherByCId(String cId) {
		if (cId == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findByCidAndIsClassTeacherTrueAndActiveTrue(cId);
		if (teacher == null)
			throw new ValidationException("No class teacher found.");
		return new TeacherResponse(teacher);
	}

	@Override
	public TeacherResponse findClassTeacherById(Long id) {
		if (id == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findByIdAndIsClassTeacherTrueAndActiveTrue(id);
		if (teacher == null)
			throw new ValidationException("No class teacher found.");
		return new TeacherResponse(teacher);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_FACULTY_FETCH)
	public List<TeacherResponse> getAllTeachersOfSchool(String schoolCid) {
		schoolCid = !getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))  ? getUser().getSchool().getCid() : schoolCid ; 
		if(schoolCid == null)
			throw new ValidationException("School id cannot be null.");
		List<Teacher> teachers = new ArrayList<Teacher>();

		if ((teachers = teacherRepository.findAllBySchoolCidAndActiveTrue(schoolCid)) == null)
			throw new ValidationException(String.format("No teachers found in school with id (%s) .", schoolCid));
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> getAllClassTeachersOfSchool(String schoolCid) {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers = teacherRepository.findAllBySchoolCidAndIsClassTeacherTrueAndActiveTrue(schoolCid);
		if (teachers == null)
			throw new ValidationException("No class teachers found.");
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_FACULTY_FETCH)
	public List<TeacherResponse> getAllCoachesOfSchool(String schoolCid ,String gradeId) {
		schoolCid = !getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))  ? getUser().getSchool().getCid() : schoolCid ; 
		if(schoolCid == null)
			throw new ValidationException("School id cannot be null.");
		List<Long> grades = null;
		if(gradeId != null) {
			grades = Arrays.asList(gradeRepository.findIdByCidAndActiveTrue(gradeId));
		}else {
			Long tId = teacherRepository.getIdByUserIdAndActiveTrue(getUserId());
			if(tId != null)
				grades = gradeRepository.findGradeIdsOfTeacher(tId);
		}
		
		List<Teacher> teachers ;
		if(grades == null)
		    teachers= teacherActivityGradeRepository.findAllTeacherByTeacherSchoolCidActiveTrue(schoolCid);
		else
			teachers = teacherActivityGradeRepository.findAllTeacherByTeacherSchoolCidAndGradeIdsInAndActiveTrue(schoolCid, grades);
		// findAllBySchoolCidAndIsCoachTrue();
		if (teachers == null)
			throw new ValidationException("No coaches found.");
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_FACULTY_FETCH)
	public List<TeacherResponse> findCoachesBySchoolCidAndActivityCid(String schoolCid, String activityCid) {

		schoolCid = !getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))  ? getUser().getSchool().getCid() : schoolCid ; 
		if(schoolCid == null)
			throw new ValidationException("School id cannot be null.");

		if (!schoolRepository.existsByCidAndActiveTrue(schoolCid))
			throw new ValidationException(String.format("School having id (%s) does not exist.", schoolCid));
		if (!activityRepository.existsByCidAndActiveTrue(activityCid))
			throw new ValidationException(String.format("Activity having id (%s) does not exist.", activityCid));

		List<Teacher> teachers = teacherActivityGradeRepository
				.findAllTeacherByActivityCidAndTeacherSchoolCidActiveTrue(activityCid, schoolCid);
		if (teachers == null)
			throw new ValidationException(String.format(
					"No coaches found for activity having id (%s) in school having id (%s).", activityCid, schoolCid));

		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> getAllManagmentBySchool(String schoolCid) {

		if (schoolCid == null)
			throw new ValidationException("School id can't be null");

		List<Teacher> managmentMembers = teacherRepository
				.findAllBySchoolCidAndIsManagmentMemberTrueAndSchoolActiveTrueAndActiveTrue(schoolCid);

		if (managmentMembers == null || managmentMembers.isEmpty())
			throw new NotFoundException(
					String.format("no managment members are found in school having id [%s]", schoolCid));

		return managmentMembers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Secured(AuthorityUtils.SCHOOL_STAKEHOLDER_DELETE)
	@Override
	@Transactional
	public SuccessResponse delete(String teacherId) {

		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head") )) {
			if (teacherId == null || !teacherRepository.existsByCidAndActiveTrue(teacherId)) {
				throw new ValidationException("teacherId cannot be null or invalid.");
			}
		}else {
			teacherId = teacherRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (teacherId == null) 
				throw new ValidationException("Teacher not found probably userId is not set for student.");
		}
		String msg = new String("Teacher deleted successfully");
		if(teacherRepository.deleteByCidAndActiveTrue(teacherId,false) == 0)
			msg = new String("Teacher already deleted");
		return new SuccessResponse(org.springframework.http.HttpStatus.OK.value(), msg);
	}

	@Secured(AuthorityUtils.SCHOOL_FACULTY_PROFILE_UPDATE)
	@Override
	public TeacherResponse setProfilePic(MultipartFile file,String teacherCid) {
		if (file == null || file.getSize() == 0)
			throw new ValidationException("profilePic cannot be null or empty.");
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head") )) {
			if (teacherCid == null || !teacherRepository.existsByCidAndActiveTrue(teacherCid)) {
				throw new ValidationException("teacherId cannot be null or invalid.");
			}
		}else {
			teacherCid = teacherRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (teacherCid == null) 
				throw new ValidationException("Teacher not found probably userId is not set for student.");
		}

		Teacher teacher = teacherRepository.findByCidAndActiveTrue(teacherCid);
		if (teacher == null) {
			throw new ValidationException("User not login as either of them [Teacher, Coach, Management]");
		}

		if (teacher.getImageUrl() != null)
			fileStorageService.delete(teacher.getImageUrl());
		String imageUrl = fileStorageService.storeFile(file, file.getOriginalFilename(), "/profile-image/", true, true);
		teacher.setImageUrl(imageUrl);
		if (teacher.getUser() != null)
			teacher.getUser().setPicUrl(imageUrl);

		return new TeacherResponse(teacherRepository.save(teacher));
	}

	@Secured(AuthorityUtils.SCHOOL_STAKEHOLDER_CREATE)
	@Override
	public ResponseEntity<?> uploadTeachersFromExcel(MultipartFile file , String schoolCid) {

		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		if(getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))) {
			if(schoolCid == null || !schoolRepository.existsByCidAndActiveTrue(schoolCid))
				throw new ValidationException(String.format("School with id : (%s) not found.", schoolCid));
		}
		else
			schoolCid = schoolRepository.findCidById(getUser().gettSchoolId());
		if (schoolCid == null)
			throw new ValidationException("School id cannot be null.");

		List<String> errors = new ArrayList<String>();
		List<TeacherResponse> teacherResponseList = new ArrayList<>();
		List<Map<String, Object>> teacherRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook teachersSheet = new XSSFWorkbook(file.getInputStream());

			teacherRecords = findSheetRowValues(teachersSheet, "TEACHER", errors);
			for (int i = 0; i < teacherRecords.size(); i++) {
				List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
				tempStudentsRecords.add(teacherRecords.get(i));
				teacherResponseList.add(
						save(validateTeacherRequest(tempStudentsRecords, errors, schoolCid)));

			}


		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("TeacherResponseList", teacherResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

//	@Secured(AuthorityUtils.SCHOOL_STAKEHOLDER_CREATE)
//	@Override
//	public ResponseEntity<?> uploadManagementFromExcel(MultipartFile file, String schoolCid) {
//
//		if (file == null || file.isEmpty() || file.getSize() == 0)
//			throw new ValidationException("Pls upload valid excel file.");
//
//		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))) {
//			if(schoolCid == null || !schoolRepository.existsByCidAndActiveTrue(schoolCid))
//				throw new ValidationException(String.format("School with id : (%s) not found.", schoolCid));
//		}
//		else
//			schoolCid = schoolRepository.findCidById(getUser().gettSchoolId());
//		if (schoolCid == null)
//			throw new ValidationException("School id cannot be null.");
//
//		List<String> errors = new ArrayList<String>();
//		List<TeacherResponse> managementResponseList = new ArrayList<>();
//		List<Map<String, Object>> managementRecords = new ArrayList<Map<String, Object>>();
//
//		try {
//			XSSFWorkbook managementSheet = new XSSFWorkbook(file.getInputStream());
//			managementRecords = findSheetRowValues(managementSheet, "TEACHER", errors);
//			// errors = (List<String>)
//			// managementRecords.get(managementRecords.size() -
//			// 1).get("errors");
//			for (int i = 0; i < managementRecords.size(); i++) {
//				List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
//				tempStudentsRecords.add(managementRecords.get(i));
//				managementResponseList.add(save(validateTeacherRequest(tempStudentsRecords, errors, schoolCid)));
//			}
//		} catch (IOException e) {
//
//			throw new ValidationException("something wrong happened may be file not in acceptable format.");
//		}
//		Map<String, Object> responseMap = new HashMap<String, Object>();
//		responseMap.put("TeacherResponseList", managementResponseList);
//		responseMap.put("errors", errors);
//		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
//
//	}

	private List<Map<String, Object>> fetchRowValues(Map<String, CellType> columnTypes, XSSFSheet sheet,
			List<String> errors, String sheetName) {
		List<Map<String, Object>> rows = new ArrayList<>();
		Map<String, Object> columnValues = null;
		List<String> headers = new ArrayList<>();
		int columnSize = columnTypes.keySet().size();
		XSSFRow row;
		XSSFCell cell;
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			// System.out.println("Columns : " +
			// row.getPhysicalNumberOfCells());
			if (row.getPhysicalNumberOfCells() != columnSize) {
				errors.add(String.format("Some of the cells (Row number : %d) are missing or extras in %s sheet", i + 1,
						sheetName));
				// continue;
				if (i == 0)
					throw new ValidationException(String.format(
							"Some of the cells (Row number : %d) are missing or extras in %s sheet", i + 1, sheetName));
			}
			if (i == 0) {
				row.forEach(c -> {
					System.out.println(c.getStringCellValue());
					// c.getStringCellValue().trim();
					if (!columnTypes.containsKey(c.getStringCellValue().trim())) {
						errors.add(String.format("This cell (%s) is not valid", c.getStringCellValue()));
					} else {
						headers.add(c.getStringCellValue().trim());
					}
				});
			} else {
				columnValues = new HashMap<>();
				rows.add(columnValues);
				for (int j = 0; j < columnSize; j++) {
					cell = row.getCell(j, MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (cell != null) {
						if (columnTypes.get(headers.get(j)).equals(cell.getCellType())) {
							if (cell.getCellType() == CellType.NUMERIC) {
								if (headers.get(j).contains("DATE") || headers.get(j).contains("DOB"))
									columnValues.put(headers.get(j), cell.getDateCellValue());
								else
									columnValues.put(headers.get(j), new DataFormatter().formatCellValue(cell));
							} else if (cell.getCellType() == CellType.BOOLEAN) {
								columnValues.put(headers.get(j), cell.getBooleanCellValue());
							} else {
								columnValues.put(headers.get(j), cell.getStringCellValue());
							}
						} else {
							errors.add(String.format(
									"Cell Type is incorrect (Expected : %s, Actual : %s) for column %s of sheet (%s)",
									columnTypes.get(headers.get(j)), cell.getCellType(), headers.get(j), sheetName));
						}
					} else {
						// if(columnTypes.get(headers.get(j)).equals(cell.getCellType()))
						if (headers.get(j).equalsIgnoreCase("NAME") || headers.get(j).equalsIgnoreCase("EMAIL")
								|| headers.get(j).equalsIgnoreCase("MOBILE NUMBER")
								|| headers.get(j).equalsIgnoreCase("GRADE") || headers.get(j).equalsIgnoreCase("ROLE"))
							errors.add(String.format("Cell at row %d and column %d is blank for header %s.", i + 1,
									j + 1, headers.get(j)));
						columnValues.put(headers.get(j), null);
						// columnValues.put(headers.get(j), null);
					}
				}

			}
		}
		return rows;
	}

	private List<Map<String, Object>> findSheetRowValues(XSSFWorkbook workbook, String sheetName, List<String> errors) {
		// XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			errors.add(sheetName + " sheet not found");
			return null;
		}
		// if (sheet.getPhysicalNumberOfRows() > rowLimit) {
		// errors.add(String.format("Number of row can't be more than %d for %s
		// sheet", rowLimit, sheetName));
		// }
		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(sheetName);
		return fetchRowValues(columnTypes, sheet, errors, sheetName);

	}

	private TeacherRequest validateTeacherRequest(List<Map<String, Object>> teacherDetails, List<String> errors, String schoolCid) {
		if (teacherDetails == null || teacherDetails.isEmpty()) {
			errors.add("Teacher details not found");
		}
		TeacherRequest teacherRequest = new TeacherRequest();
		teacherRequest.setName((String) teacherDetails.get(0).get("NAME"));
		teacherRequest.setQualification((String) teacherDetails.get(0).get("QUALIFICATION"));
		teacherRequest.setDob(DateUtil.formatDate((Date) teacherDetails.get(0).get("DOB"), null, null));

		String designation = (String) teacherDetails.get(0).get("DESIGNATION");
		if (designation != null) 
			teacherRequest.setDesignation(designation);

		teacherRequest.setSchoolId(schoolCid);

			String activities = (String) teacherDetails.get(0).get("ACTIVITY");
			String[] activityNames = null;
			if (activities != null)
				activityNames = activities.split(",");

			if (activityNames != null && activityNames.length > 0) {
				List<ActivityRequestResponse> activitiesList = new ArrayList<ActivityRequestResponse>();
				for (String activity : activityNames) {
					String actCid = activityRepository.findCidByNameAndActiveTrue(activity);
					if (actCid != null) {
						ActivityRequestResponse act = new ActivityRequestResponse();
						act.setId(actCid);
						activitiesList.add(act);
					}
					else
						errors.add(String.format("Activity with name : %s does not exist.", activity));
				}
				teacherRequest.setActivities(activitiesList);
			} 
		

		if (teacherDetails.get(0).get("ROLE") != null) {
			teacherRequest
			.setRoles(new HashSet<>(Arrays.asList(((String) teacherDetails.get(0).get("ROLE")).split(","))));
		}
		String standard = (String) teacherDetails.get(0).get("GRADE");
		String gradeNames[] = null;
		List<String> gradeCIds = new ArrayList<String>();

		if (standard != null) {
			gradeNames = standard.split(",");
		}

		if (gradeNames != null && gradeNames.length > 0) {
			for (String grd : gradeNames) {
				Grade grade = null;
				String gradeAndSection[] = grd.split("-");

				if (gradeAndSection[0] == null && gradeAndSection[1] == null) {
					errors.add("GRADE and SECTION are empty.");
				} else if (gradeAndSection[0] != null && gradeAndSection[1] == null) {
					errors.add("SECTION is empty");
					grade = gradeRepository.findByNameAndSchoolsCid(gradeAndSection[0], schoolCid);
				} else if (gradeAndSection[1] != null && gradeAndSection[0] == null) {
					errors.add("GRADE is empty");
				} else {
					grade = gradeRepository.findByNameAndSchoolsCidAndSection(gradeAndSection[0], schoolCid,
							gradeAndSection[1]);
				}

				if (grade == null)
					errors.add(String.format("Grade  %s not found in records.", gradeAndSection[0]));
				else
					gradeCIds.add(grade.getCid());

			}
			teacherRequest.setGradeIds(gradeCIds);
		} 
		// }

		teacherRequest.setEmail((String) teacherDetails.get(0).get("EMAIL"));
		if (teacherDetails.get(0).get("ACTIVE") != null)
			teacherRequest.setActive(Boolean.valueOf((Boolean) teacherDetails.get(0).get("ACTIVE")));
		teacherRequest.setMobileNumber((String) teacherDetails.get(0).get("MOBILE NUMBER"));
		teacherRequest.setGender((String) teacherDetails.get(0).get("GENDER"));

		return teacherRequest;

	}

	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_ASSIGN)
	@Override
	public TeacherResponse addOrRemoveActivitiesToTeachers(TeacherRequest request) {
//		Long userId = getUserId();
//		if (userId == null)
//			throw new ValidationException(
//					"No user logged in currently, kindly log in as School Management to assign activities to Teachers.");
//		if (!schoolRepository.existsByUserIdAndActiveTrue(userId)
//				&& !teacherRepository.existsByUserIdAndIsManagmentMemberTrueAndActiveTrue(userId))
//			throw new UnauthorizedUserException(
//					"Not Authorized to assign activities to Teachers pls login as School or Management Member.");
		if (request == null)
			throw new ValidationException("Request cannot be null.");
		if (request.getTeachers() == null || request.getTeachers().isEmpty())
			throw new ValidationException(
					"teachers cannot be null or empty, its required to add activities to teachers.");
		List<Teacher> teachersToSave = new ArrayList<Teacher>();
		for (TeacherRequest teacherRequest : request.getTeachers()) {
			if (!teacherRepository.existsByCidAndActiveTrue(teacherRequest.getId()))
				throw new ValidationException(
						String.format("Teacher or Coach with id (%s) does not exist.", teacherRequest.getId()));
			Teacher teacher = teacherRepository.findByCidAndActiveTrue(teacherRequest.getId());

			if (teacherRequest.getActivities() != null && !teacherRequest.getActivities().isEmpty()) {
				List<ActivityRequestResponse> requestActivityGrades = teacherRequest.getActivities();
				List<TeacherActivityGrade> teacherActivityGradesOldList = teacher.getTeacherActivityGrades();
				List<TeacherActivityGrade> toDelete = new ArrayList<TeacherActivityGrade>();

				for (int j = 0; j < teacherActivityGradesOldList.size(); j++) {
					final int i = j;
					ActivityRequestResponse activity = requestActivityGrades.stream().filter(
							act -> teacherActivityGradesOldList.get(i).getActivity().getCid().equals(act.getId()))
							.findFirst().orElse(null);

					if (activity == null) {
						if (toDelete.stream().anyMatch(actGrd -> actGrd.getActivity().getId()
								.equals(teacherActivityGradesOldList.get(i).getActivity().getId())))
							continue;

						if (activityPerformedRepository.existsByTeacherCidAndActivityCidAndActiveTrue(teacher.getCid(),
								teacherActivityGradesOldList.get(j).getActivity().getCid()))
							throw new ValidationException(String.format(
									"You cannot delete the activty : %s as few student has already performed this activity under you.",
									teacherActivityGradesOldList.get(j).getActivity().getName()));
						else
							toDelete.addAll(teacherActivityGradesOldList.stream()
									.filter(old -> old.getActivity().getId()
											.equals(teacherActivityGradesOldList.get(i).getActivity().getId()))
									.collect(Collectors.toList()));
						// teacherActivityGradeRepository.deleteAllByTeacherCidAndActivityCid(teacher.getCid()
						// ,teacherActivityGradesOldList.get(j).getActivity().getCid()
						// );
					} else {
						if (activity.getGrades() == null || activity.getGrades().isEmpty()) {
							ActivityRequestResponse deletedActivity = teacherRequest.getActivities().stream()
									.filter(act -> act.getId().equals(activity.getId())
											&& (act.getGrades() == null || act.getGrades().isEmpty())
											&& !act.getVisited())
									.findFirst().orElse(null);
							if (deletedActivity != null) {
								toDelete.addAll(teacherActivityGradesOldList.stream()
										.filter(old -> old.getActivity().getCid().equals(deletedActivity.getId()))
										.collect(Collectors.toList()));
							}
							requestActivityGrades.remove(deletedActivity);
						}
						// requestActivityGrades.removeIf(actGrd ->
						// actGrd.getId().equals(activity.getId()));
						// throw new ValidationException(String.format("Grades
						// cannot be null or empty for activity with id
						// (%s)",activity.getId()));

						if (!activity.getGrades().contains(teacherActivityGradesOldList.get(j).getGrade().getCid()))
							toDelete.add(teacherActivityGradesOldList.get(j));
						else {
							activity.getGrades().remove(teacherActivityGradesOldList.get(i).getGrade().getCid());
							activity.setVisited(true);
						}
						// If(grd ->
						// grd.equals(teacherActivityGradesOldList.get(i).getGrade().getCid()));

					}
				}

				teacherActivityGradesOldList.removeAll(toDelete);
				// now delete from db
				if (!requestActivityGrades.isEmpty()) {
					for (ActivityRequestResponse actGrades : requestActivityGrades) {
						if (!activityRepository.existsByCidAndActiveTrue(actGrades.getId()))
							throw new ValidationException(
									String.format("Activity with id (%s) not found.", actGrades.getId()));
						if (actGrades.getGrades() != null) {
							for (String gradeCid : actGrades.getGrades()) {
								if (!gradeRepository.existsByCidAndActiveTrue(gradeCid))
									throw new ValidationException(
											String.format("Grade with id (%s) not found.", gradeCid));
								teacherActivityGradesOldList.add(
										teacherActivityGradeRepository.save(new TeacherActivityGrade(teacher.getId(),
												activityRepository.findIdByCidAndActiveTrue(actGrades.getId()),
												gradeRepository.findIdByCidAndActiveTrue(gradeCid))));
							}
						}
					}
				}
				teacherActivityGradeRepository.deleteAll(toDelete);
				teachersToSave.add(teacher);
			}


		}
		teachersToSave = teacherRepository.saveAll(teachersToSave);
		if (teachersToSave.isEmpty())
			throw new RuntimeException("Something went wrong operation failed ,please try again.");

		TeacherResponse response = new TeacherResponse();
		response.setTeachers(teachersToSave.stream().map(TeacherResponse::new).collect(Collectors.toList()));
		return response;
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_CLUB_MEMBERSHIP_FETCH)
	public List<ClubMembershipResponse> getMembershipDetails(String teacherCid) {
		Long teacherId = null;
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head"))) {
			if(teacherCid == null)
				throw new ValidationException("TeacherId cannot be null.");
			teacherId = teacherRepository.findIdByCidAndActiveTrue(teacherCid);
		}else {
			teacherId = teacherRepository.getIdByUserIdAndActiveTrue(getUserId());
		}

		if (teacherId == null)
			throw new ValidationException("Login as teacher/coach to view pending requests.");
		List<StudentClub> membership = studentClubRepository.findAllByTeacherIdAndActiveTrue(teacherId);

		if (membership == null || membership.isEmpty())
			throw new ValidationException("No membership application found for you.");

		return membership.stream().map(ClubMembershipResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_CLUB_MEMBERSHIP_FETCH)
	public List<ClubMembershipResponse> getMembershipDetailsbyClub(String clubId ,String teacherCid) {
		if (clubId == null)
			throw new ValidationException("club id cannot be null.");
		if (!activityRepository.existsByCidAndActiveTrue(clubId))
			throw new ValidationException(String.format("club with id (%s) not found.", clubId));
		Long teacherId = null;
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head"))) {
			if(teacherCid == null)
				throw new ValidationException("TeacherId cannot be null.");
			teacherId = teacherRepository.findIdByCidAndActiveTrue(teacherCid);
		}else {
			teacherId = teacherRepository.getIdByUserIdAndActiveTrue(getUserId());
		}
		if (teacherId == null)
			throw new ValidationException("Login as teacher/coach to view pending requests.");
		List<StudentClub> membership = studentClubRepository.findAllByTeacherIdAndActivityCidAndActiveTrue(teacherId,
				clubId);

		if (membership == null || membership.isEmpty())
			throw new ValidationException("No membership application found for you.");

		return membership.stream().map(ClubMembershipResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_CLUB_MEMBERSHIP_UPDATE)
	public ClubMembershipResponse updateStatus(String studentId, String activityId, Boolean isVerified ,String teacherCid) {
		Long teacherId = null;
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head"))) {
			if(teacherCid == null)
				throw new ValidationException("TeacherId cannot be null.");
			teacherId = teacherRepository.findIdByCidAndActiveTrue(teacherCid);
		}else {
			teacherId = teacherRepository.getIdByUserIdAndActiveTrue(getUserId());
		}
		if (teacherId == null)
			throw new ValidationException("Login as teacher/coach to verify/reject pending requests.");
		Optional<StudentClub> studentClub = studentClubRepository
				.findById(new StudentActivityId(studentRepository.findIdByCidAndActiveTrue(studentId),
						activityRepository.findIdByCidAndActiveTrue(activityId), teacherId));
		if (studentClub == null || !studentClub.isPresent()) {
			throw new ValidationException(String.format("This membership request not found in records."));
		}
		if (studentClub.get().getMembershipStatus().equals(ApprovalStatus.PENDING)) {
			studentClub.get().setMembershipStatus(isVerified ? ApprovalStatus.VERIFIED : ApprovalStatus.REJECTED);

			studentClub.get().setConsideredOn(new Date());
		} else {
			throw new ValidationException("This membership request already rejected or verified");
		}

		//sstudentClub = studentClubRepository.save(studentClub.get());
		return new ClubMembershipResponse(studentClubRepository.save(studentClub.get()));
	}

}
