package com.nxtlife.mgs.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
@Entity
public class Authority extends AbstractAuditable<Authority, Long> implements GrantedAuthority {

	@Column(nullable = false)
	private String name;

	@Column
	private String description;
	
//	@Column(unique = true , nullable = false)
//	private String cid;

	@Override
	public String getAuthority() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Authority() {
	}
	
	
	public String getName() {
		return name;
	}

	public Authority(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
