package com.nxtlife.mgs.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_ABSENT)
public class GroupResponseBy<T> {
	private String criterion;
	private String criterionValue;
	private List<T> responses;
	private Long count;
	
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
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
	public void setCriterionAndValue(String criterion , String criterionValue) {
		this.criterion = criterion;
		this.criterionValue = criterionValue;
	}
	public List<T> getResponses() {
		return responses;
	}
	public void setResponses(List<T> responses) {
		this.responses = responses;
		if(this.responses != null) {
			this.count = responses.stream().count();
		}
	}
	
	public GroupResponseBy(String criterion, String criterionValue, List<T> responses, Long count) {
		super();
		this.count = count;
		this.criterion = criterion;
		this.criterionValue = criterionValue;
		this.responses = responses;
	}
	
	public GroupResponseBy() {
		super();
	}
	
	
}
