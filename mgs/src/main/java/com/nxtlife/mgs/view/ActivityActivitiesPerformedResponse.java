package com.nxtlife.mgs.view;

import java.util.List;

public class ActivityActivitiesPerformedResponse {

	private String activityName;
	private List<ActivityPerformedResponse> activities;
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public List<ActivityPerformedResponse> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityPerformedResponse> activities) {
		this.activities = activities;
	}
	
	
}
