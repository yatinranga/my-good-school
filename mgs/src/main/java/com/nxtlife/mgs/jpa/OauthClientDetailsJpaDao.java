package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.mgs.entity.oauth.OauthClientDetails;

public interface OauthClientDetailsJpaDao extends JpaRepository<OauthClientDetails, String> {

	public OauthClientDetails findByClientId(String clientId);

	@Query(value = "select clientId from OauthClientDetails")
	public List<String> findClientIds();

}
