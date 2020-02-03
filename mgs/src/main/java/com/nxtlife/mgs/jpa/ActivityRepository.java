package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.activity.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>{

	Activity getOneByName(String name);
	
	Activity getOneBycId(String cId);
	
	List<Activity> findAllByTeachersId(Long id);
	
	List<Activity> findAllByTeachersCid(String cId);
	
}
