package com.nxtlife.mgs.entity.activity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.enums.PSDArea;

@Entity
public class FocusArea extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull
	@Column(unique = true)
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cid;
	
	private Boolean active;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	PSDArea psdArea;
	
	@ManyToMany(mappedBy = "focusAreas")
	private List<Activity> activities;
	
//	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY,mappedBy ="focusArea")
//	private List<ActivityOfferedFocusArea> activityOfferedFocusArea;

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

//	public List<ActivityOfferedFocusArea> getActivityOfferedFocusArea() {
//		return activityOfferedFocusArea;
//	}
//
//	public void setActivityOfferedFocusArea(List<ActivityOfferedFocusArea> activityOfferedFocusArea) {
//		this.activityOfferedFocusArea = activityOfferedFocusArea;
//	}

	public String getCid() {
		return cid;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FocusArea(@NotNull String name, @NotNull String cid, Boolean active, String description, PSDArea psdArea,
			List<Activity> activities) {
		this.name = name;
		this.cid = cid;
		this.active = active;
		this.description = description;
		this.psdArea = psdArea;
		this.activities = activities;
	}

	public FocusArea() {
		
	}
	
}
