package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.activity.FocusArea;

@Repository
public interface FocusAreaRepository extends JpaRepository<FocusArea, Long> {

	FocusArea getOneByCidAndActiveTrue(String cid);
	
	FocusArea findByNameAndActiveTrue(String name);

	FocusArea findByCidAndActiveTrue(String cid);
	
	List<FocusArea> findAllByActivitiesSchoolsCidAndActiveTrue(String schoolCid);

	List<FocusArea> findAllByActiveTrue();
	}
