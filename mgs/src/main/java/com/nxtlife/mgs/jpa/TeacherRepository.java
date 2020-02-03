package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	Teacher getOneBycid(String cId);

	int countByEmail(String email);

	int countByUsername(String username);

	List<Teacher> findByName(String name);

	Teacher findByCid(String cId);

	Teacher findByMobileNumber(String mobileNumber);

	Teacher findByUsername(String username);

	List<Teacher> findAllByActivitiesName(String activityName);

	List<Teacher> findAllByActivitiesId(Long id);

}
