package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.SequenceGeneratorService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.store.FileStore;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.GradeRequest;
import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@Service
public class SchoolServiceImpl extends BaseService implements SchoolService {

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	UserService userService;

	@Autowired
	Utils utils;

	@Autowired
	FileStore filestore;

	@Autowired
	SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	FileService fileService;

	@Autowired
	ActivityService activityService;

	@Value("${spring.mail.username}")
	private String emailUsername;

	@PostConstruct
	public void init() {

		Role role = roleRepository.getOneByName("School");
		if (role == null) {
			role = new Role();
			role.setCid(utils.generateRandomAlphaNumString(8));
			role.setName("School");
			role.setActive(true);
			roleRepository.save(role);
		}
//		 Logic for authorities missing

		School school = schoolRepository.findByNameAndActiveTrue("my good school");
		if (school == null) {
			school = new School();
			school.setName("my good school");
			Long schoolsequence = sequenceGeneratorService.findSequenceByUserType(UserType.School);
			sequenceGeneratorService.updateSequenceByUserType(schoolsequence, UserType.School);
			school.setEmail("mygoodschool@gmail.com");
			school.setCid(utils.generateRandomAlphaNumString(8));
			school.setActive(true);

		} else {
			if (!school.getActive())
				school.setActive(true);
		}

		if (userRepository.findByUserName(school.getUsername()) == null) {
			User user = new User();
			user.setRoleForUser(role);
			user.setUserName(school.getUsername());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode("root");
			user.setPassword(encodedPassword);
			user.setCid(utils.generateRandomAlphaNumString(8));
			user.setActive(true);
			user.setMobileNo(utils.generateRandomNumString(10));
			user.setEmail(school.getEmail());
			userRepository.save(user);
			school.setUser(user);
		}

		schoolRepository.save(school);
	}

	@Override
	public SchoolResponse save(SchoolRequest request) {

		if (request == null)
			throw new ValidationException("Request can not be null.");

		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");

		if (schoolRepository.countByEmailAndActiveTrue(request.getEmail()) > 0)
			throw new ValidationException(String.format("Email [%s] already exists", request.getEmail()));

		if (request.getName() == null)
			throw new ValidationException("School name can not be null");

		School school = request.toEntity();

		Long schoolsequence = sequenceGeneratorService.findSequenceByUserType(UserType.School);

		school.setUsername(String.format("SCH%08d", schoolsequence));
		school.setCid(utils.generateRandomAlphaNumString(8));
		school.setActive(true);

		User user = userService.createSchoolUser(school);

		if (StringUtils.isEmpty(user))
			throw new ValidationException("User not created successfully");
		school.setUser(user);
		user.setSchool(school);

		school = schoolRepository.save(school);

		Activity activity;
		List<Activity> allActivities = new ArrayList<Activity>();

		if (request.getGeneralActivities() != null && !request.getGeneralActivities().isEmpty()) {

			for (String generalActivity : request.getGeneralActivities()) {

				activity = activityRepository.getOneByCid(generalActivity);

				if (activity == null)
					throw new NotFoundException(
							String.format("General Activity having id [%s] didn't exist", generalActivity));

				if (activity.getSchools() == null || activity.getSchools().isEmpty()) {
					List<School> schoolList = new ArrayList<School>();
					schoolList.add(school);
					activity.setSchools(schoolList);
					allActivities.add(activity);
				} else {
					activity.getSchools().add(school);
					allActivities.add(activity);
				}
			}

		}

		if (request.getNewActivities() != null && !request.getNewActivities().isEmpty()) {

			for (ActivityRequestResponse newActivity : request.getNewActivities()) {

				activity = activityRepository.getOneByNameAndActiveTrue(newActivity.getName());
				if (activity != null) {

					if (activity.getSchools() == null || activity.getSchools().isEmpty()) {
						List<School> schoolList = new ArrayList<School>();
						schoolList.add(school);
						activity.setSchools(schoolList);
						// activity.setCid(utils.generateRandomAlphaNumString(8));
						allActivities.add(activity);
					} else {
						activity.getSchools().add(school);
						allActivities.add(activity);
					}

				} else {

					activity = newActivity.toEntity();

					List<School> sl = new ArrayList<School>();
					sl.add(school);
					activity.setSchools(sl);
					activity.setCid(utils.generateRandomAlphaNumString(8));
					activity.setActive(true);
					allActivities.add(activity);

				}

			}
		}

		school.setActivities(allActivities);

		if (request.getLogo() != null) {
//			String fileExtn = request.getLogo().getOriginalFilename().split("\\.")[1];
			String filename = UUID.randomUUID().toString() + "."
					+ fileService.getFileExtension(request.getLogo().getOriginalFilename());
			try {
				String logoUrl = filestore.store("schoolLogo", filename, request.getLogo().getBytes());
				school.setLogo(logoUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		school = schoolRepository.save(school);

		if (school == null)
			throw new RuntimeException("Something went wrong school not saved.");

		// sending login credentials

		Boolean emailFlag = false;

		if (user.getEmail() != null)
			emailFlag = userService.sendLoginCredentialsBySMTP(userService.usernamePasswordSendContentBuilder(
					user.getUsername(), user.getRawPassword(), emailUsername, user.getEmail()));

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("Teacher", new SchoolResponse(school));
		String emailMessage = emailFlag ? String.format("Email sent successfully to (%s)", user.getEmail())
				: String.format("Email not sent successfully to (%s) , email address might be wrong.", user.getEmail());
		int emailStatusCode = emailFlag ? 200 : 400;
		response.put("MailResponse", new SuccessResponse(emailStatusCode, emailMessage));
//		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		return new SchoolResponse(school);

	}

	@Override
	public SchoolResponse update(SchoolRequest request, String cid) {

		if (cid == null) {
			throw new ValidationException("School id can't be null");
		}

		School school = schoolRepository.findByCidAndActiveTrue(cid);

		if (school == null)
			throw new NotFoundException(String.format("school havind id [%s] didn't exist", cid));

		school = request.toEntity(school);

		if (request.getGradeRequests() != null && !request.getGradeRequests().isEmpty()) {
			List<GradeRequest> gradeRequests = request.getGradeRequests();
			List<Grade> previousGrades = new ArrayList<Grade>();
			List<Grade> gradesToDelete = new ArrayList<Grade>();
			previousGrades = school.getGrades();
			if (previousGrades.isEmpty()) {
				updateGradesOfSchool(gradeRequests, previousGrades, school);
			} else {
				for (int i = 0; i < previousGrades.size(); i++) {
					String grdCid = previousGrades.get(i).getCid();
					GradeRequest gradeRequest = gradeRequests.stream()
							.filter(grd -> grd.getId() != null && grd.getId().equals(grdCid)).findAny().orElse(null);
					if (gradeRequest != null) {
						gradeRequests.remove(gradeRequest);
					} else {
						List<School> schools = previousGrades.get(i).getSchools();
						if (!schools.isEmpty() && !schools.isEmpty()) {
							schools.remove(school);
							previousGrades.get(i).setSchools(schools);
							gradesToDelete.add(previousGrades.get(i));
						}
						previousGrades.remove(i--);
					}
				}

				if (gradeRequests != null && !gradeRequests.isEmpty())
					updateGradesOfSchool(gradeRequests, previousGrades, school);

			}

			school.setGrades(previousGrades);
			gradeRepository.save(gradesToDelete);
		}

		if (request.getLogo() != null) {
//			String fileExtn = request.getLogo().getOriginalFilename().split("\\.")[1];
			String filename = UUID.randomUUID().toString() + "."
					+ fileService.getFileExtension(request.getLogo().getOriginalFilename());
			try {
				String logoUrl = filestore.store("schoolLogo", filename, request.getLogo().getBytes());
				school.setLogo(logoUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (request.getActivities() != null && !request.getActivities().isEmpty()) {
			List<ActivityRequestResponse> activityRequests = request.getActivities();
			List<Activity> previousActivities = new ArrayList<Activity>();
			List<Activity> activitiesToDelete = new ArrayList<Activity>();
			previousActivities = school.getActivities();
			if (previousActivities.isEmpty()) {
				updateActivitiesOfSchool(activityRequests, previousActivities, school);
			} else {
				for (int i = 0; i < previousActivities.size(); i++) {
					String actCid = previousActivities.get(i).getCid();
					ActivityRequestResponse activityRequest = activityRequests.stream()
							.filter(act -> act.getId() != null && act.getId().equals(actCid)).findAny().orElse(null);
					if (activityRequest != null) {
						activityRequests.remove(activityRequest);
					} else {
						List<School> schools = previousActivities.get(i).getSchools();
						if (!schools.isEmpty() && !schools.isEmpty()) {
							schools.remove(school);
							previousActivities.get(i).setSchools(schools);
							activitiesToDelete.add(previousActivities.get(i));
						}
						previousActivities.remove(i--);
					}
				}

				if (activityRequests != null && !activityRequests.isEmpty())
					updateActivitiesOfSchool(activityRequests, previousActivities, school);

			}

			school.setActivities(previousActivities);
			if(!activitiesToDelete.isEmpty())
			   activityRepository.save(activitiesToDelete);
		}

		school = schoolRepository.save(school);

		return new SchoolResponse(school);
	}

	private void updateActivitiesOfSchool(List<ActivityRequestResponse> activityRequests,
			List<Activity> previousActivities, School school) {

		for (ActivityRequestResponse actReq : activityRequests) {
			if (actReq.getId() != null) {
				Activity activity = activityRepository.getOneByCid(actReq.getId());
				if (activity == null)
					throw new ValidationException(String.format("Activity having id (%s) not found.", actReq.getId()));
				if (!activity.getActive())
					activity.setActive(true);
				List<School> schools = activity.getSchools();
				schools.add(school);
				activity.setSchools(schools);
				previousActivities.add(activity);

			} else {
				List<String> schoolCids = actReq.getSchoolIds();
				if(schoolCids == null)
					schoolCids = new ArrayList<String>();
				schoolCids.add(school.getCid());
				actReq.setSchoolIds(schoolCids);
				ActivityRequestResponse actResponse = activityService.saveActivity(actReq);
				if (actResponse == null)
					throw new RuntimeException(String
							.format("Something went wrong activity with name (%s) not created.", actReq.getName()));
				previousActivities.add(activityRepository.getOneByCidAndActiveTrue(actResponse.getId()));
			}
		}

	}

	private void updateGradesOfSchool(List<GradeRequest> gradeRequests, List<Grade> previousGrades, School school) {
		for (GradeRequest gradeReq : gradeRequests) {
			if (gradeReq.getId() != null) {
				if (!gradeRepository.existsByCidAndActiveTrue(gradeReq.getId()))
					throw new ValidationException(
							String.format("Grade with id (%s) does not exist.", gradeReq.getId()));
				Grade grade = gradeRepository.findByCidAndActiveTrue(gradeReq.getId());
				List<School> schools = grade.getSchools();
				schools.add(school);
				grade.setSchools(schools);
				previousGrades.add(grade);

			} else {
				Grade grade = gradeReq.toEntity();
				List<School> schools = new ArrayList<School>();
				schools.add(school);
				grade.setSchools(schools);
				grade.setCid(utils.generateRandomAlphaNumString(8));
				grade.setActive(true);
				grade = gradeRepository.save(grade);
				previousGrades.add(grade);
			}
		}
	}

	@Override
	public SchoolResponse findById(Long id) {
		if (id == null)
			throw new ValidationException("id cannot be null.");
		School school = schoolRepository.findById(id);
		if (school == null)
			throw new ValidationException("School not found.");

		return new SchoolResponse(school);
	}

	@Override
	public SchoolResponse findByCid(String cid) {
		if (cid == null)
			throw new ValidationException("id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(cid);
		if (school == null)
			throw new ValidationException("School not found.");

		return new SchoolResponse(school);
	}

	@Override
	public List<SchoolResponse> getAllSchools() {
		List<School> schoolList = schoolRepository.findAll();
		List<SchoolResponse> schoolResponses = new ArrayList<SchoolResponse>();
		if (schoolList == null || schoolList.isEmpty())
			throw new ValidationException("No schools found.");
		schoolList.forEach(s -> {
			schoolResponses.add(new SchoolResponse(s));
		});
		return schoolResponses;
	}

	@Override
	public SuccessResponse delete(String cid) {
		if (cid == null)
			throw new ValidationException("cid cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(cid);
		if (school == null)
			throw new ValidationException(String.format("School with id : %s not found", cid));
		school.setActive(false);
		school = schoolRepository.save(school);
		if (school == null)
			throw new RuntimeException("Something went wrong school not deleted successfully.");
		return new SuccessResponse(200, String.format("School with id : %s deleted successfully", cid));
	}

	@Override
	public ResponseEntity<?> uploadSchoolsFromExcel(MultipartFile file) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<SchoolResponse> schoolResponseList = new ArrayList<>();
		List<Map<String, Object>> schoolRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook studentsSheet = new XSSFWorkbook(file.getInputStream());
			schoolRecords = findSheetRowValues(studentsSheet, "SCHOOL", errors);
//			errors = (List<String>) schoolRecords.get(schoolRecords.size() - 1).get("errors");
			for (int i = 0; i < schoolRecords.size(); i++) {
				List<Map<String, Object>> tempSchoolRecords = new ArrayList<Map<String, Object>>();
				tempSchoolRecords.add(schoolRecords.get(i));
				schoolResponseList.add(save(validateSchoolRequest(tempSchoolRecords, errors)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("SchoolResponseList", schoolResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	private SchoolRequest validateSchoolRequest(List<Map<String, Object>> schoolDetails, List<String> errors) {
		if (schoolDetails == null || schoolDetails.isEmpty()) {
			errors.add("School details not found");
		}
		SchoolRequest schoolRequest = new SchoolRequest();
		if (schoolDetails.get(0).get("NAME") != null) {
			schoolRequest.setName((String) schoolDetails.get(0).get("NAME"));
		}
//		if(schoolDetails.get(0).get("USERNAME")!=null)
//		   schoolRequest.setUsername((String) schoolDetails.get(0).get("USERNAME"));

		if (schoolDetails.get(0).get("EMAIL") != null)
			schoolRequest.setEmail((String) schoolDetails.get(0).get("EMAIL"));

		schoolRequest.setContactNumber((String) schoolDetails.get(0).get("CONTACT NUMBER"));

		schoolRequest.setAddress((String) schoolDetails.get(0).get("ADDRESS"));

		return schoolRequest;
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
						if (headers.get(j).equalsIgnoreCase("NAME") || headers.get(j).equalsIgnoreCase("EMAIL"))
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

}
