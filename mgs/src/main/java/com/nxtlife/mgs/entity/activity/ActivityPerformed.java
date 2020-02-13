package com.nxtlife.mgs.entity.activity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ActivityStatus;

@Entity
public class ActivityPerformed extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull
	private Date dateOfActivity;
	
	@NotNull
	@Column(unique = true)
	private String cid;
	
	private String description;
	
	private String coachRemark;
	
	private Date coachRemarkDate;
	
	private Boolean active;
	
	@Min(value = 0)
	@Max(value = 10)
	private Integer participationScore;
	
	@Min(value = 0)
	@Max(value = 10)
	private Integer initiativeScore;
	
	@Min(value = 0)
	@Max(value = 5)
	private Integer achievementScore;
	
	@Min(value = 0)
	@Max(value = 5)
	private Integer star;
	
	@Enumerated(EnumType.STRING)
	private ActivityStatus activityStatus;
	
//	@OneToOne
//	private ActivityOfferedFocusArea activityOffered;
	
	@OneToOne
	private Activity activity;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="activityPerformed")
	private List<File> files;
	
	@NotNull
	@ManyToOne
	private Student student;
	
	@NotNull
	@ManyToOne
	private Teacher teacher;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public ActivityPerformed(Date dateOfActivity, String cid, String description, String coachRemark,
			Date coachRemarkDate, Boolean active, Integer participationScore, Integer initiativeScore,
			Integer achievementScore, Integer star, ActivityStatus activityStatus, Activity activity, List<File> files,
			Student student, Teacher teacher) {
		this.dateOfActivity = dateOfActivity;
		this.cid = cid;
		this.description = description;
		this.coachRemark = coachRemark;
		this.coachRemarkDate = coachRemarkDate;
		this.active = active;
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
