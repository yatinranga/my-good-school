package com.nxtlife.mgs.service;


import java.util.List;

import com.nxtlife.mgs.entity.user.Authority;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.view.user.security.AuthorityResponse;

public interface AuthorityService {

	public void save(Authority authority);

	/**
	 * this method used to fetch authority using id
	 * 
	 * @param id
	 * @return <tt>Authority</tt>
	 * @throws NotFoundException
	 *             if authority not found
	 */
	public Authority findById(long id);

	/**
	 * this method used to fetch all authorities
	 * 
	 * @return list of <tt>AuthorityResponse</tt>
	 */
	public List<AuthorityResponse> getAllAuthorities();

	/**
	 * this method used to fetch all authorities by role
	 * 
	 * @param roleId
	 * @return list of <tt>AuthorityResponse</tt>
	 * @throws NotFoundException
	 *             if role id not found
	 */
	public List<AuthorityResponse> getAllAuthoritiesByRoleId(Long roleId);

}

