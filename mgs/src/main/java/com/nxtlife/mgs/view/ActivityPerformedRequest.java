package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.joda.time.LocalDateTime;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.util.DateUtil;

public class ActivityPerformedRequest {

	private String dateOfActivity;

	private String id;

	private String description;

	private String coachRemark;

//	private String coachRemarkDate;

	private Boolean active;

	@Min(value = 0, message = "Minimum permissible value is 0.")
	@Max(value = 10, message = "Maximum permissible value is 10.")
	private Integer participationScore;

	@Min(value = 0, message = "Minimum permissible value is 0.")
	@Max(value = 10, message = "Maximum permissible value is 10.")
	private Integer initiativeScore;

	@Min(value = 0, message = "Minimum permissible value is 0.")
	@Max(value = 5, message = "Maximum permissible value is 5.")
	private Integer achievementScore;

//	@Min(value = 0,message = "Minimum permissible value is 0.")
//	@Max(value = 5,message = "Maximum permissible value is 5.")
//	private Integer star;

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
		if (this.dateOfActivity != null) {
			if (LocalDateTime.now().toDate().before(DateUtil.convertStringToDate(this.dateOfActivity)))
				throw new ValidationException("Date of activity cannot be a future date.");
			activityPerformed.setDateOfActivity(DateUtil.convertStringToDate(this.dateOfActivity));
		}
//		activityPerformed.setActivityStatus(this.activityStatus);
		if (this.description != null)
			activityPerformed.setDescription(this.description);
		if (this.getCoachRemark() != null)
			activityPerformed.setCoachRemark(this.coachRemark);
//		activityPerformed.setCoachRemarkDate(DateUtil.convertStringToDate(this.coachRemarkDate));
//		activityPerformed.setActive(this.active);

		activityPerformed
				.setInitiativeScore(this.initiativeScore = this.initiativeScore == null ? 0 : this.initiativeScore);

		activityPerformed.setParticipationScore(
				this.participationScore = this.participationScore == null ? 0 : this.participationScore);

		activityPerformed
				.setAchievementScore(this.achievementScore = this.achievementScore == null ? 0 : this.achievementScore);

		Integer totalScore = this.achievementScore + this.initiativeScore + this.participationScore;

		double percentScore = ((double) totalScore / 25) * 100;
		double fractionalStar = 0;

		if (percentScore > 90.0)
			fractionalStar = 5.0;
		else if (percentScore > 80 && percentScore <= 90)
			fractionalStar = 4.5;
		else if (percentScore > 70 && percentScore <= 80)
			fractionalStar = 4;
		else if (percentScore > 60 && percentScore <= 70)
			fractionalStar = 3.5;
		else if (percentScore > 50 && percentScore <= 60)
			fractionalStar = 3;
		else if (percentScore > 40 && percentScore <= 50)
			fractionalStar = 2.5;
		else if (percentScore > 30 && percentScore <= 40)
			fractionalStar = 2;
		else if (percentScore > 20 && percentScore <= 30)
			fractionalStar = 1.5;
		else if (percentScore > 10 && percentScore <= 20)
			fractionalStar = 1;
		else if (percentScore > 0 && percentScore <= 10)
			fractionalStar = 0.5;
		else
			fractionalStar = 0;

		System.out.println("fractionalStar :" + fractionalStar);

		activityPerformed.setStar(fractionalStar);

		return activityPerformed;
	}

	public ActivityPerformed toEntity() {
		return toEntity(null);
	}
}
