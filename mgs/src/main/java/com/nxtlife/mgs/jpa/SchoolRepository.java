package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.school.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

	School getOneByCidAndActiveTrue(String cid);
	
	School findByNameOrEmail(String name , String email);
	
	School findByName(String name);
	
	School findByEmail(String email);

	int countByEmailAndActiveTrue(String email);

	int countByUsername(String username);

	School findByCidAndActiveTrue(String cid);

	School findById(Long id);

	List<School> findAllByActiveTrue();
	
	
}
