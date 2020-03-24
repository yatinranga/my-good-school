package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.user.LFIN;

public interface LFINRepository extends JpaRepository<LFIN	, Long> {

	boolean existsByCidAndActiveTrue(String cid);
	
	boolean existsByEmailAndActiveTrue(String email);
}
