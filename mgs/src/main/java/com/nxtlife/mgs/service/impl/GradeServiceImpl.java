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

import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.GradeService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.ExcelUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.GradeRequest;
import com.nxtlife.mgs.view.GradeResponse;

@Service
public class GradeServiceImpl extends BaseService implements GradeService {

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private GradeRepository gradeRepository;

	@Override
	@Secured(AuthorityUtils.SCHOOL_STAKEHOLDER_CREATE)
	public GradeResponse save(GradeRequest request) {
		if (request == null)
			throw new ValidationException("Request can not be null.");

		if (request.getGrade() == null || request.getSection() == null)
			throw new ValidationException("Grade and section cannot be null.");

		Grade grade = gradeRepository.findByNameAndSectionAndActiveTrue(request.getGrade(), request.getSection());
		if (grade != null)
			throw new ValidationException(
					String.format("Grade %s-%s already exist.", request.getGrade(), request.getSection()));

		grade = request.toEntity();

		List<School> schools = new ArrayList<>();
		School school;

		if (request.getSchoolIds() == null || request.getSchoolIds().isEmpty()) {
			throw new ValidationException("No grade is created without any school id");
		}

		for (String schoolRequest : request.getSchoolIds()) {

			if ((school = schoolRepository.getOneByCid(schoolRequest)) == null)
				throw new ValidationException(String.format("School having id [%s] didn't exist", schoolRequest));
			else if (school.getCid().equals(schoolRequest)) {
				school.getGrades().add(grade);
				schools.add(school);
			}
		}

		grade.setSchools(schools);

		grade.setCid(Utils.generateRandomAlphaNumString(8));

		grade = gradeRepository.save(grade);

		if (grade == null)
			throw new RuntimeException("Something went wrong grade not created.");
		return new GradeResponse(grade);
	}

	@Override
	public GradeResponse findByCid(String cid) {
		if(cid == null)
			throw new ValidationException("Grade id cannot be null.");
		if(!gradeRepository.existsByCidAndActiveTrue(cid))
			throw new ValidationException(String.format("Grade having id (%s) does not exist", cid));
		return new GradeResponse(gradeRepository.findByCidAndActiveTrue(cid));
	}
	
	@Override
	public List<GradeResponse> getAllGradesOfSchool(String schoolCid) {
		
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")))
			schoolCid = getUser().getSchool().getCid();
		
		if (schoolCid == null)
			throw new ValidationException("School Id cannot be null.");

		if (!schoolRepository.existsByCidAndActive(schoolCid ,true))
			throw new ValidationException("School not found.");

		List<Grade> gradeList = gradeRepository.findAllBySchoolsCidAndActiveTrue(schoolCid);
//		List<GradeResponse> gradeResponseList = new ArrayList<GradeResponse>();

		if (gradeList == null)
			throw new ValidationException("No Grades found in this school.");
//
//		gradeList.forEach(g -> {
//			gradeResponseList.add(new GradeResponse(g));
//		});

		return gradeList.stream().map(GradeResponse :: new).collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_STAKEHOLDER_CREATE)
	public ResponseEntity<?> uploadGradesFromExcel(MultipartFile file, String schoolCid) {
		if (file == null || file.isEmpty() || file.getSize() == 0)
			throw new ValidationException("Pls upload valid excel file.");

		List<String> errors = new ArrayList<String>();
		List<GradeResponse> gradeResponseList = new ArrayList<>();
		List<Map<String, Object>> gradeRecords = new ArrayList<Map<String, Object>>();
		try {
			XSSFWorkbook gradesheet = new XSSFWorkbook(file.getInputStream());
			gradeRecords = findSheetRowValues(gradesheet, "GRADE", errors);
//			errors = (List<String>) gradeRecords.get(gradeRecords.size() - 1).get("errors");
			for (int i = 0; i < gradeRecords.size(); i++) {
				List<Map<String, Object>> tempGradesRecords = new ArrayList<Map<String, Object>>();
				tempGradesRecords.add(gradeRecords.get(i));
				gradeResponseList.add(save(validateGradeRequest(tempGradesRecords, errors, schoolCid)));
			}

		} catch (IOException e) {

			throw new ValidationException("something wrong happened may be file not in acceptable format.");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("GradeResponseList", gradeResponseList);
		responseMap.put("errors", errors);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	private GradeRequest validateGradeRequest(List<Map<String, Object>> gradeDetails, List<String> errors,
			String schoolCid) {
		if (gradeDetails == null || gradeDetails.isEmpty())
			errors.add("Grade details not found");
		GradeRequest gradeRequest = new GradeRequest();
		gradeRequest.setGrade((String) gradeDetails.get(0).get("GRADE"));
		gradeRequest.setSection((String) gradeDetails.get(0).get("SECTION"));
		if (schoolCid == null)
			throw new ValidationException("School Id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);
		if (school == null)
			throw new ValidationException(String.format("No School found with id : %s ", schoolCid));

		return gradeRequest;
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
