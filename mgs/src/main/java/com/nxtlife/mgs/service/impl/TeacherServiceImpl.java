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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.SequenceGeneratorRepo;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.SequenceGeneratorService;
import com.nxtlife.mgs.service.TeacherService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityRequestResponse;
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
	Utils utils;

	@Override
	public TeacherResponse save(TeacherRequest request) {

		if (request == null)
			throw new ValidationException("Request can not be null.");

		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");

		if (teacherRepository.countByEmailAndActiveTrue(request.getEmail()) > 0)
			throw new ValidationException(String.format("Email %s already exists", request.getEmail()));

//		if (request.getUsername() == null)
//			request.setUsername(request.getEmail());

//		if (teacherRepository.countByUsernameAndActiveTrue(request.getUsername()) > 0)
//			throw new ValidationException("Username already exists");

		if (request.getName() == null)
			throw new ValidationException("Teacher name can not be null");

		Teacher teacher = request.toEntity();

		Long teachersequence;
		if (request.getIsCoach()) {
			teachersequence = sequenceGeneratorService.findSequenceByUserType(UserType.Coach);
			teacher.setUsername(String.format("COA%08d", teachersequence));

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

		}

		// saving activities

		if (request.getActivitiyIds() != null && !request.getActivitiyIds().isEmpty()) {

			List<Activity> repoActivityList = activityRepository
					.findAllBySchoolsCidAndActiveTrue(request.getSchoolId());

			List<Activity> finalActivityList = new ArrayList<Activity>();

			// validating activities present in request are also present in school activity
			// list or not.

			for (int i = 0; i < request.getActivitiyIds().size(); i++) {

				boolean flag = false, aptFlag = false;
				;

				for (Activity activity : repoActivityList) {

					if (activity.getCid().equals(request.getActivitiyIds().get(i))) {
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
							String.format("activity having id [%s] didn't exist", request.getActivitiyIds().get(i)));
				}
			}
			teacher.setActivities(finalActivityList);
		}

		teacher.setcId(utils.generateRandomAlphaNumString(8));

		User user = userService.createTeacherUser(teacher);

		if (user == null)
			throw new ValidationException("User not created successfully");

		teacher.setUser(user);
		teacher.setActive(true);

		teacher = teacherRepository.save(teacher);

		if (teacher == null)
			throw new RuntimeException("Something went wrong teacher not saved.");

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

		teacher = teacherRepository.save(teacher);

		return new TeacherResponse(teacher);

	}

	@Override
	public ResponseEntity<?> uploadTeachersFromExcel(MultipartFile file, Boolean isCoach, String schoolCid) {

		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<TeacherResponse> teacherResponseList = new ArrayList<>();
		List<Map<String, Object>> teacherRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook studentsSheet = new XSSFWorkbook(file.getInputStream());
			if (isCoach == true) {
				teacherRecords = findSheetRowValues(studentsSheet, "COACH", errors);
				errors = (List<String>) teacherRecords.get(teacherRecords.size() - 1).get("errors");
				for (int i = 0; i < teacherRecords.size(); i++) {
					List<Map<String, Object>> tempStudentsRecords = new ArrayList<Map<String, Object>>();
					tempStudentsRecords.add(teacherRecords.get(i));
					teacherResponseList
							.add(saveCoach(validateTeacherRequest(tempStudentsRecords, errors, true, schoolCid)));
				}
			} else {
				teacherRecords = findSheetRowValues(studentsSheet, "TEACHER", errors);
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
						if (headers.get(j).equalsIgnoreCase("NAME") || headers.get(j).equalsIgnoreCase("EMAIL")
								|| headers.get(j).equalsIgnoreCase("MOBILE NUMBER")
								|| headers.get(j).equalsIgnoreCase("GRADE"))
							errors.add(String.format("Cell at row %d and column %d is blank for header %s.", i + 1,
									j + 1, headers.get(j)));
						columnValues.put(headers.get(j), null);
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
		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);
//		if (teacherDetails.get(0).get("SCHOOL") != null)
//			school = schoolRepository.findByName((String) teacherDetails.get(0).get("SCHOOL"));
//		if (teacherDetails.get(0).get("SCHOOLS EMAIL") != null)
//			school = schoolRepository.findByEmail((String) teacherDetails.get(0).get("SCHOOLS EMAIL"));

		if (school == null)
			errors.add(String.format("School with id : %s not found ", schoolCid));
		else {
			teacherRequest.setSchoolId(school.getCid());
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
						errors.add(String.format("Grade  %s not found in records.", gradeAndSection[0]));
					else
						gradeCIds.add(grade.getCid());

				}
				teacherRequest.setGradeIds(gradeCIds);
			} else
				errors.add("No grades provided for the teacher");
		}

		if (isCoach == true) {
			String activities = (String) teacherDetails.get(0).get("ACTIVITY");
			List<String> activityCIds = new ArrayList<String>();
			String[] activityNames = null;
			if (activities != null)
				activityNames = activities.split(",");
//			List<Activity> activityList = activityRepository.findAll();
//			for (int i = 0; i < activityList.size(); i++) {
//				if (!activityNames[i].equalsIgnoreCase(activityList.get(i).getName()))
//					activityList.remove(i);
//				else
//					activityCIds.add(activityList.get(i).getCid());
//			}
			if (activityNames != null && activityNames.length > 0) {
				for (String activity : activityNames) {
					String actCid = activityRepository.findCidByNameAndActiveTrue(activity);
					if (actCid != null)
						activityCIds.add(actCid);
					else
						errors.add(String.format("Activity with name : %s does not exist.", activity));
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
		List<Teacher> teachersList = new ArrayList<>();
		List<TeacherResponse> teachersResponseList = new ArrayList<>();
		teachersList = teacherRepository.findAllBySchoolCidAndActivitiesNameAndIsCoachTrueAndActiveTrue(schoolCid,
				activityName);

		if (teachersList != null && !teachersList.isEmpty()) {
			for (Teacher teacher : teachersList)
				teachersResponseList.add(new TeacherResponse(teacher));
		}

		return teachersResponseList;
	}

	@Override
	public List<ActivityRequestResponse> findAllActivitiesByCoachId(Long id) {

		if (id == null)
			throw new ValidationException("Id can not be null");

		List<Activity> activities = new ArrayList<Activity>();
		List<ActivityRequestResponse> activityResponses = new ArrayList<ActivityRequestResponse>();

		activities = activityRepository.findAllByTeachersIdAndActiveTrue(id);
		if (activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");
		for (Activity activity : activities) {
			activityResponses.add(new ActivityRequestResponse(activity));
		}
		return activityResponses;
	}

	@Override
	public List<ActivityRequestResponse> findAllActivitiesByCoachCId(String cId) {
		if (cId == null)
			throw new ValidationException("Id can not be null");
		List<Activity> activities = new ArrayList<Activity>();
		List<ActivityRequestResponse> activityResponses = new ArrayList<ActivityRequestResponse>();

		activities = activityRepository.findAllByTeachersCidAndActiveTrue(cId);
		if (activities == null || activities.isEmpty())
			throw new ValidationException("No Activities found.");
		for (Activity activity : activities) {
			activityResponses.add(new ActivityRequestResponse(activity));
		}
		return activityResponses;
	}

	@Override
	public List<TeacherResponse> getAllTeachers(Integer pageNo, Integer pageSize) {

		Pageable paging = new PageRequest(pageNo, pageSize);

		Page<Teacher> teachers;
		List<TeacherResponse> teacherResponses = new ArrayList<>();

		teachers = teacherRepository.findAllByActiveTrue(paging);

		if (!teachers.hasContent())
			throw new ValidationException("No teachers found.");

		teachers.forEach(teacher -> {
			teacherResponses.add(new TeacherResponse(teacher));
		});

		return teacherResponses;
	}

	@Override
	public List<TeacherResponse> getAllCoaches() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		List<TeacherResponse> teacherResponses = new ArrayList<>();
		teachers = teacherRepository.findAllByIsCoachTrueAndActiveTrue();
//				findAllBySchoolCidAndIsCoachTrue();
		if (teachers == null)
			throw new ValidationException("No coaches found.");
		teachers.forEach(teacher -> {
			teacherResponses.add(new TeacherResponse(teacher));
		});
		return teacherResponses;
	}

	@Override
	public List<TeacherResponse> getAllClassTeachers() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		List<TeacherResponse> teacherResponses = new ArrayList<>();
		teachers = teacherRepository.findAllByIsClassTeacherTrueAndActiveTrue();
//				findAllBySchoolCidAndIsClassTeacherTrue();
		if (teachers == null)
			throw new ValidationException("No class teachers found.");
		teachers.forEach(teacher -> {
			teacherResponses.add(new TeacherResponse(teacher));
		});
		return teacherResponses;
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
		List<TeacherResponse> teacherResponses = new ArrayList<>();
		teachers = teacherRepository.findAllBySchoolCidAndActiveTrue(schoolCid);
		if (teachers == null)
			throw new ValidationException("No teachers found.");
		teachers.forEach(teacher -> {
			teacherResponses.add(new TeacherResponse(teacher));
		});
		return teacherResponses;
	}

	@Override
	public List<TeacherResponse> getAllClassTeachersOfSchool(String schoolCid) {
		List<Teacher> teachers = new ArrayList<Teacher>();
		List<TeacherResponse> teacherResponses = new ArrayList<>();
		teachers = teacherRepository.findAllBySchoolCidAndIsClassTeacherTrueAndActiveTrue(schoolCid);
		if (teachers == null)
			throw new ValidationException("No class teachers found.");
		teachers.forEach(teacher -> {
			teacherResponses.add(new TeacherResponse(teacher));
		});
		return teacherResponses;
	}

	@Override
	public List<TeacherResponse> getAllCoachesOfSchool(String schoolCid) {
		List<Teacher> teachers = new ArrayList<Teacher>();
		List<TeacherResponse> teacherResponses = new ArrayList<>();
		teachers = teacherRepository.findAllBySchoolCidAndIsCoachTrueAndActiveTrue(schoolCid);
//				findAllBySchoolCidAndIsCoachTrue();
		if (teachers == null)
			throw new ValidationException("No coaches found.");
		teachers.forEach(teacher -> {
			teacherResponses.add(new TeacherResponse(teacher));
		});
		return teacherResponses;
	}

	@Override
	public List<TeacherResponse> findCoachesBySchoolCidAndActivityCid(String schoolCid, String activityCid) {

		List<TeacherResponse> teacherResponses = new ArrayList<>();
		List<Teacher> teachers = teacherRepository
				.findAllBySchoolCidAndActivitiesCidAndIsCoachTrueAndActiveTrue(schoolCid, activityCid);
		if (teachers == null)
			throw new ValidationException("No coaches found.");
		teachers.forEach(teacher -> {
			teacherResponses.add(new TeacherResponse(teacher));
		});
		return teacherResponses;
	}

	@Override
	public SuccessResponse delete(String cid) {

		if (cid == null) {
			throw new ValidationException("teacher id can't be null");
		}

		Teacher teacher = teacherRepository.findByCidAndActiveTrue(cid);

		if (teacher == null || teacher.getActive() == false) {
			throw new NotFoundException(String.format("teacher having id [%s] can't exist", cid));
		}

		teacher.setActive(false);
		teacherRepository.save(teacher);

		teacher = teacherRepository.findByCidAndActiveTrue(cid);

		if (teacher != null) {
			throw new ValidationException(
					String.format("something went wrong teacher having id [%s] can't deleted", cid));
		} else {
			return new SuccessResponse(org.springframework.http.HttpStatus.OK.value(), "teacher deleted successfully");
		}
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

		List<TeacherResponse> responseList = new ArrayList<TeacherResponse>();

		for (Teacher member : managmentMembers) {

			responseList.add(new TeacherResponse(member));
		}

		return responseList;
	}

}
