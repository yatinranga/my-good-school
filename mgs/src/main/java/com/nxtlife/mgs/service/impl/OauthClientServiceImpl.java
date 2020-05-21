package com.nxtlife.mgs.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.oauth.OauthClientDetails;
import com.nxtlife.mgs.jpa.OauthClientDetailsJpaDao;

@Service("oauthClientService")
public class OauthClientServiceImpl {

	@Autowired
	private OauthClientDetailsJpaDao oauthClientDetailsDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		if (oauthClientDetailsDao.findByClientId("my-good-school") == null) {
			OauthClientDetails oauthClientDetails = new OauthClientDetails();
			oauthClientDetails.setAccessTokenValidity(-1);
			oauthClientDetails.setScope("read,write");
			oauthClientDetails.setClientId("my-good-school");
			oauthClientDetails.setAuthorizedGrantTypes("password");
			oauthClientDetails.setAutoapprove("1");
			oauthClientDetails.setClientSecret(passwordEncoder.encode("nxtlife"));
			oauthClientDetails.setRefreshTokenValidity(-1);
			oauthClientDetails.setResourceIds("mgs-api");
			oauthClientDetailsDao.save(oauthClientDetails);
		}
	}

}
