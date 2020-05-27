package com.nxtlife.mgs.entity.school;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.common.StudentSchoolGradeId;
import com.nxtlife.mgs.entity.user.Student;

@Entity
public class StudentSchoolGrade  implements Serializable {

	private static final long serialVersionUID = 2848327034025855705L;
	
	@EmbeddedId
	StudentSchoolGradeId studentSchoolGradeId;
	
	@MapsId(value = "studentId")
	@ManyToOne
	private Student student ;
	
	@MapsId(value="schoolId")
	@ManyToOne
	private School school;
	
	@MapsId(value="gradeId")
	@ManyToOne
	private Grade grade;
	
	private String year;

	public StudentSchoolGradeId getStudentSchoolGradeId() {
		return studentSchoolGradeId;
	}

	public void setStudentSchoolGradeId(StudentSchoolGradeId studentSchoolGradeId) {
		this.studentSchoolGradeId = studentSchoolGradeId;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
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


	public StudentSchoolGrade(StudentSchoolGradeId studentSchoolGradeId, Student student, School school, Grade grade,
			String year) {
		this.studentSchoolGradeId = studentSchoolGradeId;
		this.student = student;
		this.school = school;
		this.grade = grade;
		this.year = year;
	}

	public StudentSchoolGrade(StudentSchoolGradeId studentSchoolGradeId, Student student, School school, Grade grade) {
		this.studentSchoolGradeId = studentSchoolGradeId;
		this.student = student;
		this.school = school;
		this.grade = grade;
	}
	
	public StudentSchoolGrade() {
		
	}
	
	
	
}
