package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Guardian;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
	public Guardian getOneByCid(String cid);
	
	public Guardian getOneByEmail(String email);
	
	public Guardian findByEmailOrMobileNumber(String email, String mobileNumber);
	
	public Guardian getOneByMobileNumber(String mobileNumber);

	public Guardian findByMobileNumberAndActiveTrue(String mobileNumber);

	public Guardian findByEmailAndActiveTrue(String email);

	public boolean existsByCidAndActiveTrue(String cid);

	public Guardian findByCidAndActiveTrue(String id);

}
