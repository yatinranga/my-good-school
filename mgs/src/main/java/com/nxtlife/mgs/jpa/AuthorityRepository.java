package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.user.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	boolean existsByName(String name);
}
