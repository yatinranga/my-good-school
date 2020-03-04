package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User getOneByCid(String cid);

	int countByUserNameAndActiveTrue(String username);
	
	public User findByUserName(String username);
	
	public User findOneByUserName(String username);
	
    User findByEmailOrMobileNo(String email,String contact);

    boolean existsByEmail(String email);

    boolean existsByMobileNo(String email);

    User findByMobileNo(String contactNo);

    User findByEmail(String email);

	User findByPassword(String passwordHash);

	User findByUserNameAndActiveTrue(String username);

	int countByMobileNo(String string);

	int countByEmail(String email);

}
