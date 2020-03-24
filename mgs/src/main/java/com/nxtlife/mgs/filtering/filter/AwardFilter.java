package com.nxtlife.mgs.filtering.filter;

import com.nxtlife.mgs.enums.ApprovalStatus;

import lombok.Data;

@Data
public class AwardFilter {

	private String year;
	private ApprovalStatus status;
	private String fourS;
	private String psdArea;
	private String activityId;
	private String focusArea;
	private String studentId;
	private String teacherId;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatus status) {
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

	public String getFocusArea() {
		return focusArea;
	}

	public void setFocusArea(String focusArea) {
		this.focusArea = focusArea;
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
