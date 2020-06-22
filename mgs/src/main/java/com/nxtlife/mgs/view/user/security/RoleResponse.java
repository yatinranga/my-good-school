package com.nxtlife.mgs.view.user.security;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.user.Role;

@SuppressWarnings("serial")
@JsonInclude(value = Include.NON_ABSENT)
public class RoleResponse implements Serializable {

	public String name;

	public Long id;

	public Boolean active;

	public List<AuthorityResponse> authorities;

	public RoleResponse(Long id, String name, Boolean active) {
		this.id = id;
		this.name = name;
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<AuthorityResponse> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<AuthorityResponse> authorities) {
		this.authorities = authorities;
	}

	public static RoleResponse get(Role role) {
		return new RoleResponse(role.getId(), role.getName(), role.getActive());
	}
}
