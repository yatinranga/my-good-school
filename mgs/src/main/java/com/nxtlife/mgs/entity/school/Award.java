package com.nxtlife.mgs.entity.school;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","description","student"}))
public class Award extends BaseEntity{
	
	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	private Boolean active;
	
	@ManyToOne
	private Teacher teacher;
	
	@ManyToOne
	@JoinColumn(name = "student")
	private Student student;

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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Award(@NotNull String name, @NotNull String description, Boolean active, Teacher teacher, Student student) {
		this.name = name;
		this.description = description;
		this.active = active;
		this.teacher = teacher;
		this.student = student;
	}
	
	public Award() {
		
	}
	

}
