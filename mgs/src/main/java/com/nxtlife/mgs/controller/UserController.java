package com.nxtlife.mgs.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.view.PasswordRequest;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.UserRequest;
import com.nxtlife.mgs.view.user.UserResponse;

@RestController
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenStore tokenStore;

	@GetMapping(value = "api/info")
	public UserResponse getLoggedInUser() {
		return userService.getLoggedInUserResponse();
	}

//	@PutMapping("api/change-password")
//	public SuccessResponse changePassword(@Valid @RequestBody PasswordRequest request) {
//		return userService.changePassword(request);
//	}

	@GetMapping("/forgot-password/{username}")
	public SuccessResponse forgotPassword(@PathVariable String username) {
		return userService.forgotPassword(username);
	}

	@GetMapping(produces = { "application/json" }, value = "api/me")
//	@Operation(summary = "Find login user info", description = "return user info using access token detail", tags = {
//			"User", "Me" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "User info successfully fetched", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
//			@ApiResponse(description = "If token is not valid", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse find() {
		return userService.findByAuthentication();
	}

	@PostMapping(produces = { "application/json" }, consumes = { "application/json" }, value = "api/user")
//	@Operation(summary = "Save user details", description = "return user info after saving user details", tags = {
//			"User" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "User info after saving user details", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
//			@ApiResponse(description = "If user doesn't have access to save user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If required field are not filled or role ids not valid or username already exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse save(@Valid @RequestBody(required = true) UserRequest request) {
		return userService.save(request);
	}

	@GetMapping(produces = { "application/json" }, value = "api/users")
//	@Operation(summary = "Find all user info", description = "return list of user details for an organization", tags = {
//			"User" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "User info successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
//			@ApiResponse(description = "If user doesn't have access to fetch list of user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<UserResponse> findAll() {
		return userService.findAll();
	}

	@GetMapping(produces = { "application/json" }, value = "api/user/{userId}")
//	@Operation(summary = "Find user info", description = "return user details by userId", tags = { "User" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "User info successfully fetched", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
//			@ApiResponse(description = "If user doesn't have access to fetch list of user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse findById(
			/* @Parameter(description = "User id", required = true) */ @PathVariable("userId") String userId) {
		return userService.findById(userId);
	}

	@PutMapping(produces = { "application/json" }, consumes = { "application/json" }, value = "api/user/{id}")
//	@Operation(summary = "Update user details", description = "return user info after updating user details", tags = {
//			"User" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "User info after updating user details", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
//			@ApiResponse(description = "If user doesn't have access to save user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If required field are not filled or role ids not valid", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public UserResponse update(/* @Parameter(description = "User id", required = true) */ @PathVariable String id,
			/* @Parameter(description = "User details", required = true) */ @RequestBody UserRequest request) {
		return userService.update(id, request);
	}

	@PutMapping(value = "api/me/password", produces = { "application/json" }, consumes = { "application/json" })
//	@Operation(summary = "Change user password using old password", description = "This api used to change password using old password", tags = {
//			"User", "Me" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "Success message", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
//			@ApiResponse(description = "If user not found with password", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If old password is not correct", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse changePassword(/*
											 * @Parameter(description = "Old password and new password", required =
											 * true)
											 */ @Valid @RequestBody PasswordRequest request) {
		return userService.changePassword(request);
	}

	@GetMapping(produces = { "application/json" }, value = "api/me/logout")
//	@Operation(summary = "Logout", description = "This api used to logout", tags = { "User", "Logout", "Me" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "Success message", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))) })
	public SuccessResponse logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			tokenStore.removeAccessToken(accessToken);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Logout successfully");
	}

	@PutMapping(produces = { "application/json" }, value = "api/user/{userId}/activate")
//	@Operation(summary = "Activate User", description = "return success message if user successfully activated", tags = {
//			"User" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "User successfully activated", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
//			@ApiResponse(description = "If user doesn't have access to activate user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If user id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse activate(
			/* @Parameter(description = "User id", required = true) */ @PathVariable String userId) {
		return userService.activate(userId);
	}

	@DeleteMapping(produces = { "application/json" }, value = "api/user/{userId}")
//	@Operation(summary = "Delete User", description = "return success message if user successfully deleted", tags = {
//			"User" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "User successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
//			@ApiResponse(description = "If user doesn't have access to delete user", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If user id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(
			/* @Parameter(description = "User id", required = true) */ @PathVariable String userId) {
		return userService.delete(userId);
	}

	@GetMapping(produces = { "application/json" }, value = "api/user/role/{roleId}")
	public List<UserResponse> findByRoleId(
			/* @Parameter(description = "User id", required = true) */ @PathVariable("roleId") Long roleId) {
		return userService.findAllByRoleId(roleId);
	}
}
