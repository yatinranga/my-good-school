package com.nxtlife.mgs.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nxtlife.mgs.service.CommonService;

@Controller
@RequestMapping("/")
public class CommonController {

	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/file/download", method = RequestMethod.GET)
	public ResponseEntity<?> downloadFile(@RequestParam("filePath") String filePath) {
		return commonService.downloadFile(filePath);
	}
}
