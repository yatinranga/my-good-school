package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.view.SchoolResponse;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

	School getOneByCidAndActiveTrue(String cid);

	School findByNameOrEmail(String name, String email);

	School findByNameAndActiveTrue(String name);

	School findByEmail(String email);

	int countByEmailAndActiveTrue(String email);

	int countByUsername(String username);

	School findByCidAndActiveTrue(String cid);

	List<School> findAllByActiveTrue();

	Boolean existsByCidAndActiveTrue(String schoolId);

	School getOneByCid(String schoolRequest);

	boolean existsByNameAndActiveTrue(String string);

	School getByUserId(Long userId);

	boolean existsByUserIdAndActiveTrue(Long userId);

	boolean existsByNameAndCidNotAndActiveTrue(String name, String id);

	boolean existsByAddressAndCidNotAndActiveTrue(String address, String id);

	public SchoolResponse findResponseById(Long schoolId);

	@Query(value = "select id from School where name=?1")
	public Long findIdByName(String name);
	
	@Query(value = "select id from School where cid=?1")
	public Long findIdByCid(String cid);
	
	@Query(value = "select cid from School where id=?1")
	public String findCidById(Long cid);

	@Modifying
	@Query(value = "update School s set s.active = ?2 where s.cid = ?1 and s.active = true")
	int deleteByCidAndActiveTrue(String cid,Boolean active);

}
