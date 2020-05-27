package com.nxtlife.mgs.entity.activity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ClubOrSociety;
import com.nxtlife.mgs.enums.FourS;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","fourS"}))
public class Activity extends BaseEntity {

	@NotNull
	@Column(unique = true)
	private String name;

	@Enumerated(EnumType.STRING)
	private FourS fourS;

	@NotNull
	@Column(unique = true)
	private String cid;

	private String description;

	private Boolean isGeneral;
	
	@Enumerated(EnumType.STRING)
	private ClubOrSociety clubOrSociety;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "activity_focus_area", joinColumns = { @JoinColumn(name = "activity_id") }, inverseJoinColumns = {
			@JoinColumn(name = "focus_area_id") },uniqueConstraints = @UniqueConstraint(columnNames = {"activity_id" , "focus_area_id"}))
	private List<FocusArea> focusAreas;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "teacher_activity", joinColumns = { @JoinColumn(name = "activity_id") }, inverseJoinColumns = {
			@JoinColumn(name = "teacher_id") },uniqueConstraints = {@UniqueConstraint(columnNames = {"activity_id","teacher_id"})})
	private List<Teacher> teachers;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "school_activity", joinColumns = { @JoinColumn(name = "activity_id") }, inverseJoinColumns = {
			@JoinColumn(name = "school_id") }, uniqueConstraints = @UniqueConstraint(columnNames = { "activity_id",
					"school_id" }))
	@Fetch(value = FetchMode.SUBSELECT)
	private List<School> schools;
	
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY , mappedBy = "activity")
	private List<StudentClub> studentClubs;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity")
	private List<TeacherActivityGrade> teacherActivityGrades;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FourS getFourS() {
		return fourS;
	}

	public void setFourS(FourS fourS) {
		this.fourS = fourS;
		if(fourS != null) {
			if(fourS.equals(FourS.Service) || fourS.equals(FourS.Study))
				this.clubOrSociety = ClubOrSociety.Society;
			else
				this.clubOrSociety = ClubOrSociety.Club;
		}
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FocusArea> getFocusAreas() {
		return focusAreas;
	}

	public void setFocusAreas(List<FocusArea> focusAreas) {
		this.focusAreas = focusAreas;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<School> getSchools() {
		return schools;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public Boolean getIsGeneral() {
		return isGeneral;
	}

	public void setIsGeneral(Boolean isGeneral) {
		this.isGeneral = isGeneral;
	}

	public List<StudentClub> getStudentClubs() {
		return studentClubs;
	}

	public void setStudentClubs(List<StudentClub> studentClubs) {
		this.studentClubs = studentClubs;
	}

	public ClubOrSociety getClubOrSociety() {
		return clubOrSociety;
	}

	public void setClubOrSociety(ClubOrSociety clubOrSociety) {
		this.clubOrSociety = clubOrSociety;
	}

	public List<TeacherActivityGrade> getTeacherActivityGrades() {
		return teacherActivityGrades;
	}

	public void setTeacherActivityGrades(List<TeacherActivityGrade> teacherActivityGrades) {
		this.teacherActivityGrades = teacherActivityGrades;
	}

	public Activity(@NotNull String name, FourS fourS, @NotNull String cid, String description, Boolean active,
			List<FocusArea> focusAreas) {
		this.name = name;
		this.fourS = fourS;
		this.cid = cid;
		this.description = description;
		this.setActive(active);
		this.focusAreas = focusAreas;
	}

	public Activity(@NotNull String name, FourS fourS, @NotNull String cid, String description, Boolean active,
			List<FocusArea> focusAreas, List<Teacher> teachers, List<School> schools) {
		this.name = name;
		this.fourS = fourS;
		this.cid = cid;
		this.description = description;
		this.setActive(active);
		this.focusAreas = focusAreas;
		this.teachers = teachers;
		this.schools = schools;
	}

	public Activity() {

	}
}
