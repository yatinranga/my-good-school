package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.glassfish.jersey.server.validation.ValidationError;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.enums.AwardCriterion;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.util.DateUtil;

public class AwardRequest extends Request{

	@NotEmpty(message = "Award Type cannot be null or empty.")
	private String awardType;
	private String id;
	
	@NotNull
	private String description;
	
	@NotNull(message = "teacher id cannot be null.")
	private String teacherId;
	
	private String studentId;
	private List<String> activityPerformedIds;
	private String schoolId;
	private String gradeId;
	private String activityId;
	private List<AwardActivityPerformedCid> awardActivityPerformedList;
	private String validFrom;
	private String validUntil;
	private String awardCriterion;
	private String criterionValue;

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

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public List<String> getActivityPerformedIds() {
		return activityPerformedIds;
	}

	public void setActivityPerformedIds(List<String> activityPerformedIds) {
		this.activityPerformedIds = activityPerformedIds;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public List<AwardActivityPerformedCid> getAwardActivityPerformedList() {
		return awardActivityPerformedList;
	}

	public void setAwardActivityPerformedList(List<AwardActivityPerformedCid> awardActivityPerformedList) {
		this.awardActivityPerformedList = awardActivityPerformedList;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
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

	public Award toEntity() {
		return toEntity(null);
	}
	
	public Award toEntity(Award award) {
		award = award == null ? new Award() : award;
		award.setCid(this.id);
		if(this.description != null) {
			if(countWords(this.description) < 10)
				throw new ValidationException("description cannot be less than 10 words.");
			award.setDescription(this.description);
		}
		LocalDateTime currentDateTime = LocalDateTime.now(DateTimeZone.forTimeZone(DateUtil.defaultTimeZone));
		if((validFrom !=null && DateUtil.convertStringToDate(validFrom).after(currentDateTime.toDate())) ||
				validUntil !=null && DateUtil.convertStringToDate(validUntil).after(currentDateTime.toDate()))
			throw new ValidationException("StartDate or endDate cannot be a future date.");
		Date startDate,endDate; 
		if(validFrom == null && validUntil == null) {
			endDate = currentDateTime.toDate();
			startDate = currentDateTime.minusMonths(4).toDate();
		}else if(validFrom == null && validUntil!=null) {
			endDate = DateUtil.convertStringToDate(validUntil);
			startDate = LocalDateTime.fromDateFields(endDate).minusMonths(4).toDate();
		}else if(validFrom != null && validUntil == null) {
			 startDate = DateUtil.convertStringToDate(validFrom);
			 endDate = currentDateTime.toDate();
		}else {
			startDate = DateUtil.convertStringToDate(validFrom);
            endDate = DateUtil.convertStringToDate(validUntil);
            if(startDate.after(endDate))
            	throw new ValidationException("startDate shall fall before end date.");
		}
		award.setValidFrom(startDate);
		award.setValidUntil(endDate);

		
		
		
//		if(this.getValidFrom() == null && this.getValidUntil() == null) {
//			LocalDateTime currentDate = LocalDateTime.now();
//			award.setValidFrom(currentDate.toDate());
//			award.setValidUntil(currentDate.minusMonths(4).toDate());
//		}
//		if(this.validFrom != null) {
//			if (LocalDateTime.now().toDate().before(DateUtil.convertStringToDate(this.validFrom)))
//				throw new ValidationException("ValidFrom cannot be a future date.");
//			 award.setValidFrom(DateUtil.convertStringToDate(this.validFrom));
//		}
//		  
//		if(this.validUntil != null) {
//			if (LocalDateTime.now().toDate().before(DateUtil.convertStringToDate(this.validUntil)))
//				throw new ValidationException("validUntil cannot be a future date.");
//			 award.setValidUntil(DateUtil.convertStringToDate(this.validUntil));
//		}
		return award;
	}

}
