package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.school.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>{

	Grade getOneBycId(String cId);
	
	Grade findByNameAndSchoolIdAndSection(String name, Long schoolId , String section);
	
	Grade findByNameAndSchoolId(String name, Long schoolId);
}
