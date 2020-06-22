package com.nxtlife.mgs.config.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.view.user.security.RoleResponse;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new ValidationException("No user found.");
		}

		if (authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			if (user == null)
				throw new ValidationException("No user found.");

			final Map<String, Object> additionalInfo = new HashMap<>();
			Set<RoleResponse> roles = user.getRoles().stream().map(RoleResponse::get).collect(Collectors.toSet());
			roles = roles == null || roles.isEmpty() ? user.getUserRoles().stream().map(ur -> RoleResponse.get(ur.getRole())).collect(Collectors.toSet()) : roles;
			additionalInfo.put("user_role",roles
					);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo); // User
																								// info
																								// added
																								// to
																								// token
																								// response
		}
		return accessToken;
	}

}
