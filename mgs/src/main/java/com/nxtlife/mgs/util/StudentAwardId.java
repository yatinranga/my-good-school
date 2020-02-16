package com.nxtlife.mgs.util;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class StudentAwardId implements Serializable{

	private static final long serialVersionUID = -8019856266355024203L;

		private Long studentId;
//		private Long teacherId;
		private Long awardId;
		
		public Long getStudentId() {
			return studentId;
		}
		public void setStudentId(Long studentId) {
			this.studentId = studentId;
		}
		
		public Long getAwardId() {
			return awardId;
		}
		public void setAwardId(Long awardId) {
			this.awardId = awardId;
		}
		
				
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((awardId == null) ? 0 : awardId.hashCode());
			result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
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
			StudentAwardId other = (StudentAwardId) obj;
			if (awardId == null) {
				if (other.awardId != null)
					return false;
			} else if (!awardId.equals(other.awardId))
				return false;
			if (studentId == null) {
				if (other.studentId != null)
					return false;
			} else if (!studentId.equals(other.studentId))
				return false;
			return true;
		}
		public StudentAwardId(Long studentId, Long awardId) {
			this.studentId = studentId;
			this.awardId = awardId;
		}
		
		public StudentAwardId() {
			
		}

				
}
