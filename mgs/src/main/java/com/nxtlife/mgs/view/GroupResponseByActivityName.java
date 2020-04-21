package com.nxtlife.mgs.view;

import java.text.DecimalFormat;
import java.util.List;

import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.util.Utils;

@JsonInclude(content = Include.NON_ABSENT)
public class GroupResponseByActivityName<T> {

	private String activityName;
	private Long count;
	private Double averageStars;
	private String criterion;
	private String criterionValue;
	private List<T> responses;

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

//	public List<ActivityPerformedResponse> getActivities() {
//		return activities;
//	}
//	public void setActivities(List<ActivityPerformedResponse> activities) {
//		this.activities = activities;
//	}
	public Long getCount() {
		return count;
	}

	public List<T> getResponses() {
		return responses;
	}

	public void setResponses(List<T> responses) {
		this.responses = responses;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getAverageStars() {
		if (this.averageStars != null) {
			DecimalFormat df = new DecimalFormat("####0.00");
			return df.format((double)this.averageStars);
		}
		return null;
	}

	public void setAverageStars(Double averageStars) {
		this.averageStars = averageStars;
	}

	public String getCriterion() {
		return criterion;
	}

	public void setCriterion(String criterion) {
		this.criterion = criterion;
	}

	public String getCriterionValue() {
		return criterionValue;
	}

	public void setCriterionValue(String criterionValue) {
		this.criterionValue = criterionValue;
	}

	public GroupResponseByActivityName() {
		super();
	}

	public GroupResponseByActivityName(String activityName, Long count, Double averageStars, String criterion,
			String criterionValue, List<T> responses) {
		super();
		this.activityName = activityName;
		this.count = count;
		this.averageStars = averageStars;
		this.criterion = criterion;
		this.criterionValue = criterionValue;
		this.responses = responses;
	}

}
