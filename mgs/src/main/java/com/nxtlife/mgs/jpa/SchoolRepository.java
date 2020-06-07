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

	public School getOneByCidAndActiveTrue(String cid);

	public School findByNameOrEmail(String name, String email);

	public School findByNameAndActiveTrue(String name);

	public School findByEmail(String email);

	public int countByEmailAndActiveTrue(String email);

	public int countByUsername(String username);

	public School findByCidAndActiveTrue(String cid);

	public List<School> findAllByActiveTrue();

	public Boolean existsByCidAndActiveTrue(String schoolId);

	public School getOneByCid(String schoolRequest);

	public boolean existsByNameAndActiveTrue(String string);

	public School getByUserId(Long userId);

	public boolean existsByUserIdAndActiveTrue(Long userId);

	public boolean existsByNameAndCidNotAndActiveTrue(String name, String id);

	public boolean existsByAddressAndCidNotAndActiveTrue(String address, String id);

	public SchoolResponse findResponseById(Long schoolId);

	@Query(value = "select id from School where name=?1")
	public Long findIdByName(String name);
	
	@Query(value = "select id from School where cid=?1")
	public Long findIdByCid(String cid);
	
	@Query(value = "select cid from School where id=?1")
	public String findCidById(Long cid);

	@Modifying
	@Query(value = "update School s set s.active = ?2 where s.cid = ?1 and s.active = true")
	public int deleteByCidAndActiveTrue(String cid,Boolean active);

}
