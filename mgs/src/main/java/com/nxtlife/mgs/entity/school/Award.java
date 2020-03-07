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
import com.nxtlife.mgs.enums.AwardStatus;

@SuppressWarnings("serial")
@Entity
@Table
@DynamicUpdate(true)
public class Award extends BaseEntity {

	@NotNull
	@Column(unique = true)
	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	@NotNull
	private String description;

	private Boolean active = true;

	private Date dateOfReceipt;

	@Enumerated(EnumType.STRING)
	private AwardStatus status;

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	private Activity activity;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "award")
	private List<AwardActivityPerformed> awardActivityPerformed;

	public Award() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public AwardStatus getStatus() {
		return status;
	}

	public void setStatus(AwardStatus status) {
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

	public Award(@NotNull String name, @NotNull String description, Boolean active, Teacher teacher) {
		this.name = name;
		this.description = description;
		this.active = active;
		this.teacher = teacher;
	}

}
