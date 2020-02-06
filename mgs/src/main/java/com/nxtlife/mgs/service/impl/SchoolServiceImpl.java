package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.store.FileStore;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.StudentRequest;
import com.nxtlife.mgs.view.StudentResponse;

@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	SchoolRepository schoolRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	Utils utils;
	
	@Autowired
	FileStore filestore;
	
	
	@Override
	public SchoolResponse save(SchoolRequest request) {
		if(request==null)
			throw new ValidationException("Request can not be null.");
		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");
		if (schoolRepository.countByEmail(request.getEmail()) > 0)
			throw new ValidationException("Email already exists i.e, school already exists.");
		if (request.getUsername() == null) {
			request.setUsername(request.getEmail());
			if (schoolRepository.countByUsername(request.getUsername()) > 0)
				throw new ValidationException("Username already exists");
		} else {
			if (schoolRepository.countByUsername(request.getUsername()) > 0)
				throw new ValidationException("Username already exists");
		}
		
		if (request.getName() == null)
			throw new ValidationException("School name can not be null");
		School school = request.toEntity();
		
		try {
			school.setCid(utils.generateRandomAlphaNumString(8));
		} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
			school.setCid(utils.generateRandomAlphaNumString(8));
		}
		
		User user = userService.createSchoolUser(school);
		if (StringUtils.isEmpty(user))
			throw new ValidationException("User not created successfully");
		school.setUser(user);
		
		if(request.getLogo()!=null) {
			String fileExtn = request.getLogo().getOriginalFilename().split("\\.")[1];
		       String filename = UUID.randomUUID().toString() + "." + fileExtn;
			try {
		       String logoUrl = filestore.store("schoolLogo", filename, request.getLogo().getBytes());
		       school.setLogo(logoUrl);
			}catch (IOException e)
		       {
		         e.printStackTrace();
		       }
		}
		school = schoolRepository.save(school);
		if(school==null)
			throw new RuntimeException("Something went wrong school not saved.");
		return new SchoolResponse(school);
		
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

	private List<Map<String, Object>> findSheetRowValues(XSSFWorkbook workbook, String sheetName,List<String> errors) {
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
	public List<SchoolResponse> uploadSchoolsFromExcel(MultipartFile file) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<SchoolResponse> schoolResponseList = new ArrayList<>();
		List<Map<String, Object>> schoolRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook studentsSheet = new XSSFWorkbook(file.getInputStream());
			schoolRecords = findSheetRowValues(studentsSheet, "SCHOOL", errors);
			for (int i = 0; i < schoolRecords.size(); i++) {
				List<Map<String, Object>> tempSchoolRecords = new ArrayList<Map<String, Object>>();
				tempSchoolRecords.add(schoolRecords.get(i));
				schoolResponseList.add(save(validateSchoolRequest(tempSchoolRecords, errors)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}

		return schoolResponseList;
	}
	
	private SchoolRequest validateSchoolRequest(List<Map<String, Object>> schoolDetails, List<String> errors) {
		if (schoolDetails == null || schoolDetails.isEmpty()) {
			errors.add("School details not found");
		}
		SchoolRequest schoolRequest = new SchoolRequest();
		if(schoolDetails.get(0).get("NAME")!=null) {
		    schoolRequest.setName((String) schoolDetails.get(0).get("NAME"));
		    }
		if(schoolDetails.get(0).get("USERNAME")!=null)
		   schoolRequest.setUsername((String) schoolDetails.get(0).get("USERNAME"));
	
		if (schoolDetails.get(0).get("ACTIVE") != null)
			schoolRequest.setActive(Boolean.valueOf((Boolean) schoolDetails.get(0).get("ACTIVE")));
		if(schoolDetails.get(0).get("EMAIL") != null)
			schoolRequest.setEmail((String) schoolDetails.get(0).get("EMAIL"));
		
		schoolRequest.setContactNumber((String) schoolDetails.get(0).get("CONTACT NUMBER"));
		
		schoolRequest.setAddress((String) schoolDetails.get(0).get("ADDRESS"));

		return schoolRequest;
	}

	@Override
	public SchoolResponse findById(Long id) {
		return null;
	}

	@Override
	public SchoolResponse findByCid(String cId) {
		return null;
	}

}
