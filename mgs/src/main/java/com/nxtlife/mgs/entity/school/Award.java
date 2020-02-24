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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

//uniqueConstraints = @UniqueConstraint(columnNames = {"name","description","student"})
@Entity
@Table()
@DynamicUpdate(true)
public class Award extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@NotNull
	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	@NotNull
	private String description;

	private Boolean active;

	@ManyToOne
	@JoinColumn(name = "teacherId")
	private Teacher teacher;

//	@ManyToOne
//	@JoinColumn(name = "studentId")
//	private Student student;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "award")
	private List<StudentAward> studentAwards;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<StudentAward> getStudentAwards() {
		return studentAwards;
	}

	public void setStudentAwards(List<StudentAward> studentAwards) {
		this.studentAwards = studentAwards;
	}

	public Award(@NotNull String name, @NotNull String description, Boolean active, Teacher teacher) {
		this.name = name;
		this.description = description;
		this.active = active;
		this.teacher = teacher;
	}

	public Award() {

	}

}
