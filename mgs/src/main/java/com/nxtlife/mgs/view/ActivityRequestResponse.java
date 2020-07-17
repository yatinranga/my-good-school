package com.nxtlife.mgs.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.ex.ValidationException;

@JsonInclude(content = Include.NON_EMPTY)
public class ActivityRequestResponse {

//	@NotEmpty(message = " activity name can't be null")
	private String name;
	private String description;
	private String id;

	@NotEmpty(message = "fourS cannot be empty or null.")
	private String fourS;

	private Boolean isGeneral;
	private Set<String> focusAreaIds;
	private List<String> schoolIds;
	private Set<String> focusAreas;

	private List<FocusAreaRequestResponse> focusAreaRequests;
	private Set<FocusAreaRequestResponse> focusAreaResponses;

	private String clubOrSociety;

	private List<String> grades;

	private List<GradeResponse> gradeResponses;

	private Boolean visited = false;

	private String supervisorName;

	private Set<String> psdAreas;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFourS() {
		return fourS;
	}

	public void setFourS(String fourS) {
		this.fourS = fourS;
	}

	public Set<String> getFocusAreas() {
		return focusAreas;
	}

	public void setFocusAreas(Set<String> focusAreas) {
		this.focusAreas = focusAreas;
	}

	public Set<String> getFocusAreaIds() {
		return focusAreaIds;
	}

	public void setFocusAreaIds(Set<String> focusAreaIds) {
		this.focusAreaIds = focusAreaIds;
	}

	public Boolean getIsGeneral() {
		return isGeneral;
	}

	public void setIsGeneral(Boolean isGeneral) {
		this.isGeneral = isGeneral;
	}

	public List<String> getSchoolIds() {
		return schoolIds;
	}

	public void setSchoolIds(List<String> schoolIds) {
		this.schoolIds = schoolIds;
	}

	public List<FocusAreaRequestResponse> getFocusAreaRequests() {
		return focusAreaRequests;
	}

	public void setFocusAreaRequests(List<FocusAreaRequestResponse> focusAreaRequests) {
		this.focusAreaRequests = focusAreaRequests;
	}

	public Activity toEntity() {
		return toEntity(null);
	}

	public Set<FocusAreaRequestResponse> getFocusAreaResponses() {
		return focusAreaResponses;
	}

	public void setFocusAreaResponses(Set<FocusAreaRequestResponse> focusAreaResponses) {
		this.focusAreaResponses = focusAreaResponses;
	}

	public String getClubOrSociety() {
		return clubOrSociety;
	}

	public void setClubOrSociety(String clubOrSociety) {
		this.clubOrSociety = clubOrSociety;
	}

	public List<String> getGrades() {
		return grades;
	}

	public void setGrades(List<String> grades) {
		this.grades = grades;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public List<GradeResponse> getGradeResponses() {
		return gradeResponses;
	}

	public void setGradeResponses(List<GradeResponse> gradeResponses) {
		this.gradeResponses = gradeResponses;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public Set<String> getPsdAreas() {
		return psdAreas;
	}

	public void setPsdAreas(Set<String> psdAreas) {
		this.psdAreas = psdAreas;
	}

	public Activity toEntity(Activity activity) {
		activity = activity == null ? new Activity() : activity;
		activity.setName(this.name == null ? activity.getName():this.getName());
		activity.setDescription(this.description);
		if (!FourS.matches(this.fourS))
			throw new ValidationException(
					"Invalid value for field fourS , it should belong to list : [Skill ,Sport ,Study ,Service]");
		activity.setFourS(FourS.valueOf(this.fourS));
//		activity.setIsGeneral(this.isGeneral);
		return activity;
	}

	public ActivityRequestResponse(Activity activity) {
		this.id = activity.getCid();

		if (activity.getFourS() != null)
			this.fourS = activity.getFourS().name();
		this.name = activity.getName();
		this.description = activity.getDescription();
		this.isGeneral = activity.getIsGeneral();
		if (activity.getClubOrSociety() != null)
			this.clubOrSociety = activity.getClubOrSociety().toString();
		this.focusAreaResponses = activity.getFocusAreas().stream().map(FocusAreaRequestResponse::new).distinct()
				.collect(Collectors.toSet());
		if (this.focusAreaResponses != null) {
			this.focusAreas = new HashSet<String>();
			this.psdAreas = new HashSet<String>();
			this.focusAreaIds = new HashSet<String>();
			this.focusAreaResponses.stream().forEach(fa -> {
				this.focusAreas.add(fa.getName());
				this.psdAreas.add(fa.getPsdArea());
				this.focusAreaIds.add(fa.getId());
			});
		}
		if (activity.getSchools() != null) {
			this.schoolIds = activity.getSchools().stream().distinct().map(s -> s.getCid())
					.collect(Collectors.toList());
		}

	}

	public ActivityRequestResponse() {

	}
}
