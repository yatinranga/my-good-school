package com.nxtlife.mgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.common.UserRoleKey;
import com.nxtlife.mgs.entity.user.LFIN;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.entity.user.UserRole;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.LFINRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.LFINService;
import com.nxtlife.mgs.service.SequenceGeneratorService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.view.ActivityRequestResponse;
import com.nxtlife.mgs.view.LFINRequestResponse;

@Service
public class LFINServiceImpl extends BaseService implements LFINService {

	@Autowired
	private LFINRepository lFINRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public LFINRequestResponse save(LFINRequestResponse request) {

		User loggedInUser = getUser();
		if (loggedInUser == null)
			throw new ValidationException("You need to be logged in as main Admin to create and add LFIN.");
		loggedInUser = userRepository.getOne(loggedInUser.getId());

		if (!loggedInUser.getRoles().stream().anyMatch(a -> a.getName().equalsIgnoreCase("MainAdmin")))
			throw new UnauthorizedUserException("You are not authorized to create and add LFIN.");

		if (lFINRepository.existsByEmailAndActiveTrue(request.getEmail()))
			throw new ValidationException(
					String.format("A LFIN member with email (%s) already exist.", request.getEmail()));

		LFIN lfin = request.toEntity();
		// lfin.setCid(utils.generateRandomAlphaNumString(8));
		// Long lfinsequence =
		// sequenceGeneratorService.findSequenceByUserType(UserType.LFIN);
		// lfin.setUsername(String.format("LFI%08d", lfinsequence));

		// User user = userService.createUserForEntity(lfin);

		Long roleId = roleRepository.findIdByName("Lfin");
		if (roleId == null)
			throw new ValidationException("Role Lfin not created yet.");
		User user = userService.createUser(lfin.getName(), lfin.getContactNumber(), lfin.getEmail(), null);
		user.setUserRoles(
				Arrays.asList(new UserRole(new UserRoleKey(roleId, user.getId()), new Role(roleId, "Lfin"), user)));

		if (StringUtils.isEmpty(user))
			throw new ValidationException("User not created successfully");
		lfin.setUser(user = userRepository.save(user));
		lfin.setUsername(lfin.getUser().getUsername());

		if (StringUtils.isEmpty(user)) {
			throw new ValidationException("User not created successfully");
		}
		lfin.setUser(user = userRepository.save(user));
		lfin.setActive(true);
		lfin = lFINRepository.save(lfin);

		if (lfin == null)
			throw new RuntimeException("Something went wrong record not saved.");

		return new LFINRequestResponse(lfin);
	}

	@Override
	public ActivityRequestResponse updateActivityStatus(String activityId, Boolean isVerified) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<?> uploadLFINFromExcel(MultipartFile file) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<LFINRequestResponse> lfinResponseList = new ArrayList<>();
		List<Map<String, Object>> lfinsRecords = new ArrayList<Map<String, Object>>();

		try {
			XSSFWorkbook lfinsSheet = new XSSFWorkbook(file.getInputStream());
			lfinsRecords = findSheetRowValues(lfinsSheet, "LFIN", errors);
			// errors = (List<String>)
			// studentsRecords.get(studentsRecords.size() - 1).get("errors");
			for (int i = 0; i < lfinsRecords.size() - 1; i++) {
				List<Map<String, Object>> tempLfinsRecords = new ArrayList<Map<String, Object>>();
				tempLfinsRecords.add(lfinsRecords.get(i));
				lfinResponseList.add(save(validateLfinRequest(tempLfinsRecords, errors)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("LFINResponseList", lfinResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
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
						if (headers.get(j).equalsIgnoreCase("NAME") || headers.get(j).equalsIgnoreCase("EMAIL"))
							errors.add(String.format(
									"Cell at row %d and column %d is blank for header %s which is mandatory.", i + 1,
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
		// XSSFSheet sheet = workbook.getSheet(sheetName);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			errors.add(sheetName + " sheet not found");
			return null;
		}
		// if (sheet.getPhysicalNumberOfRows() > rowLimit) {
		// errors.add(String.format("Number of row can't be more than %d for %s
		// sheet", rowLimit, sheetName));
		// }
		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(sheetName);
		return fetchRowValues(columnTypes, sheet, errors, sheetName);

	}

	private LFINRequestResponse validateLfinRequest(List<Map<String, Object>> lfinDetails, List<String> errors) {
		if (lfinDetails == null || lfinDetails.isEmpty()) {
			errors.add("LFIN details not found");
		}
		LFINRequestResponse lfinRequest = new LFINRequestResponse();
		lfinRequest.setName((String) lfinDetails.get(0).get("NAME"));

		if ((Date) lfinDetails.get(0).get("DOB") != null)
			lfinRequest.setDob(DateUtil.formatDate((Date) lfinDetails.get(0).get("DOB"), null, null));

		lfinRequest.setEmail((String) lfinDetails.get(0).get("EMAIL"));
		lfinRequest.setContactNumber((String) lfinDetails.get(0).get("CONTACT NUMBER"));
		lfinRequest.setGender((String) lfinDetails.get(0).get("GENDER"));

		return lfinRequest;
	}

}
