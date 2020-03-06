package com.nxtlife.mgs.view;

import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.enums.PSDArea;

public class FocusAreaRequestResponse {

	private String name;
	private String psdArea;
	private String id;
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPsdArea() {
		return psdArea;
	}
	public void setPsdArea(String psdArea) {
		this.psdArea = psdArea;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public FocusArea toEntity(FocusArea focusArea) {
		focusArea = focusArea==null?new FocusArea():focusArea;
		focusArea.setName(this.name);
		focusArea.setCid(this.id);
		focusArea.setDescription(this.description);
		focusArea.setPsdArea(PSDArea.fromString(this.psdArea));
		return focusArea;
	}
	
    public FocusArea toEntity() {
		return toEntity(null);
	}
    
    public FocusAreaRequestResponse(FocusArea focusArea) {
    	this.id = focusArea.getCid();
    	this.name = focusArea.getName();
    	this.description = focusArea.getDescription();
    	if(focusArea.getPsdArea() != null)
    	   this.psdArea = focusArea.getPsdArea().toString();
    }
    public FocusAreaRequestResponse() {
    	
    }
}
