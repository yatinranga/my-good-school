package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.enums.ActivityStatus;

public class ActivityPerformedResponse {

    private Date dateOfActivity;
	
	private String id;
	
	private String description;
	
	private String teacherRemark;
	
	private Date teacherRemarkDate;
	
	private Boolean active;
	
	private Integer participationScore;
	
	private Integer initiativeScore;
	
	private Integer achievementScore;
	
	private Integer star;
	
	private ActivityStatus activityStatus;
	
	private String activityId;
	
	private List<FileResponse> fileResponses;
	
	private String teacherId;

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

	public String getTeacherRemark() {
		return teacherRemark;
	}

	public void setTeacherRemark(String teacherRemark) {
		this.teacherRemark = teacherRemark;
	}

	public Date getTeacherRemarkDate() {
		return teacherRemarkDate;
	}

	public void setTeacherRemarkDate(Date teacherRemarkDate) {
		this.teacherRemarkDate = teacherRemarkDate;
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

	public ActivityStatus getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(ActivityStatus activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public List<FileResponse> getFileResponses() {
		return fileResponses;
	}

	public void setFileResponses(List<FileResponse> fileResponses) {
		this.fileResponses = fileResponses;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	
	public ActivityPerformedResponse(ActivityPerformed activityPerformed) {
		this.activityId = activityPerformed.getCid();
		this.active = activityPerformed.getActive();
		this.activityId = activityPerformed.getActivity().getCid();
		this.description = activityPerformed.getDescription();
		this.activityStatus = activityPerformed.getActivityStatus();
		this.dateOfActivity = activityPerformed.getDateOfActivity();
		this.teacherId = activityPerformed.getTeacher().getcId();
		this.teacherRemark = activityPerformed.getTeacherRemark();
		this.teacherRemarkDate = activityPerformed.getTeacherRemarkDate();
		this.participationScore = activityPerformed.getParticipationScore();
		this.initiativeScore = activityPerformed.getInitiativeScore();
		this.achievementScore = activityPerformed.getAchievementScore();
		this.star = activityPerformed.getStar();
		this.fileResponses = new ArrayList<FileResponse>();
		if (activityPerformed.getFiles() != null)
			for (File file : activityPerformed.getFiles())
				this.fileResponses.add(new FileResponse(file));
		
	}
    
	public ActivityPerformedResponse() {
		
	}

}