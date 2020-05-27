package com.nxtlife.mgs.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	boolean existsByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(String gradeId, String cid);
	
	Grade findByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(String gradeId, String cid);

	boolean existsByCidAndActiveTrue(String gradeCid);

//	@Query("SELECT CASE WHEN count(g) > 0 THEN true ELSE false END FROM Grade g where g.cid = :gradeCid AND g.schools.cid =:schoolCid AND g.active = true")
	boolean existsBySchoolsCidAndCidAndActiveTrue(String schoolCid, String gradeCid);
	
	@Query(value = "select a.id from Grade a where a.cid = :cid and a.active = true")
	Long findIdByCidAndActiveTrue(@Param("cid") String cid);
	
	@Query("Select distinct g.id from Grade g join g.teachers ts where ts.cid = ?1")
	public List<Long> findGradeIdsOfTeacher(String teacherCid);
	
	@Query("Select distinct g.id from Grade g join g.teachers ts where ts.id = ?1")
	public List<Long> findGradeIdsOfTeacher(Long teacherId);
}
