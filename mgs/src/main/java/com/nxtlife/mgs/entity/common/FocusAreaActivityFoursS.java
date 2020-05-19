package com.nxtlife.mgs.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.nxtlife.mgs.enums.FourS;

@Embeddable
public class FocusAreaActivityFoursS implements Serializable {

	private Long focusAreaId;
	
	private Long ActivityId;
	
	@Enumerated(EnumType.STRING)
	private FourS fourS;
	
	public Long getFocusAreaId() {
		return focusAreaId;
	}
	public void setFocusAreaId(Long focusAreaId) {
		this.focusAreaId = focusAreaId;
	}
	public Long getActivityId() {
		return ActivityId;
	}
	public void setActivityId(Long activityId) {
		ActivityId = activityId;
	}
	public FourS getFourS() {
		return fourS;
	}
	public void setFourS(FourS fourS) {
		this.fourS = fourS;
	}
	public FocusAreaActivityFoursS(Long focusAreaId, Long activityId, FourS fourS) {
		this.focusAreaId = focusAreaId;
		ActivityId = activityId;
		this.fourS = fourS;
	}
	
	public FocusAreaActivityFoursS()
	{
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ActivityId == null) ? 0 : ActivityId.hashCode());
		result = prime * result + ((focusAreaId == null) ? 0 : focusAreaId.hashCode());
		result = prime * result + ((fourS == null) ? 0 : fourS.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FocusAreaActivityFoursS other = (FocusAreaActivityFoursS) obj;
		if (ActivityId == null) {
			if (other.ActivityId != null)
				return false;
		} else if (!ActivityId.equals(other.ActivityId))
			return false;
		if (focusAreaId == null) {
			if (other.focusAreaId != null)
				return false;
		} else if (!focusAreaId.equals(other.focusAreaId))
			return false;
		if (fourS != other.fourS)
			return false;
		return true;
	}
	
	
	
}
