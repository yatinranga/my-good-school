package com.nxtlife.mgs.entity.school;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.mgs.entity.common.TeacherSchoolGradeId;
import com.nxtlife.mgs.entity.user.Teacher;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(true)
public class TeacherSchoolGrade implements Serializable {

	@EmbeddedId
	TeacherSchoolGradeId teacherSchoolGradeId;

	@MapsId(value = "teacherId")
	@ManyToOne
	private Teacher teacher;

	@MapsId(value = "schoolId")
	@ManyToOne
	private School school;

	@MapsId(value = "gradeId")
	@ManyToOne
	private Grade grade;

	private String year;

	@NotNull
	@Column(name = "active", columnDefinition = "boolean default true")
	private Boolean active = true;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public TeacherSchoolGradeId getTeacherSchoolGradeId() {
		return teacherSchoolGradeId;
	}

	public void setTeacherSchoolGradeId(TeacherSchoolGradeId teacherSchoolGradeId) {
		this.teacherSchoolGradeId = teacherSchoolGradeId;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public TeacherSchoolGrade(TeacherSchoolGradeId teacherSchoolGradeId, Teacher teacher, School school, Grade grade,
			String year) {
		this.teacherSchoolGradeId = teacherSchoolGradeId;
		this.teacher = teacher;
		this.school = school;
		this.grade = grade;
		this.year = year;
	}

	public TeacherSchoolGrade(TeacherSchoolGradeId teacherSchoolGradeId, Teacher teacher, School school, Grade grade) {
		this.teacherSchoolGradeId = teacherSchoolGradeId;
		this.teacher = teacher;
		this.school = school;
		this.grade = grade;
	}

	public TeacherSchoolGrade() {

	}

}
