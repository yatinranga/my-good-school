package com.nxtlife.mgs.entity.school;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ApprovalStatus;

@SuppressWarnings("serial")
@Entity
@Table
@DynamicUpdate(true)
public class Award extends BaseEntity {

//	@NotNull
//	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	@NotNull
	@Column(columnDefinition = "TEXT ")
	private String description;

	private Boolean active = true;

	private Date dateOfReceipt;

	@Enumerated(EnumType.STRING)
	private ApprovalStatus status;

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;
	
	@ManyToOne
	private Teacher statusModifiedBy;
	
	private Date statusModifiedAt;

	@ManyToOne
	private Activity activity;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "award")
	private List<AwardActivityPerformed> awardActivityPerformed;
	
	@ManyToOne
	private AwardType awardType;

	public Award() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getCid() {
		return cid;
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

	public Date getDateOfReceipt() {
		return dateOfReceipt;
	}

	public void setDateOfReceipt(Date dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Teacher getStatusModifiedBy() {
		return statusModifiedBy;
	}

	public void setStatusModifiedBy(Teacher statusModifiedBy) {
		this.statusModifiedBy = statusModifiedBy;
	}

	public Date getStatusModifiedAt() {
		return statusModifiedAt;
	}

	public void setStatusModifiedAt(Date statusModifiedAt) {
		this.statusModifiedAt = statusModifiedAt;
	}

	public AwardType getAwardType() {
		return awardType;
	}

	public void setAwardType(AwardType awardType) {
		this.awardType = awardType;
	}

	public Award(@NotNull AwardType awardType, @NotNull String description, Boolean active, Teacher teacher) {
		this.awardType = awardType;
		this.description = description;
		this.active = active;
		this.teacher = teacher;
	}

}
