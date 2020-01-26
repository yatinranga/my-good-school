package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.user.Guardian;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {
	Guardian getOneBycId(String cId);
	
	Guardian getOneByEmail(String email);
	
	Guardian getOneByMobileNumber(String mobileNumber);

}
