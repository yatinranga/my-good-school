package com.nxtlife.mgs.view;

import com.nxtlife.mgs.entity.school.Grade;

public class GradeResponse {

	private String id;
	private String grade;
	private String section;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	public GradeResponse(Grade grade) {
		this.setGrade(grade.getName());
		this.setSection(grade.getSection());
		this.setId(grade.getCid());
	}
}
