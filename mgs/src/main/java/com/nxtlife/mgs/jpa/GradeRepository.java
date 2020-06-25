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

	public Grade getOneByCid(String cid);
	
	public Grade findByNameAndSchoolsIdAndSection(String name, Long schoolId , String section);
	
	public Grade findByNameAndSchoolsId(String name, Long schoolId);
	
	public Grade findByNameAndSchoolsCid(String name , String schoolCid);

	public List<Grade> findAllBySchoolsCidAndActiveTrue(String schoolCid);

	public Grade findByCidAndActiveTrue(String gradeCid);

	public Grade findByNameAndSectionAndActiveTrue(String grade, String section);

	public Grade findByNameAndSchoolsCidAndSection(String string, String schoolCid, String section);

	public boolean existsByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(String gradeId, String cid);
	
	public Grade findByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(String gradeId, String cid);

	public boolean existsByCidAndActiveTrue(String gradeCid);

//	@Query("SELECT CASE WHEN count(g) > 0 THEN true ELSE false END FROM Grade g where g.cid = :gradeCid AND g.schools.cid =:schoolCid AND g.active = true")
	public boolean existsBySchoolsCidAndCidAndActiveTrue(String schoolCid, String gradeCid);
	
	@Query(value = "select a.id from Grade a where a.cid = :cid and a.active = true")
	public Long findIdByCidAndActiveTrue(@Param("cid") String cid);
	
	@Query(value = "select g.cid from Grade g join g.students ts where ts.cid = ?1 and g.active = true")
	public String findIdByStudentCidAndActiveTrue(String cid);
	
	@Query("Select distinct g.id from Grade g join g.teachers ts where ts.cid = ?1 and g.active = true")
	public List<Long> findGradeIdsOfTeacher(String teacherCid);
	
	@Query("Select distinct g.id from Grade g join g.teachers ts where ts.id = ?1 and g.active = true")
	public List<Long> findGradeIdsOfTeacher(Long teacherId);

	@Query("Select distinct g.cid from Grade g join g.schools s where s.cid = ?1 and g.active = true")
	public List<String> findAllCidBySchoolsCidAndActiveTrue(String schoolCid);
	
	@Query("Select distinct g.id from Grade g join g.schools s where s.cid = ?1 and g.active = true")
	public List<Long> findAllIdBySchoolsCidAndActiveTrue(String schoolCid);

	@Query("Select distinct g.cid from Grade g join g.schools s join g.teachers ts where s.cid = ?1 and ts.id = ?2 and g.active = true")
	public List<String> findAllCidBySchoolsCidAndTeacherIdActiveTrue(String schoolCid, Long teacherId);
	
	@Query("Select distinct g.id from Grade g join g.schools s join g.teachers ts where s.cid = ?1 and ts.id = ?2 and g.active = true")
	public List<Long> findAllIdBySchoolsCidAndTeacherIdActiveTrue(String schoolCid, Long teacherId);
	
}
