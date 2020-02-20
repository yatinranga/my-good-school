package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User getOneByCid(String cid);

	int countByUserNameAndActiveTrue(String username);
	
	public User findByUserName(String username);
	
	public User findOneByUserName(String username);
	
    User findByEmailOrContactNo(String email,String contact);

    boolean existsByEmail(String email);

    boolean existsByContactNo(String email);

    User findByContactNo(String contactNo);

    User findByEmail(String email);

	User findByPassword(String passwordHash);

	User findByUserNameAndActiveTrue(String username);

}
