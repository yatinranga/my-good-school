package com.nxtlife.mgs.view.user.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.user.Authority;


@JsonInclude(value = Include.NON_ABSENT)
public class AuthorityResponse  {

	private Long id;

	private String name;

	private String groupName;

	private String description;

	public AuthorityResponse() {
	}

	public AuthorityResponse(Authority authority) {
		if (authority != null) {
			this.id = authority.getId();
			this.name = authority.getName();
			this.description = authority.getDescription();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
