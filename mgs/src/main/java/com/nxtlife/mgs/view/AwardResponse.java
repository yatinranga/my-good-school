package com.nxtlife.mgs.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.enums.ApprovalStatus;

@JsonInclude(value = Include.NON_ABSENT)
public class AwardResponse {

	private String awardType;
	private String id;
	private String description;
	private String createrId;
	private String createdBy;
	private String statusModifiedBy;
	private String statusModifierId;
	private Date statusModifiedAt;
	private List<GroupResponseByActivityName<ActivityPerformedResponse>> activityPerformedResponses;
	private Date dateOfReceipt;
	private ApprovalStatus status;
	private String studentId;
	private String studentName;
	private String studentGrade;
	private ActivityRequestResponse activity;
	/*
	 * Adding these fields fourS focusAreas and psdAreas on front end's demand but
	 * they are already present in activityPerformedResponses
	 */
	private String fourS;
	private Set<String> focusAreas;
	private Set<String> psdAreas;
	private List<GroupResponseByActivityName<AwardResponse>> awards;
	private String validFrom;
	private String validUntil;
	private String awardCriterion;
	private String criterionValue;
	private String gradeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public AwardResponse() {

	}

//	public List<ActivityPerformedResponse> getActivityPerformedResponses() {
//		return activityPerformedResponses;
//	}
//
//	public void setActivityPerformedResponses(List<ActivityPerformedResponse> activityPerformedResponses) {
//		this.activityPerformedResponses = activityPerformedResponses;
//	}

	public Date getDateOfReceipt() {
		return dateOfReceipt;
	}

	public void setDateOfReceipt(Date dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public ActivityRequestResponse getActivity() {
		return activity;
	}

	public void setActivity(ActivityRequestResponse activity) {
		this.activity = activity;
	}

	public String getStatusModifiedBy() {
		return statusModifiedBy;
	}

	public void setStatusModifiedBy(String statusModifiedBy) {
		this.statusModifiedBy = statusModifiedBy;
	}

	public String getStatusModifierId() {
		return statusModifierId;
	}

	public void setStatusModifierId(String statusModifierId) {
		this.statusModifierId = statusModifierId;
	}

	public Date getStatusModifiedAt() {
		return statusModifiedAt;
	}

	public void setStatusModifiedAt(Date statusModifiedAt) {
		this.statusModifiedAt = statusModifiedAt;
	}

	public String getStudentGrade() {
		return studentGrade;
	}

	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
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

	public Set<String> getPsdAreas() {
		return psdAreas;
	}

	public void setPsdAreas(Set<String> psdAreas) {
		this.psdAreas = psdAreas;
	}

	public List<GroupResponseByActivityName<AwardResponse>> getAwards() {
		return awards;
	}

	public void setAwards(List<GroupResponseByActivityName<AwardResponse>> awards) {
		this.awards = awards;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}

	public String getAwardCriterion() {
		return awardCriterion;
	}

	public void setAwardCriterion(String awardCriterion) {
		this.awardCriterion = awardCriterion;
	}

	public String getCriterionValue() {
		return criterionValue;
	}

	public void setCriterionValue(String criterionValue) {
		this.criterionValue = criterionValue;
	}

	public List<GroupResponseByActivityName<ActivityPerformedResponse>> getActivityPerformedResponses() {
		return activityPerformedResponses;
	}

	public void setActivityPerformedResponses(
			List<GroupResponseByActivityName<ActivityPerformedResponse>> activityPerformedResponses) {
		this.activityPerformedResponses = activityPerformedResponses;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public AwardResponse(Award award) {
		this.id = award.getCid();
		this.description = award.getDescription();
		if (award.getAwardType() != null)
			this.awardType = award.getAwardType().getName();
		this.dateOfReceipt = award.getDateOfReceipt();
		this.status = award.getStatus();
		this.statusModifiedAt = award.getStatusModifiedAt();
		if (award.getTeacher() != null) {
			this.createrId = award.getTeacher().getCid();
			this.createdBy = award.getTeacher().getName();
		}
		if (award.getStudent() != null) {
			this.studentId = award.getStudent().getCid();
			this.studentName = award.getStudent().getName();
			if (award.getStudent().getGrade() != null)
				this.studentGrade = String.format("%s-%s", award.getStudent().getGrade().getName(),
						award.getStudent().getGrade().getSection());
		}
		if (award.getAwardActivityPerformed() != null && !award.getAwardActivityPerformed().isEmpty()) {
			this.activityPerformedResponses = new ArrayList<>();
			Set<String> activityTypes = new HashSet<String>();
			award.getAwardActivityPerformed().stream().forEach(act -> {
				activityTypes.add(act.getActivityPerformed().getActivity().getName());
			});
			if (activityTypes != null && !activityTypes.isEmpty()) {
				for (String type : activityTypes) {
					GroupResponseByActivityName<ActivityPerformedResponse> partialList = new GroupResponseByActivityName<ActivityPerformedResponse>();
					partialList.setCriterion("activityName");
					partialList.setCriterionValue(type);
					double avgStars[] = { 0d };
					Long[] count = { 0l };
					List<ActivityPerformedResponse> activityList = new ArrayList<ActivityPerformedResponse>();
					award.getAwardActivityPerformed().stream().forEach(act -> {
						if (act.getActivityPerformed().getActivity().getName().equalsIgnoreCase(type)) {
							activityList.add(new ActivityPerformedResponse(act.getActivityPerformed()));
							avgStars[0] += act.getActivityPerformed().getStar();
							count[0]++;
						}
					});
					partialList.setResponses(activityList);
					partialList.setCount(count[0]);
					partialList.setAverageStars(avgStars[0] / count[0]);
					this.activityPerformedResponses.add(partialList);
				}

			}
		}
//			for (AwardActivityPerformed awardActivityPerformed : award.getAwardActivityPerformed()) {
//				this.activityPerformedResponses
//						.add(new ActivityPerformedResponse(awardActivityPerformed.getActivityPerformed()));
//			}

//			ActivityPerformedResponse activityPerfResp = this.getActivityPerformedResponses().stream().findFirst().orElse(null);
//			if(activityPerfResp != null) {
//				this.fourS = activityPerfResp.getFourS();
//				this.focusAreas = activityPerfResp.getFocusAreas();
//				this.psdAreas = activityPerfResp.getPsdAreas();
//			}
//		}
		if (award.getActivity() != null) {
			activity = new ActivityRequestResponse(award.getActivity());
		}
		if (award.getStatusModifiedBy() != null) {
			this.statusModifiedBy = award.getStatusModifiedBy().getName();
			this.statusModifierId = award.getStatusModifiedBy().getCid();
		}
		this.validFrom = award.getValidFrom().toString();// DateUtil.formatDate(award.getValidFrom());
		this.validUntil = award.getValidUntil().toString(); // DateUtil.formatDate(award.getValidUntil());
		if (award.getAwardCriterion() != null)
			this.awardCriterion = award.getAwardCriterion().toString();
		this.criterionValue = award.getCriterionValue();
		if (award.getGrade() != null)
			this.gradeId = award.getGrade().getCid();

	}

//	public AwardResponse performActivityGrouping() {
//		if(this.awards != null && !this.awards.isEmpty()) {
//			for()
//		}
//		
//		return this;
//	}

}
