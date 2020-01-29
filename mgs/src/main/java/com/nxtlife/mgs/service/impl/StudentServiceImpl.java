package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.GuardianRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.service.StudentService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	GuardianRepository guardianRepository;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	UserService userService;

	@Autowired
	Utils utils;

	@Override
	public StudentResponse save(StudentRequest request) {
		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");
		if (studentRepository.countByEmail(request.getEmail()) > 0)
			throw new ValidationException("Email already exists");
		if (request.getUsername() == null) {
			request.setUsername(request.getEmail());
			if (studentRepository.countByUsername(request.getUsername()) > 0)
				throw new ValidationException("Username already exists");
		} else {
			if (studentRepository.countByUsername(request.getUsername()) > 0)
				throw new ValidationException("Username already exists");
		}

		if (request.getName() == null)
			throw new ValidationException("Student name can not be null");
//		if (request.getSchoolCId() == null)
//			throw new ValidationException("School id can not be null");
//		if (request.getGradeCId() == null)
//			throw new ValidationException("Grade id can not be null");

		Student student = request.toEntity();
		if (request.getSchoolId() != null) {
			student.setSchool(schoolRepository.getOneBycId(request.getSchoolId()));
			if (request.getGradeId() != null)
				student.setGrade(gradeRepository.getOneBycId(request.getGradeId()));

		}
		try {
			student.setcId(utils.generateRandomAlphaNumString(8));
		} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
			student.setcId(utils.generateRandomAlphaNumString(8));
		}
		List<Guardian> guardians = createParent(request, student);
		student.setGuardians(guardians);
//		if (request.getActive() != null) {
//			student.setActive(request.getActive());
//		} else
//			student.setActive(false);
		if (request.getSubscriptionEndDate().after(LocalDateTime.now().toDate())) {
			student.setActive(true);
		}

		User user = userService.createStudentUser(student);
		if (StringUtils.isEmpty(user))
			throw new ValidationException("User not created successfully");
		student.setUser(user);
		return new StudentResponse(studentRepository.save(student));
	}

	public List<Guardian> createParent(StudentRequest studentRequest, Student student) {
		List<Guardian> parents = new ArrayList<>();
		Guardian parent = null;

		if (studentRequest.getFathersName() != null && (studentRequest.getFathersEmail() != null || studentRequest.getFathersMobileNumber() != null )) {
			if (studentRequest.getFathersEmail() != null) {
				parent = guardianRepository.getOneByEmail(studentRequest.getFathersEmail());
			}
			if (studentRequest.getFathersMobileNumber() != null) {
				parent = guardianRepository.getOneByMobileNumber(studentRequest.getFathersMobileNumber());
			}
			if (parent == null) {
				parent = new Guardian();
				parent.setName(studentRequest.getFathersName());
				String username = null;
				if (studentRequest.getFathersEmail() != null) {
					parent.setEmail(studentRequest.getFathersEmail());
					username = studentRequest.getFathersEmail();
//					parent.setUsername(studentRequest.getFathersEmail());
				}
				if (studentRequest.getFathersMobileNumber() != null) {
					parent.setMobileNumber(studentRequest.getFathersMobileNumber());
					if(username==null)
						username =studentRequest.getFathersMobileNumber();
//					parent.setUsername(studentRequest.getFathersMobileNumber());
				}

				try {
					parent.setcId(utils.generateRandomAlphaNumString(8));
				} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
					parent.setcId(utils.generateRandomAlphaNumString(8));
				}
				parent.setUsername(username);
				parent.setMobileNumber(studentRequest.getFathersMobileNumber());
				parent.setGender("Male");
				parent.setActive(true);
				parent.setStudent(student);
				User user = userService.createParentUser(parent);
				parent.setUser(user);
				parents.add(parent);

			} else {
				parent.setStudent(student);
				parents.add(parent);
			}
		}

		parent = null;
		if (studentRequest.getMothersName() != null && (studentRequest.getMothersEmail() != null || studentRequest.getMothersMobileNumber() != null)) {
			if (studentRequest.getMothersEmail() != null) {
				parent = guardianRepository.getOneByEmail(studentRequest.getMothersEmail());
			}
			if (studentRequest.getMothersMobileNumber() != null) {
				parent = guardianRepository.getOneByMobileNumber(studentRequest.getMothersMobileNumber());
			}
			if (parent == null) {
				parent = new Guardian();
				parent.setName(studentRequest.getMothersName());
				String username = null;
				if (studentRequest.getMothersEmail() != null) {
					parent.setEmail(studentRequest.getMothersEmail());
					username = studentRequest.getMothersEmail();
//					parent.setUsername(studentRequest.getMothersEmail());
				}
				
				if (studentRequest.getMothersMobileNumber() != null) {
					parent.setMobileNumber(studentRequest.getMothersMobileNumber());
					if(username==null)
						username = studentRequest.getMothersMobileNumber();
//					parent.setUsername(studentRequest.getMothersMobileNumber());
				}
				
				try {
					parent.setcId(utils.generateRandomAlphaNumString(8));
				} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
					parent.setcId(utils.generateRandomAlphaNumString(8));
				}
				parent.setUsername(username);
				parent.setGender("Female");
				parent.setActive(true);
				parent.setStudent(student);
				User user = userService.createParentUser(parent);
				parent.setUser(user);
				parents.add(parent);

			} else {
				parent.setStudent(student);
				parents.add(parent);
			}
		}

		return parents;
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
					throw new ValidationException(String.format("Some of the cells (Row number : %d) are missing or extras in %s sheet", i + 1,
							sheetName));
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
					cell = row.getCell(j,MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (cell != null) {
						if (columnTypes.get(headers.get(j)).equals(cell.getCellType())) {
							if (cell.getCellType() == CellType.NUMERIC) {
								if(headers.get(j).contains("DATE")||headers.get(j).contains("DOB"))
								   columnValues.put(headers.get(j), cell.getDateCellValue());
								else
									columnValues.put(headers.get(j), new DataFormatter().formatCellValue(cell));
							}
							else if (cell.getCellType() == CellType.BOOLEAN) {
								columnValues.put(headers.get(j), cell.getBooleanCellValue());
							} else {
								columnValues.put(headers.get(j), cell.getStringCellValue());
							}
						} else { 
							errors.add(String.format(
									"Cell Type is incorrect (Expected : %s, Actual : %s) for column %s of sheet (%s)",
									columnTypes.get(headers.get(j)), cell.getCellType(), headers.get(j), sheetName));
						}
					}
					else {
//						if(columnTypes.get(headers.get(j)).equals(cell.getCellType()))
						columnValues.put(headers.get(j), null);
					}
				}

			}
		}
		return rows;
	}

	private List<Map<String, Object>> findSheetRowValues(XSSFWorkbook workbook, String sheetName, Integer rowLimit,
			List<String> errors) {
//		XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			errors.add(sheetName + " sheet not found");
			return null;
		}
		if (sheet.getPhysicalNumberOfRows() > rowLimit) {
			errors.add(String.format("Number of row can't be more than %d for %s sheet", rowLimit, sheetName));
		}
		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(sheetName);
		return fetchRowValues(columnTypes, sheet, errors, sheetName);

	}

	@Override
	public List<StudentResponse> uploadStudentsFromExcel(MultipartFile file, Integer rowLimit) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");
//				|| !(file.getContentType().equalsIgnoreCase("xlsx") || file.getContentType().equalsIgnoreCase("xls")
//						|| file.getContentType().equalsIgnoreCase("xlsb")
//						|| file.getContentType().equalsIgnoreCase("xlsm")))

		List<String> errors = new ArrayList<String>();
		List<StudentResponse> studentResponseList = new ArrayList<>();
		List<Map<String, Object>> studentsRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook studentsSheet = new XSSFWorkbook(file.getInputStream());
			studentsRecords = findSheetRowValues(studentsSheet, "STUDENT", rowLimit, errors);
			for (int i = 0; i < studentsRecords.size(); i++) {
				List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
				tempStudentsRecords.add(studentsRecords.get(i));
				studentResponseList.add(save(validateStudentRequest(tempStudentsRecords, errors)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}

//		findSheetRowValues(studentsSheet,"STUDENT",);
		return studentResponseList;
	}

	private StudentRequest validateStudentRequest(List<Map<String, Object>> studentDetails, List<String> errors) {
		if (studentDetails == null || studentDetails.isEmpty()) {
			errors.add("Equipment details not found");
		}
		StudentRequest studentRequest = new StudentRequest();
		studentRequest.setName((String) studentDetails.get(0).get("NAME"));
		studentRequest.setUsername((String) studentDetails.get(0).get("USERNAME"));
		studentRequest.setDob(
				DateUtil.convertStringToDate(DateUtil.formatDate((Date) studentDetails.get(0).get("DOB"), null, null)));
		School school = null;
		if(studentDetails.get(0).get("SCHOOL")!=null) 
		   school = schoolRepository.findByName((String) studentDetails.get(0).get("SCHOOL"));
		if(studentDetails.get(0).get("SCHOOLS EMAIL")!=null)
			school = 	schoolRepository.findByEmail((String) studentDetails.get(0).get("SCHOOLS EMAIL"));
		
		if (school == null)
			errors.add(String.format("School %s not found ", (String) studentDetails.get(0).get("SCHOOL")));
		else {
			studentRequest.setSchoolId(school.getcId());
			String standard = (String) studentDetails.get(0).get("GRADE");
			String section = (String) studentDetails.get(0).get("SECTION");
			Grade grade = null;
			if (standard == null && section == null) {
				errors.add("GRADE and SECTION are empty.");
			} else if (standard != null && section == null) {
				errors.add("SECTION is empty");
				grade = gradeRepository.findByNameAndSchoolId(standard, school.getId());
			} else if (section != null && standard == null) {
				errors.add("GRADE is empty");
			} else {
				grade = gradeRepository.findByNameAndSchoolIdAndSection(standard, school.getId(), section);
			}
			if (grade == null)
				errors.add(String.format("Grade  %s not found ", (String) studentDetails.get(0).get("GRADE")));
			studentRequest.setGradeId(grade.getcId());
		}
		

		studentRequest.setEmail((String) studentDetails.get(0).get("EMAIL"));
		if (studentDetails.get(0).get("ACTIVE") != null)
			studentRequest.setActive(Boolean.valueOf((Boolean) studentDetails.get(0).get("ACTIVE")));
		studentRequest.setMobileNumber((String) studentDetails.get(0).get("MOBILE NUMBER"));
		studentRequest.setGender((String) studentDetails.get(0).get("GENDER"));
		studentRequest.setSubscriptionEndDate(DateUtil.convertStringToDate(
				DateUtil.formatDate((Date) studentDetails.get(0).get("SUBSCRIPTION END DATE"), null, null)));
		studentRequest.setFathersName((String) studentDetails.get(0).get("FATHERS NAME"));
		studentRequest.setFathersEmail((String) studentDetails.get(0).get("FATHERS EMAIL"));
		studentRequest.setFathersMobileNumber((String) studentDetails.get(0).get("FATHERS MOBILE NUMBER"));
		studentRequest.setMothersName((String) studentDetails.get(0).get("MOTHERS NAME"));
		studentRequest.setMothersEmail((String) studentDetails.get(0).get("MOTHERS EMAIL"));
		studentRequest.setMothersMobileNumber((String) studentDetails.get(0).get("MOTHERS MOBILE NUMBER"));

		return studentRequest;
	}

}
