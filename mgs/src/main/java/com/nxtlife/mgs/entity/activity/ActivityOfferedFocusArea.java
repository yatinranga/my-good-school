package com.nxtlife.mgs.entity.activity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.util.FocusAreaActivityFoursS;

@Entity
public class ActivityOfferedFocusArea extends BaseEntity implements Serializable {

	@EmbeddedId
	private FocusAreaActivityFoursS compKey;
	
	@MapsId(value = "activityOfferedId")
	@ManyToOne
	private ActivityOffered activityOffered ;
	
	@MapsId(value="focusAreaId")
	@ManyToOne
	private FocusArea focusArea;

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

	public ActivityOfferedFocusArea(FocusAreaActivityFoursS compKey, ActivityOffered activityOffered,
			FocusArea focusArea) {
		this.compKey = compKey;
		this.activityOffered = activityOffered;
		this.focusArea = focusArea;
	}

	ActivityOfferedFocusArea(){
		
	}
	
}
