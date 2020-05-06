package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.SequenceGeneratorRepo;
import com.nxtlife.mgs.jpa.StudentClubRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.service.SequenceGeneratorService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.store.FileStore;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.StudentActivityId;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.ClubMembershipResponse;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

@Service
public class TeacherServiceImpl extends BaseService implements TeacherService {

	@Autowired
	TeacherRepository teacherRepository;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	SequenceGeneratorRepo sequenceGeneratorRepo;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	ActivityPerformedRepository activityPerformedRepository;

	@Value("${spring.mail.username}")
	private String emailUsername;

	@Autowired
	private FileService fileService;

	@Autowired
	private FileStore filestore;
	
	@Autowired
	StudentClubRepository studentClubRepository;
	
	@Autowired
	StudentRepository studentRepository;

	@Override
	public TeacherResponse save(TeacherRequest request) {

		if (request == null)
			throw new ValidationException("Request can not be null.");
		if (request.getSchoolId() == null)
			throw new ValidationException(String.format("School id cannot be null."));
		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");
		if (request.getName() == null)
			throw new ValidationException("Teacher name can not be null");

		// int emailCount =
		// teacherRepository.countByEmailAndActiveTrue(request.getEmail());
		if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail()))
			throw new ValidationException(String.format("Email %s already exists", request.getEmail()));

		if (request.getMobileNumber() != null && userRepository.existsByContactNumber(request.getMobileNumber())) {
			throw new ValidationException(
					String.format("Mobile number (%s) already exists", request.getMobileNumber()));
		}

		Teacher teacher = request.toEntity();

		Long teachersequence;
		if (request.getIsCoach() != null && request.getIsCoach()) {
			teachersequence = sequenceGeneratorService.findSequenceByUserType(UserType.Coach);
			teacher.setUsername(String.format("COA%08d", teachersequence));

		} else if (request.getIsManagmentMember() != null && request.getIsManagmentMember()) {
			teachersequence = sequenceGeneratorService.findSequenceByUserType(UserType.SchoolManagement);
			teacher.setUsername(String.format("MGM%08d", teachersequence));
		} else {
			teachersequence = sequenceGeneratorService.findSequenceByUserType(UserType.Teacher);
			teacher.setUsername(String.format("TEA%08d", teachersequence));
		}

		// saving school

		if (request.getSchoolId() != null) {

			School school = schoolRepository.findByCidAndActiveTrue(request.getSchoolId());

			if (school == null) {
				throw new NotFoundException(String.format("school having id [%s] didn't exist", request.getSchoolId()));
			}
			teacher.setSchool(school);

		}

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

		// saving activities

		if (request.getActivityIds() != null && !request.getActivityIds().isEmpty()) {

			List<Activity> repoActivityList = activityRepository
					.findAllBySchoolsCidAndActiveTrue(request.getSchoolId());

			List<Activity> finalActivityList = new ArrayList<Activity>();

			// validating activities present in request are also present in school activity
			// list or not.

			for (int i = 0; i < request.getActivityIds().size(); i++) {

				boolean flag = false, aptFlag = false;

				for (Activity activity : repoActivityList) {

					if (activity.getCid().equals(request.getActivityIds().get(i))) {
						flag = true;
						finalActivityList.add(activity);

						for (Teacher apt /* apt = alreadyPresentTeacher */ : activity.getTeachers()) {

							if (apt.getEmail() == teacher.getEmail()
									|| apt.getMobileNumber() == teacher.getMobileNumber()) {
								aptFlag = true;
								break;
							}
						}

						if (aptFlag == false) {
							activity.getTeachers().add(teacher);
						}

						break;
					}
				}

				if (flag == false) {
					throw new NotFoundException(
							String.format("activity having id [%s] didn't exist", request.getActivityIds().get(i)));
				}
			}
			teacher.setActivities(finalActivityList);
			teacher.setIsCoach(true);
		}

		teacher.setCid(utils.generateRandomAlphaNumString(8));

//		User user = userService.createTeacherUser(teacher);
		User user = userService.createUserForEntity(teacher);

		if (user == null)
			throw new ValidationException("User not created successfully");

		teacher.setUser(user);
		teacher.setActive(true);

		teacher = teacherRepository.save(teacher);

		if (teacher == null)
			throw new RuntimeException("Something went wrong teacher not saved.");

		Boolean emailFlag = false;

		if (user.getEmail() != null)
			emailFlag = userService.sendLoginCredentialsBySMTP(userService.usernamePasswordSendContentBuilder(
					user.getUsername(), user.getRawPassword(), emailUsername, user.getEmail()));
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("Teacher", new TeacherResponse(teacher));
		String emailMessage = emailFlag ? String.format("Email sent successfully to (%s)", user.getEmail())
				: String.format("Email not sent successfully to (%s) , email address might be wrong.", user.getEmail());
		int emailStatusCode = emailFlag ? 200 : 400;
		response.put("MailResponse", new SuccessResponse(emailStatusCode, emailMessage));
//		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		return new TeacherResponse(teacher);
	}

	@Override
	public TeacherResponse update(TeacherRequest request, String cid) {

		if (cid == null) {
			throw new ValidationException("id can't be null");
		}

		Teacher teacher = teacherRepository.findByCidAndActiveTrue(cid);

		if (teacher == null) {
			throw new NotFoundException(String.format("teacher having id [%s] didn't exist", cid));
		}

		teacher = request.toEntity(teacher);

		if (request.getActivityIds() != null && !request.getActivityIds().isEmpty()) {
			List<String> requestActivityIds = request.getActivityIds();
			List<Activity> previousActivities = teacher.getActivities();
			List<Activity> toBeDeletedActivities = new ArrayList<Activity>();

			for (int i = 0; i < previousActivities.size(); i++) {

				if (requestActivityIds.contains(previousActivities.get(i).getCid())) {
					requestActivityIds.remove(previousActivities.get(i).getCid());
				} else {
					if (activityPerformedRepository.existsByTeacherCidAndActivityCidAndActiveTrue(teacher.getCid(),previousActivities.get(i).getCid())) {
						throw new ValidationException(String.format(
								"You cannot delete the activty : %s as few student has already performed activity %s under you.",
								previousActivities.get(i).getName(), previousActivities.get(i).getName()));
					} else {
						List<Teacher> teachers = previousActivities.get(i).getTeachers();
						if (teachers != null && !teachers.isEmpty()) {
							teachers.remove(teacher);
							previousActivities.get(i).setTeachers(teachers);
							toBeDeletedActivities.add(previousActivities.get(i));
						}
						previousActivities.remove(i--);
					}
				}
			}

			if (requestActivityIds != null && !requestActivityIds.isEmpty()) {
				for (String actId : requestActivityIds) {
					if (!activityRepository.existsByCidAndActiveTrue(actId))
						throw new ValidationException(String.format("Activity with id (%s) not found .", actId));
					Activity activity = activityRepository.findByCidAndActiveTrue(actId);
					List<Teacher> teachers = new ArrayList<Teacher>();
					teachers = activity.getTeachers();
					teachers.add(teacher);
					activity.setTeachers(teachers);
					previousActivities.add(activity);
				}
			}
			if (previousActivities != null && !previousActivities.isEmpty())
				teacher.setIsCoach(true);
			teacher.setActivities(previousActivities);
			activityRepository.save(toBeDeletedActivities);
//			teacher.setActivities(activityRepository.save(previousActivities));

		}

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
		
		if(request.getMobileNumber() != null) {
			
//			if(!userRepository.existsByContactNumberAndCid(request.getMobileNumber(),teacher.getUser().getCid())) {
				if(!userRepository.existsByContactNumberAndCidNot(request.getMobileNumber(),teacher.getUser().getCid())) {
					teacher.setMobileNumber(request.getMobileNumber());
					teacher.getUser().setContactNumber(request.getMobileNumber());
				}else {
					throw new ValidationException(String.format("Mobile Number (%s) already belongs to some other user.",request.getMobileNumber()));
				}
//			}
		}
		
		if(request.getEmail() != null) {
//			if(!userRepository.existsByEmailAndCid(request.getEmail(),teacher.getUser().getCid())) {
				if(!userRepository.existsByEmailAndCidNot(request.getEmail(),teacher.getUser().getCid())) {
					teacher.setEmail(request.getEmail());
					teacher.getUser().setEmail(request.getEmail());
				}else {
					throw new ValidationException(String.format("Email (%s) already belongs to some other user.",request.getEmail()));
				}
//			}
		}
		

		teacher = teacherRepository.save(teacher);

		return new TeacherResponse(teacher);

	}

	@Override
	public TeacherResponse saveClassTeacher(TeacherRequest request) {

		if (request != null)
			request.setIsClassTeacher(true);
		return save(request);
	}

	@Override
	public TeacherResponse saveCoach(TeacherRequest request) {

		if (request != null)
			request.setIsCoach(true);
		return save(request);
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
	public TeacherResponse findByCId(String cId) {
		if (cId == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(cId);
		if (teacher == null)
			throw new ValidationException(String.format("Teacher having id : %s not found", cId));

		return new TeacherResponse(teacher);
	}

	@Override
	public List<TeacherResponse> findCoachesBySchoolAndActivityName(String schoolCid, String activityName) {
		if (activityName == null)
			throw new ValidationException("Activity name can not be null");
		List<Teacher> teachersList = teacherRepository
				.findAllBySchoolCidAndActivitiesNameAndIsCoachTrueAndActiveTrue(schoolCid, activityName);

		if (teachersList == null || teachersList.isEmpty())
			throw new ValidationException(
					String.format("No coaches found in the school with id (%s) for activity (%s)", activityName));

		return teachersList.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<ActivityRequestResponse> findAllActivitiesByCoachId(Long id) {

		if (id == null)
			throw new ValidationException("Id can not be null");

		List<Activity> activities = activityRepository.findAllByTeachersIdAndActiveTrue(id);
		if (activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");

		return activities.stream().map(ActivityRequestResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<ActivityRequestResponse> findAllActivitiesByCoachCId(String cId) {
		if (cId == null)
			throw new ValidationException("Id can not be null");
		List<Activity> activities = activityRepository.findAllByTeachersCidAndActiveTrue(cId);
		if (activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");

		return activities.stream().map(ActivityRequestResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> getAllTeachers(Integer pageNo, Integer pageSize) {

		Pageable paging = new PageRequest(pageNo, pageSize);

		Page<Teacher> teachers;

		teachers = teacherRepository.findAllByActiveTrue(paging);

		if (!teachers.hasContent())
			throw new ValidationException("No teachers found.");

		return teachers.getContent().stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> getAllCoaches() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers = teacherRepository.findAllByIsCoachTrueAndActiveTrue();
//				findAllBySchoolCidAndIsCoachTrue();
		if (teachers == null)
			throw new ValidationException("No coaches found.");
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> getAllClassTeachers() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers = teacherRepository.findAllByIsClassTeacherTrueAndActiveTrue();
//				findAllBySchoolCidAndIsClassTeacherTrue();
		if (teachers == null)
			throw new ValidationException("No class teachers found.");
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public TeacherResponse findCoachByCId(String cId) {
		if (cId == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findByCidAndIsCoachTrueAndActiveTrue(cId);
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
	public List<TeacherResponse> getAllTeachersOfSchool(String schoolCid) {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers = teacherRepository.findAllBySchoolCidAndActiveTrue(schoolCid);
		if (teachers == null)
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
	public List<TeacherResponse> getAllCoachesOfSchool(String schoolCid) {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers = teacherRepository.findAllBySchoolCidAndIsCoachTrueAndActiveTrue(schoolCid);
//				findAllBySchoolCidAndIsCoachTrue();
		if (teachers == null)
			throw new ValidationException("No coaches found.");
		return teachers.stream().map(TeacherResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<TeacherResponse> findCoachesBySchoolCidAndActivityCid(String schoolCid, String activityCid) {

		if(!schoolRepository.existsByCidAndActiveTrue(schoolCid))
			throw new ValidationException(String.format("School having id (%s) does not exist.", schoolCid));
		if(!activityRepository.existsByCidAndActiveTrue(activityCid))
			throw new ValidationException(String.format("Activity having id (%s) does not exist.", activityCid));
		
		List<Teacher> teachers = teacherRepository
				.findAllBySchoolCidAndActivitiesCidAndIsCoachTrueAndActiveTrue(schoolCid, activityCid);
		if (teachers == null)
			throw new ValidationException(String.format("No coaches found for activity having id (%s) in school having id (%s)." , activityCid , schoolCid));

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

	@Override
	public SuccessResponse delete(String cid) {

		if (cid == null) {
			throw new ValidationException("teacher id can't be null");
		}

		Teacher teacher = teacherRepository.findByCidAndActiveTrue(cid);

		if (teacher == null) {
			throw new NotFoundException(String.format("teacher having id [%s] doesn't exist", cid));
		}

		teacher.setActive(false);
		teacher = teacherRepository.save(teacher);

		if (teacher == null)
			throw new RuntimeException(
					String.format("something went wrong teacher having id [%s] can't be deleted", cid));

		return new SuccessResponse(org.springframework.http.HttpStatus.OK.value(), "teacher deleted successfully");
	}
	
	@Override
	public TeacherResponse setProfilePic(MultipartFile file) {
		if (file == null || file.getSize() == 0)
			throw new ValidationException("profilePic cannot be null or empty.");
		
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("No user logged in currently.");
		
		Teacher teacher = teacherRepository.getByUserId(userId);
		if (teacher == null) {
			throw new ValidationException("User not login as either of them [Teacher, Coach, Management]");
		}
		
		String filename = UUID.randomUUID().toString() + "." + fileService.getFileExtension(file.getOriginalFilename());
		try {
			String imageUrl = filestore.store("profilePic", filename, file.getBytes());
			teacher.setImageUrl(imageUrl);
			if(teacher.getUser() != null)
				teacher.getUser().setImagePath(imageUrl);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ValidationException("Something went wromg image not saved/uploaded.");
		}

		return new TeacherResponse(teacherRepository.save(teacher));
	}

	@Override
	public ResponseEntity<?> uploadTeachersFromExcel(MultipartFile file, Boolean isCoach, String schoolCid) {

		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");
		if (schoolCid == null)
			throw new ValidationException("School id cannot be null.");
		if (!schoolRepository.existsByCidAndActiveTrue(schoolCid))
			throw new ValidationException(String.format("School with id : (%s) not found.", schoolCid));

		List<String> errors = new ArrayList<String>();
		List<TeacherResponse> teacherResponseList = new ArrayList<>();
		List<Map<String, Object>> teacherRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook teachersSheet = new XSSFWorkbook(file.getInputStream());
			if (isCoach == true) {
				teacherRecords = findSheetRowValues(teachersSheet, "COACH", errors);
//				errors = (List<String>) teacherRecords.get(teacherRecords.size() - 1).get("errors");
				for (int i = 0; i < teacherRecords.size(); i++) {
					List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
					tempStudentsRecords.add(teacherRecords.get(i));
					teacherResponseList
							.add(saveCoach(validateTeacherRequest(tempStudentsRecords, errors, true, schoolCid)));
				}
			} else {
				teacherRecords = findSheetRowValues(teachersSheet, "TEACHER", errors);
				for (int i = 0; i < teacherRecords.size(); i++) {
					List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
					tempStudentsRecords.add(teacherRecords.get(i));
					teacherResponseList.add(
							saveClassTeacher(validateTeacherRequest(tempStudentsRecords, errors, false, schoolCid)));
				}
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("TeacherResponseList", teacherResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> uploadManagementFromExcel(MultipartFile file, String schoolCid) {

		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		if (schoolCid == null)
			throw new ValidationException("School id cannot be null.");
		if (!schoolRepository.existsByCidAndActiveTrue(schoolCid))
			throw new ValidationException(String.format("School with id : (%s) not found.", schoolCid));

		List<String> errors = new ArrayList<String>();
		List<TeacherResponse> managementResponseList = new ArrayList<>();
		List<Map<String, Object>> managementRecords = new ArrayList<Map<String, Object>>();

		try {
			XSSFWorkbook managementSheet = new XSSFWorkbook(file.getInputStream());
			managementRecords = findSheetRowValues(managementSheet, "MANAGEMENT", errors);
//			errors = (List<String>) managementRecords.get(managementRecords.size() - 1).get("errors");
			for (int i = 0; i < managementRecords.size(); i++) {
				List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
				tempStudentsRecords.add(managementRecords.get(i));
				managementResponseList.add(save(validateTeacherRequest(tempStudentsRecords, errors, false, schoolCid)));
			}
		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("TeacherResponseList", managementResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);

	}

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
//					continue;
				if (i == 0)
					throw new ValidationException(String.format(
							"Some of the cells (Row number : %d) are missing or extras in %s sheet", i + 1, sheetName));
			}
			if (i == 0) {
				row.forEach(c -> {
					System.out.println(c.getStringCellValue());
//					 c.getStringCellValue().trim();
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
//						if(columnTypes.get(headers.get(j)).equals(cell.getCellType()))
						if (headers.get(j).equalsIgnoreCase("NAME") || headers.get(j).equalsIgnoreCase("EMAIL")
								|| headers.get(j).equalsIgnoreCase("MOBILE NUMBER")
								|| headers.get(j).equalsIgnoreCase("GRADE"))
							errors.add(String.format("Cell at row %d and column %d is blank for header %s.", i + 1,
									j + 1, headers.get(j)));
						columnValues.put(headers.get(j), null);
//						columnValues.put(headers.get(j), null);
					}
				}

			}
		}
		return rows;
	}

	private List<Map<String, Object>> findSheetRowValues(XSSFWorkbook workbook, String sheetName, List<String> errors) {
//		XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			errors.add(sheetName + " sheet not found");
			return null;
		}
//		if (sheet.getPhysicalNumberOfRows() > rowLimit) {
//			errors.add(String.format("Number of row can't be more than %d for %s sheet", rowLimit, sheetName));
//		}
		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(sheetName);
		return fetchRowValues(columnTypes, sheet, errors, sheetName);

	}

	private TeacherRequest validateTeacherRequest(List<Map<String, Object>> teacherDetails, List<String> errors,
			Boolean isCoach, String schoolCid) {
		if (teacherDetails == null || teacherDetails.isEmpty()) {
			errors.add("Teacher details not found");
		}
		TeacherRequest teacherRequest = new TeacherRequest();
		teacherRequest.setName((String) teacherDetails.get(0).get("NAME"));
		teacherRequest.setQualification((String) teacherDetails.get(0).get("QUALIFICATION"));
//		teacherRequest.setUsername((String) teacherDetails.get(0).get("USERNAME"));
//		teacherRequest.setDob(
//				DateUtil.convertStringToDate(DateUtil.formatDate((Date) teacherDetails.get(0).get("DOB"), null, null)));
		teacherRequest.setDob(DateUtil.formatDate((Date) teacherDetails.get(0).get("DOB"), null, null));
//		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);

		String designation = (String) teacherDetails.get(0).get("DESIGNATION");
		if (designation != null) {
			teacherRequest.setIsManagmentMember(true);
			teacherRequest.setDesignation(designation);
		} else {
			teacherRequest.setIsManagmentMember(false);
		}

//		if (school == null)
//			errors.add(String.format("School with id : %s not found ", schoolCid));
//		else {
		teacherRequest.setSchoolId(schoolCid);

		if (isCoach == true) {
			String activities = (String) teacherDetails.get(0).get("ACTIVITY");
			List<String> activityCIds = new ArrayList<String>();
			String[] activityNames = null;
			if (activities != null)
				activityNames = activities.split(",");

			if (activityNames != null && activityNames.length > 0) {
				for (String activity : activityNames) {
					String actCid = activityRepository.findCidByNameAndActiveTrue(activity);
					if (actCid != null)
						activityCIds.add(actCid);
					else
						errors.add(String.format("Activity with name : %s does not exist.", activity));
				}
				teacherRequest.setIsCoach(true);
				teacherRequest.setActivityIds(activityCIds);
			} else {
				if (!teacherRequest.getIsManagmentMember())
					errors.add(String.format("No activities provided for %s.", teacherRequest.getName()));
			}
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
			teacherRequest.setIsClassTeacher(true);
			teacherRequest.setGradeIds(gradeCIds);
		} else {
			if (!teacherRequest.getIsManagmentMember())
				errors.add(String.format("No grades provided for %s.", teacherRequest.getName()));
		}
//		}

		teacherRequest.setEmail((String) teacherDetails.get(0).get("EMAIL"));
		if (teacherDetails.get(0).get("ACTIVE") != null)
			teacherRequest.setActive(Boolean.valueOf((Boolean) teacherDetails.get(0).get("ACTIVE")));
		teacherRequest.setMobileNumber((String) teacherDetails.get(0).get("MOBILE NUMBER"));
		teacherRequest.setGender((String) teacherDetails.get(0).get("GENDER"));

		return teacherRequest;

	}
	
	@Override
	public TeacherResponse addOrRemoveActivitiesToTeachers(TeacherRequest request) {
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("No user logged in currently, kindly log in as School or School Management to assign activities to Teachers.");
		if(!schoolRepository.existsByUserIdAndActiveTrue(userId) && !teacherRepository.existsByUserIdAndIsManagmentMemberTrueAndActiveTrue(userId))
			throw new UnauthorizedUserException("Not Authorized to assign activities to Teachers pls login as School or Management Member.");
		if(request == null)
			throw new ValidationException("Request cannot be null.");
		if(request.getTeachers() == null || request.getTeachers().isEmpty())
			throw new ValidationException("teachers cannot be null or empty, its required to add activities to teachers.");
		List<Teacher> teachersToSave = new ArrayList<Teacher>();
		for(TeacherRequest teacherRequest : request.getTeachers()) {
			if(!teacherRepository.existsByCidAndActiveTrue(teacherRequest.getId()))
				throw new ValidationException(String.format("Teacher or Coach with id (%s) does not exist.",teacherRequest.getId()));
			Teacher teacher = teacherRepository.findByCidAndActiveTrue(teacherRequest.getId());
			
			if (teacherRequest.getActivityIds() != null && !teacherRequest.getActivityIds().isEmpty()) {
				List<String> requestActivityIds = teacherRequest.getActivityIds();
				List<Activity> previousActivities = teacher.getActivities();
				List<Activity> toBeDeletedActivities = new ArrayList<Activity>();

				for (int i = 0; i < previousActivities.size(); i++) {

					if (requestActivityIds.contains(previousActivities.get(i).getCid())) {
						requestActivityIds.remove(previousActivities.get(i).getCid());
					} else {
						if (activityPerformedRepository.existsByTeacherCidAndActivityCidAndActiveTrue(teacher.getCid(),previousActivities.get(i).getCid())) {
							throw new ValidationException(String.format(
									"You cannot delete the activty : %s as few student has already performed activity %s under you.",
									previousActivities.get(i).getName(), previousActivities.get(i).getName()));
						} else {
							List<Teacher> teachers = previousActivities.get(i).getTeachers();
							if (teachers != null && !teachers.isEmpty()) {
								teachers.remove(teacher);
								previousActivities.get(i).setTeachers(teachers);
								toBeDeletedActivities.add(previousActivities.get(i));
							}
							previousActivities.remove(i--);
						}
					}
				}

				if (requestActivityIds != null && !requestActivityIds.isEmpty()) {
					for (String actId : requestActivityIds) {
						if (!activityRepository.existsByCidAndActiveTrue(actId))
							throw new ValidationException(String.format("Activity with id (%s) not found .", actId));
						Activity activity = activityRepository.findByCidAndActiveTrue(actId);
						List<Teacher> teachers = new ArrayList<Teacher>();
						teachers = activity.getTeachers();
						teachers.add(teacher);
						activity.setTeachers(teachers);
						previousActivities.add(activity);
					}
				}
				if (previousActivities != null && !previousActivities.isEmpty())
					teacher.setIsCoach(true);
				teacher.setActivities(previousActivities);
				activityRepository.save(toBeDeletedActivities);
				teachersToSave.add(teacher);
//				teacher.setActivities(activityRepository.save(previousActivities));

			}
		}
		teachersToSave = teacherRepository.save(teachersToSave);
		if(teachersToSave.isEmpty())
			throw new RuntimeException("Something went wrong operation failed ,please try again.");
		
		TeacherResponse response =  new TeacherResponse();
		response.setTeachers(teachersToSave.stream().map(TeacherResponse :: new).collect(Collectors.toList()));
		return response;
	}
	

	@Override
	public List<ClubMembershipResponse> getMembershipDetails(){
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("Login as teacher/coach to view pending requests.");
		Long teacherId = teacherRepository.getIdByUserIdAndActiveTrue(userId);
		if(teacherId == null)
			throw new ValidationException("Login as teacher/coach to view pending requests.");
		List<StudentClub> membership = studentClubRepository.findAllByTeacherIdAndActiveTrue(teacherId);
		
		if(membership == null || membership.isEmpty())
			throw new ValidationException("No membership application found for you.");
		
		return membership.stream().map(ClubMembershipResponse :: new).distinct().collect(Collectors.toList());
	}
	
	@Override
	public ClubMembershipResponse updateStatus(String studentId,String activityId , Boolean isVerified) {
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("Login as teacher/coach to verify/reject pending requests.");
		Long teacherId = teacherRepository.getIdByUserIdAndActiveTrue(userId);
		if(teacherId == null)
			throw new ValidationException("Login as teacher/coach to verify/reject pending requests.");
		StudentClub studentClub = studentClubRepository.findOne(new StudentActivityId(studentRepository.findIdByCidAndActiveTrue(studentId), activityRepository.findIdByCidAndActiveTrue(activityId), teacherId));
		if (studentClub == null) {
			throw new ValidationException(String.format("This membership request not found in records."));
		}
		if (studentClub.getMembershipStatus().equals(ApprovalStatus.PENDING)) {
			studentClub.setMembershipStatus(isVerified ? ApprovalStatus.VERIFIED : ApprovalStatus.REJECTED);
				
			studentClub.setConsideredOn(new Date());
		} else {
			throw new ValidationException("This membership request already rejected or verified");
		}
		
		studentClub = studentClubRepository.save(studentClub);
		return new ClubMembershipResponse(studentClub);
	}
}
