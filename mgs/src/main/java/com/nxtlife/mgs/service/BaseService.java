package com.nxtlife.mgs.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
import com.nxtlife.mgs.entity.user.User;

public abstract class BaseService {

	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		if (authentication.getPrincipal() instanceof User) {
			User user = ((User) authentication.getPrincipal());
			user.setId(user.getUserId());
			return user;
		}
		return null;
	}
	
	public static Long getUserId() {
		User user = getUser();
		if(user!=null){
			return user.getUserId();
		}
		return null;
	}
}
