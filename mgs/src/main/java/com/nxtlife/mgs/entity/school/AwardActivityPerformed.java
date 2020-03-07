package com.nxtlife.mgs.entity.school;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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

	public AwardActivityPerformed() {
		super();
	}

	public AwardActivityPerformed(Long awardId, Long activityPerformedId) {
		super();
		this.awardActivityPerformedId = new AwardActivityPerformedId(awardId, activityPerformedId);
		if (awardId != null) {
			this.award = new Award();
			this.award.setId(awardId);
		}
		if (activityPerformedId != null) {
			this.activityPerformed = new ActivityPerformed();
			this.activityPerformed.setId(activityPerformedId);
		}
	}

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

}
