package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.Certificate;
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.AwardCriterion;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.AwardActivityPerformedRepository;
import com.nxtlife.mgs.jpa.CertificateRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.GuardianRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.SequenceGeneratorRepo;
import com.nxtlife.mgs.jpa.StudentClubRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.StudentSchoolGradeRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.service.SequenceGeneratorService;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.store.FileStore;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.GroupResponseByActivityName;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.CertificateRequest;
import com.nxtlife.mgs.view.CertificateResponse;
import com.nxtlife.mgs.view.ClubMembershipResponse;
import com.nxtlife.mgs.view.GuardianRequest;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@Service
public class StudentServiceImpl extends BaseService implements StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	GuardianRepository guardianRepository;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	StudentSchoolGradeRepository studentSchoolGradeRepository;

	@Autowired
	UserService userService;

	@Autowired
	SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	SequenceGeneratorRepo sequenceGeneratorRepo;

	@Autowired
	Utils utils;

	@Autowired
	ActivityPerformedService activityPerformedService;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TeacherRepository teacherRepository;

	@Autowired
	AwardActivityPerformedRepository awardActivityPerformedRepository;

	@Autowired
	ActivityPerformedRepository activityPerformedRepository;

	@Autowired
	FileStore filestore;
	
	@Autowired
	CertificateRepository certificateRepository;
	
	@Autowired
	FileService fileService; 
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FocusAreaRepository focusAreaRepository;
	
	@Autowired
	StudentClubRepository studentClubRepository;

	@Value("${spring.mail.username}")
	private String emailUsername;

	@Override
	public StudentResponse save(StudentRequest request) {

		if (request.getEmail() == null)
			throw new ValidationException("Email cannot be null.");

		if (studentRepository.countByEmail(request.getEmail()) > 0) {
			throw new ValidationException(String.format("Email [%s] already exist", request.getEmail()));
		}

		if (request.getGuardians() != null) {
			for (GuardianRequest guardian : request.getGuardians()) {
				if (guardian.getName() == null) {
					throw new ValidationException("One of the guardian didn't have name");
				} else if (guardian.getEmail() == null && guardian.getMobileNumber() == null) {
					throw new ValidationException(String
							.format("Guardian (%s) should have atleast email and mobile number", guardian.getName()));
				}
			}
		}

		School school = null;
		Grade grade = null;
		if (request.getSchoolId() != null) {
			school = schoolRepository.getOneByCidAndActiveTrue(request.getSchoolId());
			if (school == null)
				throw new ValidationException(String.format("School with id : %s not found.", request.getSchoolId()));
		}
		if (request.getGradeId() != null) {
			grade = gradeRepository.getOneByCid(request.getGradeId());
			if (grade == null) {
				throw new ValidationException(String.format("Grade (%s) not found", request.getGradeId()));
			}

		}

		Long studsequence = sequenceGeneratorService.findSequenceByUserType(UserType.Student);

		List<Guardian> guardians = new ArrayList<>();
		Guardian guardian;
		Student student = request.toEntity();
		student.setCid(utils.generateRandomAlphaNumString(8));
		student.setUsername(String.format("STU%08d", studsequence));
		student.setGrade(grade);
		student.setSchool(school);

//		User user = userService.createStudentUser(student);
		User user = userService.createUserForEntity(student);
		
		if (StringUtils.isEmpty(user)) {
			throw new ValidationException("User not created successfully");
		}
		student.setUser(user);
		student = studentRepository.save(student);

		if (request.getGuardians() != null) {
			for (GuardianRequest guardianRequest : request.getGuardians()) {
				if ((guardianRequest.getMobileNumber() != null && guardianRequest.getEmail() != null)) {
					guardian = guardianRepository.findByEmailOrMobileNumber(guardianRequest.getEmail(),
							guardianRequest.getMobileNumber());
					
					createOrSetGuardianUtility(student, guardian, guardians,  guardianRequest);
					
				} else if (guardianRequest.getMobileNumber() != null) {
					
					guardian = guardianRepository.getOneByMobileNumber(guardianRequest.getMobileNumber());
					
					createOrSetGuardianUtility(student, guardian, guardians,  guardianRequest);

				} else if (guardianRequest.getEmail() != null) {
					
					guardian = guardianRepository.getOneByEmail(guardianRequest.getEmail());

					createOrSetGuardianUtility(student, guardian, guardians,  guardianRequest);
				}
			}
			student.setGuardians(guardians);
		}

		student = studentRepository.save(student);
		if (student == null) {
			throw new RuntimeException("Something went wrong student not saved.");
		}
		
		Boolean emailFlag = false;

		if (user.getEmail() != null)
			emailFlag =userService.sendLoginCredentialsBySMTP(userService.usernamePasswordSendContentBuilder(user.getUsername(),
					user.getRawPassword(), emailUsername, user.getEmail()));
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("Student", new StudentResponse(student));
		String emailMessage = emailFlag?String.format("Email sent successfully to (%s)", user.getEmail()):String.format("Email not sent successfully to (%s) , email address might be wrong.", user.getEmail());
		int emailStatusCode = emailFlag?200:400;
		response.put("MailResponse", new SuccessResponse(emailStatusCode,emailMessage));
//		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		return new StudentResponse(student);
	}
	
	private void createOrSetGuardianUtility(Student student , Guardian guardian ,  List<Guardian> guardians ,GuardianRequest guardianRequest) {
		 List<Student> studList;
		 Long sequence;
		 
		if (guardian == null) {
			guardian = guardianRequest.toEntity();
			sequence = sequenceGeneratorService.findSequenceByUserType(UserType.Parent);
			guardian.setUsername(String.format("GRD%08d", sequence));
			guardian.setCid(utils.generateRandomAlphaNumString(8));
//			guardian.setUser(userService.createParentUser(guardian));
			guardian.setUser(userService.createUserForEntity(guardian));
			guardian = guardianRepository.save(guardian);
			studList = new ArrayList<Student>();
			studList.add(student);
			guardian.setStudents(studList);
			guardians.add(guardian);
		} else {
			studList = guardian.getStudents();
			studList.add(student);
			guardian.setStudents(studList);
			guardians.add(guardian);
		}
	}

	@Override
	public StudentResponse update(StudentRequest request, String cid) {

		if (cid == null) {
			throw new ValidationException("id can't be null");
		}

		Student student = studentRepository.findByCid(cid);

		if (student == null) {
			throw new NotFoundException(String.format("student having id [%s] didn't exist", cid));
		}
		
		School school = null;
		Grade grade = null;
		if (request.getSchoolId() != null) {
			school = schoolRepository.getOneByCidAndActiveTrue(request.getSchoolId());
			if (school == null)
				throw new ValidationException(String.format("School with id : %s not found.", request.getSchoolId()));
		}
		if (request.getGradeId() != null) {
			grade = gradeRepository.getOneByCid(request.getGradeId());
			if (grade == null) {
				throw new ValidationException(String.format("Grade (%s) not found", request.getGradeId()));
			}

		}

		student = request.toEntity(student);
		
		if (request.getGuardians() != null && !request.getGuardians().isEmpty()) {
			List<GuardianRequest> guardianRequests = request.getGuardians();
			List<Guardian> previousGuardians = new ArrayList<Guardian>();
			List<Guardian> guardiansToDelete = new ArrayList<Guardian>();
			previousGuardians = student.getGuardians();
			if (previousGuardians.isEmpty()) {
				updateGuardiansOfStudent(guardianRequests, previousGuardians, student);
			} else {
				for (int i = 0; i < previousGuardians.size(); i++) {
					String grdCid = previousGuardians.get(i).getCid();
					GuardianRequest guardianRequest = guardianRequests.stream()
							.filter(grd -> grd.getId() != null && grd.getId().equals(grdCid)).findAny().orElse(null);
					if (guardianRequest != null) {
						guardianRequests.remove(guardianRequest);
					} else {
						List<Student> students = previousGuardians.get(i).getStudents();
						if (!students.isEmpty() && !students.isEmpty()) {
							students.remove(student);
							previousGuardians.get(i).setStudents(students);
							guardiansToDelete.add(previousGuardians.get(i));
						}
						previousGuardians.remove(i--);
					}
				}

				if (guardianRequests != null && !guardianRequests.isEmpty())
					updateGuardiansOfStudent(guardianRequests, previousGuardians, student);

			}

			student.setGuardians(previousGuardians);
			guardianRepository.save(guardiansToDelete);
		}
		
		student.setSchool(school);
		student.setGrade(grade);
		if(request.getMobileNumber() != null) {
			if(userRepository.existsByContactNumber(request.getMobileNumber()))
					throw new ValidationException(String.format("Mobile Number (%s) already belongs to some other user.",request.getMobileNumber()));
			student.setMobileNumber(request.getMobileNumber());
			student.getUser().setContactNumber(request.getMobileNumber());
		}
		
		if(request.getEmail() != null) {
			if(userRepository.existsByEmail(request.getEmail()))
					throw new ValidationException(String.format("Email (%s) already belongs to some other user.",request.getEmail()));
			student.setEmail(request.getEmail());
			student.getUser().setEmail(request.getEmail());
		}
		
		student = studentRepository.save(student);

		return new StudentResponse(student);
	}

	private void updateGuardiansOfStudent(List<GuardianRequest> guardianRequests, List<Guardian> previousGuardians,
			Student student) {
		for (GuardianRequest guardReq : guardianRequests) {
			if (guardReq.getId() != null) {
				if (!guardianRepository.existsByCidAndActiveTrue(guardReq.getId()))
					throw new ValidationException(
							String.format("Guardian with id (%s) does not exist.", guardReq.getId()));
				Guardian guardian = guardianRepository.findByCidAndActiveTrue(guardReq.getId());
				List<Student> students = guardian.getStudents();
				students.add(student);
				guardian.setStudents(students);
				previousGuardians.add(guardian);

			} else {
				Guardian guardian = guardReq.toEntity();
				Long sequence = sequenceGeneratorService.findSequenceByUserType(UserType.Parent);
				guardian.setUsername(String.format("GRD%08d", sequence));
				guardian.setCid(utils.generateRandomAlphaNumString(8));
//				guardian.setUser(userService.createParentUser(guardian));
				guardian.setUser(userService.createUserForEntity(guardian));
				List<Student> students = new ArrayList<Student>();
				students.add(student);
				guardian.setStudents(students);
				guardian.setActive(true);
				guardian = guardianRepository.save(guardian);
				previousGuardians.add(guardian);
			}
		}
		
	}
	
	@Override
	public List<StudentResponse> findByName(String name) {

		if (name == null)
			throw new ValidationException("name can't be null");

		List<Student> studentList = studentRepository.findByNameAndActiveTrue(name);
		
		if (studentList.isEmpty())
			throw new NotFoundException(String.format("students having name [%s] didn't exist", name));

		return studentList.stream().map(StudentResponse::new).collect(Collectors.toList());
	}

	@Override
	public StudentResponse findByid(Long id) {

		if (id == null)
			throw new ValidationException("id can't be null");

		Student student = studentRepository.findByIdAndActiveTrue(id);

		if (student == null)
			throw new NotFoundException(String.format("Student having id [%d] didn't exist", id));

		return new StudentResponse(student);
	}

	public StudentResponse findByCId(String cId) {

		if (cId == null)
			throw new ValidationException("Id can't be null");

		Student student = studentRepository.findByCidAndActiveTrue(cId);

		if (student == null)
			throw new NotFoundException(String.format("Student having cId [%s] didn't exist", cId));

		return new StudentResponse(student);
	}

	@Override
	public StudentResponse findByMobileNumber(String mobileNumber) {

		if (mobileNumber.isEmpty())
			throw new ValidationException("mobile number can't be null");

		Student student = studentRepository.findByMobileNumberAndActiveTrue(mobileNumber);

		if (student == null)
			throw new ValidationException(
					String.format("Student having mobile number [%s] didn't exist", mobileNumber));

		return new StudentResponse(student);
	}

	@Override
	public StudentResponse findByUsername(String username) {

		if (username.isEmpty())
			throw new ValidationException("username can't be null");

		Student student = studentRepository.findByUsernameAndActiveTrue(username);

		if (student == null)
			throw new NotFoundException(String.format("student having username [%s] didn't exist", username));

		return new StudentResponse(student);
	}

	@Override
	public List<StudentResponse> getAll() {

		List<Student> studentList = studentRepository.findAllByActiveTrue();

		if (studentList.isEmpty())
			throw new NotFoundException("No student found");

		return studentList.stream().map(StudentResponse :: new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StudentResponse> getAllBySchoolCid(String schoolCid) {
		if (schoolCid == null) {
			throw new NotFoundException("school id can't be null");
		}
		List<Student> studentsList = studentRepository.findAllBySchoolCidAndActiveTrue(schoolCid);
		if (studentsList.isEmpty())
			throw new NotFoundException(String.format("No student found for school having id [%s]", schoolCid));
		return studentsList.stream().map(StudentResponse :: new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StudentResponse> getAllByGradeId(String gradeId) {
		if (gradeId == null) {
			throw new NotFoundException("Grade id can't be null");
		}
		List<Student> studentsList = studentRepository.findAllByGradeCidAndActiveTrue(gradeId);
		if (studentsList.isEmpty())
			throw new NotFoundException(String.format("No student found for grade having id [%s]", gradeId));
		return studentsList.stream().map(StudentResponse :: new)
				.collect(Collectors.toList());
	}

	@Override
	public List<StudentResponse> getAllStudentsBySchoolAndActivityAndCoachAndStatusReviewed(String schoolCid,
			String gradeCid, String activityCid, String activityStatus, String teacherCid) {
		if (schoolCid == null)
			throw new ValidationException("School id cannot be null.");
		if (activityCid == null)
			throw new ValidationException("Activity id cannot be null.");
		if (teacherCid == null)
			throw new ValidationException("Teacher id cannot be null.");
		if (gradeCid == null)
			throw new ValidationException("Grade id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);
		if (school == null)
			throw new ValidationException(String.format("School with id : %s not found.", schoolCid));
		Grade grade = gradeRepository.findByCidAndActiveTrue(gradeCid);
		if (grade == null)
			throw new ValidationException(
					String.format("Grade with id : %s not found in school : %s", gradeCid, school.getName()));
		Activity activity = activityRepository.findByCidAndActiveTrue(activityCid);
		if (activity == null)
			throw new ValidationException(String.format("Activity with id : %s not found.", activityCid));
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(teacherCid);
		if (teacher == null)
			throw new ValidationException(String.format("Teacher with id : %s not found.", teacherCid));
		if (activityStatus == null)
			activityStatus = ActivityStatus.Reviewed.toString();
		List<Student> students = studentRepository
				.findAllBySchoolCidAndGradeCidAndActivitiesActivityCidAndActivitiesActivityStatusAndSchoolActiveTrueAndGradeActiveTrueAndActivitiesActivityActiveTrueAndActiveTrue(
						schoolCid, gradeCid, activityCid, ActivityStatus.valueOf(activityStatus));
		if (students == null)
			throw new ValidationException(String.format(
					"No student found in the school : %s under teacher : %s having performed activity : %s and status is %s .",
					school.getName(), teacher.getName(), activity.getName(), activityStatus));
		return students.stream().distinct().map(StudentResponse::new).collect(Collectors.toList());

	}

	@Override
	public StudentResponse setProfilePic(MultipartFile file, String studentCid) {
		if (file == null || file.getSize() == 0)
			throw new ValidationException("profilePic cannot be null or empty.");
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (!studentRepository.existsByCidAndActiveTrue(studentCid))
			throw new ValidationException(String.format("Student with id (%s) does not exist.", studentCid));

		Student student = studentRepository.findByCidAndActiveTrue(studentCid);

		String filename = UUID.randomUUID().toString() + "." + fileService.getFileExtension(file.getOriginalFilename());
		try {
			String imageUrl = filestore.store("profilePic", filename, file.getBytes());
			student.setImageUrl(imageUrl);
			if(student.getUser() != null)
				student.getUser().setImagePath(imageUrl);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ValidationException("Something went wromg image not saved/uploaded.");
		}

		return new StudentResponse(studentRepository.save(student));
	}
	
	@Override
	public CertificateResponse uploadCertificate(CertificateRequest request) {
		Long userId = getUserId();
		Student student = studentRepository.getByUserIdAndActiveTrue(userId);
		if (student == null) {
			throw new ValidationException("User not login as a student.");
		}
		
		if(request == null)
			throw new ValidationException("Request cannot be null.");
		if(request.getImage() == null || request.getImage().isEmpty())
			throw new ValidationException("Image file cannot be null or empty.");
		final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
		if(!contentTypes.contains(request.getImage().getContentType()))
			throw new ValidationException("Please select image file only to upload not of any other type.");
		
		if(certificateRepository.existsByStudentCidAndTitleAndDescriptionAndActiveTrue(student.getCid(), request.getTitle(), request.getDescription()))
			throw new ValidationException(String.format("A certificate for you under title : (%s) and description (%s) already exist.", request.getTitle(), request.getDescription()));
		
		Certificate certificate = request.toEntity();

		String filename = UUID.randomUUID().toString() + "." + fileService.getFileExtension(request.getImage().getOriginalFilename());
		try {
			String imageUrl = filestore.store("certificate", filename, request.getImage().getBytes());
			certificate.setImageUrl(imageUrl);
			certificate.setStudent(student);
//			List<Certificate> certificates = new ArrayList<Certificate>();
//			certificates = student.getCertificates();
//			certificates.add(certificate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		certificate.setCid(utils.generateRandomAlphaNumString(8));
		certificate = certificateRepository.save(certificate);
		if(certificate == null)
			throw new RuntimeException("Something went wrong certificate not uploaded.");
		return new CertificateResponse(certificate);
	}
	
	@Override
	public List<CertificateResponse> getAllCertificatesOfStudent(){
		Long userId = getUserId();
		Student student = studentRepository.getByUserIdAndActiveTrue(userId);
		if (student == null) {
			throw new ValidationException("User not login as a student.");
		}
		List<CertificateResponse> responseList = certificateRepository.findAllByStudentCidAndActiveTrue(student.getCid());
		if(responseList == null || responseList.isEmpty())
			throw new ValidationException(String.format("No certificates found for student (%s)", student.getName()));
		
		return responseList;	
	}
	
	@Override
	public SuccessResponse delete(String cid) {
		if (cid == null) {
			throw new ValidationException("student id can't be null");
		}
		Student student = studentRepository.findByCidAndActiveTrue(cid);
		if (student == null) {
			throw new NotFoundException(String.format("student having id [%s] can't exist", cid));
		}
		student.setActive(false);
		studentRepository.save(student);
		return new SuccessResponse(org.springframework.http.HttpStatus.OK.value(), "student deleted successfully");
	}
	
	
	@Override
	public ResponseEntity<?> uploadStudentsFromExcel(MultipartFile file, String schoolCid) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		if (schoolCid == null)
			throw new ValidationException("School id cannot be null.");
		if (!schoolRepository.existsByCidAndActiveTrue(schoolCid))
			throw new ValidationException(String.format("School with id : (%s) not found.", schoolCid));

		List<String> errors = new ArrayList<String>();
		List<StudentResponse> studentResponseList = new ArrayList<>();
		List<Map<String, Object>> studentsRecords = new ArrayList<Map<String, Object>>();

		try {
			XSSFWorkbook studentsSheet = new XSSFWorkbook(file.getInputStream());
			studentsRecords = findSheetRowValues(studentsSheet, "STUDENT", errors);
//			errors = (List<String>) studentsRecords.get(studentsRecords.size() - 1).get("errors");
			for (int i = 0; i < studentsRecords.size() - 1; i++) {
				List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
				tempStudentsRecords.add(studentsRecords.get(i));
				studentResponseList.add(save(validateStudentRequest(tempStudentsRecords, errors, schoolCid)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("StudentResponseList", studentResponseList);
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
								if (headers.get(j).contains("DATE") || headers.get(j).contains("DOB")
										|| headers.get(j).contains("SESSION START DATE"))
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
								|| headers.get(j).equalsIgnoreCase("FATHERS NAME")
								|| headers.get(j).equalsIgnoreCase("FATHERS EMAIL")
								|| headers.get(j).equalsIgnoreCase("FATHERS MOBILE NUMBER")
								|| headers.get(j).equalsIgnoreCase("MOTHERS NAME")
								|| headers.get(j).equalsIgnoreCase("MOTHERS EMAIL")
								|| headers.get(j).equalsIgnoreCase("MOTHERS MOBILE NUMBER"))
							errors.add(String.format("Cell at row %d and column %d is blank for header %s.", i + 1,
									j + 1, headers.get(j)));
						columnValues.put(headers.get(j), null);
					}
				}

			}
		}
		Map<String, Object> err = new HashMap<String, Object>();
		err.put("errors", errors);
		rows.add(err);
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

	

	private StudentRequest validateStudentRequest(List<Map<String, Object>> studentDetails, List<String> errors,
			String schoolCid) {
		if (studentDetails == null || studentDetails.isEmpty()) {
			errors.add("Student details not found");
		}
		StudentRequest studentRequest = new StudentRequest();
		studentRequest.setName((String) studentDetails.get(0).get("NAME"));

		if ((Date) studentDetails.get(0).get("DOB") != null)
			studentRequest.setDob(DateUtil.formatDate((Date) studentDetails.get(0).get("DOB"), null, null));

		studentRequest.setSchoolId(schoolCid);
		String standard = (String) studentDetails.get(0).get("GRADE");
		String section = (String) studentDetails.get(0).get("SECTION");
		Grade grade = null;
		if (standard == null && section == null) {
			errors.add("GRADE and SECTION are empty.");
		} else if (standard != null && section == null) {
			errors.add("SECTION is empty");
			grade = gradeRepository.findByNameAndSchoolsCid(standard, schoolCid);
		} else if (section != null && standard == null) {
			errors.add("GRADE is empty");
		} else {
			grade = gradeRepository.findByNameAndSchoolsCidAndSection(standard, schoolCid, section);
		}
		if (grade == null)
			errors.add(String.format("Grade  %s not found ", (String) studentDetails.get(0).get("GRADE")));
		else
			studentRequest.setGradeId(grade.getCid());

		if ((Date) studentDetails.get(0).get("SESSION START DATE") != null)
			studentRequest.setSessionStartDate(DateUtil.convertStringToDate(
					DateUtil.formatDate((Date) studentDetails.get(0).get("SESSION START DATE"), null, null)));
		studentRequest.setEmail((String) studentDetails.get(0).get("EMAIL"));
		if (studentDetails.get(0).get("ACTIVE") != null)
			// studentRequest.setActive(Boolean.valueOf((Boolean)
			// studentDetails.get(0).get("ACTIVE")));
			studentRequest.setMobileNumber((String) studentDetails.get(0).get("MOBILE NUMBER"));
		studentRequest.setGender((String) studentDetails.get(0).get("GENDER"));
		if ((Date) studentDetails.get(0).get("SUBSCRIPTION END DATE") != null)
			studentRequest.setSubscriptionEndDate(DateUtil.convertStringToDate(
					DateUtil.formatDate((Date) studentDetails.get(0).get("SUBSCRIPTION END DATE"), null, null)));
		List<GuardianRequest> guardians = new ArrayList<GuardianRequest>();
		guardians.add(new GuardianRequest((String) studentDetails.get(0).get("FATHERS NAME"),
				(String) studentDetails.get(0).get("FATHERS EMAIL"), "male",
				(String) studentDetails.get(0).get("FATHERS MOBILE NUMBER"), "Father"));
		guardians.add(new GuardianRequest((String) studentDetails.get(0).get("MOTHERS NAME"),
				(String) studentDetails.get(0).get("MOTHERS EMAIL"), "male",
				(String) studentDetails.get(0).get("MOTHERS MOBILE NUMBER"), "Mother"));
		studentRequest.setGuardians(guardians);
		return studentRequest;
	}

	@Override
	public Set<StudentResponse> getAllStudentsAndItsActivitiesByAwardCriterion(String awardCriterion , String criterionValue,String gradeCid ,String initial, String last){
		
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("No user logged in currently.");
		if(awardCriterion == null)
			throw new ValidationException("Award Criterion cannot be null.");
		if(criterionValue == null)
			throw new ValidationException("Criterion Value cannot be null.");
		if(!AwardCriterion.matches(awardCriterion))
			throw new ValidationException(String.format("Invalid value (%s) for awardCriterion it should be of form : [PSD Area ,Focus Area , 4S ,Activity Type]", awardCriterion));
		
		Teacher teacher = teacherRepository.getByUserId(userId);
		if(teacher == null)
			throw new UnauthorizedUserException("User not logged in as Faculty of school i.e(Teacher ,Coach , Management)");
		LocalDateTime currentDateTime = LocalDateTime.now();
		if((initial !=null && DateUtil.convertStringToDate(initial).after(currentDateTime.toDate())) ||
				last !=null && DateUtil.convertStringToDate(last).after(currentDateTime.toDate()))
			throw new ValidationException("StartDate or endDate cannot be a future date.");
		Date startDate,endDate; 
		if(initial == null && last == null) {
			endDate = currentDateTime.toDate();
			startDate = currentDateTime.minusMonths(4).toDate();
		}else if(initial == null && last!=null) {
			endDate = DateUtil.convertStringToDate(last);
			startDate = LocalDateTime.fromDateFields(endDate).minusMonths(4).toDate();
		}else if(initial != null && last == null) {
			 startDate = DateUtil.convertStringToDate(initial);
			 endDate = currentDateTime.toDate();
		}else {
			startDate = DateUtil.convertStringToDate(initial);
            endDate = DateUtil.convertStringToDate(last);
            if(startDate.after(endDate))
            	throw new ValidationException("startDate shall fall before end date.");
		}
//		Date startDate = DateUtil.convertStringToDate(initial);
//		Date endDate = DateUtil.convertStringToDate(last);
		
		AwardCriterion criterion = AwardCriterion.fromString(awardCriterion);
		
		if(criterion.equals(AwardCriterion.PSDArea)) {
			return getAllStudentsAndItsActivitiesByPsdArea(criterionValue, gradeCid , teacher.getSchool().getCid(),startDate,endDate);
		}else if(criterion.equals(AwardCriterion.FocusArea)){
			return getAllStudentsAndItsActivitiesByFocusArea(criterionValue, gradeCid, teacher.getSchool().getCid(),startDate,endDate);
		}else if(criterion.equals(AwardCriterion.FourS)){
			return getAllStudentsAndItsActivitiesByFourS(criterionValue, gradeCid, teacher.getSchool().getCid(),startDate,endDate);
		}else if(criterion.equals(AwardCriterion.ActivityType)){
			return getAllStudentsAndItsActivitiesByActivity(criterionValue, gradeCid, teacher.getSchool().getCid(),startDate,endDate);
		}else {
			throw new ValidationException(String.format("Invalid value (%s) for awardCriterion it should be of form : [PSD Area ,Focus Area , 4S ,Activity Type]", awardCriterion));
		}
	}
	
	private Set<StudentResponse> getAllStudentsAndItsActivitiesByActivity(String activityName ,String gradeCid , String schoolCid,Date startDate, Date endDate){
		if(activityName == null)
			throw new ValidationException("activityName cannot be null.");
		if(!activityRepository.existsByNameAndActiveTrue(activityName))
			throw new ValidationException(String.format("Activity with name  (%s) does not exist.", activityName));
		
		Set<ActivityPerformed> activitiesPerformed = null ;
		Set<StudentResponse> students = null;
		Set<String> focusAreas,psdAreas,fourS;
		if(gradeCid== null) {
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid, activityName, ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
				  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				GroupResponseByActivityName<ActivityPerformedResponse> performedActivities = new GroupResponseByActivityName<ActivityPerformedResponse>();
				performedActivities.setActivityName(activityName);
				performedActivities.setResponses(activitiesToReturn);
				Long count = performedActivities.getResponses().stream().count();
				if(count!= 0) {
					performedActivities.setCount(count);
					double avgStars = 0d;
					for(ActivityPerformedResponse act : performedActivities.getResponses()) {
						avgStars+= act.getStar();
					}
					performedActivities.setAverageStars(avgStars/count);
				}
				finalActivitiesList.add(performedActivities);
				studResp.setPerformedActivities(finalActivitiesList);
				
				Double score = 0d;
				Double starSum = 0d;
				focusAreas = new HashSet<String>();
				psdAreas = new HashSet<String>();
				fourS = new HashSet<String>();
				
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					focusAreas.addAll(act.getFocusAreas());
					psdAreas.addAll(act.getPsdAreas());
					fourS.add(act.getFourS());
				}
				score =  (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setFocusAreas(focusAreas);
				studResp.setPsdAreas(psdAreas);
				studResp.setFourS(fourS);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();

		}else {
			if(!gradeRepository.existsBySchoolsCidAndCidAndActiveTrue(schoolCid,gradeCid))
				throw new ValidationException(String.format("Grade with id (%s) does not exist in school having id (%s).",gradeCid,schoolCid));
			
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndStudentGradeCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid ,gradeCid ,activityName ,ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
					  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
		
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				GroupResponseByActivityName<ActivityPerformedResponse> performedActivities = new GroupResponseByActivityName();
				performedActivities.setActivityName(activityName);
				performedActivities.setResponses(activitiesToReturn);
				Long count = performedActivities.getResponses().stream().count();
				if(count!= 0) {
					performedActivities.setCount(count);
					double avgStars = 0d;
					for(ActivityPerformedResponse act : performedActivities.getResponses()) {
						avgStars+= act.getStar();
					}
					performedActivities.setAverageStars(avgStars/count);
				}
				finalActivitiesList.add(performedActivities);
				studResp.setPerformedActivities(finalActivitiesList);
				
				Double score = 0d;
				Double starSum = 0d;
				
				focusAreas = new HashSet<String>();
				psdAreas = new HashSet<String>();
				fourS = new HashSet<String>();
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					focusAreas.addAll(act.getFocusAreas());
					psdAreas.addAll(act.getPsdAreas());
					fourS.add(act.getFourS());
				}
				score =  (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setFocusAreas(focusAreas);
				studResp.setPsdAreas(psdAreas);
				studResp.setFourS(fourS);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();
		}
		if(students == null || students.isEmpty())
			throw new ValidationException("No Student found for Activity having name : "+activityName);
		return students;
	}
	
	private Set<StudentResponse> getAllStudentsAndItsActivitiesByFocusArea(String focusArea ,String gradeCid , String schoolCid,Date startDate, Date endDate){
		if(focusArea == null)
			throw new ValidationException("focusArea cannot be null.");
		if(!focusAreaRepository.existsByNameAndActiveTrue(focusArea))
			throw new ValidationException(String.format("Focus Area  (%s) does not exist.", focusArea));
		
		Set<ActivityPerformed> activitiesPerformed = null ;
		Set<StudentResponse> students = null;
		Set<String> activityTypes ,psdAreas,fourS;
		if(gradeCid== null) {
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid, focusArea, ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
					  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
				
				Double score = 0d;
				Double starSum = 0d;
				activityTypes = new HashSet<String>();
				psdAreas = new HashSet<String>();
				fourS = new HashSet<String>();
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					activityTypes.add(act.getActivityName());
//					focusAreas.addAll(act.getFocusAreas());
					psdAreas.addAll(act.getPsdAreas());
					fourS.add(act.getFourS());
				}
//				score = focusAreas.size() * (starSum / activitiesToReturn.size());
				score =  (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setActivityTypes(activityTypes);
				studResp.setPsdAreas(psdAreas);
				studResp.setFourS(fourS);
				
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				for(String type : activityTypes) {
					
					GroupResponseByActivityName<ActivityPerformedResponse> activitiesPerformedResponse = new GroupResponseByActivityName();
					activitiesPerformedResponse.setActivityName(type);
					activitiesPerformedResponse.setResponses(activitiesToReturn.stream().filter(act-> act.getActivityName().equalsIgnoreCase(type)).collect(Collectors.toList()));
					Long count = activitiesPerformedResponse.getResponses().stream().count();
					if(count!= 0) {
						activitiesPerformedResponse.setCount(count);
						double avgStars = 0d;
						for(ActivityPerformedResponse act : activitiesPerformedResponse.getResponses()) {
							avgStars+= act.getStar();
						}
						activitiesPerformedResponse.setAverageStars(avgStars/count);
					}
					finalActivitiesList.add(activitiesPerformedResponse);
				}
				studResp.setPerformedActivities(finalActivitiesList);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();

		}else {
			if(!gradeRepository.existsBySchoolsCidAndCidAndActiveTrue(schoolCid,gradeCid))
				throw new ValidationException(String.format("Grade with id (%s) does not exist in school having id (%s).",gradeCid,schoolCid));
			
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid ,gradeCid ,focusArea,ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
					  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
				
				Double score = 0d;
				Double starSum = 0d;
				activityTypes = new HashSet<String>();
				psdAreas = new HashSet<String>();
				fourS = new HashSet<String>();
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					activityTypes.add(act.getActivityName());
					psdAreas.addAll(act.getPsdAreas());
					fourS.add(act.getFourS());
				}
				score =  (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setActivityTypes(activityTypes);
				studResp.setPsdAreas(psdAreas);
				studResp.setFourS(fourS);
				
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				for(String type : activityTypes) {
					
					GroupResponseByActivityName<ActivityPerformedResponse> activitiesPerformedResponse = new GroupResponseByActivityName();
					activitiesPerformedResponse.setActivityName(type);
					activitiesPerformedResponse.setResponses(activitiesToReturn.stream().filter(act-> act.getActivityName().equalsIgnoreCase(type)).collect(Collectors.toList()));
					Long count = activitiesPerformedResponse.getResponses().stream().count();
					if(count!= 0) {
						activitiesPerformedResponse.setCount(count);
						double avgStars = 0d;
						for(ActivityPerformedResponse act : activitiesPerformedResponse.getResponses()) {
							avgStars+= act.getStar();
						}
						activitiesPerformedResponse.setAverageStars(avgStars/count);
					}
					finalActivitiesList.add(activitiesPerformedResponse);
				}
				studResp.setPerformedActivities(finalActivitiesList);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();
		}
		if(students == null || students.isEmpty())
			throw new ValidationException("No Student found for Focus Area : "+focusArea);
		return students;
	}
	
	private Set<StudentResponse> getAllStudentsAndItsActivitiesByFourS(String fourS ,String gradeCid , String schoolCid,Date startDate, Date endDate){
		if(fourS == null)
			throw new ValidationException("fourS cannot be null.");
		if(!FourS.matches(fourS))
			throw new ValidationException(String.format("FourS  (%s) does not matches with available values for fourS i.e [Skill , Sport ,Study ,Service]", fourS));
		
		Set<ActivityPerformed> activitiesPerformed = null ;
		Set<StudentResponse> students = null;
		Set<String> focusAreas ,activityTypes , psdAreas;
		if(gradeCid== null) {
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid, FourS.valueOf(fourS), ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
					  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
				
				Double score = 0d;
				Double starSum = 0d;
				focusAreas = new HashSet<String>();
				activityTypes = new HashSet<String>();
				psdAreas = new HashSet<String>();
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					focusAreas.addAll(act.getFocusAreas());
					activityTypes.add(act.getActivityName());
					psdAreas.addAll(act.getPsdAreas());
				}
				score = focusAreas.size() * (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setActivityTypes(activityTypes);
				studResp.setFocusAreas(focusAreas);
				studResp.setPsdAreas(psdAreas);
				
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				for(String type : activityTypes) {
					
					GroupResponseByActivityName<ActivityPerformedResponse> activitiesPerformedResponse = new GroupResponseByActivityName();
					activitiesPerformedResponse.setActivityName(type);
					activitiesPerformedResponse.setResponses(activitiesToReturn.stream().filter(act-> act.getActivityName().equalsIgnoreCase(type)).collect(Collectors.toList()));
					Long count = activitiesPerformedResponse.getResponses().stream().count();
					if(count!= 0) {
						activitiesPerformedResponse.setCount(count);
						double avgStars = 0d;
						for(ActivityPerformedResponse act : activitiesPerformedResponse.getResponses()) {
							avgStars+= act.getStar();
						}
						activitiesPerformedResponse.setAverageStars(avgStars/count);
					}
					finalActivitiesList.add(activitiesPerformedResponse);
				}
				studResp.setPerformedActivities(finalActivitiesList);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();

		}else {
			if(!gradeRepository.existsBySchoolsCidAndCidAndActiveTrue(schoolCid,gradeCid))
				throw new ValidationException(String.format("Grade with id (%s) does not exist in school having id (%s).",gradeCid,schoolCid));
			
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndStudentGradeCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid ,gradeCid ,FourS.valueOf(fourS),ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
					  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
				
				Double score = 0d;
				Double starSum = 0d;
				focusAreas = new HashSet<String>();
				activityTypes = new HashSet<String>();
				psdAreas = new HashSet<String>();
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					focusAreas.addAll(act.getFocusAreas());
					activityTypes.add(act.getActivityName());
					psdAreas.addAll(act.getPsdAreas());
				}
				score = focusAreas.size() * (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setActivityTypes(activityTypes);
				studResp.setFocusAreas(focusAreas);
				studResp.setPsdAreas(psdAreas);
				
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				for(String type : activityTypes) {
					
					GroupResponseByActivityName<ActivityPerformedResponse> activitiesPerformedResponse = new GroupResponseByActivityName();
					activitiesPerformedResponse.setActivityName(type);
					activitiesPerformedResponse.setResponses(activitiesToReturn.stream().filter(act-> act.getActivityName().equalsIgnoreCase(type)).collect(Collectors.toList()));
					Long count = activitiesPerformedResponse.getResponses().stream().count();
					if(count!= 0) {
						activitiesPerformedResponse.setCount(count);
						double avgStars = 0d;
						for(ActivityPerformedResponse act : activitiesPerformedResponse.getResponses()) {
							avgStars+= act.getStar();
						}
						activitiesPerformedResponse.setAverageStars(avgStars/count);
					}
					finalActivitiesList.add(activitiesPerformedResponse);
				}
				studResp.setPerformedActivities(finalActivitiesList);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();
		}
		if(students == null || students.isEmpty())
			throw new ValidationException("No Student found for fourS : "+fourS);
		return students;
	}
	
	private Set<StudentResponse> getAllStudentsAndItsActivitiesByPsdArea(String psdArea ,String gradeCid , String schoolCid,Date startDate, Date endDate){
		if(psdArea == null)
			throw new ValidationException("Psd Area cannot be null.");
		if(!PSDArea.matches(psdArea))
			throw new ValidationException(String.format("PSD Area (%s) does not matches with available PSD Areas i.e [Personal Development, Social Development]", psdArea));
		
		Set<ActivityPerformed> activitiesPerformed = null ;
		Set<StudentResponse> students = null;
		Set<String> focusAreas, activityTypes , fourS;
		if(gradeCid== null) {
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid ,PSDArea.fromString(psdArea),ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
					  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
				
				Double score = 0d;
				Double starSum = 0d;
				focusAreas = new HashSet<String>();
				activityTypes = new HashSet<String>();
			    fourS = new HashSet<String>();
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					focusAreas.addAll(act.getFocusAreas());
					activityTypes.add(act.getActivityName());
					fourS.add(act.getFourS());
				}
				score = focusAreas.size() * (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setActivityTypes(activityTypes);
				studResp.setFocusAreas(focusAreas);
				studResp.setFourS(fourS);
				
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				for(String type : activityTypes) {
					
					GroupResponseByActivityName<ActivityPerformedResponse> activitiesPerformedResponse = new GroupResponseByActivityName();
					activitiesPerformedResponse.setActivityName(type);
					activitiesPerformedResponse.setResponses(activitiesToReturn.stream().filter(act-> act.getActivityName().equalsIgnoreCase(type)).collect(Collectors.toList()));
					Long count = activitiesPerformedResponse.getResponses().stream().count();
					if(count!= 0) {
						activitiesPerformedResponse.setCount(count);
						double avgStars = 0d;
						for(ActivityPerformedResponse act : activitiesPerformedResponse.getResponses()) {
							avgStars+= act.getStar();
						}
						activitiesPerformedResponse.setAverageStars(avgStars/count);
					}
					   
					finalActivitiesList.add(activitiesPerformedResponse);
				}
				studResp.setPerformedActivities(finalActivitiesList);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();

		}else {
			if(!gradeRepository.existsBySchoolsCidAndCidAndActiveTrue(schoolCid,gradeCid))
				throw new ValidationException(String.format("Grade with id (%s) does not exist in school having id (%s).",gradeCid,schoolCid));
			
			activitiesPerformed = new HashSet<ActivityPerformed>();
			activitiesPerformed = activityPerformedRepository.findAllByStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(schoolCid ,gradeCid ,PSDArea.fromString(psdArea),ActivityStatus.Reviewed,startDate ,endDate);
			students = new HashSet<StudentResponse>();
			for(ActivityPerformed act : activitiesPerformed) {
				if(!students.stream().anyMatch(st -> st.getId().equals(act.getStudent().getCid())) )
					  students.add( new StudentResponse(act.getStudent()));
			}
//			activitiesPerformed.stream().forEach(ap -> students.add( new StudentResponse(ap.getStudent())));
			
			Map<StudentResponse,Double> studentScoreLookUp = new HashMap<StudentResponse,Double>();
			for(StudentResponse studResp : students) {
				List<ActivityPerformedResponse>	activitiesToReturn = activitiesPerformed.stream().filter(ap -> ap.getStudent().getCid().equals(studResp.getId())).map(ActivityPerformedResponse :: new).distinct().collect(Collectors.toList());
				
				Double score = 0d;
				Double starSum = 0d;
				focusAreas = new HashSet<String>();
				activityTypes = new HashSet<String>();
				fourS = new HashSet<String>();
				for(ActivityPerformedResponse act : activitiesToReturn) {
					starSum+= act.getStar();
					focusAreas.addAll(act.getFocusAreas());
					activityTypes.add(act.getActivityName());
					fourS.add(act.getFourS());
				}
				score = focusAreas.size() * (starSum / activitiesToReturn.size());
				System.out.println(score);
				studResp.setScoreForAward(score);
				studResp.setActivityTypes(activityTypes);
				studResp.setFocusAreas(focusAreas);
				studResp.setFourS(fourS);
				
				List<GroupResponseByActivityName> finalActivitiesList = new ArrayList<GroupResponseByActivityName>();
				for(String type : activityTypes) {
					
					GroupResponseByActivityName<ActivityPerformedResponse> activitiesPerformedResponse = new GroupResponseByActivityName();
					activitiesPerformedResponse.setActivityName(type);
					activitiesPerformedResponse.setResponses(activitiesToReturn.stream().filter(act-> act.getActivityName().equalsIgnoreCase(type)).collect(Collectors.toList()));
					Long count = activitiesPerformedResponse.getResponses().stream().count();
					if(count!= 0) {
						activitiesPerformedResponse.setCount(count);
						double avgStars = 0d;
						for(ActivityPerformedResponse act : activitiesPerformedResponse.getResponses()) {
							avgStars+= act.getStar();
						}
						activitiesPerformedResponse.setAverageStars(avgStars/count);
					}
					finalActivitiesList.add(activitiesPerformedResponse);
				}
				studResp.setPerformedActivities(finalActivitiesList);
				studentScoreLookUp.put(studResp, score);
			}
			studentScoreLookUp = studentScoreLookUp.entrySet() .stream() .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			students = studentScoreLookUp.keySet();
		}
		if(students == null || students.isEmpty())
			throw new ValidationException("No Student found for PSD Area : "+psdArea);
		return students;
	}
	
	@Override
	public List<StudentResponse> getAllStudentsOfSchoolForParticularActivity(String activityCid , String activityStatus){
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("Please login first as a teacher or coach or management or school or student to view students.");
		Object user =null;
		String schoolCid = null;
		 user = teacherRepository.getByUserId(userId);
		 if(user == null)
			 user = studentRepository.getByUserId(userId);
		 if(user == null)
			 user = schoolRepository.getByUserId(userId);
		 if(user == null)
			 throw new UnauthorizedUserException("Not logged in as either a teacher or coach or management or school or student.");
		if(user instanceof Teacher) {
			if(((Teacher) user).getSchool()!=null)
				schoolCid = ((Teacher) user).getSchool().getCid();
		}
		if(user instanceof Student) {
			if(((Student) user).getSchool()!=null)
				schoolCid = ((Student) user).getSchool().getCid();
		}
		if(user instanceof School) {
				schoolCid = ((School) user).getCid();
		}
		if(activityCid == null)
			throw new ValidationException("activity id cannot be null.");
		if(!activityRepository.existsByCidAndActiveTrue(activityCid))
			throw new ValidationException(String.format("Activity with id (%s) not found",activityCid));
		if(!activityRepository.existsByCidAndSchoolsCidAndActiveTrue(activityCid, schoolCid)) 
			throw new ValidationException(String.format("Activity with id (%s) not offered in school",activityCid));
		List<Student> students = studentRepository
				.findAllBySchoolCidAndActivitiesActivityCidAndActivitiesActivityStatusAndSchoolActiveTrueAndActivitiesActivityActiveTrueAndActiveTrue(
						schoolCid, activityCid, ActivityStatus.valueOf(activityStatus));
		if (students == null || students.isEmpty())
			throw new ValidationException(String.format(
					"No student in the school has performed any activity having id (%s) or activities might not have been %s.",
					 activityCid, activityStatus));
		return students.stream().distinct().map(StudentResponse::new).collect(Collectors.toList());
	}

	@Override
	public ClubMembershipResponse applyForClubMembership(String activityCid , String supervisorCid) {
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("Login first as student to apply for membership.");
		Long  studentId = studentRepository.findIdByUserIdAndActiveTrue(userId);
		if(studentId == null)
			throw new ValidationException("Login as student to apply for membership.");
		if(activityCid == null)
			throw new ValidationException("activityCid cannot be null.");
		if(supervisorCid == null)
			throw new ValidationException("supervisorId cannot be null.");
		if(studentClubRepository.existsByStudentIdAndActivityCidAndMembershipStatusAndActiveTrue(studentId, activityCid, ApprovalStatus.VERIFIED))
			throw new ValidationException("Already applied for the membership of this club.");
		if(studentClubRepository.existsByStudentIdAndActivityCidAndMembershipStatusAndActiveTrue(studentId, activityCid, ApprovalStatus.PENDING))
			throw new ValidationException("Already applied for the membership of this club and its pending.");
		
		StudentClub studentClub = new StudentClub(studentId, activityRepository.findIdByCidAndActiveTrue(activityCid), teacherRepository.findIdByCidAndActiveTrue(supervisorCid));
		studentClub.setAppliedOn(LocalDateTime.now().toDate());
		studentClub.setMembershipStatus(ApprovalStatus.PENDING);
		studentClub = studentClubRepository.save(studentClub);
		return new ClubMembershipResponse(studentClub);
	}
}
