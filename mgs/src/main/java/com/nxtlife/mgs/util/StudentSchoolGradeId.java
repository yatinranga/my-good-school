package com.nxtlife.mgs.util;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class StudentSchoolGradeId implements Serializable{
	
	    private static final long serialVersionUID = 9110852979613819318L;

		private Long studentId;
		private Long schoolId;
		private Long gradeId;
		
		public Long getStudentId() {
			return studentId;
		}
		public void setStudentId(Long studentId) {
			this.studentId = studentId;
		}
		public Long getSchoolId() {
			return schoolId;
		}
		public void setSchoolId(Long schoolId) {
			this.schoolId = schoolId;
		}
		public Long getGradeId() {
			return gradeId;
		}
		public void setGradeId(Long gradeId) {
			this.gradeId = gradeId;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((gradeId == null) ? 0 : gradeId.hashCode());
			result = prime * result + ((schoolId == null) ? 0 : schoolId.hashCode());
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
			StudentSchoolGradeId other = (StudentSchoolGradeId) obj;
			if (gradeId == null) {
				if (other.gradeId != null)
					return false;
			} else if (!gradeId.equals(other.gradeId))
				return false;
			if (schoolId == null) {
				if (other.schoolId != null)
					return false;
			} else if (!schoolId.equals(other.schoolId))
				return false;
			if (studentId == null) {
				if (other.studentId != null)
					return false;
			} else if (!studentId.equals(other.studentId))
				return false;
			return true;
		}
		
		public StudentSchoolGradeId(Long studentId, Long schoolId, Long gradeId) {
			this.studentId = studentId;
			this.schoolId = schoolId;
			this.gradeId = gradeId;
		}
		
		public StudentSchoolGradeId() {
			
		}
		
}
