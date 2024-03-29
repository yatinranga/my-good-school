package com.nxtlife.mgs.entity.school;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.user.SchoolManagementMember;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(true)
// @Table(uniqueConstraints = @UniqueConstraint(columnNames =
// {"name","activity_id"}))
public class School extends BaseEntity {
	@NotNull
	@Column(unique = true)
	private String name;

	@NotNull
	@Column(unique = true)
	private String username;

	@NotNull
	@Column(unique = true)
	private String cid;

	@Column(unique = true)
	private String address;

	@NotNull
	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String contactNumber;

	private String logo;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "user_id")
//	User user;

	@ManyToMany(mappedBy = "schools")
	private List<Grade> grades;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "school")
	private List<Teacher> teachers;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "school")
	private List<Student> students;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "school")
	private List<SchoolManagementMember> schoolManagementMembers;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "school")
	private List<StudentSchoolGrade> studentSchoolGrades;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	private List<TeacherSchoolGrade> teacherSchoolGrades;

	@ManyToMany(mappedBy = "schools", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Activity> activities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<SchoolManagementMember> getSchoolManagementMembers() {
		return schoolManagementMembers;
	}

	public void setSchoolManagementMembers(List<SchoolManagementMember> schoolManagementMembers) {
		this.schoolManagementMembers = schoolManagementMembers;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	// public Long getId() {
	// return id;
	// }
	//
	// public void setId(Long id) {
	// this.id = id;
	// }

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<StudentSchoolGrade> getStudentSchoolGrades() {
		return studentSchoolGrades;
	}

	public void setStudentSchoolGrades(List<StudentSchoolGrade> studentSchoolGrades) {
		this.studentSchoolGrades = studentSchoolGrades;
	}

	public List<TeacherSchoolGrade> getTeacherSchoolGrades() {
		return teacherSchoolGrades;
	}

	public void setTeacherSchoolGrades(List<TeacherSchoolGrade> teacherSchoolGrades) {
		this.teacherSchoolGrades = teacherSchoolGrades;
	}

	public School(@NotNull String name, @NotNull String username, @NotNull String cid, String address,
			@NotNull String email, String contactNumber, String logo, Boolean active, List<Grade> grades,
			List<Teacher> teachers, List<Student> students, List<SchoolManagementMember> schoolManagementMembers,
			List<StudentSchoolGrade> studentSchoolGrades, List<Activity> activities) {
		this.name = name;
		this.username = username;
		this.cid = cid;
		this.address = address;
		this.email = email;
		this.contactNumber = contactNumber;
		this.logo = logo;
		this.setActive(active);
//		this.user = user;
		this.grades = grades;
		this.teachers = teachers;
		this.students = students;
		this.schoolManagementMembers = schoolManagementMembers;
		this.studentSchoolGrades = studentSchoolGrades;
		this.activities = activities;
	}

	public School() {

	}

}
