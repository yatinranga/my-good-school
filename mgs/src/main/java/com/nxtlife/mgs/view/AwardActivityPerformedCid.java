package com.nxtlife.mgs.view;

public class AwardActivityPerformedCid {

	private String awardId;
	private String activityPerformedId;
	private String status;
	
	public String getAwardId() {
		return awardId;
	}
	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}
	public String getActivityPerformedId() {
		return activityPerformedId;
	}
	public void setActivityPerformedId(String activityPerformedId) {
		this.activityPerformedId = activityPerformedId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public AwardActivityPerformedCid(String awardId, String activityPerformedId) {
		this.awardId = awardId;
		this.activityPerformedId = activityPerformedId;
	}
	
	public AwardActivityPerformedCid(String awardId, String activityPerformedId,String status) {
		this.awardId = awardId;
		this.activityPerformedId = activityPerformedId;
		this.status = status;
	}
	
	public AwardActivityPerformedCid()
	{
		
	}
	
	
}
