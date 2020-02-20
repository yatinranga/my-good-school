package com.nxtlife.mgs.config.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.view.user.UserResponse;

//@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    	if (authentication == null || !authentication.isAuthenticated()) {
		    throw new ValidationException("No user found.");
		}
    	
    	if (authentication.getPrincipal() instanceof User) {
			User user= (User) authentication.getPrincipal();
			if(user==null)
	    		throw new ValidationException("No user found.");
			
			final Map<String, Object> additionalInfo = new HashMap<>();
//			additionalInfo.put("user_role",user.getRoleForUser());
			additionalInfo.put("user_type",user.getUserType().toString());
			
			
			 ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo); //User info added to token response
		}
//      additionalInfo.put("CUSTOM_PARAM1", "CUSTOM_VALUE1");
        return accessToken;
    }

}

