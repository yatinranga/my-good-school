package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.util.DateUtil;

public class ActivityPerformedRequest {

	private String dateOfActivity;

	private String id;

	private String description;

	private String coachRemark;

//	private String coachRemarkDate;

	private Boolean active;

	@Min(value = 0,message = "Minimum permissible value is 0.")
	@Max(value = 10,message = "Maximum permissible value is 10.")
	private Integer participationScore;

	@Min(value = 0,message = "Minimum permissible value is 0.")
	@Max(value = 10,message = "Maximum permissible value is 10.")
	private Integer initiativeScore;

	@Min(value = 0,message = "Minimum permissible value is 0.")
	@Max(value = 5,message = "Maximum permissible value is 5.")
	private Integer achievementScore;

	@Min(value = 0,message = "Minimum permissible value is 0.")
	@Max(value = 5,message = "Maximum permissible value is 5.")
	private Integer star;

//	private ActivityStatus activityStatus;

	private String activityId;

	private List<FileRequest> fileRequests;

	private String coachId;

	private String studentId;

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

	public String getCoachRemark() {
		return coachRemark;
	}

	public void setCoachRemark(String coachRemark) {
		this.coachRemark = coachRemark;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getParticipationScore() {
		return participationScore;
	}

	public void setParticipationScore(Integer participationScore) {
		this.participationScore = participationScore;
	}

	public Integer getInitiativeScore() {
		return initiativeScore;
	}

	public void setInitiativeScore(Integer initiativeScore) {
		this.initiativeScore = initiativeScore;
	}

	public Integer getAchievementScore() {
		return achievementScore;
	}

	public void setAchievementScore(Integer achievementScore) {
		this.achievementScore = achievementScore;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

//	public ActivityStatus getActivityStatus() {
//		return activityStatus;
//	}
//
//	public void setActivityStatus(ActivityStatus activityStatus) {
//		this.activityStatus = activityStatus;
//	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public List<FileRequest> getFileRequests() {
		return fileRequests;
	}

	public void setFileRequests(List<FileRequest> fileRequests) {
		this.fileRequests = fileRequests;
	}

	public String getCoachId() {
		return coachId;
	}

	public void setCoachId(String coachId) {
		this.coachId = coachId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getDateOfActivity() {
		return dateOfActivity;
	}

	public void setDateOfActivity(String dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
	}

//	public String getCoachRemarkDate() {
//		return coachRemarkDate;
//	}
//
//	public void setCoachRemarkDate(String coachRemarkDate) {
//		this.coachRemarkDate = coachRemarkDate;
//	}

	public ActivityPerformed toEntity(ActivityPerformed activityPerformed) {
		activityPerformed = activityPerformed == null ? new ActivityPerformed() : activityPerformed;
		if (this.id != null)
			activityPerformed.setCid(this.id);
		if (this.dateOfActivity != null)
			activityPerformed.setDateOfActivity(DateUtil.convertStringToDate(this.dateOfActivity));
//		activityPerformed.setActivityStatus(this.activityStatus);
		if (this.description != null)
			activityPerformed.setDescription(this.description);
		if (this.getCoachRemark() != null)
			activityPerformed.setCoachRemark(this.coachRemark);
//		activityPerformed.setCoachRemarkDate(DateUtil.convertStringToDate(this.coachRemarkDate));
//		activityPerformed.setActive(this.active);
		if (this.getInitiativeScore() != null)
			activityPerformed.setInitiativeScore(this.initiativeScore);
		if (this.getParticipationScore() != null)
			activityPerformed.setParticipationScore(this.participationScore);
		if (this.getAchievementScore() != null)
			activityPerformed.setAchievementScore(this.achievementScore);
		if (this.getStar() != null)
			activityPerformed.setStar(this.star);

		return activityPerformed;
	}

	public ActivityPerformed toEntity() {
		return toEntity(null);
	}
}
