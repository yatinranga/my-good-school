package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.entity.activity.TeacherActivityGrade;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentClubRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherActivityGradeRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FocusAreaService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.FocusAreaRequestResponse;
import com.nxtlife.mgs.view.GradeResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@Service
public class ActivityServiceImpl extends BaseService implements ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private FocusAreaRepository focusAreaRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private FocusAreaService focusAreaService;

	@Autowired
	private StudentClubRepository studentClubRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private TeacherActivityGradeRepository teacherActivityGradeRepository;
	
	@Autowired
	GradeRepository gradeRepository;
	
	@Autowired
	ActivityPerformedRepository activityPerformedRepository;

	@PostConstruct
	public void init() {
		List<FocusArea> focusAreaRepoList = focusAreaRepository.findAllDistinctByActiveTrue();
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
				focusArea.setCid(Utils.generateRandomAlphaNumString(8));
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
				focusArea.setCid(Utils.generateRandomAlphaNumString(8));
				focusAreaList.add(focusArea);
			}
		}

		focusAreaList = focusAreaRepository.saveAll(focusAreaList);

		String[] skillActivities = { "Yoga","Art And Craft",
				"Music And Dance", "Band", "Computers", "Cooking" };
		String[] sportActivities = { "Martial Arts", "Cricket", "Fencing", "Football", "Kho-Kho", "Badminton",
				"Kabaddi" };
		String[] serviceActivities = { "Social Service League", "NCC", "Scouts And Guides", "Rural Livelihood Maping",
				"Green School Club", "Eco Club" };
		String[] studyActivities = {"Literary Society Hindi", "Literary Society English", "Reading" };

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
				activity.setCid(Utils.generateRandomAlphaNumString(8));
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
				activity.setCid(Utils.generateRandomAlphaNumString(8));
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
				activity.setCid(Utils.generateRandomAlphaNumString(8));
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
				activity.setCid(Utils.generateRandomAlphaNumString(8));
				activityList.add(activity);
			}
		}

		activityList = activityRepository.saveAll(activityList);

		activityList = activityRepository.findAllByActiveTrue();
		focusAreaList = focusAreaRepository.findAllDistinctByActiveTrue();
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
			case "Art And Craft":
				fAs = new ArrayList<String>();
				fAs.add("Identity");
				fAs.add("Spiritual & Aesthetic Awareness");
				fAs.add("Employment Skills");

				act = activityFocusAreaMappingUtility(act, fAs, focusAreaList);
				break;
			case "Music And Dance":
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
			case "Scouts And Guides":
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

		activityList = activityRepository.saveAll(activityList);

		School school = schoolRepository.findByNameAndActiveTrue("my good school");
		if (school != null) {
			activityList.forEach(act -> {
				if (!act.getSchools().contains(school))
					act.getSchools().add(school);
			});
			school.setActivities(activityList);
			schoolRepository.save(school);
		}

		activityList = activityRepository.findAll();
		activityList.stream().forEach(a -> {
			if (a.getClubOrSociety() == null)
				a.setFourS(a.getFourS());
		});
		activityList = activityRepository.saveAll(activityList);

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
	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_CREATE)
	public ActivityRequestResponse saveActivity(ActivityRequestResponse request) {
		if (request == null)
			throw new ValidationException("Request cannot be null");
		if (request.getName() == null && request.getId() == null)
			throw new ValidationException("Activity name and id cannot be null simultaneously , provide id when updating activity.");
		
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")))
			request.setSchoolIds(new ArrayList<String>(Arrays.asList(getUser().getSchool().getCid())));
		if(request.getSchoolIds() == null || request.getSchoolIds().isEmpty())
			throw new ValidationException("school ids cannot be null or empty.");
		// if (request.getFocusAreaIds() == null)
		// throw new ValidationException("Focus area ids cannot be null.");
		Activity activity ;
		if(request.getId() != null) {
			activity = activityRepository.findByCid(request.getId());
		}else {
			activity =	activityRepository.findByName(request.getName());
		}
		Boolean activityPresent = false;
		if (activity != null ) {
			if(!activity.getActive()) {
				activity.setActive(true);
//				activity.setSchools(new ArrayList<School>());
				List<School> schools = activity.getSchools();
				final Long id = activity.getId();
				schools.stream().forEach(s-> s.getActivities().removeIf(a -> a.getId().equals(id)));
				schools = schoolRepository.saveAll(schools);
			}
			activityPresent = true;
//			List<String> reqSchoolIds = new ArrayList<>(request.getSchoolIds());
//			reqSchoolIds.removeAll(activity.getSchools().stream().map(s -> s.getCid()).collect(Collectors.toList()));
//			if(reqSchoolIds.isEmpty())
//				throw new ValidationException(String.format("Activity already exists in schools (%s).",request.getSchoolIds()));
//			activity.getSchools().removeIf(s -> !request.getSchoolIds().contains(s.getCid()));
		}
			
		// List<FocusArea> focusAreaList = focusAreaRepository.findAll();
		// if (focusAreaList == null)
		// throw new ValidationException("No Focus Areas found.");
		// for(int i = 0 ; i < request.getFocusAreaIds().size() ; i++) {
		// if(!focusAreaRepository.existsByCidAndActiveTrue(request.getFocusAreaIds().get(i)))
		// throw new ValidationException(String.format("Focus Area with id (%s)
		// does not exist.", request.getFocusAreaIds().get(i)));
		// else
		// focusAreas.add(focusAreaRepository.findByCidAndActiveTrue(request.getFocusAreaIds().get(i)));
		// }

		List<FocusArea> focusAreas = new ArrayList<>();
		if(!activityPresent) {
			if (request.getFourS() == null)
				throw new ValidationException("Four S cannot be null.");
			activity = request.toEntity();
			activity.setCid(Utils.generateRandomAlphaNumString(8));
			activity.setActive(true);
			
			if (request.getFocusAreaRequests() != null && !request.getFocusAreaRequests().isEmpty()) {
				addOrCreateFocusAreas(request.getFocusAreaRequests(), focusAreas, activity);
			}

			activity.setFocusAreas(focusAreas);

		}else {
			if(request.getName() != null) {
				
				if(activityPerformedRepository.existsByActivityCidAndActive(activity.getCid(), true))
					throw new ValidationException(String.format("Activity with id (%s) has already been performed by some students , so it can't be renamed contact your service provider for that.",activity.getCid()));
				
				if(activityRepository.existsByNameAndCidNot(request.getName() , activity.getCid()))
					throw new ValidationException(String.format("Another activity with this name (%s) already exists.",request.getName()));
			}
				
			activity = request.toEntity(activity);
			activity.setActive(true);
			
			if(request.getFocusAreaRequests() != null && !request.getFocusAreaRequests().isEmpty()) {
				List<FocusArea> toDeleteList = activity.getFocusAreas();
				Set<String> reqFocAreaIds = request.getFocusAreaRequests().stream().map(f -> f.getId()).collect(Collectors.toSet());
				toDeleteList.stream().forEach(f -> f.getActivities().removeIf(a -> !reqFocAreaIds.contains(a.getCid())));
				toDeleteList = focusAreaRepository.saveAll(toDeleteList);
				
				addOrCreateFocusAreas(request.getFocusAreaRequests(), focusAreas, activity);
			}
			activity.setFocusAreas(focusAreas);
		}
		

		List<School> schools = new ArrayList<School>();
		if (request.getSchoolIds() != null && !request.getSchoolIds().isEmpty()) {
			List<School> toDeleteList = activity.getSchools();

			for (int i =0 ; i< request.getSchoolIds().size() ; i++) {
				String schoolId = request.getSchoolIds().get(i);
				if (!schoolRepository.existsByCidAndActiveTrue(schoolId))
					throw new ValidationException(String.format("School with id (%s) not found", schoolId));
				School preExist = null ;
				if(toDeleteList != null)
				    preExist = toDeleteList.stream().distinct().filter(s -> s.getCid().equals(schoolId)).findAny().orElse(null);
				if(preExist != null) {
					schools.add(preExist);
					toDeleteList.remove(preExist);
					request.getSchoolIds().remove(i--);
//					i = i != 0 ?i-1 :i ;
				}else {
					School school = schoolRepository.findByCidAndActiveTrue(schoolId);
					List<Activity> activities = school.getActivities();
					activities.add(activity);
					school.setActivities(activities);
					schools.add(school);
				}
			}
			
			if(toDeleteList != null && !toDeleteList.isEmpty()) {
				final Long id = activity.getId();
				toDeleteList.stream().forEach(sc -> sc.getActivities().removeIf(a -> a.getId().equals(id)));
				toDeleteList = schoolRepository.saveAll(toDeleteList);
			}

		} 
		activity.setSchools(schools);
		// activity = activityRepository.save(activity);

		
		activity = activityRepository.save(activity);
		if (activity == null)
			throw new RuntimeException("Something went wrong activity not created.");

		return new ActivityRequestResponse(activity);

	}

	private void addOrCreateFocusAreas(List<FocusAreaRequestResponse> focusAreaRequests, List<FocusArea> focusAreas,
			Activity activity) {
		for (FocusAreaRequestResponse focusAreaReq : focusAreaRequests) {
			FocusArea focusArea;
			if (focusAreaReq.getId() == null) {
				focusArea = focusAreaRepository.findByNameAndPsdArea(focusAreaReq.getName(),
						focusAreaReq.toEntity().getPsdArea());
				if (focusArea == null) {
					focusArea = focusAreaReq.toEntity();
					focusArea.setCid(Utils.generateRandomAlphaNumString(8));
					focusArea.setActive(true);

					focusArea = focusAreaRepository.save(focusArea);

					List<Activity> activities = new ArrayList<Activity>();
					activities.add(activity);
					focusArea.setActivities(activities);
					focusAreas.add(focusArea);
				} else {/*
						 * Indicates that FocusArea with this name and psdArea
						 * exists but may be inactive
						 */
					if (!focusArea.getActive())
						focusArea.setActive(true);

					List<Activity> activities = focusArea.getActivities();
					if (activities == null)
						activities = new ArrayList<Activity>();
					
					if(!activities.stream().anyMatch(act -> act.getCid().equals(activity.getCid()))) {
						activities.add(activity);
						focusArea.setActivities(activities);
					}
					// focusArea = focusAreaRepository.save(focusArea);
					focusAreas.add(focusArea);
				}
			} else {/*
					 * Outer else loop to indicate that id of focusArea is not
					 * null and its not a new focusArea
					 */
				focusArea = focusAreaRepository.findByCid(focusAreaReq.getId());
				if (focusArea == null)
					throw new ValidationException(
							String.format("Focus Area having id (%s) not found.", focusAreaReq.getId()));

				if (!focusArea.getActive())
					focusArea.setActive(true);

				List<Activity> activities = focusArea.getActivities();
				if (activities == null)
					activities = new ArrayList<Activity>();
				
				if(!activities.stream().anyMatch(act -> act.getCid().equals(activity.getCid()))) {
					activities.add(activity);
					focusArea.setActivities(activities);
				}
				
				// focusArea = focusAreaRepository.save(focusArea);
				focusAreas.add(focusArea);

			}
		}
	}

	@Override
	public List<ActivityRequestResponse> getAllOfferedActivities(Integer pageNo, Integer pageSize) {

		Pageable paging = PageRequest.of(pageNo, pageSize);

		Page<Activity> activities = activityRepository.findAllByActiveTrue(paging);
		if (activities == null)
			throw new ValidationException("No activities found.");
		return activities.getContent().stream().map(ActivityRequestResponse::new).collect(Collectors.toList());
	}

	@Override
//	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_VIEW)
	public List<ActivityRequestResponse> getAllOfferedActivitiesBySchool(String schoolCid) {
		List<Activity> activities;

		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")))
			schoolCid = getUser().getSchool().getCid();
		if(schoolCid == null)
			throw new ValidationException("school id cannot be null.");

//			activities = activityRepository.findAllByIsGeneralTrueAndActiveTrue();
//		else
			activities = activityRepository.findAllBySchoolsCidAndActiveTrue(schoolCid);

		if (activities == null || activities.isEmpty())
			throw new ValidationException("No activities found for school");

		return activities.stream().map(ActivityRequestResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityRequestResponse> getAllOfferedActivitiesBySchoolAsPerGrade(String schoolCid) {
		List<Activity> activities;
		List<String> gradeIds = null;
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))) {
			schoolCid = getUser().getSchool().getCid();
			if(schoolCid == null)
				throw new ValidationException("School id not assigned to user logged in.");

			if(getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("SchoolAdmin"))) {

				if(!teacherRepository.existsByUserIdAndActiveTrue(getUserId()))
					gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue( schoolCid);
				else
					gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue( schoolCid, teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
			}else {
				if(!teacherRepository.existsByUserIdAndActiveTrue(getUserId())) {
					if(!studentRepository.existsByUserIdAndActiveTrue(getUserId()))
						throw new ValidationException("Not Authorized to see details.");
					gradeIds = Arrays.asList(studentRepository.findGradeCidByCidAndActiveTrue(studentRepository.findCidByUserIdAndActiveTrue(getUserId())));
				}else {
					gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue( schoolCid,teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
				}
			}

		}else {	
			if(schoolCid == null)
				throw new ValidationException("School id cannot be null.");
			gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue( schoolCid);
		}

			activities = teacherActivityGradeRepository.findAllActivityBySchoolCidAndGradeCidsInActiveTrue(schoolCid ,gradeIds);

		if (activities == null || activities.isEmpty())
			throw new ValidationException(String.format("No activities found in school for grades (%s)", gradeIds));

		return activities.stream().map(ActivityRequestResponse::new).collect(Collectors.toList());
	}
	
	@Override
	@Secured(AuthorityUtils.SCHOOL_CLUB_MEMBERSHIP_FETCH)
	public List<ActivityRequestResponse> getAllClubsOfStudent(String studentCid) {
//		List<Activity> activities;
		Long studentId = null;
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Student"))) {
			if(studentCid == null)
				throw new ValidationException("StudentId cannot be null.");
			studentId = studentRepository.findIdByCidAndActiveTrue(studentCid);
		}else {
			studentId = studentRepository.findIdByUserIdAndActiveTrue(getUserId());
		}
		if (!studentClubRepository.existsByStudentIdAndMembershipStatusAndActiveTrue(studentId,
				ApprovalStatus.VERIFIED))
			throw new ValidationException("Student not member of any Clubs.");

//		activities = studentClubRepository.findActivityByStudentIdAndMembershipStatusAndActiveTrue(studentId,
//				ApprovalStatus.VERIFIED);
		Set<StudentClub> activitiyTeacherLookUp = studentClubRepository.findActivityAndTeacherByStudentIdAndMembershipStatusAndActiveTrue(studentId, ApprovalStatus.VERIFIED);

		if(activitiyTeacherLookUp == null || activitiyTeacherLookUp.isEmpty())
			throw new ValidationException("Student not member of any Clubs.");
		List<ActivityRequestResponse> activities = new ArrayList<>();
		activitiyTeacherLookUp.stream().forEach(entry -> {
			ActivityRequestResponse act = new ActivityRequestResponse(entry.getActivity());
			act.setSupervisorName(entry.getTeacher().getName());
			activities.add(act);
		});
//		return activities.stream().map(ActivityRequestResponse::new).collect(Collectors.toList());
		return activities;
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_CLUB_MEMBERSHIP_FETCH)
	public List<ActivityRequestResponse> getAllClubsOfTeacher(String teacherCid , String schoolCid) {
		List<ActivityRequestResponse> activities = null;
		List<TeacherActivityGrade> teacherActivityGradesList = null;
		Long teacherId = null;
		List<String> gradeIds;
		
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor"))) {
			if(teacherCid == null)
				throw new ValidationException("TeacherId cannot be null.");
			teacherId = teacherRepository.findIdByCidAndActiveTrue(teacherCid);
			
			if (teacherId == null)
				throw new ValidationException("Teacher id is null or user id not set for teacher");
			if (!teacherActivityGradeRepository.existsByTeacherIdAndActiveTrue(teacherId))
				throw new ValidationException("Teacher not running any clubs or societies.");
			
			if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))) {
				schoolCid = getUser().getSchool().getCid();
				if(schoolCid == null)
					throw new ValidationException("School id not assigned to user logged in.");
				
	        if(getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("SchoolAdmin"))) {
					
					if(!teacherRepository.existsByUserIdAndActiveTrue(getUserId()))
						gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue( schoolCid);
					else
						gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue( schoolCid, teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
				}else {
					if(!teacherRepository.existsByUserIdAndActiveTrue(getUserId())) {
						if(!studentRepository.existsByUserIdAndActiveTrue(getUserId()))
							throw new ValidationException("Not Authorized to see details.");
						gradeIds = Arrays.asList(studentRepository.findGradeCidByCidAndActiveTrue(studentRepository.findCidByUserIdAndActiveTrue(getUserId())));
					}else {
						gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue( schoolCid,teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
					}
				}
				
			}else {	
				
				if(schoolCid == null)
					throw new ValidationException("School id cannot be null.");
				gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue( schoolCid);
			}
			
			teacherActivityGradesList = teacherActivityGradeRepository.findAllByTeacherIdAndGradeCidInAndActiveTrue(teacherId ,gradeIds);
			
		}else {
			teacherId = teacherRepository.getIdByUserIdAndActiveTrue(getUserId());
			if (teacherId == null)
				throw new ValidationException("Teacher id is null or user id not set for teacher");
			if (!teacherActivityGradeRepository.existsByTeacherIdAndActiveTrue(teacherId))
				throw new ValidationException("Teacher not running any clubs or societies.");

			 teacherActivityGradesList = teacherActivityGradeRepository.findAllByTeacherIdAndActiveTrue(teacherId);
		}
		
		
		if(teacherActivityGradesList != null) {
			activities = new ArrayList<ActivityRequestResponse>();
			Map<Activity,List<Grade>> activityGradeLookUp = new HashMap<>();
			teacherActivityGradesList.stream().distinct().forEach(tag -> {
				if(activityGradeLookUp.containsKey(tag.getActivity())) {
					List<Grade> grades = activityGradeLookUp.get(tag.getActivity()) == null ? new ArrayList<Grade>() : activityGradeLookUp.get(tag.getActivity());
					if(!grades.contains(tag.getGrade()) ) 
						grades.add(tag.getGrade());
					activityGradeLookUp.replace(tag.getActivity(),grades);
				}else {
					activityGradeLookUp.put(tag.getActivity(), new ArrayList<Grade>(Arrays.asList(tag.getGrade())));
				}
			});
			
		if(!activityGradeLookUp.isEmpty())	
			for(Activity activity : activityGradeLookUp.keySet()) {
				ActivityRequestResponse item = new ActivityRequestResponse(activity);
				item.setGradeResponses(activityGradeLookUp.get(activity).stream().distinct().map(GradeResponse :: new).collect(Collectors.toList()));
				activities.add(item);
			}
		}

		return activities ;
	}

	@Override
//	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_VIEW)
	public List<ActivityRequestResponse> getAllGeneralActivities() {

		List<Activity> generalActivities = activityRepository.findAllBySchoolsIdNotAndActiveTrue(getUser().gettSchoolId());

		if (generalActivities == null || generalActivities.isEmpty()) {
			throw new NotFoundException("No general activities found which is not being currently offered in your school.");
		}

		return generalActivities.stream().map(ActivityRequestResponse::new).collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> getAvailableFilters() {
		Map<String, Object> response = new HashMap<String, Object>();

		Set<String> fourS = new HashSet<String>();
		for (FourS fours : FourS.values()) {
			fourS.add(fours.toString());
		}
		response.put("Four S", fourS);

		Set<String> psdAreas = new HashSet<String>();
		for (PSDArea psd : PSDArea.values()) {
			psdAreas.add(psd.getPsdArea());
		}
		response.put("PSD Areas", psdAreas);

		Set<String> focusAreas = new HashSet<String>();
		focusAreaService.getAllFocusAreas().forEach(fa -> {
			focusAreas.add(fa.getName());
		});
		response.put("Focus Areas", focusAreas);

		return response;
	}

	@Override
	@Transactional
	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_DELETE)
	public SuccessResponse deleteActivityByCid(String cid ,String schoolCid ,Boolean forAll) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")))
			schoolCid = getUser().getSchool().getCid();
		else {
			if(forAll) {
				if(activityPerformedRepository.existsByActivityCidAndActive(cid, true))
					throw new ValidationException(String.format("Activity with id (%s) has already been performed by some students , so it can't be deleted contact your service provider for that.",cid));
				activityRepository.updateActivitySetActiveByCid(false, cid);
				return new SuccessResponse(200, "Activity successfully deleted for all schools.");
			}else {
				if(schoolCid == null)
					throw new ValidationException("school id cannot be null.");
			}
		}
		if(activityPerformedRepository.existsByActivityCidAndStudentSchoolCidInAndActive(cid, Arrays.asList(schoolCid), true))
			throw new ValidationException(String.format("Activity with id (%s) has already been performed by some students in school (id : %s) , so it can't be deleted contact your service provider for that.",cid,schoolCid));
		Activity activity = activityRepository.getOneByCidAndActiveTrue(cid);
		if (activity == null)
			throw new ValidationException(String.format("No activity found with id : %s ", cid));
		List<School> schools = activity.getSchools();
		final String schoolId = schoolCid;
		schools.removeIf(s-> s.getCid().equals(schoolId));
		activity.setSchools(activity.getSchools());
		activity = activityRepository.save(activity);
//		int i = activityRepository.updateActivitySetActiveByCid(false, cid);
//		if (i == 0)
//			throw new RuntimeException("Something went wrong Activity not deleted.");
		return new SuccessResponse(200, String.format("Activity successfuly deleted from school (id : %s).",schoolCid));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_CREATE)
	public ResponseEntity<?> uploadActivityFromExcel(MultipartFile file, String schoolCid) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");
		
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")))
			schoolCid = getUser().getSchool().getCid();
		if(schoolCid == null)
			throw new ValidationException("school id cannot be null.");

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

		Set<String> focusAreaCIds = new HashSet<String>();
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
		// logic to fetch comma separated schoolCids and set it to
		// activityRequest

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
						columnValues.put(headers.get(j), null);
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

		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(sheetName);
		return fetchRowValues(columnTypes, sheet, errors, sheetName);

	}

	@Override
	public ActivityRequestResponse getById(String cid) {
		Activity activity = activityRepository.getOneByCidAndActiveTrue(cid);
		if (activity == null)
			throw new ValidationException(String.format("No activity found with id : %s ", cid));
		return new ActivityRequestResponse(activity);
	}

}
