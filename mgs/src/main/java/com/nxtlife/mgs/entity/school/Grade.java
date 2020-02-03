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

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","section"}))
public class Grade extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cId;
	
	private String section;
	
	private Boolean active;
	
	@ManyToMany
	@JoinTable(name = "school_grade",
		joinColumns = { @JoinColumn(name = "school_id") },
		inverseJoinColumns = { @JoinColumn(name = "grade_id") })
	private List<School> schools;
	
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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Grade(@NotNull String name, @NotNull String cId, String section, Boolean active, List<School> schools,
			Teacher teacher, List<Student> students) {
		this.name = name;
		this.cId = cId;
		this.section = section;
		this.active = active;
		this.schools = schools;
		this.teacher = teacher;
		this.students = students;
	}

	public Grade(@NotNull String name, String section) {
		super();
		this.name = name;
		this.section = section;
	}

	public Grade() {
		
	}
	
}
