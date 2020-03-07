package com.nxtlife.mgs.view;

import java.util.List;

import com.nxtlife.mgs.entity.school.Grade;

public class GradeRequest {

	private String id;
	private String grade;
	private String section;
	private List<String> schoolIds;

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

	public List<String> getSchoolIds() {
		return schoolIds;
	}

	public void setSchoolIds(List<String> schoolIds) {
		this.schoolIds = schoolIds;
	}

	public Grade toEntity() {
		return toEntity(null);
	}

	public Grade toEntity(Grade grade) {

		grade = grade == null ? new Grade() : grade;

		if (this.grade != null)
			grade.setName(this.grade);
		if (this.section != null)
			grade.setSection(this.section);

		return grade;
	}
}
