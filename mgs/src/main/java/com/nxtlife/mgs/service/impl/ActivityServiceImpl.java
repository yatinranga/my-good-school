package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FocusAreaService;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityRequestResponse;
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

	@PostConstruct
	public void init() {
		List<FocusArea> focusAreaRepoList = focusAreaRepository.findAllByActiveTrue();
		List<FocusArea> focusAreaList = new ArrayList<FocusArea>();
//		List<FocusArea> focusAreasForPD = new ArrayList<FocusArea>();
//		List<FocusArea> focusAreasForSD = new ArrayList<FocusArea>();
		String[] focusAreasPd = { "Identity", "Spiritual & Aesthetic Awareness", "Decision Making", "Health",
				"Intellectual Growth" };
		String[] focusAreasSD = { "Community Skills", "Employment Skills", "Citizenship", "Environmental Awareness" };

//		for(FocusArea foc : focusAreasForPD ) {
//			foc.setPsdArea(PSDArea.PersonalDevelopment);
//			foc.setActive(true);
//			foc.setCid(utils.generateRandomAlphaNumString(8));
//		}

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

		String[] skillActivities = { "Yoga", "Literary Society Hindi", "Literary Society English", "Art & Craft",
				"Music & Dance", "Band", "Computers", "Cooking" };
		String[] sportActivities = { "Martial Arts", "Cricket", "Fencing", "Football", "Kho-Kho", "Badminton",
				"Kabaddi" };
		String[] serviceActivities = { "Social Service League", "NCC", "Scouts & Guides", "Rural Livelihood Maping",
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

		
//		activityList = activityRepository.findAllByIsGeneralTrueAndActiveTrue();
//		List<School> schoolList = schoolRepository.findAllByActiveTrue();
//		
//		activityList.forEach(act-> {act.getSchools().addAll(schoolList);});
//		final List<Activity> activities = activityList;
//		schoolList.forEach(sch -> { sch.setActivities(activities);});
//		 activityRepository.save(activities);
		School school = schoolRepository.findByNameAndActiveTrue("my good school");
		if(school != null) {
			activityList.forEach(act -> {
				if(!act.getSchools().contains(school))
				    act.getSchools().add(school);
				});
			school.setActivities(activityList);
			schoolRepository.save(school);
		}
		

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
	public List<ActivityRequestResponse> getAllOfferedActivities(Integer pageNo, Integer pageSize) {

		Pageable paging = new PageRequest(pageNo, pageSize);

		Page<Activity> activities = activityRepository.findAllByActiveTrue(paging);
		List<ActivityRequestResponse> activityResponses = new ArrayList<ActivityRequestResponse>();
		if (activities == null)
			throw new ValidationException("No activities found.");
		activities.forEach(activity -> {
			activityResponses.add(new ActivityRequestResponse(activity));
		});
		return activityResponses;
	}

	@Override
	public List<ActivityRequestResponse> getAllOfferedActivitiesBySchool(String schoolCid) {
		List<Activity> activities;
		if (schoolCid == null)
			activities = activityRepository.findAllByActiveTrue();
		else
			activities = activityRepository.findAllBySchoolsCidAndActiveTrue(schoolCid);

		List<ActivityRequestResponse> activityResponses = new ArrayList<ActivityRequestResponse>();

		if (activities == null || activities.isEmpty())
			throw new ValidationException("No general or school specific activities found.");
		activities.forEach(activity -> {
			activityResponses.add(new ActivityRequestResponse(activity));
		});
		return activityResponses;
	}

	@Override
	public ActivityRequestResponse saveActivity(ActivityRequestResponse request) {
		if (request == null)
			throw new ValidationException("Request cannot be null");
		if (request.getName() == null)
			throw new ValidationException("Activity name cannot be null");
		if (request.getFourS() == null)
			throw new ValidationException("Four S cannot be null.");
		if (request.getFocusAreaIds() == null)
			throw new ValidationException("Focus area ids cannot be null.");
		Activity activity = activityRepository.findByNameAndActiveTrue(request.getName());
		if (activity != null)
			throw new ValidationException("Activity already exist.");
		List<FocusArea> focusAreaList = focusAreaRepository.findAll();
		if (focusAreaList == null)
			throw new ValidationException("No Focus Areas found.");
//		for(int i =0;i<focusAreaList.size();i++) {
//			if(!request.getFocusAreaIds().contains(focusAreaList.get(i).getCid()))
//				focusAreaList.remove(i);
//		}
		List<FocusArea> focusAreas = new ArrayList<>();
		for (int i = 0; i < request.getFocusAreaIds().size(); i++) {
			for (int j = 0; j < focusAreaList.size(); j++)
				if (focusAreaList.get(j).getCid().equals(request.getFocusAreaIds().get(i)))
					focusAreas.add(focusAreaList.get(j));
		}
		activity = request.toEntitity();
		activity.setFocusAreas(focusAreas);
		List<School> schools = schoolRepository.findAll();
		if (request.getSchoolIds() != null && !request.getSchoolIds().isEmpty()) {
			for (int i = 0; i < schools.size(); i++) {
				if (!request.getSchoolIds().contains(schools.get(i).getCid()))
					schools.remove(i);
			}
		}

		activity.setCid(utils.generateRandomAlphaNumString(8));
		activity.setActive(true);
		activity = activityRepository.save(activity);
		if (activity == null)
			throw new RuntimeException("Something went wrong activity not created.");

		return new ActivityRequestResponse(activity);

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

//     NAME DESCRIPTION FOUR S FOCUS AREAS		

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

	@Override
	public List<ActivityRequestResponse> getAllGeneralActivities() {

		List<Activity> generalActivities = activityRepository.findAllByIsGeneralTrueAndActiveTrue();

		if (generalActivities == null || generalActivities.isEmpty()) {
			throw new NotFoundException("no general activities found");
		}

		List<ActivityRequestResponse> responseList = new ArrayList<ActivityRequestResponse>();

		for (Activity activity : generalActivities) {

			responseList.add(new ActivityRequestResponse(activity));

		}

		return responseList;
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
			psdAreas.add(psd.toString());
		}
		response.put("PSD AREAS", psdAreas);
		
		Set<String> focusAreas = new HashSet<String>();
		focusAreaService.getAllFocusAreas().forEach(fa -> {focusAreas.add(fa.getName());});
		response.put("Focus Areas", focusAreas);
		
		return response;
	}
}
