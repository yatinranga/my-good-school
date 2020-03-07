package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.school.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>{

	Grade getOneByCid(String cid);
	
	Grade findByNameAndSchoolsIdAndSection(String name, Long schoolId , String section);
	
	Grade findByNameAndSchoolsId(String name, Long schoolId);
	
	Grade findByNameAndSchoolsCid(String name , String schoolCid);

	List<Grade> findAllBySchoolsCidAndActiveTrue(String schoolCid);

	Grade findByCidAndActiveTrue(String gradeCid);

	Grade findByNameAndSectionAndActiveTrue(String grade, String section);

	Grade findByNameAndSchoolsCidAndSection(String string, String schoolCid, String section);
}
