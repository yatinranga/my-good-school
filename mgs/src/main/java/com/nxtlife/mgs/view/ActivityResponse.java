package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.List;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.FocusArea;

public class ActivityResponse {

	private String name;
	
	private String description;
	
	private String id;
	
	private String fourS;
	
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
	
	public ActivityResponse(Activity activity)
	{
		this.id = activity.getcId();
		this.fourS = activity.getFourS().name();
		this.name = activity.getName();
		this.description = activity.getDescription();
		focusAreas = new ArrayList<String>();
		for(FocusArea fa : activity.getFocusAreas()) {
			focusAreas.add(fa.getName());
		}
		
	}
	
	public ActivityResponse() {
		
	}
}
