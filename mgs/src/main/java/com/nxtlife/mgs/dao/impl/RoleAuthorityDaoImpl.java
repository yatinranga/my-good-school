package com.nxtlife.mgs.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.dao.RoleAuthorityDao;
import com.nxtlife.mgs.entity.common.RoleAuthorityKey;
import com.nxtlife.mgs.entity.user.RoleAuthority;

@Repository("roleAuthorityDaoImpl")
public class RoleAuthorityDaoImpl extends BaseDao<RoleAuthorityKey, RoleAuthority> implements RoleAuthorityDao {

	@Override
	public int deleteByRoleIdAndAuthorityIds(Long roleId, List<Long> authorityIds) {
		return query("delete from RoleAuthority where role_id=:roleId and authority_id in :authorityIds")
				.setParameter("roleId", roleId).setParameter("authorityIds", authorityIds).executeUpdate();
	}

}
