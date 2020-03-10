package com.nxtlife.mgs.entity.school;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

@Entity
@DynamicUpdate(true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name", "section" }))
public class Grade extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@NotNull
	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	private String section;

	private Boolean active = true;

	@ManyToMany
	@JoinTable(name = "school_grade", joinColumns = { @JoinColumn(name = "grade_id") }, inverseJoinColumns = {
			@JoinColumn(name = "school_id") })
	private List<School> schools;

//	@ManyToOne
//	private Teacher teacher;
	
	@ManyToMany(mappedBy = "grades", cascade = CascadeType.ALL)
	private List<Teacher> teachers;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "grade")
	private List<Student> students;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "grade")
	private List<StudentSchoolGrade> studentSchoolGrades;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "grade")
	private List<TeacherSchoolGrade> teacherSchoolGrades;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<School> getSchools() {
		return schools;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

//	public Teacher getTeacher() {
//		return teacher;
//	}
//
//	public void setTeacher(Teacher teacher) {
//		this.teacher = teacher;
//	}

	public String getCid() {
		return cid;
	}

	public void setcId(String cid) {
		this.cid = cid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<StudentSchoolGrade> getStudentSchoolGrades() {
		return studentSchoolGrades;
	}

	public void setStudentSchoolGrades(List<StudentSchoolGrade> studentSchoolGrades) {
		this.studentSchoolGrades = studentSchoolGrades;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public List<TeacherSchoolGrade> getTeacherSchoolGrades() {
		return teacherSchoolGrades;
	}

	public void setTeacherSchoolGrades(List<TeacherSchoolGrade> teacherSchoolGrades) {
		this.teacherSchoolGrades = teacherSchoolGrades;
	}
	

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public Grade(@NotNull String name, @NotNull String cid, String section, Boolean active, List<School> schools, List<Student> students) {
		this.name = name;
		this.cid = cid;
		this.section = section;
		this.active = active;
		this.schools = schools;
//		this.teacher = teacher;
		this.students = students;
	}

	public Grade(@NotNull String name, String section) {
		this.name = name;
		this.section = section;
	}

	public Grade() {

	}

}
