package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FocusAreaService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.FocusAreaRequestResponse;

@Service
public class FocusAreaServiceImpl extends BaseService implements FocusAreaService {

	@Autowired
	private FocusAreaRepository focusAreaRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_CREATE)
	public FocusAreaRequestResponse save(FocusAreaRequestResponse request) {

		if (request == null)
			throw new ValidationException("request cannot be null.");
		if (request.getName() == null)
			throw new ValidationException("Focus area name cannot be null.");
		if (request.getPsdArea() == null)
			throw new ValidationException("PSD area cannot be null.");

		FocusArea focusArea = focusAreaRepository.findByNameAndActiveTrue(request.getName());
//		if (focusArea != null)
//			throw new ValidationException("This focus Area already exists.");
		if (focusArea != null) {
			focusArea = request.toEntity(focusArea);
		}else {
			focusArea = request.toEntity();
			focusArea.setCid(Utils.generateRandomAlphaNumString(8));
		}
		focusArea = focusAreaRepository.save(focusArea);
		if (focusArea == null)
			throw new RuntimeException("Something went wrong Focus Area not saved.");

		return new FocusAreaRequestResponse(focusArea);

	}

	@Override
	public List<FocusAreaRequestResponse> getAllFocusAreas() {
		List<FocusArea> focusAreaList = focusAreaRepository.findAllDistinctByActiveTrue();
		if (focusAreaList == null)
			throw new ValidationException("No Focus Areas found.");
		return focusAreaList.stream().map(FocusAreaRequestResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<FocusAreaRequestResponse> getAllFocusAreasBySchool(String schoolCid) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")))
			schoolCid = getUser().getSchool().getCid();
		if (schoolCid == null)
			throw new ValidationException("school id cannot be null.");
		if (!schoolRepository.existsByCidAndActive(schoolCid, true))
			throw new ValidationException("School with id : " + schoolCid + " not found.");
		List<FocusArea> focusAreaList = focusAreaRepository.findAllDistinctByActivitiesSchoolsCidAndActiveTrue(schoolCid);
		if (focusAreaList == null)
			throw new ValidationException("No Focus Areas found.");
		return focusAreaList.stream().distinct().map(FocusAreaRequestResponse::new).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	@Secured(AuthorityUtils.SCHOOL_ACTIVITY_CREATE)
	public ResponseEntity<?> uploadFocusAreasFromExcel(MultipartFile file) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<FocusAreaRequestResponse> focusAreaResponseList = new ArrayList<>();
		List<Map<String, Object>> focusAreaRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook gradesheet = new XSSFWorkbook(file.getInputStream());
			focusAreaRecords = findSheetRowValues(gradesheet, "FOCUS AREA", errors);
			errors = (List<String>) focusAreaRecords.get(focusAreaRecords.size() - 1).get("errors");
			for (int i = 0; i < focusAreaRecords.size(); i++) {
				List<Map<String, Object>> tempFocusAreaRecords = new ArrayList<Map<String, Object>>();
				tempFocusAreaRecords.add(focusAreaRecords.get(i));
				focusAreaResponseList.add(save(validateFocusAreaRequest(tempFocusAreaRecords, errors)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("FocusAreaResponseList", focusAreaResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	private FocusAreaRequestResponse validateFocusAreaRequest(List<Map<String, Object>> focusAreaDetails,
			List<String> errors) {
		if (focusAreaDetails == null || focusAreaDetails.isEmpty())
			errors.add("Focus Area details not found");
		FocusAreaRequestResponse focusAreaRequest = new FocusAreaRequestResponse();
		focusAreaRequest.setName((String) focusAreaDetails.get(0).get("NAME"));
		focusAreaRequest.setDescription((String) focusAreaDetails.get(0).get("DESCRIPTION"));
		focusAreaRequest.setPsdArea((String) focusAreaDetails.get(0).get("PSD AREA"));

		return focusAreaRequest;
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
