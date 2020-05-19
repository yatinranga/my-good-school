package com.nxtlife.mgs.dao.impl;

import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.dao.RoleDao;
import com.nxtlife.mgs.entity.user.Role;

@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDao<Long, Role> implements RoleDao {

}
