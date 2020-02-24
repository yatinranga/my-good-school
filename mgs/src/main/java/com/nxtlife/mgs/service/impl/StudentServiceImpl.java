package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.GuardianRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.SequenceGeneratorRepo;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.StudentSchoolGradeRepository;
import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.SequenceGeneratorService;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.SequenceGenerator;
import com.nxtlife.mgs.util.Utils;
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

	@Override
	public StudentResponse save(StudentRequest request) {

		if (studentRepository.countByEmail(request.getEmail()) > 0)
			throw new ValidationException(String.format("Email [%s] already exist", request.getEmail()));

		Long studsequence = sequenceGeneratorService.findSequenceByUserType(UserType.Student);
		if (studsequence == null) {
			sequenceGeneratorRepo.save(new SequenceGenerator(UserType.Student, 0l));
		}
		++studsequence;
		request.setUsername(String.format("STU%08d", studsequence));
		sequenceGeneratorService.updateSequenceByUserType(studsequence, UserType.Student);

		List<Guardian> repoGuardians = guardianRepository.findAll();
		List<Guardian> alreadyPresentGuardiansList = new ArrayList<Guardian>();

		if (request.getGuardians() != null) {
			for (int i = 0; i < request.getGuardians().size(); i++) {
				GuardianRequest guardian = request.getGuardians().get(i);

				if (repoGuardians.isEmpty()) {
					break;
				}
				for (Guardian g : repoGuardians) {
					if (g.getEmail().equals(guardian.getEmail())
							|| g.getMobileNumber().equals(guardian.getMobileNumber())) {
						alreadyPresentGuardiansList.add(g);
						request.getGuardians().remove(i);
						i--;
						break;
					}
				}

			}
		}

		if (request.getGuardians() != null) {
			for (GuardianRequest greq : request.getGuardians()) {
				Long sequence = sequenceGeneratorService.findSequenceByUserType(UserType.Parent);
				if (sequence == null) {
					sequenceGeneratorRepo.save(new SequenceGenerator(UserType.Parent, 0l));
				}
				++sequence;
				greq.setUsername(String.format("GRD%08d", sequence));
				sequenceGeneratorService.updateSequenceByUserType(sequence, UserType.Parent);
			}
		}

		Student student = request.toEntity();

		// here we're creating parent user for new guardians not for existing guardians

		if (student.getGuardians() != null) {
			for (Guardian gua : student.getGuardians()) {
				List<Student> studentList = new ArrayList<Student>();
				studentList.add(student);
				gua.setStudents(studentList);
				gua.setCid(utils.generateRandomAlphaNumString(8));
				gua.setUser(userService.createParentUser(gua));
			}
		}

		// adding newly created student to already presented guardian list

		for (Guardian alreadyPresentGuardian : alreadyPresentGuardiansList) {
			List<Student> tempList = alreadyPresentGuardian.getStudents();
			tempList.add(student);
			alreadyPresentGuardian.setStudents(tempList);
		}

		// setting all the existing and newely created guardians to student

		if (student.getGuardians() != null) {
			alreadyPresentGuardiansList.addAll(student.getGuardians());
		}

		student.setGuardians(alreadyPresentGuardiansList);

		if (request.getSchoolId() != null) {
			School school = schoolRepository.getOneByCidAndActiveTrue(request.getSchoolId());
			if (school == null)
				throw new ValidationException(String.format("School with id : %s not found.", request.getSchoolId()));
			student.setSchool(school);
			if (request.getGradeId() != null)
				student.setGrade(gradeRepository.getOneByCid(request.getGradeId()));

		}

		student.setCid(utils.generateRandomAlphaNumString(8));

		/*
		 * if (request.getSubscriptionEndDate().after(LocalDateTime.now().toDate())) {
		 * student.setActive(true); }
		 */

		User user = userService.createStudentUser(student);

		if (StringUtils.isEmpty(user)) {
			throw new ValidationException("User not created successfully");
		}

		student.setUser(user);
		student = studentRepository.save(student);

		if (student == null) {
			throw new RuntimeException("Something went wrong student not saved.");
		}

		return new StudentResponse(student);
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

		List<Guardian> alreadyPresentGuardiansList = new ArrayList<Guardian>();

		if (request.getGuardians() != null) {

			List<Guardian> repoGuardians = guardianRepository.findAll();

			for (int i = 0; i < request.getGuardians().size(); i++) {
				GuardianRequest guardian = request.getGuardians().get(i);

				if (repoGuardians.isEmpty()) {
					break;
				}
				for (Guardian g : repoGuardians) {
					if (g.getEmail().equals(guardian.getEmail())
							|| g.getMobileNumber().equals(guardian.getMobileNumber())) {
						alreadyPresentGuardiansList.add(g);
						request.getGuardians().remove(i);
						i--;
						break;
					}
				}

			}
		}

		if (request.getGuardians() != null) {
			for (GuardianRequest greq : request.getGuardians()) {
				Long sequence = sequenceGeneratorService.findSequenceByUserType(UserType.Parent);
				if (sequence == null) {
					sequenceGeneratorRepo.save(new SequenceGenerator(UserType.Parent, 0l));
				}
				++sequence;
				greq.setUsername(String.format("GRD%08d", sequence));
				sequenceGeneratorService.updateSequenceByUserType(sequence, UserType.Parent);
			}
		}

		student = request.toEntity(student);

		// here we're creating parent user for new guardians not for existing guardians

		/*
		 * if (request.getActive() == false && request.getActive() != null) {
		 * student.setActive(false); studentRepository.save(student); return new
		 * StudentResponse(student); }
		 */

		if (student.getGuardians() != null) {
			for (Guardian gua : student.getGuardians()) {
				List<Student> studentList = new ArrayList<Student>();
				studentList.add(student);
				gua.setStudents(studentList);
				gua.setCid(utils.generateRandomAlphaNumString(8));
				gua.setUser(userService.createParentUser(gua));
			}
		}

		// adding newly created student to already presented guardian list

		for (Guardian alreadyPresentGuardian : alreadyPresentGuardiansList) {
			List<Student> tempList = alreadyPresentGuardian.getStudents();
			tempList.add(student);
			alreadyPresentGuardian.setStudents(tempList);
		}

		// setting all the existing and newely created guardians to student

		if (student.getGuardians() != null) {
			alreadyPresentGuardiansList.addAll(student.getGuardians());
		}

		student.setGuardians(alreadyPresentGuardiansList);

		// student = request.toEntity(student);

		student = studentRepository.save(student);

		return new StudentResponse(student);
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
//						if(columnTypes.get(headers.get(j)).equals(cell.getCellType()))
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

	@Override
	public Map<String, List<Object>> uploadStudentsFromExcel(MultipartFile file, String schoolCid) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");
//				|| !(file.getContentType().equalsIgnoreCase("xlsx") || file.getContentType().equalsIgnoreCase("xls")
//						|| file.getContentType().equalsIgnoreCase("xlsb")
//						|| file.getContentType().equalsIgnoreCase("xlsm")))

		List<String> errors = new ArrayList<String>();
		List<StudentResponse> studentResponseList = new ArrayList<>();
		List<Map<String, Object>> studentsRecords = new ArrayList<Map<String, Object>>();
		Map<String, List<Object>> responseMap = new HashMap<String, List<Object>>();
		try {
			XSSFWorkbook studentsSheet = new XSSFWorkbook(file.getInputStream());
			studentsRecords = findSheetRowValues(studentsSheet, "STUDENT", errors);
			for (int i = 0; i < studentsRecords.size(); i++) {
				List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
				tempStudentsRecords.add(studentsRecords.get(i));
				studentResponseList.add(save(validateStudentRequest(tempStudentsRecords, errors, schoolCid)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}

		responseMap.put("StudentResponseList", (List) studentResponseList);
		responseMap.put("errors", (List) errors);

//		findSheetRowValues(studentsSheet,"STUDENT",);
		return responseMap;
	}

	private StudentRequest validateStudentRequest(List<Map<String, Object>> studentDetails, List<String> errors,
			String schoolCid) {
		if (studentDetails == null || studentDetails.isEmpty()) {
			errors.add("Student details not found");
		}
		StudentRequest studentRequest = new StudentRequest();
		studentRequest.setName((String) studentDetails.get(0).get("NAME"));
//		studentRequest.setUsername((String) studentDetails.get(0).get("USERNAME"));
		studentRequest.setDob(
				DateUtil.convertStringToDate(DateUtil.formatDate((Date) studentDetails.get(0).get("DOB"), null, null)));
		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);
//		if (studentDetails.get(0).get("SCHOOL") != null)
//			school = schoolRepository.findByName((String) studentDetails.get(0).get("SCHOOL"));
//		if (studentDetails.get(0).get("SCHOOLS EMAIL") != null)
//			school = schoolRepository.findByEmail((String) studentDetails.get(0).get("SCHOOLS EMAIL"));

		if (school == null)
			errors.add(String.format("School with id : %s not found ", schoolCid));
		else {
			studentRequest.setSchoolId(school.getCid());
			String standard = (String) studentDetails.get(0).get("GRADE");
			String section = (String) studentDetails.get(0).get("SECTION");
			Grade grade = null;
			if (standard == null && section == null) {
				errors.add("GRADE and SECTION are empty.");
			} else if (standard != null && section == null) {
				errors.add("SECTION is empty");
				grade = gradeRepository.findByNameAndSchoolsId(standard, school.getId());
			} else if (section != null && standard == null) {
				errors.add("GRADE is empty");
			} else {
				grade = gradeRepository.findByNameAndSchoolsIdAndSection(standard, school.getId(), section);
			}
			if (grade == null)
				errors.add(String.format("Grade  %s not found ", (String) studentDetails.get(0).get("GRADE")));
			studentRequest.setGradeId(grade.getCid());
		}
		studentRequest.setSessionStartDate(
				DateUtil.convertStringToDate(DateUtil.formatDate((Date) studentDetails.get(0).get("DOB"), null, null)));
		studentRequest.setEmail((String) studentDetails.get(0).get("EMAIL"));
		if (studentDetails.get(0).get("ACTIVE") != null)
			studentRequest.setActive(Boolean.valueOf((Boolean) studentDetails.get(0).get("ACTIVE")));
		studentRequest.setMobileNumber((String) studentDetails.get(0).get("MOBILE NUMBER"));
		studentRequest.setGender((String) studentDetails.get(0).get("GENDER"));
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
//		studentRequest.setFathersName((String) studentDetails.get(0).get("FATHERS NAME"));
//		studentRequest.setFathersEmail((String) studentDetails.get(0).get("FATHERS EMAIL"));
//		studentRequest.setFathersMobileNumber((String) studentDetails.get(0).get("FATHERS MOBILE NUMBER"));
//		studentRequest.setMothersName((String) studentDetails.get(0).get("MOTHERS NAME"));
//		studentRequest.setMothersEmail((String) studentDetails.get(0).get("MOTHERS EMAIL"));
//		studentRequest.setMothersMobileNumber((String) studentDetails.get(0).get("MOTHERS MOBILE NUMBER"));

		return studentRequest;
	}

	@Override
	public List<StudentResponse> findByName(String name) {

		if (name == null)
			throw new ValidationException("name can't be null");

		List<Student> studentList = studentRepository.findByNameAndActiveTrue(name);

		List<StudentResponse> studentResponseList = new ArrayList<StudentResponse>();

		if (studentList.isEmpty())
			throw new NotFoundException(String.format("students having name [%s] didn't exist", name));

		for (Student student : studentList) {
			studentResponseList.add(new StudentResponse(student));
		}

		return studentResponseList;
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
			throw new ValidationException("cId can't be null");

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

		List<StudentResponse> studentResponseList = studentList.stream().map(s -> new StudentResponse(s))
				.collect(Collectors.toList());

		return studentResponseList;
	}

	@Override
	public List<StudentResponse> getAllBySchoolCid(String schoolCid) {

		if (schoolCid == null) {
			throw new NotFoundException("school id can't be null");
		}

		List<Student> studentsList = studentRepository.findAllBySchoolCidAndActiveTrue(schoolCid);

		if (studentsList.isEmpty())
			throw new NotFoundException(String.format("No student found for school having id [%s]", schoolCid));

		List<StudentResponse> responseList = studentsList.stream().map(s -> new StudentResponse(s))
				.collect(Collectors.toList());

		return responseList;
	}

	@Override
	public SuccessResponse delete(String cid) {
		if (cid == null) {
			throw new ValidationException("student id can't be null");
		}

		Student student = studentRepository.findByCidAndActiveTrue(cid);

		if (student == null || student.getActive() == false) {
			throw new NotFoundException(String.format("student having id [%s] can't exist", cid));
		}

		student.setActive(false);
		studentRepository.save(student);

		student = studentRepository.findByCidAndActiveTrue(cid);

		if (student != null) {
			throw new ValidationException(
					String.format("something went wrong student having id [%s] can't deleted", cid));
		} else {
			return new SuccessResponse(org.springframework.http.HttpStatus.OK.value(), "student deleted successfully");
		}

	}

}
