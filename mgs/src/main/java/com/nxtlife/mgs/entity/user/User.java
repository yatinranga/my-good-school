package com.nxtlife.mgs.entity.user;

import javax.persistence.Entity;

import com.nxtlife.mgs.entity.BaseEntity;

@Entity
public class User extends BaseEntity {

	private String username;
    private String passwordHash;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
    
    
}
