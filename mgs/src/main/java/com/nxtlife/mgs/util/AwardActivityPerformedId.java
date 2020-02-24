package com.nxtlife.mgs.util;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class AwardActivityPerformedId implements Serializable{

	private static final long serialVersionUID = -8019856266355024203L;

		private Long awardId;
//		private Long teacherId;
		private Long activityPerformedId;
		
			
		
		public Long getAwardId() {
			return awardId;
		}
		public void setAwardId(Long awardId) {
			this.awardId = awardId;
		}
		public Long getActivityPerformedId() {
			return activityPerformedId;
		}
		public void setActivityPerformedId(Long activityPerformedId) {
			this.activityPerformedId = activityPerformedId;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((activityPerformedId == null) ? 0 : activityPerformedId.hashCode());
			result = prime * result + ((awardId == null) ? 0 : awardId.hashCode());
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
			AwardActivityPerformedId other = (AwardActivityPerformedId) obj;
			if (activityPerformedId == null) {
				if (other.activityPerformedId != null)
					return false;
			} else if (!activityPerformedId.equals(other.activityPerformedId))
				return false;
			if (awardId == null) {
				if (other.awardId != null)
					return false;
			} else if (!awardId.equals(other.awardId))
				return false;
			return true;
		}
		
		public AwardActivityPerformedId(Long awardId, Long activityPerformedId) {
			this.awardId = awardId;
			this.activityPerformedId = activityPerformedId;
		}
		
		public AwardActivityPerformedId() {
			
		}

				
}
