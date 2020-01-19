package com.nxtlife.mgs.entity.activity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;

@Entity
public class ActivityOffered extends BaseEntity {

	@NotNull
	private String name;
	
	private String description;
	
	private Boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ActivityOffered(String name, String description, Boolean active) {
		this.name = name;
		this.description = description;
		this.active = active;
	}
	
	public ActivityOffered() {
		
	}
	
}
