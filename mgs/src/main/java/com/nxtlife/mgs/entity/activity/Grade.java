package com.nxtlife.mgs.entity.activity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","school","section"}))
public class Grade extends BaseEntity{

	@NotNull
	private String name;
	
	private String section;
	
	private Boolean active;
	
	@ManyToOne
	@JoinColumn(name="school")
	private School school;
	
	@ManyToOne
	private Teacher teacher;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "grade")
	private List<Student> students;

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

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Grade(@NotNull String name, String section, Boolean active, School school, Teacher teacher,
			List<Student> students) {
		this.name = name;
		this.section = section;
		this.active = active;
		this.school = school;
		this.teacher = teacher;
		this.students = students;
	}
	
	public Grade() {
		
	}
	
}
