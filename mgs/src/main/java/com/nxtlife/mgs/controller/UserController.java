package com.nxtlife.mgs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.view.PasswordRequest;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.UserResponse;

@RestController
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "api/info")
	public UserResponse getLoggedInUser() {
		return userService.getLoggedInUser();
	}

	@PutMapping("api/change-password")
	public SuccessResponse changePassword(@Valid @RequestBody PasswordRequest request) {
		return userService.changePassword(request);
	}
	
	@GetMapping("/forgot-password/{username}")
	public SuccessResponse forgotPassword(@PathVariable String username) {
		return userService.forgotPassword(username);
	}
}
