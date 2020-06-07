package com.nxtlife.mgs.view;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.util.DateUtil;

@JsonInclude(content = Include.NON_ABSENT)
public class ClubMembershipResponse {

	private StudentResponse student;
	private ActivityRequestResponse club;
	private String appliedOn;
	private String consideredOn;
	private String membershipStatus;
	private String supervisorName;
	
	public StudentResponse getStudent() {
		return student;
	}


	public void setStudent(StudentResponse student) {
		this.student = student;
	}

	public ActivityRequestResponse getClub() {
		return club;
	}


	public void setClub(ActivityRequestResponse club) {
		this.club = club;
	}


	public String getAppliedOn() {
		return appliedOn;
	}


	public void setAppliedOn(String appliedOn) {
		this.appliedOn = appliedOn;
	}


	public String getConsideredOn() {
		return consideredOn;
	}


	public void setConsideredOn(String consideredOn) {
		this.consideredOn = consideredOn;
	}


	public String getMembershipStatus() {
		return membershipStatus;
	}


	public void setMembershipStatus(String membershipStatus) {
		this.membershipStatus = membershipStatus;
	}


	public String getSupervisorName() {
		return supervisorName;
	}


	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}


	public ClubMembershipResponse(StudentClub studentClub) {
		this.student = new StudentResponse(studentClub.getStudent());
		this.club = new ActivityRequestResponse(studentClub.getActivity());
		if(studentClub.getAppliedOn() != null)
			this.appliedOn = studentClub.getAppliedOn().toString(); // DateUtil.formatDate(studentClub.getAppliedOn());
		if(studentClub.getConsideredOn() != null)
			this.consideredOn = studentClub.getConsideredOn().toString(); // DateUtil.formatDate(studentClub.getConsideredOn());
		if(studentClub.getMembershipStatus() != null)
			this.membershipStatus = studentClub.getMembershipStatus().toString();
		if(studentClub.getTeacher() != null)
			this.supervisorName = studentClub.getTeacher().getName();
		
	}
	
	public ClubMembershipResponse() {
		
	}
	
}
