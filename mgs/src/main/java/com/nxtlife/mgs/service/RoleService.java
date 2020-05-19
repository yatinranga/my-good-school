package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.security.RoleRequest;
import com.nxtlife.mgs.view.user.security.RoleResponse;

public interface RoleService {

	/**
	 * this method used to get roles related to that organization.
	 * 
	 * @return list of <tt>RoleResponse</tt>
	 * @throws NotFoundException
	 *             if organization id not found
	 */
	public List<RoleResponse> getAllRoles();

	/**
	 * this method used to save role details. If role already exist in database
	 * then it throws exception otherwise it return saved role details.
	 * 
	 * @param request
	 * @return <tt>RoleResponse</tt>
	 * @throws ValidationException
	 *             if role already exist in database or some of the authority
	 *             ids are not valid
	 */
	public RoleResponse save(RoleRequest request);

	/**
	 * this method used to fetch role details by id
	 * 
	 * @param id
	 * @return {@link RoleResponse}
	 * @throws NotFoundException
	 *             if role not found
	 */
	public RoleResponse findById(Long id);

	/**
	 * this method used to update role details. It throws exception if role id
	 * isn't correct or authority ids are incorrect.
	 * 
	 * @param roleId
	 * @param request
	 * @return {@link RoleResponse} after updating role information
	 * @throws NotFoundException
	 *             if role id isn't correct
	 * @throws ValidationException
	 *             if authority ids aren't correct or if updated role name is
	 *             already exist in database
	 */
	public RoleResponse update(Long roleId, RoleRequest request);

	/**
	 * this method used to activate role. It throws exception if role id not
	 * found or some users are assigned with this role
	 * 
	 * @param id
	 * @return {@link SuccessResponse} if user deleted successfully
	 * @throws NotFoundException
	 *             if role not found
	 */
	public SuccessResponse activate(Long id);

	/**
	 * this method used to delete role. It throws exception if role id not found
	 * or some users are assigned with this role
	 * 
	 * @param id
	 * @return {@link SuccessResponse} if user deleted successfully
	 * @throws NotFoundException
	 *             if role not found
	 * @throws ValidationException
	 *             if some of the users are assigned with this role or role name
	 *             is superadmin
	 */
	public SuccessResponse delete(Long id);
}
