package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	Teacher getOneByCid(String cid);

	int countByEmail(String email);

	int countByUsername(String username);

	Teacher findByName(String name);

	Teacher findByCid(String cid);
	
	Teacher findByCidAndIsCoachTrue(String cid);
	
	Teacher findByCidAndIsClassTeacherTrue(String cid);
	
	Teacher findByIdAndIsCoachTrue(Long id);
	
	Teacher findByIdAndIsClassTeacherTrue(Long id);

	Teacher findByMobileNumber(String mobileNumber);

	Teacher findByUsername(String username);

	List<Teacher> findAllBySchoolCidAndActivitiesNameAndIsCoachTrue(String schoolCid,String activityName);

	List<Teacher> findAllBySchoolCidAndActivitiesCidAndIsCoachTrue(String schoolCid ,String activityCid);
	
	List<Teacher> findAllBySchoolCidAndIsCoachTrue(String schoolCid);
	
	List<Teacher> findAllBySchoolCidAndIsClassTeacherTrue(String schoolCid);
	
	List<Teacher> findAllBySchoolCid(String schoolCid);

	List<Teacher> findAllByIsCoachTrue();

	List<Teacher> findAllByIsClassTeacherTrue();
	
//	List<Teacher> findAllByActivitiesCid(String cid);

}
