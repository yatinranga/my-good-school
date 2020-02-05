package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.school.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

	School getOneByCid(String cid);
	
	School findByNameOrEmail(String name , String email);
	
	School findByName(String name);
	
	School findByEmail(String email);

	int countByEmail(String email);

	int countByUsername(String username);
	
	
}
