package com.nxtlife.mgs.dao;

import java.util.List;

import com.nxtlife.mgs.entity.user.RoleAuthority;

public interface RoleAuthorityDao {
	
	public RoleAuthority save(RoleAuthority roleAuthority);

	public int deleteByRoleIdAndAuthorityIds(Long roleId, List<Long> authorityIds);

}
