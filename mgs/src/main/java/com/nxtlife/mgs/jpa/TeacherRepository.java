package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	Teacher getOneByCidAndActiveTrue(String cid);
	
	Teacher getByUserId(Long userId);

	int countByEmailAndActiveTrue(String email);

	int countByUsernameAndActiveTrue(String username);

	Teacher findByNameAndActiveTrue(String name);

	Teacher findByCidAndActiveTrue(String cid);

	Teacher findByCidAndIsCoachTrueAndActiveTrue(String cid);

	Teacher findByCidAndIsClassTeacherTrueAndActiveTrue(String cid);

	Teacher findByIdAndIsCoachTrueAndActiveTrue(Long id);

	Teacher findByIdAndIsClassTeacherTrueAndActiveTrue(Long id);

	Teacher findByMobileNumberAndActiveTrue(String mobileNumber);

	Teacher findByUsernameAndActiveTrue(String username);

	List<Teacher> findAllBySchoolCidAndActivitiesNameAndIsCoachTrueAndActiveTrue(String schoolCid, String activityName);

	List<Teacher> findAllBySchoolCidAndActivitiesCidAndIsCoachTrueAndActiveTrue(String schoolCid, String activityCid);

	List<Teacher> findAllBySchoolCidAndIsCoachTrueAndActiveTrue(String schoolCid);

	List<Teacher> findAllBySchoolCidAndIsClassTeacherTrueAndActiveTrue(String schoolCid);

	List<Teacher> findAllBySchoolCidAndActiveTrue(String schoolCid);

	List<Teacher> findAllByIsCoachTrueAndActiveTrue();

	List<Teacher> findAllByIsClassTeacherTrueAndActiveTrue();

	Teacher findByIdAndActiveTrue(Long id);

	Page<Teacher> findAllByActiveTrue(Pageable paging);

	Boolean existsByCidAndActiveTrue(String teacherId);

	List<Teacher> findAllBySchoolCidAndIsManagmentMemberTrueAndSchoolActiveTrueAndActiveTrue(String schoolCid);

	String findNameByCidAndActiveTrue(String assignerCid);

	boolean existsByUserIdAndIsManagmentMemberTrueAndActiveTrue(Long userId);

//	List<Teacher> findAllByActivitiesCid(String cid);

}
