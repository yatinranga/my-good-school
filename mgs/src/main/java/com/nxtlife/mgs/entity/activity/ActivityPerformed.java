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
	private String cId;
	
	private String description;
	
	private String teacherRemark;
	
	private Date teacherRemarkDate;
	
	private Boolean active;
	
	@Min(value = 0)
	@Min(value = 10)
	private Integer participationScore;
	
	@Min(value = 0)
	@Min(value = 10)
	private Integer initiativeScore;
	
	@Min(value = 0)
	@Min(value = 5)
	private Integer achievementScore;
	
	@Min(value = 0)
	@Min(value = 5)
	private Integer star;
	
	@Enumerated(EnumType.STRING)
	private ActivityStatus activityStatus;
	
	@OneToOne
//	@JoinColumn
	private ActivityOfferedFocusArea activityOffered;
	
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

	public ActivityOfferedFocusArea getActivityOffered() {
		return activityOffered;
	}

	public void setActivityOffered(ActivityOfferedFocusArea activityOffered) {
		this.activityOffered = activityOffered;
	}
	
	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityPerformed(@NotNull Date dateOfActivity, String description, String teacherRemark,
			Date teacherRemarkDate, Boolean active, @Min(0) @Min(10) Integer participationScore,
			@Min(0) @Min(10) Integer initiativeScore, @Min(0) @Min(5) Integer achievementScore,
			@Min(0) @Min(5) Integer star, ActivityStatus activityStatus, ActivityOfferedFocusArea activityOffered,
			List<File> files, @NotNull Student student, @NotNull Teacher teacher) {
		this.dateOfActivity = dateOfActivity;
		this.description = description;
		this.teacherRemark = teacherRemark;
		this.teacherRemarkDate = teacherRemarkDate;
		this.active = active;
		this.participationScore = participationScore;
		this.initiativeScore = initiativeScore;
		this.achievementScore = achievementScore;
		this.star = star;
		this.activityStatus = activityStatus;
		this.activityOffered = activityOffered;
		this.files = files;
		this.student = student;
		this.teacher = teacher;
	}
	
	public ActivityPerformed() {
		
	}

}
