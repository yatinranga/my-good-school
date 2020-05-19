package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentClubRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherActivityGradeRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FocusAreaService;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.FocusAreaRequestResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@Service
public class ActivityServiceImpl extends BaseService implements ActivityService {

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	FocusAreaRepository focusAreaRepository;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	Utils utils;
	
	@Autowired
	FocusAreaService focusAreaService;
	
	@Autowired
	StudentClubRepository studentClubRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	TeacherRepository teacherRepository;
	
	@Autowired
	TeacherActivityGradeRepository teacherActivityGradeRepository;

	@PostConstruct
	public void init() {
		List<FocusArea> focusAreaRepoList = focusAreaRepository.findAllByActiveTrue();
		List<FocusArea> focusAreaList = new ArrayList<FocusArea>();
		String[] focusAreasPd = { "Identity", "Spiritual And Aesthetic Awareness", "Decision Making", "Health",
				"Intellectual Growth" };
		String[] focusAreasSD = { "Community Skills", "Employment Skills", "Citizenship", "Environmental Awareness" };


		for (String foc : focusAreasPd) {
			Boolean flag = false;
			for (int i = 0; i < focusAreaRepoList.size(); i++) {
				if (foc.equalsIgnoreCase(focusAreaRepoList.get(i).getName())
						&& focusAreaRepoList.get(i).getPsdArea().equals(PSDArea.PersonalDevelopment))
					flag = true;
			}
			if (flag == false) {
				FocusArea focusArea = new FocusArea();
				focusArea.setName(foc);
				focusArea.setDescription(foc);
				focusArea.setPsdArea(PSDArea.PersonalDevelopment);
				focusArea.setActive(true);
				focusArea.setCid(utils.generateRandomAlphaNumString(8));
				focusAreaList.add(focusArea);
			}
		}

		for (String foc : focusAreasSD) {
			Boolean flag = false;
			for (int i = 0; i < focusAreaRepoList.size(); i++) {
				if (foc.equalsIgnoreCase(focusAreaRepoList.get(i).getName())
						&& focusAreaRepoList.get(i).getPsdArea().equals(PSDArea.SocialDevelopment))
					flag = true;
			}
			if (flag == false) {
				FocusArea focusArea = new FocusArea();
				focusArea.setName(foc);
				focusArea.setDescription(foc);
				focusArea.setPsdArea(PSDArea.SocialDevelopment);
				focusArea.setActive(true);
				focusArea.setCid(utils.generateRandomAlphaNumString(8));
				focusAreaList.add(focusArea);
			}
		}

		focusAreaList = focusAreaRepository.save(focusAreaList);

		String[] skillActivities = { "Yoga", "Literary Society Hindi", "Literary Society English", "Art And Craft",
				"Music And Dance", "Band", "Computers", "Cooking" };
		String[] sportActivities = { "Martial Arts", "Cricket", "Fencing", "Football", "Kho-Kho", "Badminton",
				"Kabaddi" };
		String[] serviceActivities = { "Social Service League", "NCC", "Scouts And Guides", "Rural Livelihood Maping",
				"Green School Club", "Eco Club" };
		String[] studyActivities = { "Reading" };

		List<Activity> activityRepoList = activityRepository.findAllByActiveTrue();
		List<Activity> activityList = new ArrayList<Activity>();

		for (String act : skillActivities) {
			Boolean flag = false;
			for (int i = 0; i < activityRepoList.size(); i++) {
				if (act.equalsIgnoreCase(activityRepoList.get(i).getName())
						&& activityRepoList.get(i).getFourS().equals(FourS.Skill))
					flag = true;
			}
			if (flag == false) {
				Activity activity = new Activity();
				activity.setName(act);
				activity.setDescription(act);
				activity.setFourS(FourS.Skill);
				activity.setActive(true);
				activity.setIsGeneral(true);
				activity.setCid(utils.generateRandomAlphaNumString(8));
				activityList.add(activity);
			}
		}

		for (String act : sportActivities) {
			Boolean flag = false;
			for (int i = 0; i < activityRepoList.size(); i++) {
				if (act.equalsIgnoreCase(activityRepoList.get(i).getName())
						&& activityRepoList.get(i).getFourS().equals(FourS.Sport))
					flag = true;
			}
			if (flag == false) {
				Activity activity = new Activity();
				activity.setName(act);
				activity.setDescription(act);
				activity.setFourS(FourS.Sport);
				activity.setActive(true);
				activity.setIsGeneral(true);
				activity.setCid(utils.generateRandomAlphaNumString(8));
				activityList.add(activity);
			}
		}

		for (String act : studyActivities) {
			Boolean flag = false;
			for (int i = 0; i < activityRepoList.size(); i++) {
				if (act.equalsIgnoreCase(activityRepoList.get(i).getName())
						&& activityRepoList.get(i).getFourS().equals(FourS.Study))
					flag = true;
			}
			if (flag == false) {
				Activity activity = new Activity();
				activity.setName(act);
				activity.setDescription(act);
				activity.setFourS(FourS.Study);
				activity.setActive(true);
				activity.setIsGeneral(true);
				activity.setCid(utils.generateRandomAlphaNumString(8));
				activityList.add(activity);
			}
		}

		for (String act : serviceActivities) {
			Boolean flag = false;
			for (int i = 0; i < activityRepoList.size(); i++) {
				if (act.equalsIgnoreCase(activityRepoList.get(i).getName())
						&& activityRepoList.get(i).getFourS().equals(FourS.Service))
					flag = true;
			}
			if (flag == false) {
				Activity activity = new Activity();
				activity.setName(act);
				activity.setDescription(act);
				activity.setFourS(FourS.Service);
				activity.setActive(true);
				activity.setIsGeneral(true);
				activity.setCid(utils.generateRandomAlphaNumString(8));
				activityList.add(activity);
			}
		}

		activityList = activityRepository.save(activityList);

		activityList = activityRepository.findAllByActiveTrue();
		focusAreaList = focusAreaRepository.findAllByActiveTrue();
		for (Activity act : activityList) {
			List<String> fAs;
			switch (act.getName()) {
			case "Yoga":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Health");
				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Literary Society Hindi":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Intellectual Growth");
				fAs.add("Employment Skills");
				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Literary Society English":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Intellectual Growth");
				fAs.add("Employment Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Art & Craft":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Spiritual & Aesthetic Awareness");
				fAs.add("Employment Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Music & Dance":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Spiritual & Aesthetic Awareness");
				fAs.add("Employment Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Band":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Spiritual & Aesthetic Awareness");
				fAs.add("Employment Skills");
				fAs.add("Community  Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Martial Arts":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Decision Making");
				fAs.add("Health");
				fAs.add("Citizenship");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Social Service League":
				fAs = new ArrayList<String>();
				fAs.add("Spiritual & Aesthetic Awareness");
				fAs.add("Environmental Awareness");
				fAs.add("Community  Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Computers":
				fAs = new ArrayList<String>();
				fAs.add("Decision Making");
				fAs.add("Intellectual Growth");
				fAs.add("Employment Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Cooking":
				fAs = new ArrayList<String>();
				fAs.add("Health");
				fAs.add("Employment Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Cricket":
				fAs = new ArrayList<String>();
				fAs.add("Health");
				fAs.add("Community Skills");
				fAs.add("Citizenship");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Fencing":
				fAs = new ArrayList<String>();
				fAs.add("Health");
				fAs.add("Community Skills");
				fAs.add("Citizenship");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Football":
				fAs = new ArrayList<String>();
				fAs.add("Health");
				fAs.add("Community Skills");
				fAs.add("Citizenship");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Kho-Kho":
				fAs = new ArrayList<String>();
				fAs.add("Health");
				fAs.add("Community Skills");
				fAs.add("Citizenship");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Badminton":
				fAs = new ArrayList<String>();
				fAs.add("Health");
				fAs.add("Community Skills");
				fAs.add("Citizenship");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Kabaddi":
				fAs = new ArrayList<String>();
				fAs.add("Health");
				fAs.add("Community Skills");
				fAs.add("Citizenship");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Reading":
				fAs = new ArrayList<String>();
				fAs.add("Intellectual Growth");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "NCC":
				fAs = new ArrayList<String>();
				fAs.add("Community Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Scouts & Guides":
				fAs = new ArrayList<String>();
				fAs.add("Environmental Awareness");
				fAs.add("Community Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Rural Livelihood Maping":
				fAs = new ArrayList<String>();
				fAs.add("Employment Skills");
				fAs.add("Community Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Green School Club":
				fAs = new ArrayList<String>();
				fAs.add("Environmental Awareness");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Eco Club":
				fAs = new ArrayList<String>();
				fAs.add("Environmental Awareness");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;

			default:
				System.out.println(String.format("Invalid activity : %s", act.getName()));

			}
		}
		
		activityList = activityRepository.save(activityList);

		School school = schoolRepository.findByNameAndActiveTrue("my good school");
		if(school != null) {
			activityList.forEach(act -> {
				if(!act.getSchools().contains(school))
				    act.getSchools().add(school);
				});
			school.setActivities(activityList);
			schoolRepository.save(school);
		}
		
		activityList = activityRepository.findAll();
		activityList.stream().forEach(a -> {if(a.getClubOrSociety() == null ) a.setFourS(a.getFourS());});
		activityList = activityRepository.save(activityList);

	}

	private Activity activityFocusAreaMappingUtility(Activity act, List<String> fAs, List<FocusArea> focusAreaList) {
		List<FocusArea> focusAreas = act.getFocusAreas();
		for (String f : fAs) {
			if (focusAreas.stream().filter(focArea -> focArea.getName().equalsIgnoreCase(f)).count() <= 0) {
				focusAreaList.stream().forEach(farea -> {
					if (farea.getName().equalsIgnoreCase(f)) {
						List<Activity> tempActivities = farea.getActivities();
						tempActivities.add(act);
						farea.setActivities(tempActivities);
						focusAreas.add(farea);
					}
				});
			}
		}
		act.setFocusAreas(focusAreas);
		return act;
	}

	@Override
	public ActivityRequestResponse saveActivity(ActivityRequestResponse request) {
		if (request == null)
			throw new ValidationException("Request cannot be null");
		if (request.getName() == null)
			throw new ValidationException("Activity name cannot be null");
		if (request.getFourS() == null)
			throw new ValidationException("Four S cannot be null.");
//		if (request.getFocusAreaIds() == null)
//			throw new ValidationException("Focus area ids cannot be null.");
		Activity activity = activityRepository.findByNameAndActiveTrue(request.getName());
		if (activity != null)
			throw new ValidationException("Activity already exist.");
//		List<FocusArea> focusAreaList = focusAreaRepository.findAll();
//		if (focusAreaList == null)
//			throw new ValidationException("No Focus Areas found.");

		List<FocusArea> focusAreas = new ArrayList<>();
//		for(int i = 0 ; i < request.getFocusAreaIds().size() ; i++) {
//			if(!focusAreaRepository.existsByCidAndActiveTrue(request.getFocusAreaIds().get(i)))
//				throw new ValidationException(String.format("Focus Area with id (%s) does not exist.", request.getFocusAreaIds().get(i)));
//			else
//				focusAreas.add(focusAreaRepository.findByCidAndActiveTrue(request.getFocusAreaIds().get(i)));
//		}
		
		activity = request.toEntity();
		activity.setCid(utils.generateRandomAlphaNumString(8));
		activity.setActive(true);
		
		if (request.getSchoolIds() != null && !request.getSchoolIds().isEmpty()) {
			List<School> schools = new ArrayList<School>();
			
			for(String schoolId : request.getSchoolIds()) {
				if(!schoolRepository.existsByCidAndActiveTrue(schoolId))
					throw new ValidationException(String.format("School with id (%s) not found", schoolId));
				School school = schoolRepository.findByCidAndActiveTrue(schoolId);
				List<Activity> activities = school.getActivities();
				activities.add(activity);
				school.setActivities(activities);
				schools.add(school);
			}
			
			activity.setSchools(schools);
			
		}else {
			activity.setIsGeneral(true);
		}
//		activity = activityRepository.save(activity);
		
		if(request.getFocusAreaRequests() != null && !request.getFocusAreaRequests().isEmpty()) {
			addOrCreateFocusAreas(request.getFocusAreaRequests(), focusAreas, activity);
		}
		
		activity.setFocusAreas(focusAreas);
		
		

		activity = activityRepository.save(activity);
		if (activity == null)
			throw new RuntimeException("Something went wrong activity not created.");

		return new ActivityRequestResponse(activity);

	}
	
	private void addOrCreateFocusAreas(List<FocusAreaRequestResponse> focusAreaRequests , List<FocusArea> focusAreas , Activity activity) {
		for(FocusAreaRequestResponse  focusAreaReq : focusAreaRequests) {
			FocusArea focusArea;
			if(focusAreaReq.getId() == null) {
				focusArea = focusAreaRepository.findByNameAndPsdArea(focusAreaReq.getName(),focusAreaReq.toEntity().getPsdArea());
				if(focusArea == null) {
					focusArea = focusAreaReq.toEntity();
					focusArea.setCid(utils.generateRandomAlphaNumString(8));
					focusArea.setActive(true);
					
					focusArea = focusAreaRepository.save(focusArea);
					
					List<Activity> activities = new ArrayList<Activity>();
					activities.add(activity);
					focusArea.setActivities(activities);
					focusAreas.add(focusArea);
				}else {/*Indicates that FocusArea with this name and psdArea exists but may be inactive*/
					if (!focusArea.getActive())
						focusArea.setActive(true);
					
					List<Activity> activities  = focusArea.getActivities();
					if(activities == null)
						activities = new ArrayList<Activity>();
					
					activities.add(activity);
					focusArea.setActivities(activities);
//					focusArea = focusAreaRepository.save(focusArea);
					focusAreas.add(focusArea);
				}
			}else {/*Outer else loop to indicate that id of focusArea is not null and its not a new focusArea*/
				focusArea = focusAreaRepository.findByCid(focusAreaReq.getId());
				if(focusArea == null)
					throw new ValidationException(String.format("Focus Area having id (%s) not found.", focusAreaReq.getId()));
				
				if (!focusArea.getActive())
					focusArea.setActive(true);

				List<Activity> activities  = focusArea.getActivities();
				if(activities == null)
					activities = new ArrayList<Activity>();
				activities.add(activity);
				focusArea.setActivities(activities);
//				focusArea = focusAreaRepository.save(focusArea);
				focusAreas.add(focusArea);
				
			}
		}
	}
	@Override
	public List<ActivityRequestResponse> getAllOfferedActivities(Integer pageNo, Integer pageSize) {

		Pageable paging = new PageRequest(pageNo, pageSize);

		Page<Activity> activities = activityRepository.findAllByActiveTrue(paging);
		if (activities == null)
			throw new ValidationException("No activities found.");
		return activities.getContent().stream().map(ActivityRequestResponse:: new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityRequestResponse> getAllOfferedActivitiesBySchool(String schoolCid) {
		List<Activity> activities;
		
		if (schoolCid == null)
			activities = activityRepository.findAllByIsGeneralTrueAndActiveTrue();
		else
			activities = activityRepository.findAllBySchoolsCidAndActiveTrue(schoolCid);
		

		if (activities == null || activities.isEmpty())
			throw new ValidationException("No general or school specific activities found.");
		
		return activities.stream().map(ActivityRequestResponse:: new).collect(Collectors.toList());
	}
	
	@Override
	public List<ActivityRequestResponse> getAllClubsOfStudent(){
		List<Activity> activities;
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("Login as student to see your activities.");
		Long studentId =  studentRepository.findIdByUserIdAndActiveTrue(userId);
		if(studentId == null)
			throw new ValidationException("User not logged in as student.");
		if(!studentClubRepository.existsByStudentIdAndMembershipStatusAndActiveTrue(studentId, ApprovalStatus.VERIFIED))
			throw new ValidationException("Student not member of any Clubs.");
		
		activities = studentClubRepository.findActivityByStudentIdAndMembershipStatusAndActiveTrue(studentId, ApprovalStatus.VERIFIED);
		
		return activities.stream().map(ActivityRequestResponse:: new).collect(Collectors.toList());
	}
	
	@Override
	public List<ActivityRequestResponse> getAllClubsOfTeacher(){
		List<Activity> activities;
		Long userId = getUserId();
		if(userId == null)
			throw new ValidationException("Login as teacher to see your activities.");
		Long teacherId =  teacherRepository.getIdByUserIdAndActiveTrue(userId);
		if(teacherId == null)
			throw new ValidationException("User not logged in as teacher.");
		if(!teacherActivityGradeRepository.existsByTeacherIdAndActiveTrue(teacherId))
			throw new ValidationException("Teacher not running any clubs or societies.");
		
		activities = teacherActivityGradeRepository.findAllActivityByTeacherIdAndActiveTrue(teacherId);
		
		return activities.stream().map(ActivityRequestResponse:: new).collect(Collectors.toList());
	}
	
	@Override
	public List<ActivityRequestResponse> getAllGeneralActivities() {

		List<Activity> generalActivities = activityRepository.findAllByIsGeneralTrueAndActiveTrue();

		if (generalActivities == null || generalActivities.isEmpty()) {
			throw new NotFoundException("no general activities found");
		}
	
		return generalActivities.stream().map(ActivityRequestResponse::new).collect(Collectors.toList());
	}
	
	@Override
	public Map<String , Object> getAvailableFilters(){
		Map<String, Object> response = new HashMap<String, Object>();
		
		Set<String> fourS = new HashSet<String>();
		for(FourS fours : FourS.values()) {
			fourS.add(fours.toString());
		}
		response.put("Four S", fourS);
		
		Set<String> psdAreas = new HashSet<String>();
		for(PSDArea psd : PSDArea.values()) {
			psdAreas.add(psd.getPsdArea());
		}
		response.put("PSD Areas", psdAreas);
		
		Set<String> focusAreas = new HashSet<String>();
		focusAreaService.getAllFocusAreas().forEach(fa -> {focusAreas.add(fa.getName());});
		response.put("Focus Areas", focusAreas);
		
		return response;
	}

	@Override
	public SuccessResponse deleteActivityByCid(String cid) {
		Activity activity = activityRepository.getOneByCidAndActiveTrue(cid);
		if (activity == null)
			throw new ValidationException(String.format("No activity found with id : %s ", cid));
		int i = activityRepository.updateActivitySetActiveByCid(false, cid);
		if (i == 0)
			throw new RuntimeException("Something went wrong Activity not deleted.");
		return new SuccessResponse(200, "Activity successfuly deleted.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> uploadActivityFromExcel(MultipartFile file, String schoolCid) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<ActivityRequestResponse> activityResponseList = new ArrayList<>();
		List<Map<String, Object>> activityRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook gradesheet = new XSSFWorkbook(file.getInputStream());
			activityRecords = findSheetRowValues(gradesheet, "ACTIVITY", errors);
			errors = (List<String>) activityRecords.get(activityRecords.size() - 1).get("errors");
			for (int i = 0; i < activityRecords.size(); i++) {
				List<Map<String, Object>> tempactivityRecords = new ArrayList<Map<String, Object>>();
				tempactivityRecords.add(activityRecords.get(i));
				activityResponseList.add(saveActivity(validateActivityRequest(tempactivityRecords, errors, schoolCid)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("ActivityResponseList", activityResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	private ActivityRequestResponse validateActivityRequest(List<Map<String, Object>> activityDetails,
			List<String> errors, String schoolCids) {
		if (activityDetails == null || activityDetails.isEmpty())
			errors.add("Activity details not found");
		ActivityRequestResponse activityRequest = new ActivityRequestResponse();
		activityRequest.setName((String) activityDetails.get(0).get("NAME"));
		activityRequest.setDescription((String) activityDetails.get(0).get("DESCRIPTION"));
		activityRequest.setFourS((String) activityDetails.get(0).get("FOUR S"));
		String focusAreas = (String) activityDetails.get(0).get("FOCUS AREAS");

		List<String> focusAreaCIds = new ArrayList<String>();
		String[] focusAreaNames = focusAreas.split(",");

		if (focusAreaNames != null && focusAreaNames.length > 0) {
			List<FocusArea> focusAreaList = focusAreaRepository.findAll();
			for (int i = 0; i < focusAreaNames.length; i++) {
				for (int j = 0; j < focusAreaList.size(); j++) {
					if (focusAreaNames[i].equalsIgnoreCase(focusAreaList.get(j).getName())) {
						focusAreaCIds.add(focusAreaList.get(j).getCid());
						break;
					}
				}

			}
			activityRequest.setFocusAreaIds(focusAreaCIds);
		}
		// logic to fetch comma separated schoolCids and set it to activityRequest

		if (schoolCids != null) {
			List<String> activityReqSchoolCids = new ArrayList<String>();
			String[] schCids = schoolCids.split(",");
			if (schCids != null && schCids.length > 0) {
				for (int i = 0; i < schCids.length; i++) {
					activityReqSchoolCids.add(schCids[i]);
				}
			}
			activityRequest.setSchoolIds(activityReqSchoolCids);
		}

		return activityRequest;
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

		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(sheetName);
		return fetchRowValues(columnTypes, sheet, errors, sheetName);

	}

}
