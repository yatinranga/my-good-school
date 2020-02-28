package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.enums.ActivityStatus;

@JsonInclude(value = Include.NON_ABSENT)
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
	
	private String studentId;
	
	private String activityName;
	
	private String teacherName;
	
	private String fourS;
	
	private Set<String> focusAreas;
	
	private Set<String> psdAreas;

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
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getFourS() {
		return fourS;
	}

	public void setFourS(String fourS) {
		this.fourS = fourS;
	}
	

	public Set<String> getFocusAreas() {
		return focusAreas;
	}

	public void setFocusAreas(Set<String> focusAreas) {
		this.focusAreas = focusAreas;
	}

	public Set<String> getPsdAreas() {
		return psdAreas;
	}

	public void setPsdAreas(Set<String> psdAreas) {
		this.psdAreas = psdAreas;
	}

	public ActivityPerformedResponse(ActivityPerformed activityPerformed) {
		this.id = activityPerformed.getCid();
		this.active = activityPerformed.getActive();
		if(activityPerformed.getActivity()!=null) {
			  this.activityId = activityPerformed.getActivity().getCid();
			  this.activityName = activityPerformed.getActivity().getName();
			  this.fourS = activityPerformed.getActivity().getFourS().toString();
			  if(activityPerformed.getActivity().getFocusAreas()!=null && !activityPerformed.getActivity().getFocusAreas().isEmpty()) {
				  this.focusAreas = new HashSet<String>();
				  this.psdAreas = new HashSet<String>();
				  for(FocusArea focusarea : activityPerformed.getActivity().getFocusAreas()) {
					  this.focusAreas.add(focusarea.getName());
					  this.psdAreas.add(focusarea.getPsdArea().toString());
				  }
			  }
		}
		this.description = activityPerformed.getDescription();
		this.activityStatus = activityPerformed.getActivityStatus();
		this.dateOfActivity = activityPerformed.getDateOfActivity();
		if(activityPerformed.getTeacher()!=null) {
			this.teacherId = activityPerformed.getTeacher().getcId();
			this.teacherName=activityPerformed.getTeacher().getName();
		}
		  
		if(activityPerformed.getStudent()!=null)
		   this.studentId=activityPerformed.getStudent().getCid();
		this.teacherRemark = activityPerformed.getCoachRemark();
		this.teacherRemarkDate = activityPerformed.getCoachRemarkDate();
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
