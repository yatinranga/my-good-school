package com.nxtlife.mgs.entity.school;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.util.StudentAwardTeacherId;

@Entity
public class StudentAward implements Serializable {

	@EmbeddedId
	StudentAwardTeacherId studentAwardTeacherId;
	
	@MapsId(value = "studentId")
	@ManyToOne
	private Student student ;
	
	@MapsId(value = "awardId")
	@ManyToOne
	private Award award ;
	
//	@MapsId(value = "teacherId")
//	@ManyToOne
//	private Teacher teacher ;
	
	private Date dateOfReceipt ;
	
	private Boolean isVerified;


	public StudentAwardTeacherId getStudentAwardTeacherId() {
		return studentAwardTeacherId;
	}

	public void setStudentAwardTeacherId(StudentAwardTeacherId studentAwardTeacherId) {
		this.studentAwardTeacherId = studentAwardTeacherId;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

//	public Teacher getTeacher() {
//		return teacher;
//	}
//
//	public void setTeacher(Teacher teacher) {
//		this.teacher = teacher;
//	}

	public Date getDateOfReceipt() {
		return dateOfReceipt;
	}

	public void setDateOfReceipt(Date dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public StudentAward(StudentAwardTeacherId studentAwardTeacherId, Student student, Award award) {
		this.studentAwardTeacherId = studentAwardTeacherId;
		this.student = student;
		this.award = award;
	}

	public StudentAward(StudentAwardTeacherId studentAwardTeacherId, Student student, Award award, Date dateOfReceipt, Boolean isVerified) {
		this.studentAwardTeacherId = studentAwardTeacherId;
		this.student = student;
		this.award = award;
		this.dateOfReceipt = dateOfReceipt;
		this.isVerified = isVerified;
	}
	
	public StudentAward()
	{

	}
	
	
}
