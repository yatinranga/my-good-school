package com.nxtlife.mgs.entity.activity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ActivityStatus;

@SuppressWarnings("serial")
@Entity
@Table(name = "activity_performed")
@DynamicUpdate(true)
public class ActivityPerformed extends BaseEntity implements Serializable {

	@NotNull
	private Date dateOfActivity;

	private Date submittedOn;

	private Date reviewedOn;

	@NotNull
	@Column(unique = true)
	private String cid;

	@Column(columnDefinition = "TEXT ")
	private String description;

	@Column(columnDefinition = "TEXT ")
	private String title;

	@Column(columnDefinition = "TEXT ")
	private String coachRemark;

	private Date coachRemarkDate;

	@Min(value = 0, message = "Minimum permissible value is 0.")
	@Max(value = 10, message = "Maximum permissible value is 10.")
	@Column(columnDefinition = "INT default 0")
	private Integer participationScore;

	@Min(value = 0, message = "Minimum permissible value is 0.")
	@Max(value = 10, message = "Maximum permissible value is 10.")
	@Column(columnDefinition = "INT default 0")
	private Integer initiativeScore;

	@Min(value = 0, message = "Minimum permissible value is 0.")
	@Max(value = 5, message = "Maximum permissible value is 5.")
	@Column(columnDefinition = "INT default 0")
	private Integer achievementScore;

	@Min(value = 0, message = "Minimum permissible value is 0.")
	@Max(value = 5, message = "Maximum permissible value is 5.")
	@Column(columnDefinition = " DECIMAL(4,2) default '0.0' ")
	private Double star = 0d;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ActivityStatus activityStatus = ActivityStatus.InProgressAtStudent;

//	@OneToOne
//	private ActivityOfferedFocusArea activityOffered;

	@OneToOne
	private Activity activity;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activityPerformed")
	private List<File> files;

	@NotNull
	@ManyToOne
	private Student student;

	@NotNull
	@ManyToOne
	private Teacher teacher;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activityPerformed")
	private List<AwardActivityPerformed> awardActivityPerformed;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateOfActivity() {
		return dateOfActivity;
	}

	public void setDateOfActivity(Date dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
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

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

//	public ActivityOfferedFocusArea getActivityOffered() {
//		return activityOffered;
//	}
//
//	public void setActivityOffered(ActivityOfferedFocusArea activityOffered) {
//		this.activityOffered = activityOffered;
//	}

	public String getCid() {
		return cid;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public List<AwardActivityPerformed> getAwardActivityPerformed() {
		return awardActivityPerformed;
	}

	public void setAwardActivityPerformed(List<AwardActivityPerformed> awardActivityPerformed) {
		this.awardActivityPerformed = awardActivityPerformed;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	public Date getReviewedOn() {
		return reviewedOn;
	}

	public void setReviewedOn(Date reviewedOn) {
		this.reviewedOn = reviewedOn;
	}

	public ActivityPerformed(Date dateOfActivity, String cid, String description, String coachRemark,
			Date coachRemarkDate, Integer participationScore, Integer initiativeScore, Integer achievementScore,
			Double star, ActivityStatus activityStatus, Activity activity, List<File> files, Student student,
			Teacher teacher) {
		this.dateOfActivity = dateOfActivity;
		this.cid = cid;
		this.description = description;
		this.coachRemark = coachRemark;
		this.coachRemarkDate = coachRemarkDate;
		this.participationScore = participationScore;
		this.initiativeScore = initiativeScore;
		this.achievementScore = achievementScore;
		this.star = star;
		this.activityStatus = activityStatus;
		this.activity = activity;
		this.files = files;
		this.student = student;
		this.teacher = teacher;
	}

	public ActivityPerformed() {

	}

}
