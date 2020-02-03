package com.nxtlife.mgs.entity.activity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.FourS;

@Entity
public class Activity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String name;
	
	@Enumerated(EnumType.STRING)
	private FourS fourS;
	
	@NotNull
	@Column(unique = true)
	private String cId;
	
	private String description;
	
	private Boolean active;
	
	@ManyToMany
	@JoinTable(name = "activity_focus_area",
		joinColumns = { @JoinColumn(name = "activity_id") },
		inverseJoinColumns = { @JoinColumn(name = "focus_area_id") })
	private List<FocusArea> focusAreas;
	
	@ManyToMany
	@JoinTable(name = "teacher_activity",
		joinColumns = { @JoinColumn(name = "teacher_id") },
		inverseJoinColumns = { @JoinColumn(name = "activity_id") })
	private List<Teacher> teachers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
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

	public Activity(@NotNull String name, FourS fourS, @NotNull String cId, String description, Boolean active,
			List<FocusArea> focusAreas) {
		this.name = name;
		this.fourS = fourS;
		this.cId = cId;
		this.description = description;
		this.active = active;
		this.focusAreas = focusAreas;
	}
	
	public Activity(@NotNull String name, FourS fourS, @NotNull String cId, String description, Boolean active,
			List<FocusArea> focusAreas, List<Teacher> teachers) {
		this.name = name;
		this.fourS = fourS;
		this.cId = cId;
		this.description = description;
		this.active = active;
		this.focusAreas = focusAreas;
		this.teachers = teachers;
	}

	public Activity() {
		
	}
}
