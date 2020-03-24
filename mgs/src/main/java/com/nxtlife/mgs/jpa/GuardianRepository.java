package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Guardian;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
	Guardian getOneByCid(String cid);
	
	Guardian getOneByEmail(String email);
	
	Guardian findByEmailOrMobileNumber(String email, String mobileNumber);
	
	Guardian getOneByMobileNumber(String mobileNumber);

	Guardian findByMobileNumberAndActiveTrue(String mobileNumber);

	Guardian findByEmailAndActiveTrue(String email);

	boolean existsByCidAndActiveTrue(String cid);

	Guardian findByCidAndActiveTrue(String id);

}
