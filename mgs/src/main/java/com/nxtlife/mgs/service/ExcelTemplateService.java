package com.nxtlife.mgs.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ExcelTemplateService {

	ByteArrayInputStream exportExampleFile(String type) throws IOException;

}
