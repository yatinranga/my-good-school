package com.nxtlife.mgs.entity.activity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.enums.PSDArea;

@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name", "psdArea" }))
public class FocusArea extends BaseEntity {

	@NotNull
	@Column(unique = true)
	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	private String description;

	@Enumerated(EnumType.STRING)
	PSDArea psdArea;

	@ManyToMany(mappedBy = "focusAreas", fetch = FetchType.EAGER)
	private List<Activity> activities;

//	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY,mappedBy ="focusArea")
//	private List<ActivityOfferedFocusArea> activityOfferedFocusArea;

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

	public FocusArea(@NotNull String name, @NotNull String cid, Boolean active, String description, PSDArea psdArea,
			List<Activity> activities) {
		this.name = name;
		this.cid = cid;
		this.setActive(active);
		this.description = description;
		this.psdArea = psdArea;
		this.activities = activities;
	}

	public FocusArea() {

	}

}
