package com.nxtlife.mgs.view;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.glassfish.jersey.server.validation.ValidationError;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.nxtlife.mgs.entity.school.Award;
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
//	private String validFrom;
//	private String validUntil;

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

	public Award toEntity() {
		return toEntity(null);
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	
	public Award toEntity(Award award) {
		award = award == null ? new Award() : award;
		award.setCid(this.id);
		if(this.description != null) {
			if(countWords(this.description) < 10)
				throw new ValidationException("description cannot be less than 10 words.");
			award.setDescription(this.description);
		}
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
