package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityResponse;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;
import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;
import com.restfb.types.webhook.messaging.payment.ReuqestedUserInfo;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	TeacherRepository teacherRepository;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	GradeRepository gradeRepository;
	
	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	UserService userService;

	@Autowired
	Utils utils;

	@Override
	public List<TeacherResponse> uploadTeachersFromExcel(MultipartFile file, Integer rowLimit , Boolean isCoach) {

		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<TeacherResponse> teacherResponseList = new ArrayList<>();
		List<Map<String, Object>> teacherRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook studentsSheet = new XSSFWorkbook(file.getInputStream());
			if(isCoach==true) {
				teacherRecords = findSheetRowValues(studentsSheet, "COACH", rowLimit, errors);
				
				for (int i = 0; i < teacherRecords.size(); i++) {
					List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
					tempStudentsRecords.add(teacherRecords.get(i));
					teacherResponseList.add(saveCoach(validateTeacherRequest(tempStudentsRecords, errors,true)));
				}
			}
			else {
				teacherRecords = findSheetRowValues(studentsSheet, "TEACHER", rowLimit, errors);
				for (int i = 0; i < teacherRecords.size(); i++) {
					List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
					tempStudentsRecords.add(teacherRecords.get(i));
					teacherResponseList.add(saveClassTeacher(validateTeacherRequest(tempStudentsRecords, errors,false)));
				}
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}

		return teacherResponseList;
	}

	@Override
	public TeacherResponse save(TeacherRequest request) {
		if (request == null)
			throw new ValidationException("Request can not be null.");
		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");
		if (teacherRepository.countByEmail(request.getEmail()) > 0)
			throw new ValidationException("Email already exists");
		if (request.getUsername() == null)
			request.setUsername(request.getEmail());
		if (teacherRepository.countByUsername(request.getUsername()) > 0)
			throw new ValidationException("Username already exists");
		if (request.getName() == null)
			throw new ValidationException("Teacher name can not be null");

		Teacher teacher = request.toEntity();
		if (request.getSchoolId() != null) {
			teacher.setSchool(schoolRepository.getOneBycId(request.getSchoolId()));
			if (request.getGradeIds() != null && !request.getGradeIds().isEmpty()) {
				List<Grade> grades = new ArrayList<Grade>();
				for (String grade : request.getGradeIds()) {
					grades.add(gradeRepository.getOneBycId(grade));
				}
				teacher.setGrades(grades);
			}

		}
		
		if(request.getActivitiyIds()!= null && !request.getActivitiyIds().isEmpty() )
		{
			List<Activity> activities = new ArrayList<Activity>();
			for(String actCId : request.getActivitiyIds()) {
				Activity activity = activityRepository.getOneBycId(actCId);
				if(activity!=null)
					activities.add(activity);
			}
			teacher.setActivities(activities);
		}
		
		try {
			teacher.setcId(utils.generateRandomAlphaNumString(8));
		} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
			teacher.setcId(utils.generateRandomAlphaNumString(8));
		}
		User user = userService.createTeacherUser(teacher);
		if (StringUtils.isEmpty(user))
			throw new ValidationException("User not created successfully");
		teacher.setUser(user);
		teacher = teacherRepository.save(teacher);
		if (teacher == null)
			throw new RuntimeException("Something went wrong student not saved.");

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

	private TeacherRequest validateTeacherRequest(List<Map<String, Object>> teacherDetails, List<String> errors,
			Boolean isCoach) {
		if (teacherDetails == null || teacherDetails.isEmpty()) {
			errors.add("Teacher details not found");
		}
		TeacherRequest teacherRequest = new TeacherRequest();
		teacherRequest.setName((String) teacherDetails.get(0).get("NAME"));
		teacherRequest.setQualification((String) teacherDetails.get(0).get("QUALIFICATION"));
		teacherRequest.setUsername((String) teacherDetails.get(0).get("USERNAME"));
		teacherRequest.setDob(
				DateUtil.convertStringToDate(DateUtil.formatDate((Date) teacherDetails.get(0).get("DOB"), null, null)));
		School school = null;
		if (teacherDetails.get(0).get("SCHOOL") != null)
			school = schoolRepository.findByName((String) teacherDetails.get(0).get("SCHOOL"));
		if (teacherDetails.get(0).get("SCHOOLS EMAIL") != null)
			school = schoolRepository.findByEmail((String) teacherDetails.get(0).get("SCHOOLS EMAIL"));

		if (school == null)
			errors.add(String.format("School %s not found ", (String) teacherDetails.get(0).get("SCHOOL")));
		else {
			teacherRequest.setSchoolId(school.getcId());
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
						grade = gradeRepository.findByNameAndSchoolsId(gradeAndSection[0], school.getId());
					} else if (gradeAndSection[1] != null && gradeAndSection[0] == null) {
						errors.add("GRADE is empty");
					} else {
						grade = gradeRepository.findByNameAndSchoolsIdAndSection(gradeAndSection[0], school.getId(),
								gradeAndSection[1]);
					}

					if (grade == null)
						errors.add(String.format("Grade  %s not found ", gradeAndSection[0]));
					else
						gradeCIds.add(grade.getcId());

				}
				teacherRequest.setGradeIds(gradeCIds);
			} else
				errors.add("No classes provided for the teacher");
		}

		if (isCoach == true) {
			String activities = (String) teacherDetails.get(0).get("ACTIVITY");
			List<String> activityCIds = new ArrayList<String>();
			String[] activityNames = null;
			if (activities != null)
				activityNames = activities.split(",");

			if (activityNames != null && activityNames.length > 0) {
				for(String act : activityNames) {
					Activity activity = activityRepository.getOneByName(act);
					
					if(activity == null)
						errors.add(String.format("Activity %s  not found ", act));
					else
						activityCIds.add(activity.getcId());
				}
				teacherRequest.setActivitiyIds(activityCIds);

			} else
				errors.add("No activities provided for the coach.");
		}
		teacherRequest.setEmail((String) teacherDetails.get(0).get("EMAIL"));
		if (teacherDetails.get(0).get("ACTIVE") != null)
			teacherRequest.setActive(Boolean.valueOf((Boolean) teacherDetails.get(0).get("ACTIVE")));
		teacherRequest.setMobileNumber((String) teacherDetails.get(0).get("MOBILE NUMBER"));
		teacherRequest.setGender((String) teacherDetails.get(0).get("GENDER"));

		return teacherRequest;

	}
	
	@Override
	public TeacherResponse findById(Long id) {
		if(id == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findById(id).orElse(null);
		if(teacher == null)
			throw new ValidationException(String.format("Teacher having id : %s not found", id));
		
		return new TeacherResponse(teacher);
	}
	
	@Override
	public TeacherResponse findByCId(String cId) {
		if(cId == null)
			throw new ValidationException("Id can not be null");
		Teacher teacher = teacherRepository.findByCid(cId);
		if(teacher == null)
			throw new ValidationException(String.format("Teacher having id : %s not found", cId));
		
		return new TeacherResponse(teacher);
	}
	
	@Override
	public List<TeacherResponse> findCoachesByActivityName(String activityName){
		if(activityName == null)
			throw new ValidationException("Activity name can not be null");
		List<Teacher> teachersList = new ArrayList<>();
		List<TeacherResponse> teachersResponseList = new ArrayList<>();
		teachersList = teacherRepository.findAllByActivitiesName(activityName);
		
		if(teachersList!=null && !teachersList.isEmpty()) {
			for(Teacher teacher : teachersList)
			   teachersResponseList.add(new TeacherResponse(teacher));
		}
		
		return teachersResponseList;
	}

	@Override
	public List<ActivityResponse> findAllActivitiesByCoachId(Long id){
		
		if(id == null)
			throw new ValidationException("Id can not be null");
		
		List<Activity> activities = new ArrayList<Activity>();
		List<ActivityResponse> activityResponses = new ArrayList<ActivityResponse>();
		
		activities = activityRepository.findAllByTeachersId(id);
		if(activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");
		for(Activity activity : activities) {
			activityResponses.add(new ActivityResponse(activity));
		}
		return activityResponses;
	}
	
	@Override
    public List<ActivityResponse> findAllActivitiesByCoachCId(String cId){
    	if(cId == null)
			throw new ValidationException("Id can not be null");
    	List<Activity> activities = new ArrayList<Activity>();
		List<ActivityResponse> activityResponses = new ArrayList<ActivityResponse>();
		
		activities = activityRepository.findAllByTeachersCid(cId);
		if(activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");
		for(Activity activity : activities) {
			activityResponses.add(new ActivityResponse(activity));
		}
		return activityResponses;
	}
}
