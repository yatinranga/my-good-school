package com.nxtlife.mgs.filtering.filter;

import lombok.Data;

@Data
public class ActivityPerformedFilter {
	private String year;
	private String status;
	private String fourS;
	private String psdArea;
	private String teacherId;
	private String activityId;
	private String focusAreaId;
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFourS() {
		return fourS;
	}
	public void setFourS(String fourS) {
		this.fourS = fourS;
	}
	public String getPsdArea() {
		return psdArea;
	}
	public void setPsdArea(String psdArea) {
		this.psdArea = psdArea;
	}
	
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getFocusAreaId() {
		return focusAreaId;
	}
	public void setFocusAreaId(String focusAreaId) {
		this.focusAreaId = focusAreaId;
	}
	

}
