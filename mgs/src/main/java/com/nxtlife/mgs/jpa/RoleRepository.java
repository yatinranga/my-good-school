package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.view.user.security.RoleResponse;

@Repository(value = "roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role getOneByName(String name);

	Role getOneById(Long id);

	Role findByName(String name);

	boolean existsByName(String string);

	boolean existsByNameAndSchoolCid(String name, String schoolCid);

	boolean existsByNameAndSchoolId(String name, Long schoolId);

	Role findBySchoolIdAndName(Long schoolId, String name);

	/* Imported Code */

	public List<RoleResponse> findBySchoolId(Long schoolId);

	public List<RoleResponse> findBySchoolCid(String schoolId);

	public Role findBySchoolCidAndName(String schoolId, String name);

	public RoleResponse findResponseById(Long id);

	public Set<RoleResponse> findByRoleUsersUserId(Long userId);

	public Set<RoleResponse> findByRoleUsersUserCid(String userId);

	public Boolean existsRoleByNameAndSchoolId(String name, Long schoolId);

	public Boolean existsRoleByNameAndSchoolCid(String name, String schoolId);

	@Query(value = "select r.id from Role r where r.name=?1 ")
	public Long findIdByName(String name);

	@Query(value = "select r.id from Role r where r.name=?1 and r.school.id=?2")
	public Long findIdByNameAndSchoolId(String name, Long schoolId);

	@Query(value = "select r.name from Role r where r.school.id=?1")
	public Set<String> findNameBySchoolId(Long schoolId);

	@Query(value = "select r.id from Role r where r.name=?1 and r.school.cid=?2")
	public Long findIdByNameAndSchoolCid(String name, String schoolId);

	@Query(value = "select r.id from Role r where r.name=?1")
	public Set<Long> findIdsByName(String name);

	@Query(value = "select r.id from Role r")
	public List<Long> getAllIds();

	@Query(value = "select r.id from Role r where r.active=?1")
	public List<Long> getAllIdsByActive(Boolean active);

	@Query(value = "select r.id from Role r where r.school.id=?1 and active=?2")
	public Set<Long> findIdsBySchoolIdAandActive(Long schoolId, Boolean active);

	@Query(value = "select r.id from Role r where r.school.cid=?1 and r.active=?2")
	public Set<Long> findIdsBySchoolCidAandActive(String schoolId, Boolean active);

	@Modifying
	@Query(value = "update Role r set r.name = ?1, r.lastModifiedBy.id =?3, r.lastModifiedDate =?4 where r.id = ?2")
	public int updateName(String name, Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update Role r set r.active = true, r.lastModifiedBy.id =?2, r.lastModifiedDate =?3 where r.id =?1")
	public int activate(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update Role r set r.active = false, r.lastModifiedBy.id =?2, r.lastModifiedDate =?3 where r.id =?1")
	public int delete(Long id, Long userId, Date date);

	@Query(value = "select r from Role r where r.school.id=?1 and r.name in ?2")
	List<Role> findAllBySchoolIdAndNameIn(Long id, Collection<String> existingRoles);

	boolean existsById(Long roleId);
}
