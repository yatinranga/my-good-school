package com.nxtlife.mgs.filtering.filter;

import lombok.Data;

@Data
public class ActivityPerformedFilter {
	private String year;
	private String status;
	private String fourS;
	private String psdArea;
	private String teacherId;
	private String studentId;
	private String activityId;
	private String focusArea;
	private String schoolId;
	private String grade;
	private String section;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
	
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
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
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
		
		if(this.grade != null) {
			String[] secs = grade.split("-");
			if(secs.length > 1) {
				this.grade = secs[0];
				this.setSection(secs[1]);
			}
		}
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	

}
