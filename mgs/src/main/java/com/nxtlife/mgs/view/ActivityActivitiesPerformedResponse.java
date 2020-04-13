package com.nxtlife.mgs.view;

import java.util.List;

public class ActivityActivitiesPerformedResponse {

	private String activityName;
	private Long count;
	private Double averageStars;
	
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
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Double getAverageStars() {
		return averageStars;
	}
	public void setAverageStars(Double averageStars) {
		this.averageStars = averageStars;
	}
	
	
}
