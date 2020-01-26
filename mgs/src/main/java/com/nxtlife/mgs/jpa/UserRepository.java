package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User getOneBycId(String cId);

	int countByUsername(String username);

}
