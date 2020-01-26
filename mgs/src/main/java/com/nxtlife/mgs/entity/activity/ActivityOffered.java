package com.nxtlife.mgs.entity.activity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Teacher;

@Entity
public class ActivityOffered extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@NotNull
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cId;
	
	private String description;
	
	private Boolean active;
	
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY,mappedBy ="activityOffered")
	private List<ActivityOfferedFocusArea> activityOfferedFocusArea; 
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy ="activityOffered")
	private List<Teacher> teachers ;

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

	public List<ActivityOfferedFocusArea> getActivityOfferedFocusArea() {
		return activityOfferedFocusArea;
	}

	public void setActivityOfferedFocusArea(List<ActivityOfferedFocusArea> activityOfferedFocusArea) {
		this.activityOfferedFocusArea = activityOfferedFocusArea;
	}
	
	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityOffered(String name, String description, Boolean active) {
		this.name = name;
		this.description = description;
		this.active = active;
	}
	
	
	public ActivityOffered(@NotNull String name, String description, Boolean active,
			List<ActivityOfferedFocusArea> activityOfferedFocusArea) {
		super();
		this.name = name;
		this.description = description;
		this.active = active;
		this.activityOfferedFocusArea = activityOfferedFocusArea;
	}
	
	public ActivityOffered(@NotNull String name, @NotNull String cId, String description, Boolean active,
			List<ActivityOfferedFocusArea> activityOfferedFocusArea, List<Teacher> teachers) {
		super();
		this.name = name;
		this.cId = cId;
		this.description = description;
		this.active = active;
		this.activityOfferedFocusArea = activityOfferedFocusArea;
		this.teachers = teachers;
	}

	public ActivityOffered() {
		
	}
	
}
