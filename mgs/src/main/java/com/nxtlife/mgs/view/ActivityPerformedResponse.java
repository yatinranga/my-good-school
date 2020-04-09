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
import com.nxtlife.mgs.util.DateUtil;

@JsonInclude(value = Include.NON_ABSENT)
public class ActivityPerformedResponse {

	private String dateOfActivity;
	
    private String submittedOn;
	
	private String reviewedOn;

	private String id;

	private String description;

	private String coachRemark;

	private String coachRemarkDate;

	private Boolean active;

	private Integer participationScore;

	private Integer initiativeScore;

	private Integer achievementScore;

	private Double star;

	private ActivityStatus activityStatus;

	private String activityId;

	private List<FileResponse> fileResponses;

	private String coachId;

	private String studentId;

	private String activityName;

	private String coachName;

	private String studentName;

	private String fourS;

	private Set<String> focusAreas;

	private Set<String> psdAreas;

	private String grade;
	
	private Integer totalMarks;
	
	private String title;

	public String getDateOfActivity() {
		return dateOfActivity;
	}

	public void setDateOfActivity(String dateOfActivity) {
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

	public String getCoachRemarkDate() {
		return coachRemarkDate;
	}

	public void setCoachRemarkDate(String coachRemarkDate) {
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

	public Double getStar() {
		return star;
	}

	public void setStar(Double star) {
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

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCoachName() {
		return coachName;
	}

	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
	}

	public String getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(String submittedOn) {
		this.submittedOn = submittedOn;
	}

	public String getReviewedOn() {
		return reviewedOn;
	}

	public void setReviewedOn(String reviewedOn) {
		this.reviewedOn = reviewedOn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public ActivityPerformedResponse(ActivityPerformed activityPerformed) {
		this.id = activityPerformed.getCid();
		this.active = activityPerformed.getActive();
		if (activityPerformed.getActivity() != null) {
			this.activityId = activityPerformed.getActivity().getCid();
			this.activityName = activityPerformed.getActivity().getName();
			this.fourS = activityPerformed.getActivity().getFourS().toString();

			if (activityPerformed.getActivity().getFocusAreas() != null
					&& !activityPerformed.getActivity().getFocusAreas().isEmpty()) {
				this.focusAreas = new HashSet<String>();
				this.psdAreas = new HashSet<String>();
				for (FocusArea focusarea : activityPerformed.getActivity().getFocusAreas()) {
					this.focusAreas.add(focusarea.getName());
					this.psdAreas.add(focusarea.getPsdArea().toString());
				}
			}
		}
		this.description = activityPerformed.getDescription();
		this.activityStatus = activityPerformed.getActivityStatus();
		if (activityPerformed.getDateOfActivity() != null)
			this.dateOfActivity = DateUtil.formatDate(activityPerformed.getDateOfActivity());
		if (activityPerformed.getTeacher() != null) {
			this.coachId = activityPerformed.getTeacher().getcId();
			this.coachName = activityPerformed.getTeacher().getName();
		}

		if (activityPerformed.getStudent() != null) {
			this.studentId = activityPerformed.getStudent().getCid();
			this.studentName = activityPerformed.getStudent().getName();
			if (activityPerformed.getStudent().getGrade() != null)
				this.grade = String.format("%s - %s", activityPerformed.getStudent().getGrade().getName(),
						activityPerformed.getStudent().getGrade().getSection());
		}
		this.coachRemark = activityPerformed.getCoachRemark();
		if (activityPerformed.getCoachRemarkDate() != null)
			this.coachRemarkDate = DateUtil.formatDate(activityPerformed.getCoachRemarkDate());
		this.participationScore = activityPerformed.getParticipationScore()== null? 0 : activityPerformed.getParticipationScore() ;
		this.initiativeScore = activityPerformed.getInitiativeScore() == null ? 0 : activityPerformed.getInitiativeScore();
		this.achievementScore = activityPerformed.getAchievementScore() == null ? 0 : activityPerformed.getAchievementScore();
		this.star = activityPerformed.getStar();
		this.fileResponses = new ArrayList<FileResponse>();
		if (activityPerformed.getFiles() != null)
			for (File file : activityPerformed.getFiles())
				this.fileResponses.add(new FileResponse(file));
		this.totalMarks = this.initiativeScore + this.achievementScore + this.participationScore;
		
		if (activityPerformed.getSubmittedOn() != null)
			this.dateOfActivity = DateUtil.formatDate(activityPerformed.getSubmittedOn());
		if (activityPerformed.getReviewedOn() != null)
			this.dateOfActivity = DateUtil.formatDate(activityPerformed.getReviewedOn());
		this.title = activityPerformed.getTitle();

	}

	public ActivityPerformedResponse() {

	}

}
