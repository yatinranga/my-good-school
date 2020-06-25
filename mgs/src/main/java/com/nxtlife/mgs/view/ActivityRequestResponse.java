package com.nxtlife.mgs.view;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.ex.ValidationException;

@JsonInclude(content = Include.NON_EMPTY )
public class ActivityRequestResponse {

	@NotEmpty(message = " activity name can't be null")
	private String name;
	private String description;
	private String id;
	
	@NotEmpty(message = "fourS cannot be empty or null.")
	private String fourS;
	
	private Boolean isGeneral;
	private List<String> focusAreaIds;
	private List<String> schoolIds;
	private List<String> focusAreas;
	
	private List<FocusAreaRequestResponse> focusAreaRequests;
	private Set<FocusAreaRequestResponse> focusAreaResponses;
	
	private String clubOrSociety;
	
	private List<String> grades;
	
	private List<GradeResponse> gradeResponses;
	
	private Boolean visited = false;
	
	private String supervisorName;

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

	public List<String> getFocusAreas() {
		return focusAreas;
	}

	public void setFocusAreas(List<String> focusAreas) {
		this.focusAreas = focusAreas;
	}

	public List<String> getFocusAreaIds() {
		return focusAreaIds;
	}

	public void setFocusAreaIds(List<String> focusAreaIds) {
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

	public Activity toEntity(Activity activity) {
		activity = activity == null ? new Activity() : activity;
		activity.setName(this.name);
		activity.setDescription(this.description);
		if(!FourS.matches(this.fourS))
			throw new ValidationException("Invalid value for field fourS , it should belong to list : [Skill ,Sport ,Study ,Service]");
		activity.setFourS(FourS.valueOf(this.fourS));
		activity.setIsGeneral(this.isGeneral);
		return activity;
	}

	public ActivityRequestResponse(Activity activity) {
		this.id = activity.getCid();

		if (activity.getFourS() != null)
			this.fourS = activity.getFourS().name();
		this.name = activity.getName();
		this.description = activity.getDescription();
		this.isGeneral = activity.getIsGeneral();
		if(activity.getClubOrSociety() != null)
		      this.clubOrSociety = activity.getClubOrSociety().toString();
		this.focusAreaResponses = activity.getFocusAreas().stream().map(FocusAreaRequestResponse :: new ).distinct().collect(Collectors.toSet());
		// focusAreaIds = new ArrayList<String>();
		/*
		 * this.focusAreas = new ArrayList<String>(); for (FocusArea fa :
		 * activity.getFocusAreas()) { // focusAreaIds.add(fa.getCid());
		 * focusAreas.add(fa.getName()); } focusAreas =
		 * focusAreas.stream().distinct().collect(Collectors.toList());
		 */
		// focusAreaIds =
		// focusAreaIds.stream().distinct().collect(Collectors.toList());
		if(activity.getSchools()!=null) {
			activity.getSchools().stream().distinct().map(s -> s.getCid()).collect(Collectors.toList());
		}

	}

	public ActivityRequestResponse() {

	}
}
