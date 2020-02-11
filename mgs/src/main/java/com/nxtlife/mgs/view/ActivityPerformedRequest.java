package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.enums.ActivityStatus;

public class ActivityPerformedRequest {

    private Date dateOfActivity;
	
	private String id;
	
	private String description;
	
	private String coachRemark;
	
	private Date coachRemarkDate;
	
	private Boolean active;
	
	private Integer participationScore;
	
	private Integer initiativeScore;
	
	private Integer achievementScore;
	
	private Integer star;
	
//	private ActivityStatus activityStatus;
	
	private String activityId;
	
	private List<FileRequest> fileRequests;
	
	private String coachId;
	
	private String studentId;

	public Date getDateOfActivity() {
		return dateOfActivity;
	}

	public void setDateOfActivity(Date dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
	}

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

	public Date getCoachRemarkDate() {
		return coachRemarkDate;
	}

	public void setCoachRemarkDate(Date coachRemarkDate) {
		this.coachRemarkDate = coachRemarkDate;
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

	public ActivityPerformed toEntity(ActivityPerformed activityPerformed) {
		activityPerformed=activityPerformed==null?new ActivityPerformed( ):activityPerformed;
		activityPerformed.setCid(this.id);
		activityPerformed.setDateOfActivity(this.dateOfActivity);
//		activityPerformed.setActivityStatus(this.activityStatus);
		activityPerformed.setDescription(this.description);
		activityPerformed.setCoachRemark(this.coachRemark);
		activityPerformed.setCoachRemarkDate(this.coachRemarkDate);
		activityPerformed.setActive(this.active);
		activityPerformed.setInitiativeScore(this.initiativeScore);
		activityPerformed.setParticipationScore(this.participationScore);
		activityPerformed.setAchievementScore(this.achievementScore);
		activityPerformed.setStar(this.star);
		
		return activityPerformed;
	}
	
	public ActivityPerformed toEntity() {
		return toEntity(null);
	}
}
