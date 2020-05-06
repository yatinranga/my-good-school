package com.nxtlife.mgs.view;

import java.text.DecimalFormat;
import java.util.List;

import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.util.Utils;

@JsonInclude(content = Include.NON_ABSENT)
public class GroupResponseByActivityName<T> extends GroupResponseBy<T> {

//	private String activityName;
//	private Long count;
	private Double averageStars;
//	private String criterion;
//	private String criterionValue;
//	private List<T> responses;


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


	public GroupResponseByActivityName() {
		super();
	}

	public GroupResponseByActivityName(String criterion, String criterionValue, List<T> responses, Long count,
			Double averageStars) {
		super(criterion, criterionValue, responses ,count);
		this.averageStars = averageStars;
	}
	
	

}
