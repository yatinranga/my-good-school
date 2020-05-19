package com.nxtlife.mgs.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class TeacherSchoolGradeId implements Serializable{

	private static final long serialVersionUID = 6355243163308561168L;
	
	private Long teacherId;
	private Long schoolId;
	private Long gradeId;
	
	public Long getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
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
		result = prime * result + ((teacherId == null) ? 0 : teacherId.hashCode());
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
		TeacherSchoolGradeId other = (TeacherSchoolGradeId) obj;
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
		if (teacherId == null) {
			if (other.teacherId != null)
				return false;
		} else if (!teacherId.equals(other.teacherId))
			return false;
		return true;
	}
	
	public TeacherSchoolGradeId(Long teacherId, Long schoolId, Long gradeId) {
		this.teacherId = teacherId;
		this.schoolId = schoolId;
		this.gradeId = gradeId;
	}
	
	public TeacherSchoolGradeId() {
		
	}
	
}
