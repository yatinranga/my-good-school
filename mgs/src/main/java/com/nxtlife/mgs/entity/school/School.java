package com.nxtlife.mgs.entity.school;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.SchoolManagementMember;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;

@Entity
public class School extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull
	@Column(unique=true)
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cId;
	
	@Column(unique=true)
	private String address;
	
	@NotNull
	@Column(unique=true)
	private String email;
	
	@Column(unique=true)
	private String contactNumber;
	
	private String logo;
	
	private Boolean active;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "school")
	private List<Grade> grades;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "school")
	private List<Teacher> teachers;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "school")
	private List<Student> students;
	
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY,mappedBy = "school")
	private List<SchoolManagementMember> schoolManagementMembers;

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
	
	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public School(@NotNull String name, String address, @NotNull String email, String contactNumber, Boolean active,
			List<Grade> grades, List<Teacher> teachers, List<Student> students,
			List<SchoolManagementMember> schoolManagementMembers) {
		this.name = name;
		this.address = address;
		this.email = email;
		this.contactNumber = contactNumber;
		this.active = active;
		this.grades = grades;
		this.teachers = teachers;
		this.students = students;
		this.schoolManagementMembers = schoolManagementMembers;
	}
	
	public School(){
		
	}
	
}
