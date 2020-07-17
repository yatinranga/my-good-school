package com.nxtlife.mgs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.service.AuthorityService;
import com.nxtlife.mgs.view.user.security.AuthorityResponse;

@RestController
//@Tag(name = "Authority", description = "Authority api's for fetch and delete the authority")
@RequestMapping("/api/")
public class AuthorityController {

	@Autowired
	private AuthorityService authorityService;

	/**
	 * return a list of authority available if authorities not exist ,it will return
	 * empty list
	 *
	 * @return List of <tt>AuthorityResponse</tt>
	 */

	@GetMapping("authorities")
//	@Operation(summary = "Find all the authorities", description = "return a list of authorities", tags = {
//			"Authority" })
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorityResponse.class)))),
//			@ApiResponse(responseCode = "404", description = "authorities not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(responseCode = "403", description = "don't have access to fetch authorities", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<AuthorityResponse> getAllAuthorities() {
		return authorityService.getAllAuthorities();
	}

	/**
	 * return a list of authorities by given role id if authority not exist ,it will
	 * throw Validation Exception
	 *
	 * @return List of <tt>AuthorityResponse</tt>
	 */

	@GetMapping("role/{roleId}/authorities")
//	@Operation(summary = "Find authorities by role id", description = "return a list of authorities by role id", tags = {
//			"Authority" })
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorityResponse.class)))),
//			@ApiResponse(responseCode = "404", description = "authority not found",content =@Content(schema=@Schema(implementation = ApiError.class))) })
	public List<AuthorityResponse> getAllAuthoritiesByRoleId(@PathVariable(required = true) Long roleId) {
		return authorityService.getAllAuthoritiesByRoleId(roleId);
	}

}
