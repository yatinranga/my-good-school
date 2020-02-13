package com.nxtlife.mgs.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.ExcelTemplateService;
import com.nxtlife.mgs.util.ExcelUtil;

@Service
public class ExcelTemplateServiceImpl extends BaseService implements ExcelTemplateService{

	@Value("${mgs.file.upload.location}")
	protected String filedir;
	
	private static Logger logger = LoggerFactory.getLogger(ExcelTemplateServiceImpl.class);
	
	@Override
	public File exportExampleFile(String type) {

		Path path = Paths.get(filedir);
		logger.info(filedir);
		String filename = path.resolve(UUID.randomUUID().toString()).toString() + ".xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(type.toUpperCase());
		
		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(type.toUpperCase());

		XSSFRow row = sheet.createRow(0);

		int col = 0;

		for(Entry<String, CellType> entry : columnTypes.entrySet()) {
			XSSFCell cell = row.createCell(col++);
			cell.setCellValue(entry.getKey());
		}

		File file = new File(filename);
		try {
		workbook.write(new FileOutputStream(file));
		} catch (IOException e) {
		logger.error("error in creating ", e);
		}
		return file;

		}

}
