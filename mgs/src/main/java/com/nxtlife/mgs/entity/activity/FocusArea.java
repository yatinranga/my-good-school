package com.nxtlife.mgs.entity.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.enums.PSDArea;

@Entity
public class FocusArea extends BaseEntity{

	@NotNull
	@Column(unique = true)
	private String name;
	
	private Boolean active;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	PSDArea psdArea;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PSDArea getPsdArea() {
		return psdArea;
	}

	public void setPsdArea(PSDArea psdArea) {
		this.psdArea = psdArea;
	}

	public FocusArea(@NotNull String name, Boolean active, String description, PSDArea psdArea) {
		this.name = name;
		this.active = active;
		this.description = description;
		this.psdArea = psdArea;
	}
	
	public FocusArea() {
		
	}
	
}
