package com.nxtlife.mgs.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.Utils;

@Service
public class RoleServiceImpl {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	Utils utils;
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@PostConstruct
	public void init() {
		String[] roles = { "Teacher", "Coach", "Guardian" , "School" ,"Student" };
		for (String r : roles) {
			Role role = roleRepository.getOneByName(r);
			if (role == null) {
				role = new Role();
				role.setName(r);
				role.setCid(utils.generateRandomAlphaNumString(8));
			}
			roleRepository.save(role);
			logger.info(String.format("Created role : %s", r));
		}
	}
	
}
