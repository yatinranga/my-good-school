package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.entity.school.AwardType;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.AwardCriterion;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.filtering.filter.AwardFilter;
import com.nxtlife.mgs.filtering.filter.AwardFilterBuilder;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.AwardActivityPerformedRepository;
import com.nxtlife.mgs.jpa.AwardRepository;
import com.nxtlife.mgs.jpa.AwardTypeRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.PropertyCount;

@Service("awardServiceImpl")
@Transactional
public class AwardServiceImpl extends BaseService implements AwardService {

	@Autowired
	AwardRepository awardRepository;

	@Autowired
	Utils utils;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	ActivityPerformedRepository activityPerformedRepository;

	@Autowired
	AwardActivityPerformedRepository awardActivityPerformedRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TeacherRepository teacherRepository;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	FocusAreaRepository focusAreaRepository;
	
	@Autowired
	AwardTypeRepository awardTypeRepository;
	
	@PostConstruct
	public void init() {
		String[] awardTypes = { "Ist Position", "IInd Position", "IIIrd Position", "Participation Prize" };
		
		Set<AwardType> repoAwardTypes = awardTypeRepository.findAll().stream().collect(Collectors.toSet());
		for(String type : awardTypes) {
			AwardType awardType = repoAwardTypes.stream().filter(at -> at.getName().equalsIgnoreCase(type)).findFirst().orElse(null);
			if(awardType == null) {
				repoAwardTypes.add(new AwardType(type));
			}else {
				awardType.setActive(true);
				repoAwardTypes.add(awardType);
			}
		}
		
		awardTypeRepository.save(repoAwardTypes);
	}
	
	@Override
	public Set<String> getAwardCriterias(){
		Set<String> awardCriterias = new HashSet<String>();
		Arrays.asList(AwardCriterion.values()).stream().distinct().forEach(ac -> {awardCriterias.add(ac.getAwardCriterion());});
		return awardCriterias;
	}

	@Override
	public AwardResponse createAward(AwardRequest request) {
		if (request == null)
			throw new ValidationException("Request cannot be null.");
		if (request.getTeacherId() == null)
			throw new ValidationException("Teacher id cannot be null.");
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(request.getTeacherId());
		if (teacher == null)
			throw new ValidationException(String.format("Teacher (%s) does not exist.", request.getTeacherId()));
		if(request.getAwardType()==null)
			throw new ValidationException("Award Type cannot be null.");
		if(request.getStudentId()==null && (request.getActivityPerformedIds() == null || request.getActivityPerformedIds().isEmpty()))
			throw new ValidationException("Student and ActivityPerformedIds both cannot be empty/null simultaneously.");
		Student student = null;
		if(request.getStudentId()!=null) {
			 student = studentRepository.findByCidAndActiveTrue(request.getStudentId());
			if (student == null) {
				throw new ValidationException(String.format("Student (%s) doesn't exist.", request.getStudentId()));
			}
		}
		
		AwardType awardType = awardTypeRepository.getByNameAndActiveTrue(request.getAwardType());
		if(awardType == null)
			throw new ValidationException(String.format("AwardType (%s) not found.", request.getAwardType()));
		
		Award award = request.toEntity();
		
		if(request.getActivityId() != null) {
			Activity activity = activityRepository.findByCidAndActiveTrue(request.getActivityId());
			if (activity == null) {
				throw new ValidationException(String.format("Activity (%s) doesn't exist", request.getActivityId()));
			}
			award.setActivity(activity);
		}
		
		award.setAwardType(awardType);
		award.setActive(true);
		award.setCid(utils.generateRandomAlphaNumString(8));
		award.setTeacher(teacher);
		award.setStatus(ApprovalStatus.PENDING);
		award.setStudent(student);
		award.setActive(true);
		award = awardRepository.save(award);
		if (request.getActivityPerformedIds() != null && !request.getActivityPerformedIds().isEmpty()) {
			List<AwardActivityPerformed> awardActivityPerformedList = new ArrayList<>();
			ActivityPerformed activityPerformed;
			for (String activityPerformedId : request.getActivityPerformedIds()) {
				activityPerformed = activityPerformedRepository.findByCidAndActiveTrue(activityPerformedId);
				if (activityPerformed == null) {
					throw new ValidationException(
							String.format("This performed activity (%s) not found", activityPerformedId));
				}
				awardActivityPerformedList.add(new AwardActivityPerformed(award.getId(), activityPerformed.getId()));
			}
			award.setAwardActivityPerformed(awardActivityPerformedRepository.save(awardActivityPerformedList));
		}
		
		return new AwardResponse(award);
	}

	@Override
	public List<AwardResponse> findAllByStudent() {
		Long userId = getUserId();
		Student student = studentRepository.getByUserId(userId);
		if (student == null) {
			throw new ValidationException("User not login as a student");
		}
		List<Award> awards = awardRepository.findByStudentIdAndStatus(student.getId(), ApprovalStatus.VERIFIED);
		if(awards == null || awards.isEmpty())
			throw new NotFoundException(String.format("No Award given to student (%s) yet.",student.getName()));
		return awards.stream().map(AwardResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<AwardResponse> findAllByStudent(AwardFilter awardFilter) {
		Long userId = getUserId();
		Student student = studentRepository.getByUserId(userId);
		if (student == null) {
			throw new ValidationException("User not login as a student");
		}
		List<Award> awards = awardRepository
				.findAll(new AwardFilterBuilder().build(awardFilter, student.getCid(), ApprovalStatus.VERIFIED));
		if(awards == null || awards.isEmpty())
			throw new NotFoundException(String.format("No Award found for student (%s) after applying filters.",student.getName()));
		return awards.stream().map(AwardResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public List<AwardResponse> findAllByManagement() {
		Long userId = getUserId();
		Teacher teacher = teacherRepository.getByUserId(userId);
		if (teacher == null) {
			throw new ValidationException("User not login as a management");
		}
		if (teacher.getActivities() == null || teacher.getActivities().isEmpty()) {
			throw new ValidationException("Management not assigned with any activity");
		}
		List<Award> awards = awardRepository.findByActivityInOrActivityNull(teacher.getActivities());
		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
	}
	
	@Override
	public List<AwardResponse> findAllByManagement(AwardFilter awardFilter) {
		Long userId = getUserId();
		Teacher teacher = teacherRepository.getByUserId(userId);
		if (teacher == null) {
			throw new ValidationException("User not login as a management");
		}
		if (teacher.getActivities() == null || teacher.getActivities().isEmpty()) {
			throw new ValidationException("Management not assigned with any activity");
		}
		List<Award> awards = awardRepository.findAll(new AwardFilterBuilder().build(awardFilter, teacher.getActivities()));
		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
	}

	@Override
	public AwardResponse updateStatus(String awardId, Boolean isVerified) {
		Long userId = getUserId();
		Teacher teacher = teacherRepository.getByUserId(userId);
		if (teacher == null) {
			throw new ValidationException("User not login as a management");
		}
		if (teacher.getIsManagmentMember() == null || !teacher.getIsManagmentMember()) {
			throw new ValidationException(
					"You aren't login as a school mannagement that's why you can't verify this award");
		}
		Award award = awardRepository.findByCidAndActiveTrue(awardId);
		if (award == null) {
			throw new ValidationException(String.format("This award not exist", awardId));
		}
		if (award.getStatus().equals(ApprovalStatus.PENDING)) {
			award.setStatus(isVerified ? ApprovalStatus.VERIFIED : ApprovalStatus.REJECTED);
			if(award.getStatus().equals(ApprovalStatus.VERIFIED)) {
				award.setDateOfReceipt(new Date());
				
//				if(award.getValidFrom() == null && award.getValidUntil() == null) {
//					LocalDateTime currentDate = LocalDateTime.now();
//					award.setValidFrom(currentDate.toDate());
//					award.setValidUntil(currentDate.minusMonths(4).toDate());
//				}
			}
				
			award.setStatusModifiedBy(teacher);
			award.setStatusModifiedAt(new Date());
		} else {
			throw new ValidationException("This award already rejected or verified");
		}
		awardRepository.save(award);
		return new AwardResponse(award);
	}
	
	@Override
	public List<PropertyCount> getCount(String studentCid , String status ,String type){
		if(studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		
		if(!studentRepository.existsByCidAndActiveTrue(studentCid))
			throw new ValidationException(String.format("Student with id (%s) does not exist.", studentCid));
		
		if(!ActivityStatus.matches(status))
			throw new ValidationException(String.format("The status (%s) provided by you is invalid.", status));
		
		if(type.equalsIgnoreCase("fourS"))
		   return awardRepository.findFourSCount(studentCid , ActivityStatus.valueOf(status) , ApprovalStatus.VERIFIED);
		else if(type.equalsIgnoreCase("focusArea"))
			return awardRepository.findFocusAreaCount(studentCid, ActivityStatus.valueOf(status), ApprovalStatus.VERIFIED);
		else if(type.equalsIgnoreCase("psdArea"))
			return awardRepository.findPsdAreaCount(studentCid, ActivityStatus.valueOf(status), ApprovalStatus.VERIFIED);
		else 
			throw new ValidationException(String.format("invalid type : (%s) , type can have following values [psdArea , focusArea , fourS]", type));
	}
	
	@Override
	public Set<String> getAllAwardTypes(){
		List<String> awardTypeNames = awardTypeRepository.findAllNameByActiveTrue();
		if(awardTypeNames == null || awardTypeNames.isEmpty())
			throw new NotFoundException("No award types found.");
		return awardTypeNames.stream().collect(Collectors.toSet());
	}
}
