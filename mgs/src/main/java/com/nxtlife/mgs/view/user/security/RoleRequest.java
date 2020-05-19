package com.nxtlife.mgs.view.user.security;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.view.Request;

public class RoleRequest extends Request {

	@NotNull(message = "Role name can't be null")
	private String name;

	@NotEmpty(message = "Authority ids can't be null or empty")
	private Set<Long> authorityIds;

	public Role toEntity() {
		Role role = new Role();
		role.setName(name);
		return role;
	}

	public String getName() {
		return name;
	}

	public Set<Long> getAuthorityIds() {
		return authorityIds;
	}
}
