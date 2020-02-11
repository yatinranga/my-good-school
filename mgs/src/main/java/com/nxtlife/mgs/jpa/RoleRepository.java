package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role getOneByName(String name);
    
    Role getOneByCid(String cid);
    
    Role findByName(String name);
}
