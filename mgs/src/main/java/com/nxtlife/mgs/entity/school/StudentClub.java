package com.nxtlife.mgs.entity.school;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.common.StudentActivityId;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ApprovalStatus;

@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","activityId"}))
public class StudentClub implements Serializable {

	@EmbeddedId
	StudentActivityId studentActivityId;

	@MapsId(value = "studentId")
	@ManyToOne
	private Student student;
	
	@MapsId(value = "teacherId")
	@ManyToOne
	private Teacher teacher;

	@MapsId(value = "activityId")
	@ManyToOne
	@JoinColumn(name = "activityId")
	private Activity activity;

	@Enumerated(EnumType.STRING)
	ApprovalStatus membershipStatus;

	Date appliedOn;

	Date consideredOn;

	Boolean active = true;

	public StudentActivityId getStudentActivityId() {
		return studentActivityId;
	}

	public void setStudentActivityId(StudentActivityId studentActivityId) {
		this.studentActivityId = studentActivityId;
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

	public ApprovalStatus getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus(ApprovalStatus membershipStatus) {
		this.membershipStatus = membershipStatus;
	}

	public Date getAppliedOn() {
		return appliedOn;
	}

	public void setAppliedOn(Date appliedOn) {
		this.appliedOn = appliedOn;
	}

	public Date getConsideredOn() {
		return consideredOn;
	}

	public void setConsideredOn(Date consideredOn) {
		this.consideredOn = consideredOn;
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

	public StudentClub() {

	}

	public StudentClub(StudentActivityId studentActivityId, Student student, Activity activity,
			ApprovalStatus membershipStatus, Date appliedOn, Date consideredOn) {
		this.studentActivityId = studentActivityId;
		this.student = student;
		this.activity = activity;
		this.membershipStatus = membershipStatus;
		this.appliedOn = appliedOn;
		this.consideredOn = consideredOn;
	}

	public StudentClub(Long studentId, Long activityId ,Long teacherId) {
		if (studentId != null) {
			this.student = new Student();
			this.student.setId(studentId);
		}

		if (activityId != null) {
			this.activity = new Activity();
			this.activity.setId(activityId);
		}
		
		if(teacherId != null) {
			this.teacher = new Teacher();
			this.teacher.setId(teacherId);
		}

		this.studentActivityId = new StudentActivityId(studentId, activityId , teacherId);
	}

}
