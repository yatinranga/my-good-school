package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.enums.ActivityStatus;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Student getOneByCidAndActiveTrue(String cid);
	
	Student getByUserId(Long userId);

	int countByEmail(String email);

	int countByUsername(String username);

	List<Student> findAllBySchoolCid(String schooolCid);

	Student findByCidAndActiveTrue(String cid);

	List<Student> findAllBySchoolCidAndGradeCid(String schooolCid, String gradeCid);

	Student findById(Long id);

	void deleteByCid(String cid);

	List<Student> findAllBySchoolCidAndActiveTrue(String schoolCid);
	
	List<Student> findAllByGradeCidAndActiveTrue(String gradeCid);

	List<Student> findAllByActiveTrue();

	Student findByUsernameAndActiveTrue(String username);

	Student findByMobileNumberAndActiveTrue(String mobileNumber);

	Student findByIdAndActiveTrue(Long id);

	List<Student> findByNameAndActiveTrue(String name);

	Student findByCid(String cid);


	List<Student> findAllBySchoolCidAndSchoolActiveTrueAndGradeCidAndGradeActiveTrueAndActivitiesActivityCidAndActivitiesActivityActiveTrueAndActivitiesActivityStatusAndActivitiesTeacherCidAndActivitiesTeacherActiveTrueAndActiveTrue(
			String schoolCid, String gradeCid, String activityCid, ActivityStatus activityStatus, String teacherCid);

	List<Student> findAllBySchoolCidAndGradeCidAndActivitiesActivityCidAndActivitiesActivityStatusAndSchoolActiveTrueAndGradeActiveTrueAndActivitiesActivityActiveTrueAndActiveTrue(
			String schoolCid, String gradeCid, String activityCid, ActivityStatus valueOf);

	Boolean existsByCidAndActiveTrue(String studentCid);

	Student getByUserIdAndActiveTrue(Long userId);
	
	@Query(value = "select s.id from Student s where s.user.id = :userId and s.active = true")
	Long findIdByUserIdAndActiveTrue(@Param("userId") Long userId);

	List<Student> findAllBySchoolCidAndActivitiesActivityCidAndActivitiesActivityStatusAndSchoolActiveTrueAndActivitiesActivityActiveTrueAndActiveTrue(
			String schoolCid, String activityCid, ActivityStatus valueOf);

	@Query(value = "select s.id from Student s where s.cid = :cid and s.active = true")
	Long findIdByCidAndActiveTrue(@Param("cid")String cid);



	/*
	 * List<Student>
	 * findAllBySchoolCidAndGradeCidAndActivitiesActivityCidAndActivitiesActivityStatusAndActivitiesActivityActivityTrueAndGradeActiveTrueAndSchoolActiveTrueAndActiveTrue(
	 * String schoolCid, String gradeCid, String activityCid, ActivityStatus
	 * reviewed);
	 */
}
