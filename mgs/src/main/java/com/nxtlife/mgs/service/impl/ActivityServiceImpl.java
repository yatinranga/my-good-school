package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.service.BaseService;
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

	@Override
	public List<ActivityRequestResponse> getAllOfferedActivities() {
		List<Activity> activities = activityRepository.findAllByActiveTrue();
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
		List<Activity> activities = activityRepository.findAllBySchoolsCidAndActiveTrue(schoolCid);
		List<ActivityRequestResponse> activityResponses = new ArrayList<ActivityRequestResponse>();
		if (activities == null)
			throw new ValidationException("No activities found.");
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

	@Override
	public List<ActivityRequestResponse> uploadActivityFromExcel(MultipartFile file, String schoolCid) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<ActivityRequestResponse> activityResponseList = new ArrayList<>();
		List<Map<String, Object>> activityRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook gradesheet = new XSSFWorkbook(file.getInputStream());
			activityRecords = findSheetRowValues(gradesheet, "ACTIVITY", errors);
			for (int i = 0; i < activityRecords.size(); i++) {
				List<Map<String, Object>> tempactivityRecords = new ArrayList<Map<String, Object>>();
				tempactivityRecords.add(activityRecords.get(i));
				activityResponseList.add(saveActivity(validateActivityRequest(tempactivityRecords, errors, schoolCid)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}

		return activityResponseList;
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
}
