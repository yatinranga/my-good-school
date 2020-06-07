package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.user.LFIN;

public interface LFINRepository extends JpaRepository<LFIN	, Long> {

	public boolean existsByCidAndActiveTrue(String cid);
	
	public boolean existsByEmailAndActiveTrue(String email);
}
