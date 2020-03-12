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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.FourS;

@SuppressWarnings("serial")
@Entity
@DynamicUpdate(true)
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

	private Boolean active;

	private Boolean isGeneral;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "activity_focus_area", joinColumns = { @JoinColumn(name = "activity_id") }, inverseJoinColumns = {
			@JoinColumn(name = "focus_area_id") })
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public Activity(@NotNull String name, FourS fourS, @NotNull String cid, String description, Boolean active,
			List<FocusArea> focusAreas) {
		this.name = name;
		this.fourS = fourS;
		this.cid = cid;
		this.description = description;
		this.active = active;
		this.focusAreas = focusAreas;
	}

	public Activity(@NotNull String name, FourS fourS, @NotNull String cid, String description, Boolean active,
			List<FocusArea> focusAreas, List<Teacher> teachers, List<School> schools) {
		this.name = name;
		this.fourS = fourS;
		this.cid = cid;
		this.description = description;
		this.active = active;
		this.focusAreas = focusAreas;
		this.teachers = teachers;
		this.schools = schools;
	}

	public Activity() {

	}
}
