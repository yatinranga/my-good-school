package com.nxtlife.mgs.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;

import com.nxtlife.mgs.ex.NotFoundException;

public interface ExcelUtil {

	public static Map<String, CellType> studentColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NAME", CellType.STRING);
		columnTypes.put("SCHOOL", CellType.STRING);
		columnTypes.put("SCHOOLS EMAIL", CellType.STRING);
		columnTypes.put("USERNAME", CellType.STRING);
		columnTypes.put("DOB", CellType.NUMERIC);
		columnTypes.put("ACTIVE", CellType.BOOLEAN);
		columnTypes.put("EMAIL", CellType.STRING);
		columnTypes.put("MOBILE NUMBER", CellType.NUMERIC);
		columnTypes.put("GENDER", CellType.STRING);
		columnTypes.put("SUBSCRIPTION END DATE", CellType.NUMERIC);
		columnTypes.put("GRADE", CellType.STRING);
		columnTypes.put("SECTION", CellType.STRING);
		columnTypes.put("FATHERS NAME", CellType.STRING);
		columnTypes.put("FATHERS EMAIL", CellType.STRING);
		columnTypes.put("FATHERS MOBILE NUMBER", CellType.NUMERIC);
		columnTypes.put("MOTHERS NAME", CellType.STRING);
		columnTypes.put("MOTHERS EMAIL", CellType.STRING);
		columnTypes.put("MOTHERS MOBILE NUMBER", CellType.NUMERIC);
		
		return columnTypes;
	}
	
	public static Map<String, CellType> userColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NAME", CellType.STRING);
		return columnTypes;
	}
	
	public static Map<String, CellType> schoolColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NAME", CellType.STRING);
		columnTypes.put("ADDRESS", CellType.STRING);
		columnTypes.put("EMAIL", CellType.STRING);
		columnTypes.put("CONTACT NUMBER", CellType.STRING);
		columnTypes.put("ACTIVE", CellType.BOOLEAN);
		return columnTypes;
	}
	
	public static Map<String, CellType> teacherColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NAME", CellType.STRING);
		columnTypes.put("SCHOOL", CellType.STRING);
		columnTypes.put("SCHOOLS EMAIL", CellType.STRING);
		columnTypes.put("USERNAME", CellType.STRING);
		columnTypes.put("DOB", CellType.NUMERIC);
		columnTypes.put("ACTIVE", CellType.BOOLEAN);
		columnTypes.put("EMAIL", CellType.STRING);
		columnTypes.put("MOBILE NUMBER", CellType.NUMERIC);
		columnTypes.put("GENDER", CellType.STRING);
		columnTypes.put("QUALIFICATION", CellType.STRING);
		columnTypes.put("GRADE", CellType.STRING);
//		columnTypes.put("SECTION", CellType.STRING);
		columnTypes.put("ACTIVE", CellType.BOOLEAN);
		
		return columnTypes;
	}
	
	public static Map<String, CellType> coachColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NAME", CellType.STRING);
		columnTypes.put("SCHOOL", CellType.STRING);
		columnTypes.put("SCHOOLS EMAIL", CellType.STRING);
		columnTypes.put("USERNAME", CellType.STRING);
		columnTypes.put("DOB", CellType.NUMERIC);
		columnTypes.put("ACTIVE", CellType.BOOLEAN);
		columnTypes.put("EMAIL", CellType.STRING);
		columnTypes.put("MOBILE NUMBER", CellType.NUMERIC);
		columnTypes.put("GENDER", CellType.STRING);
		columnTypes.put("QUALIFICATION", CellType.STRING);
		columnTypes.put("ACTIVITY NAME", CellType.STRING);
		columnTypes.put("GRADE", CellType.STRING);
//		columnTypes.put("FOCUS AREA", CellType.STRING);
//		columnTypes.put("PSD AREA", CellType.STRING);
//		columnTypes.put("4S TYPE", CellType.STRING);
		
		return columnTypes;
	}
	
	public static Map<String, CellType> managementColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NAME", CellType.STRING);
		columnTypes.put("SCHOOL", CellType.STRING);
		columnTypes.put("USERNAME", CellType.STRING);
		columnTypes.put("DOB", CellType.NUMERIC);
		columnTypes.put("ACTIVE", CellType.BOOLEAN);
		columnTypes.put("EMAIL", CellType.STRING);
		columnTypes.put("MOBILE NUMBER", CellType.STRING);
		columnTypes.put("GENDER", CellType.STRING);
		return columnTypes;
	}
	
	public static Map<String, CellType> lfinColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NAME", CellType.STRING);
		columnTypes.put("USERNAME", CellType.STRING);
		columnTypes.put("DOB", CellType.NUMERIC);
		columnTypes.put("ACTIVE", CellType.BOOLEAN);
		columnTypes.put("EMAIL", CellType.STRING);
		columnTypes.put("MOBILE NUMBER", CellType.STRING);
		columnTypes.put("GENDER", CellType.STRING);
		return columnTypes;
	}

	public static Map<String, CellType> sheetColumns(String sheetName) {
		switch (sheetName) {
		case "STUDENT":
			return studentColumns();
		case "USER":
			return userColumns();
		case "SCHOOL":
			return schoolColumns();
		case "TEACHER":
			return teacherColumns();
		case "COACH":
			return coachColumns();
		case "MANAGEMENT":
			return managementColumns();
		case "LFIN":
			return lfinColumns();
		default:
			throw new NotFoundException("Sheet columns not found");
		}
	}

	public static List<String> sheets() {
		return Arrays.asList("STUDENT", "USER", "SCHOOL", "TEACHER", "COACH", "MANAGEMENT","LFIN");
	}
}
