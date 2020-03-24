package com.nxtlife.mgs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.LFINService;
import com.nxtlife.mgs.view.LFINRequestResponse;

@RestController
@RequestMapping("/api/lfin")
public class LFINController {
	
	@Autowired
	LFINService lFINService; 

	public LFINRequestResponse save (@Validated @RequestBody LFINRequestResponse request) {
		return lFINService.save(request);
	}
}
