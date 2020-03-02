package com.nxtlife.mgs.entity.school;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.util.AwardActivityPerformedId;

@SuppressWarnings("serial")
@Entity
public class AwardActivityPerformed implements Serializable {

	@EmbeddedId
	AwardActivityPerformedId awardActivityPerformedId;

	@MapsId(value = "awardId")
	@ManyToOne
	private Award award;

	@MapsId(value = "activityPerformedId")
	@ManyToOne
	private ActivityPerformed activityPerformed;

//	@MapsId(value = "teacherId")
//	@ManyToOne
	private String assignerCid ;

	private Date dateOfReceipt;

	private Boolean isVerified = false;

	@NotNull
	private Boolean active = true;

	public AwardActivityPerformedId getAwardActivityPerformedId() {
		return awardActivityPerformedId;
	}

	public void setAwardActivityPerformedId(AwardActivityPerformedId awardActivityPerformedId) {
		this.awardActivityPerformedId = awardActivityPerformedId;
	}

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	public ActivityPerformed getActivityPerformed() {
		return activityPerformed;
	}

	public void setActivityPerformed(ActivityPerformed activityPerformed) {
		this.activityPerformed = activityPerformed;
	}

	public Date getDateOfReceipt() {
		return dateOfReceipt;
	}

	public void setDateOfReceipt(Date dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getAssignerCid() {
		return assignerCid;
	}

	public void setAssignerCid(String assignerCid) {
		this.assignerCid = assignerCid;
	}

	public AwardActivityPerformed(AwardActivityPerformedId awardActivityPerformedId, Award award,
			ActivityPerformed activityPerformed,String assignerCid) {
		this.awardActivityPerformedId = awardActivityPerformedId;
		this.award = award;
		this.activityPerformed = activityPerformed;
		this.assignerCid = assignerCid;
	}

	public AwardActivityPerformed(AwardActivityPerformedId awardActivityPerformedId, Award award,
			ActivityPerformed activityPerformed, Date dateOfReceipt, Boolean isVerified) {
		this.awardActivityPerformedId = awardActivityPerformedId;
		this.award = award;
		this.activityPerformed = activityPerformed;
		this.dateOfReceipt = dateOfReceipt;
		this.isVerified = isVerified;
	}

	public AwardActivityPerformed() {

	}

}
