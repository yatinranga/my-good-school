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
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.AwardCriterion;

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

	private Date dateOfReceipt;

	private Date validFrom;

	private Date validUntil;

	@Enumerated(EnumType.STRING)
	private ApprovalStatus status;

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	private User statusModifiedBy;

	private Date statusModifiedAt;

	@ManyToOne
	private Activity activity;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "award")
	private List<AwardActivityPerformed> awardActivityPerformed;

	@ManyToOne
	private AwardType awardType;

	@Enumerated(EnumType.STRING)
	private AwardCriterion awardCriterion;

	private String criterionValue;

	@ManyToOne
	private Grade grade;

	public Award() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public User getStatusModifiedBy() {
		return statusModifiedBy;
	}

	public void setStatusModifiedBy(User statusModifiedBy) {
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

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public AwardCriterion getAwardCriterion() {
		return awardCriterion;
	}

	public void setAwardCriterion(AwardCriterion awardCriterion) {
		this.awardCriterion = awardCriterion;
	}

	public String getCriterionValue() {
		return criterionValue;
	}

	public void setCriterionValue(String criterionValue) {
		this.criterionValue = criterionValue;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Award(@NotNull AwardType awardType, @NotNull String description, Boolean active, Teacher teacher) {
		this.awardType = awardType;
		this.description = description;
		this.setActive(active);
		this.teacher = teacher;
	}

}
