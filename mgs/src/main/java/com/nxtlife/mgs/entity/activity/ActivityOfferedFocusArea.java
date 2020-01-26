package com.nxtlife.mgs.entity.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.util.FocusAreaActivityFoursS;

@Entity
public class ActivityOfferedFocusArea extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@EmbeddedId
	private FocusAreaActivityFoursS compKey;
	
	@MapsId(value = "activityOfferedId")
	@ManyToOne
	private ActivityOffered activityOffered ;
	
	@MapsId(value="focusAreaId")
	@ManyToOne
	private FocusArea focusArea;
	
//	@ManyToMany
//	@JoinTable(name = "teacher_activity_offered_focus_area",
//		joinColumns = { @JoinColumn(name = "activity_offered_focus_area_id") },
//		inverseJoinColumns = { @JoinColumn(name = "teacher_id") })
//	private List<Teacher> teachers = new ArrayList<Teacher>();

	public FocusAreaActivityFoursS getCompKey() {
		return compKey;
	}

	public void setCompKey(FocusAreaActivityFoursS compKey) {
		this.compKey = compKey;
	}

	public ActivityOffered getActivityOffered() {
		return activityOffered;
	}

	public void setActivityOffered(ActivityOffered activityOffered) {
		this.activityOffered = activityOffered;
	}

	public FocusArea getFocusArea() {
		return focusArea;
	}

	public void setFocusArea(FocusArea focusArea) {
		this.focusArea = focusArea;
	}
	

//	public List<Teacher> getTeachers() {
//		return teachers;
//	}
//
//	public void setTeachers(List<Teacher> teachers) {
//		this.teachers = teachers;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityOfferedFocusArea(FocusAreaActivityFoursS compKey, ActivityOffered activityOffered,
			FocusArea focusArea) {
		this.compKey = compKey;
		this.activityOffered = activityOffered;
		this.focusArea = focusArea;
	}

	public ActivityOfferedFocusArea(FocusAreaActivityFoursS compKey, ActivityOffered activityOffered,
			FocusArea focusArea, List<Teacher> teachers) {
		super();
		this.compKey = compKey;
		this.activityOffered = activityOffered;
		this.focusArea = focusArea;
//		this.teachers = teachers;
	}

	ActivityOfferedFocusArea(){
		
	}
	
}
