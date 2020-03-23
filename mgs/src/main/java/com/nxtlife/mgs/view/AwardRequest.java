package com.nxtlife.mgs.view;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.nxtlife.mgs.entity.school.Award;

public class AwardRequest {

	@NotEmpty(message = "Award Type cannot be null or empty.")
	private String awardType;
	private String id;
	private String description;
	@NotNull(message = "teacher id cannot be null.")
	private String teacherId;
	private String studentId;
	private List<String> activityPerformedIds;
	private String schoolId;
	private String gradeId;
	private String activityId;
	private List<AwardActivityPerformedCid> awardActivityPerformedList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public List<String> getActivityPerformedIds() {
		return activityPerformedIds;
	}

	public void setActivityPerformedIds(List<String> activityPerformedIds) {
		this.activityPerformedIds = activityPerformedIds;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public List<AwardActivityPerformedCid> getAwardActivityPerformedList() {
		return awardActivityPerformedList;
	}

	public void setAwardActivityPerformedList(List<AwardActivityPerformedCid> awardActivityPerformedList) {
		this.awardActivityPerformedList = awardActivityPerformedList;
	}

	public Award toEntity() {
		return toEntity(null);
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	public Award toEntity(Award award) {
		award = award == null ? new Award() : award;
		award.setCid(this.id);
		award.setDescription(this.description);
		return award;
	}

}
