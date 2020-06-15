package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.enums.ActivityStatus;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	public Student getOneByCidAndActiveTrue(String cid);

	public Student getByUserId(Long userId);

	public int countByEmail(String email);

	public int countByUsername(String username);

	public List<Student> findAllBySchoolCid(String schooolCid);

	public Student findByCidAndActiveTrue(String cid);

	public List<Student> findAllBySchoolCidAndGradeCid(String schooolCid, String gradeCid);

	public int deleteByCid(String cid);

	public List<Student> findAllBySchoolCidAndActiveTrue(String schoolCid);
	
	public List<Student> findAllBySchoolCidAndGradeCidInAndActiveTrue(String schoolCid ,Collection<String> gradeIds);

	public List<Student> findAllByGradeCidAndActiveTrue(String gradeCid);

	public List<Student> findAllByActiveTrue();

	public Student findByUsernameAndActiveTrue(String username);

	public Student findByMobileNumberAndActiveTrue(String mobileNumber);

	public Student findByIdAndActiveTrue(Long id);

	public List<Student> findByNameAndActiveTrue(String name);

	public Student findByCid(String cid);

	public List<Student> findAllBySchoolCidAndSchoolActiveTrueAndGradeCidAndGradeActiveTrueAndActivitiesActivityCidAndActivitiesActivityActiveTrueAndActivitiesActivityStatusAndActivitiesTeacherCidAndActivitiesTeacherActiveTrueAndActiveTrue(
			String schoolCid, String gradeCid, String activityCid, ActivityStatus activityStatus, String teacherCid);

	public List<Student> findAllBySchoolCidAndGradeCidAndActivitiesActivityCidAndActivitiesActivityStatusAndSchoolActiveTrueAndGradeActiveTrueAndActivitiesActivityActiveTrueAndActiveTrue(
			String schoolCid, String gradeCid, String activityCid, ActivityStatus valueOf);

	public Boolean existsByCidAndActiveTrue(String studentCid);

	public Student getByUserIdAndActiveTrue(Long userId);

	@Query(value = "select s.id from Student s where s.user.id = :userId and s.active = true")
	public Long findIdByUserIdAndActiveTrue(@Param("userId") Long userId);
	
	@Query(value = "select s.cid from Student s where s.user.id = :userId and s.active = true")
	public String findCidByUserIdAndActiveTrue(@Param("userId") Long userId);
	
	@Query(value = "select s.id as id , s.cid as cid from Student s where s.user.id = :userId and s.active = true")
	public Map<String, Object> findIdAndCidByUserIdAndActiveTrue(@Param("userId") Long userId);

	public List<Student> findAllBySchoolCidAndActivitiesActivityCidAndActivitiesActivityStatusAndSchoolActiveTrueAndActivitiesActivityActiveTrueAndActiveTrue(
			String schoolCid, String activityCid, ActivityStatus valueOf);

	@Query(value = "select s.id from Student s where s.cid = :cid and s.active = true")
	public Long findIdByCidAndActiveTrue(@Param("cid") String cid);

	public List<Student> findAllBySchoolCidAndGradeCidAndActiveTrue(String schoolId, String gradeId);
	
	@Modifying
	@Query(value = "update Student s set s.active = ?2 where s.cid = ?1 and s.active = true")
	public int deleteByCidAndActiveTrue(String cid ,Boolean active);
	
	@Query(value = "select s.grade.cid from Student s where s.cid = ?1 and s.active = true")
	public String findGradeCidByCidAndActiveTrue(String cid);
	
	@Query(value = "select s.studentClubs from Student s where s.cid = ?1 and s.active = true")
	public List<StudentClub> findGradeCidAndClubsByCidAndActiveTrue(String cid);

	public boolean existsByUserIdAndActiveTrue(Long userId);
	
	@Query(value = "select distinct s.cid from Student s join s.guardians g where g.cid = ?1 and s.active = true")
	public List<String> findCidByGuardianCid(String guardianCid);

	/*
	 * List<Student>
	 * findAllBySchoolCidAndGradeCidAndActivitiesActivityCidAndActivitiesActivityStatusAndActivitiesActivityActivityTrueAndGradeActiveTrueAndSchoolActiveTrueAndActiveTrue(
	 * String schoolCid, String gradeCid, String activityCid, ActivityStatus
	 * reviewed);
	 */
}
