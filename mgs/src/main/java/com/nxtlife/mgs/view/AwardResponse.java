package com.nxtlife.mgs.view;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;

@JsonInclude(value = Include.NON_ABSENT)
public class AwardResponse {

	private String name;
	private String id;
	private String description;
	private String createrId;
	private String createdBy;
	private String assignerId;
	private String assignedBy;
//	private List<String> activityPerformedIds;
	private ActivityPerformedResponse activityPerformedResponse;
	private Date dateOfReceipt;
	private Boolean isVerified;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public AwardResponse() {

	}

	public ActivityPerformedResponse getActivityPerformedResponse() {
		return activityPerformedResponse;
	}

	public void setActivityPerformedResponse(ActivityPerformedResponse activityPerformedResponse) {
		this.activityPerformedResponse = activityPerformedResponse;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Date getDateOfReceipt() {
		return dateOfReceipt;
	}

	public void setDateOfReceipt(Date dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public String getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(String assignerId) {
		this.assignerId = assignerId;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public AwardResponse(Award award) {
		this.id = award.getCid();
		this.description = award.getDescription();
		this.name = award.getName();
		if (award.getTeacher() != null) {
			this.createrId = award.getTeacher().getcId();
			this.createdBy= award.getTeacher().getName();
		}
	}

	public AwardResponse(AwardActivityPerformed aap) {

		this.name = aap.getAward().getName();
		this.dateOfReceipt = aap.getDateOfReceipt();
		if(aap.getActivityPerformed().getActivity() !=null)
	     	this.description = aap.getActivityPerformed().getActivity().getName();
		this.assignerId =aap.getAssignerCid();
	}

}
