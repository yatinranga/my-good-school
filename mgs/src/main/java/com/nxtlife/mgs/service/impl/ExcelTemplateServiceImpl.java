package com.nxtlife.mgs.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.ExcelTemplateService;
import com.nxtlife.mgs.util.ExcelUtil;

@Service
public class ExcelTemplateServiceImpl extends BaseService implements ExcelTemplateService {

	@Value("${mgs.file.upload.location}")
	protected String filedir;

//	private static Logger logger = LoggerFactory.getLogger(ExcelTemplateServiceImpl.class);

	@Override
	public ByteArrayInputStream exportExampleFile(String type) throws IOException {

//		Path path = Paths.get(filedir);
//		logger.info(filedir);
//		String filename = path.resolve(UUID.randomUUID().toString()).toString() + ".xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(type.toUpperCase());

		Map<String, CellType> columnTypes = ExcelUtil.sheetColumns(type.toUpperCase());

		XSSFRow row = sheet.createRow(0);

		int col = 0;

		for (Entry<String, CellType> entry : columnTypes.entrySet()) {
			XSSFCell cell = row.createCell(col++);
			cell.setCellValue(entry.getKey());
		}

//		File file = new File(filename);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			workbook.write(out);
			workbook.close();

//		} catch (IOException e) {
//		logger.error("error in creating ", e);
//		}
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

}
