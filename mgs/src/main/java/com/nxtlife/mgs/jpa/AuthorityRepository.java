package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.mgs.entity.user.Authority;
import com.nxtlife.mgs.view.user.security.AuthorityResponse;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	public boolean existsByName(String name);

	public Authority getOneByName(String name);

//	Authority getOneByCid(String cid);
	/* Imported Code */


	public AuthorityResponse findResponseById(Long id);

	public List<AuthorityResponse> findByAuthorityRolesRoleId(Long roleId);

	@Query(value = "select id from authority", nativeQuery = true)
	public List<Long> findAllIds();
}
