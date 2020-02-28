package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Guardian;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
	Guardian getOneByCid(String cid);
	
	Guardian getOneByEmail(String email);
	
	List<Guardian> findByEmailOrMobileNumber(String email, String mobileNumber);
	
	Guardian getOneByMobileNumber(String mobileNumber);

}
