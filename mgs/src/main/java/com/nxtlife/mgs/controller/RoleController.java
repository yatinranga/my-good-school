package com.nxtlife.mgs.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.SafeHtml.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.mgs.ex.ApiError;
import com.nxtlife.mgs.service.RoleService;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.security.RoleRequest;
import com.nxtlife.mgs.view.user.security.RoleResponse;


@RestController
//@Tag(name = "Role", description = "Role api's for fetch ,create and delete the role")
@RequestMapping("/api/")
public class RoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * return a list of roles available in that organization or that area. if
	 * organization not exist ,it will throw validation exception
	 *
	 * @Param Id
	 * @return List of <tt>RoleResponse</tt>
	 */

	@GetMapping(value = "roles", produces = { "application/json" })
//	@Operation(summary = "Find roles by organization id", description = "return a list of roles by organization id which is taken by currently login user", tags = {
//			"Role" })
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class)))),
//			@ApiResponse(responseCode = "404", description = "roles not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(responseCode = "403", description = "don't have access to fetch roles", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<RoleResponse> getAllRolesByOrganizationId() {
		return roleService.getAllRoles();
	}
	
	@GetMapping(value = "role/{roleId}", produces = { "application/json" })
//	@Operation(summary = "Find roles by organization id", description = "return a list of roles by organization id which is taken by currently login user", tags = {
//			"Role" })
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
//			@ApiResponse(responseCode = "404", description = "role not found", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(responseCode = "403", description = "don't have access to fetch roles", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public RoleResponse findById(
			/* @Parameter(description = "Id of the role", required = true) */@PathVariable Long roleId) {
		return roleService.findById(roleId);
	}

	/**
	 * save the role and return the same if authority not exist ,it will return
	 * empty list
	 *
	 * @param request
	 *            [Key value pair which we want to save that role]
	 * @return RoleResponse
	 */

	@PostMapping(value = "role", consumes = { "application/json" }, produces = { "application/json" })
//	@Operation(summary = "save the role", description = "Save the role and return the same role as role Response", tags = {
//			"Role" })
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
//			@ApiResponse(responseCode = "400", description = "If role already exist or authority ids are not valid", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public RoleResponse save(
			/*
			 * @Parameter(description = "role request for the role to be saved", required =
			 * true)
			 */ @Valid @RequestBody RoleRequest request) {
		return roleService.save(request);
	}

	/**
	 * update the role and return the same role as role response if role not
	 * exist ,it will throw validation exception
	 *
	 * @Param id
	 * @Param request
	 *
	 * @return RoleResponse
	 */
	@PutMapping(value = "role/{roleId}", consumes = { "application/json" }, produces = "application/json")
//	@Operation(summary = "update the role", description = "Update the role and return the same role as role Response", tags = {
//			"Role" })
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RoleResponse.class))),
//			@ApiResponse(responseCode = "400", description = "If role already exist or authority ids are not valid", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(responseCode = "404", description = "roles not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public RoleResponse update(
			/*
			 * @Parameter(description = "Id of the role for which role to be update",
			 * required = true)
			 */ @PathVariable Long roleId,
			/*
			 * @Parameter(description = "role request for the role to be update", required =
			 * true)
			 */ @Valid @RequestBody RoleRequest request) {
		return roleService.update(roleId, request);
	}

	@PutMapping(produces = { "application/json" }, consumes = { "application/json" }, value = "role/{roleId}/activate")
//	@Operation(summary = "Activate Role", description = "return success message if role successfully activated", tags = {
//			"Role" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "Role successfully activated", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
//			@ApiResponse(description = "If user doesn't have access to activate role", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If role id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse activate(
			/* @Parameter(description = "Role id", required = true) */ @PathVariable Long roleId) {
		return roleService.activate(roleId);
	}

	@DeleteMapping(produces = { "application/json" }, value = "role/{roleId}")
//	@Operation(summary = "Delete Role", description = "return success message if role successfully deleted", tags = {
//			"Role" })
//	@ApiResponses(value = {
//			@ApiResponse(description = "Role successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
//			@ApiResponse(description = "If user doesn't have access to delete role", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If role id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
//			@ApiResponse(description = "If this role assigned to some of the users or role name is superadmin", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(
			/* @Parameter(description = "Role id", required = true) */ @PathVariable Long roleId) {
		return roleService.delete(roleId);
	}

}
