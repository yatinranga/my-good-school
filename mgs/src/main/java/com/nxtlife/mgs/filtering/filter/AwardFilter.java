package com.nxtlife.mgs.filtering.filter;

import com.nxtlife.mgs.enums.AwardStatus;

import lombok.Data;

@Data
public class AwardFilter {

	private String year;
	private AwardStatus status;
	private String fourS;
	private String psdArea;
	private String activityId;
	private String focusAreaId;
	private String studentId;
	private String teacherId;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public AwardStatus getStatus() {
		return status;
	}

	public void setStatus(AwardStatus status) {
		this.status = status;
	}

	public String getFourS() {
		return fourS;
	}

	public void setFourS(String fourS) {
		this.fourS = fourS;
	}

	public String getPsdArea() {
		return psdArea;
	}

	public void setPsdArea(String psdArea) {
		this.psdArea = psdArea;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getFocusAreaId() {
		return focusAreaId;
	}

	public void setFocusAreaId(String focusAreaId) {
		this.focusAreaId = focusAreaId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

}
