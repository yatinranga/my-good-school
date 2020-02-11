package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.activity.FocusArea;

public interface FocusAreaRepository extends JpaRepository<FocusArea, Long> {

	FocusArea getOneByCid(String cid);
	
	FocusArea findByName(String name);
	}
