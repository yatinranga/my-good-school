package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.List;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.enums.FourS;

public class ActivityRequestResponse {

	private String name;

	private String description;

	private String id;

	private String fourS;

	private List<String> focusAreaIds;

	private List<String> schoolIds;

	private List<String> focusAreas;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFourS() {
		return fourS;
	}

	public void setFourS(String fourS) {
		this.fourS = fourS;
	}

	public List<String> getFocusAreas() {
		return focusAreas;
	}

	public void setFocusAreas(List<String> focusAreas) {
		this.focusAreas = focusAreas;
	}

	public List<String> getFocusAreaIds() {
		return focusAreaIds;
	}

	public void setFocusAreaIds(List<String> focusAreaIds) {
		this.focusAreaIds = focusAreaIds;
	}

	public List<String> getSchoolIds() {
		return schoolIds;
	}

	public void setSchoolIds(List<String> schoolIds) {
		this.schoolIds = schoolIds;
	}

	public Activity toEntitity() {
		return toEntity(null);
	}

	public Activity toEntity(Activity activity) {
		activity = activity == null ? new Activity() : activity;
		activity.setName(this.name);
		activity.setDescription(this.description);
		activity.setFourS(FourS.valueOf(this.fourS));
		return activity;
	}

	public ActivityRequestResponse(Activity activity) {
		this.id = activity.getCid();
		this.fourS = activity.getFourS().name();
		this.name = activity.getName();
		this.description = activity.getDescription();
		focusAreaIds = new ArrayList<String>();
		for (FocusArea fa : activity.getFocusAreas()) {
			focusAreaIds.add(fa.getCid());
		}

	}

	public ActivityRequestResponse() {

	}
}
