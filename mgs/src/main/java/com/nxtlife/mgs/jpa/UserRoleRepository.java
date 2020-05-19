package com.nxtlife.mgs.jpa;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.common.UserRoleKey;
import com.nxtlife.mgs.entity.user.UserRole;


public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {

	@Modifying
	@Query(value = "insert into role_user(user_id,role_id) values (?1,?2)", nativeQuery = true)
	public int save(Long userId, Long roleId);

	@Query(value = "select role_id from role_user where user_id=?1", nativeQuery = true)
	public Set<Long> findRoleIdsByUserId(Long userId);
	
	@Query(value = "select ur.role.id from UserRole ur where ur.user.cid=?1")
	public Set<Long> findRoleIdsByUserCid(String userId);

	@Query(value = "select role_id from role_user u_r inner join role r on u_r.role_id=r.id where user_id=?1 and r.active = ?2", nativeQuery = true)
	public Set<Long> findRoleIdsByUserIdAndRoleActive(Long userId, Boolean roleActive);
	
	@Query(value = "select ur.role.id from UserRole ur where ur.user.cid=?1 and ur.user.active =?2")
	public Set<Long> findRoleIdsByUserCidAndRoleActive(String userId, Boolean roleActive);

	@Query(value = "SELECT * FROM role_user u_r inner join role r where u_r.user_id = ?1 and u_r.role_id=r.id", nativeQuery = true)
	public List<UserRole> findByUserId(Long userId);
	
	public List<UserRole> findByUserCid(String userId);

	@Query(value = "select ur.user.id from UserRole ur where ur.role.id=:roleId")
	public Set<Long> findUserIdsByRoleId(@Param("roleId") Long roleId);
	
	@Query(value = "select ur.user.cid from UserRole ur where ur.role.id=:roleId")
	public Set<String> findUserCidsByRoleId(@Param("roleId") Long roleId);

	@Modifying
	@Query(value = "delete from role_user where user_id=?1 and role_id=?2", nativeQuery = true)
	public int delete(Long userId, Long roleId);
	
	@Modifying
	@Query(value = "delete from UserRole ur where ur.user.cid=?1 and ur.role.id=?2")
	public int delete(String userId, Long roleId);

}

