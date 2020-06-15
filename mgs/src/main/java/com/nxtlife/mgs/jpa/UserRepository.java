package com.nxtlife.mgs.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.view.user.UserResponse;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User getOneByCid(String cid);

	int countByUsernameAndActiveTrue(String username);
	
	public User findByUsername(String username);
	
	public User findOneByUsername(String username);

    User findByEmailOrContactNumber(String email,String contactNumber);

    boolean existsByEmail(String email);

    boolean existsByContactNumber(String email);

    User findByContactNumber(String contactNumber);

    User findByEmail(String email);

	User findByPassword(String passwordHash);

	User findByUsernameAndActiveTrue(String username);

	int countByContactNumber(String string);

	int countByEmail(String email);

	User findByIdAndActiveTrue(Long id);

	boolean existsByEmailAndCid(String email, String cid);

	boolean existsByEmailAndCidNot(String email, String cid);

	boolean existsByContactNumberAndCid(String contactNumber, String cid);

	boolean existsByContactNumberAndCidNot(String contactNumber, String cid);
	
	public User findByUsernameAndSchoolCid(String username, String schoolId);
	
	public List<UserResponse> findBySchoolCid(String schoolId);
	
	public UserResponse findResponseByCid(String id);
	
	public Boolean existsByUsernameAndSchoolCid(String username, String schoolId);
	
	public Boolean existsByContactNumberOrEmailAndSchoolCidAndContactNumberNotNull(String contactNumber, String email,
			String schoolId);
	
	@Query(value = "select id from User  u where u.school.cid = ?1 and u.contactNumber = ?2 and active = ?3")
	public Long findIdBySchoolCidAndContactNumberAndActive(String schoolId, String contactNumber, Boolean active);
	
	@Query(value = "select id from User u where u.school.cid=?1 and email = ?2 and active = ?3")
	public Long findIdBySchoolCidAndEmailAndActive(String schoolId, String email, Boolean active);
	
	@Query(value = "select u.email, u.contactNumber from User u where u.username =?1 and u.school.cid=?2")
	public Map<String, String> findEmailAndContactByUsernameAndSchoolCid(String username, String schoolId);
	
	@Query(value = "select u.password from User u where cid = ?1")
	public String findPasswordByCid(String id);
	
	@Query(value = "select u.generatedPassword from User u where u.username = ?1 and u.school.id = ?2")
	public String findGeneratedPasswordByUsernameAndSchoolCid(String username, String schoolId);
	
	
	@Modifying
	@Query(value = "update User u set u.password=:password where u.username =:username and u.school.cid=:schoolId")
	public int setPassword(@Param("username") String username, @Param("schoolId") String schoolId,
			@Param("password") String password);
	
	@Modifying
	@Query(value = "update User u set u.password=:password where cid =:cid")
	public int setPassword(@Param("cid") String cid, @Param("password") String password);
	
	@Modifying
	@Query(value = "update User u set u.generatedPassword=:password where u.username =:username and u.school.cid=:schoolId")
	public int setGeneratedPassword(@Param("username") String username, @Param("schoolId") String schoolId,
			@Param("password") String password);
	
	@Modifying
	@Query(value = "update User u set u.active=true, u.lastModifiedBy =?2, u.lastModifiedDate =?3 where u.cid =?1")
	public int activate(String id, Long userId, Date date);

	@Modifying
	@Query(value = "update User u set u.active=false, u.lastModifiedBy =?2, u.lastModifiedDate =?3 where u.cid =?1")
	public int delete(String id, Long userId, Date date);
	
	/*  Imported Code */
	

	public User findByUsernameAndSchoolNull(String username);

	public User findByUsernameAndSchoolId(String username, Long schoolId);

	public List<UserResponse> findBySchoolId(Long schoolId);
	
	public UserResponse findResponseById(Long id);

	public Boolean existsByUsernameAndSchoolId(String username, Long schoolId);

	public Boolean existsByContactNumberOrEmailAndSchoolIdAndContactNumberNotNull(String contactNumber, String email,
			Long schoolId);

	@Query(value = "select id from User  u where u.school.id = ?1 and u.contactNumber = ?2 and active = ?3")
	public Long findIdBySchoolIdAndContactNumberAndActive(Long schoolId, String contactNumber, Boolean active);

	@Query(value = "select id from User u where u.school.id=?1 and email = ?2 and active = ?3")
	public Long findIdBySchoolIdAndEmailAndActive(Long schoolId, String email, Boolean active);

	@Query(value = "select u.email, u.contactNumber from User u where u.username =?1 and u.school.id=?2")
	public Map<String, String> findEmailAndContactByUsernameAndSchoolId(String username, Long schoolId);

	@Query(value = "select u.password from User u where id = ?1")
	public String findPasswordById(Long id);

	@Query(value = "select u.generatedPassword from User u where u.username = ?1 and u.school.id = ?2")
	public String findGeneratedPasswordByUsernameAndSchoolId(String username, Long schoolId);

	@Modifying
	@Query(value = "update user set password=:password where username =:username and school_id=:schoolId", nativeQuery = true)
	public int setPassword(@Param("username") String username, @Param("schoolId") Long schoolId,
			@Param("password") String password);

	@Modifying
	@Query(value = "update user set password=:password where id =:id", nativeQuery = true)
	public int setPassword(@Param("id") Long id, @Param("password") String password);

	@Modifying
	@Query(value = "update user set generated_password=:password where username =:username and school_id=:schoolId", nativeQuery = true)
	public int setGeneratedPassword(@Param("username") String username, @Param("schoolId") Long schoolId,
			@Param("password") String password);

	@Modifying
	@Query(value = "update User u set u.active=true, u.lastModifiedBy.id =?2, u.lastModifiedDate =?3 where u.id =?1")
	public int activate(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update User u set u.active=false, u.lastModifiedBy.id =?2, u.lastModifiedDate =?3 where u.id =?1")
	public int delete(Long id, Long userId, Date date);

	User findByCid(String userId);

	boolean existsByCid(String id);
	
	@Query(value = "select u.id from User u where u.cid = ?1 and u.active = true")
	public Long findIdByCid(String cid);

	List<UserResponse> findBySchoolIdAndIdIn(Long schoolId, Set<Long> findUserIdsByRoleId);
	
	

}
