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
	private String desciption;

	@Override
	public String getAuthority() {
		return name;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Authority() {
	}

	public Authority(String name, String desciption) {
		this.name = name;
		this.desciption = desciption;
	}
}
