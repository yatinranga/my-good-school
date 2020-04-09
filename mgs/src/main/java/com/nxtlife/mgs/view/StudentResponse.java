package com.nxtlife.mgs.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.util.DateUtil;

@JsonInclude(value = Include.NON_ABSENT)
public class StudentResponse {

	private String id;
	private String name;
	private String username;
	private String userId;
	private String gender;
	private String email;
	private String section;
	private String grade;
	private String schoolId;
	private String mobileNumber;
	private String subscriptionEndDate;
	private Boolean active;
	private String dob;
	private String schoolName;
	private List<GuardianResponse> guardianResponseList;
	private String profileImage;
	private String yearOfEnrolment;
	private List<ActivityActivitiesPerformedResponse> performedActivities;
	private Set<String> focusAreas;
	private Set<String> activityTypes;
	private Double scoreForAward;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setSubscriptionEndDate(String subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public List<GuardianResponse> getGuardianResponseList() {
		return guardianResponseList;
	}

	public void setGuardianResponseList(List<GuardianResponse> guardianResponseList) {
		this.guardianResponseList = guardianResponseList;
	}

	public String getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getYearOfEnrolment() {
		return yearOfEnrolment;
	}

	public void setYearOfEnrolment(String yearOfEnrolment) {
		this.yearOfEnrolment = yearOfEnrolment;
	}

	public List<ActivityActivitiesPerformedResponse> getPerformedActivities() {
		return performedActivities;
	}

	public void setPerformedActivities(List<ActivityActivitiesPerformedResponse> performedActivities) {
		this.performedActivities = performedActivities;
	}

	public Set<String> getFocusAreas() {
		return focusAreas;
	}

	public void setFocusAreas(Set<String> focusAreas) {
		this.focusAreas = focusAreas;
	}

	public Set<String> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(Set<String> activityTypes) {
		this.activityTypes = activityTypes;
	}

	public Double getScoreForAward() {
		return scoreForAward;
	}

	public void setScoreForAward(Double scoreForAward) {
		this.scoreForAward = scoreForAward;
	}

	public StudentResponse(Student student) {
		this.id = student.getCid();
		this.name = student.getName();
		this.username = student.getUsername();
		this.email = student.getEmail();
		if (student.getDob() != null)
			this.dob = DateUtil.formatDate(student.getDob(), "yyyy-MM-dd");
		this.gender = student.getGender();
		this.mobileNumber = student.getMobileNumber();
		this.active = student.getActive();
		this.profileImage = student.getImageUrl();
		if (student.getCreatedDate() != null)
			this.yearOfEnrolment = Integer.toString(student.getCreatedDate().getYear());

		if (student.getSubscriptionEndDate() != null)
			this.subscriptionEndDate = DateUtil.formatDate(student.getSubscriptionEndDate());

		if (student.getUser() != null) {
			this.userId = student.getUser().getCid();
		}

		if (student.getGrade() != null) {
			this.grade = student.getGrade().getName();
			this.section = student.getGrade().getSection();
		}

		if (student.getSchool() != null) {
			this.schoolId = student.getSchool().getCid();
			this.schoolName = student.getSchool().getName();
		}

		if (student.getGuardians() != null && !student.getGuardians().isEmpty())
			this.guardianResponseList = student.getGuardians().stream().map(g -> new GuardianResponse(g))
					.collect(Collectors.toList());

	}

	public StudentResponse(Student student, Boolean responseForGetInfo) {
		if (responseForGetInfo == true) {
			this.id = student.getCid();
			this.name = student.getName();
			this.username = student.getUsername();
			this.email = student.getEmail();
			if (student.getDob() != null)
				this.dob = DateUtil.formatDate(student.getDob());
			this.gender = student.getGender();
			this.mobileNumber = student.getMobileNumber();
			this.active = student.getActive();
			this.profileImage = student.getImageUrl();
			if (student.getCreatedDate() != null)
				this.yearOfEnrolment = Integer.toString(student.getCreatedDate().getYear());

			if (student.getSubscriptionEndDate() != null)
				this.subscriptionEndDate = DateUtil.formatDate(student.getSubscriptionEndDate());

			if (student.getUser() != null) {
				this.userId = student.getUser().getCid();
			}

			if (student.getGrade() != null) {
				this.grade = student.getGrade().getName();
				this.section = student.getGrade().getSection();
			}

			if (student.getSchool() != null) {
				this.schoolId = student.getSchool().getCid();
				this.schoolName = student.getSchool().getName();
			}

		}

	}

	public StudentResponse() {

	}

}
