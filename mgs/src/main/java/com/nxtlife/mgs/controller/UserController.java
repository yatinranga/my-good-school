package com.nxtlife.mgs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.view.user.UserResponse;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping(value="/info")
	public UserResponse getLoggedInUser() {
		return userService.getLoggedInUser();
	}
}
