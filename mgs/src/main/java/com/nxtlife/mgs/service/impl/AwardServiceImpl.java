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
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
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
import com.nxtlife.mgs.view.GroupResponseByActivityName;
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
		if(request.getGradeId() != null) {
			if(!gradeRepository.existsByCidAndActiveTrue(request.getGradeId()))
				throw new ValidationException(String.format("Grade with id (%s) does not exist.",request.getGradeId()));
			
			award.setGrade(gradeRepository.getOneByCid(request.getGradeId()));
		}
		if(request.getAwardCriterion() != null) {
			if(!AwardCriterion.matches(request.getAwardCriterion()))
				throw new ValidationException(String.format("Invalid value (%s) for awardCriterion it should be of form : [PSD Area ,Focus Area , 4S ,Activity Type]", request.getAwardCriterion()));
			award.setAwardCriterion(AwardCriterion.fromString(request.getAwardCriterion()));
			if(request.getCriterionValue() == null)
				throw new ValidationException("Please provide criterion value.");
			if(request.getAwardCriterion().equals(AwardCriterion.PSDArea)) {
				if(!PSDArea.matches(request.getCriterionValue()))
					throw new ValidationException(String.format("PSD Area (%s) does not matches with available PSD Areas i.e [Personal Development, Social Development]", request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			}else if(request.getAwardCriterion().equals(AwardCriterion.FocusArea)){
				if(!focusAreaRepository.existsByNameAndActiveTrue(request.getCriterionValue()))
					throw new ValidationException(String.format("Focus Area  (%s) does not exist.", request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			}else if(request.getAwardCriterion().equals(AwardCriterion.FourS)){
				if(!FourS.matches(request.getCriterionValue()))
					throw new ValidationException(String.format("FourS  (%s) does not matches with available values for fourS i.e [Skill , Sport ,Study ,Service]", request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			}else if(request.getAwardCriterion().equals(AwardCriterion.ActivityType)){
				if(!activityRepository.existsByNameAndActiveTrue(request.getCriterionValue()))
					throw new ValidationException(String.format("Activity with name  (%s) does not exist.", request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			}else {
				throw new ValidationException(String.format("Invalid value (%s) for awardCriterion it should be of form : [PSD Area ,Focus Area , 4S ,Activity Type]", request.getAwardCriterion()));
			}
		}
		
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
	public AwardResponse findAllByStudent() {
		Long userId = getUserId();
		Student student = studentRepository.getByUserId(userId);
		if (student == null) {
			throw new ValidationException("User not login as a student");
		}
		List<Award> awards = awardRepository.findByStudentIdAndStatus(student.getId(), ApprovalStatus.VERIFIED);
		if(awards == null || awards.isEmpty())
			throw new NotFoundException(String.format("No Award given to student (%s) yet.",student.getName()));
		
		Set<String> criterionValues = new HashSet<String>();
		awards.stream().forEach(awrd -> {
			if(awrd.getCriterionValue() != null) 
				criterionValues.add(awrd.getCriterionValue());});
		AwardResponse response = new AwardResponse();
//		awards.stream().forEach(awrd -> {awrd.getAwardActivityPerformed().stream().forEach(awrdAct -> {activityTypes.add(awrdAct.getActivityPerformed().getActivity().getName());});});
		
//		for(String act : activityTypes) {
//			awards.stream().filter(awrd -> awrd.getAwardActivityPerformed().)
//		}
		List<AwardResponse> awardResponses = awards.stream().map(AwardResponse::new).collect(Collectors.toList());
		List<GroupResponseByActivityName<AwardResponse>> finalAwardsResponse = new ArrayList<GroupResponseByActivityName<AwardResponse>>();
		GroupResponseByActivityName<AwardResponse> partialAwardsList;
		if(criterionValues !=null && !criterionValues.isEmpty())
		for(String criterionVal : criterionValues) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setCriterionValue(criterionVal);
			partialAwardsList.setResponses(awardResponses.stream().filter(awr -> criterionVal.equalsIgnoreCase(awr.getCriterionValue())).collect(Collectors.toList()));
			partialAwardsList.setCriterion( partialAwardsList.getResponses().get(0).getAwardCriterion());
			finalAwardsResponse.add(partialAwardsList);
		}
		if(finalAwardsResponse.isEmpty()) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setResponses(awardResponses);
			finalAwardsResponse.add(partialAwardsList);
		}
		response.setAwards(finalAwardsResponse);
		return response;
//		return awards.stream().map(AwardResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public AwardResponse findAllByStudent(AwardFilter awardFilter) {
		Long userId = getUserId();
		Student student = studentRepository.getByUserId(userId);
		if (student == null) {
			throw new ValidationException("User not login as a student");
		}
		List<Award> awards = awardRepository
				.findAll(new AwardFilterBuilder().build(awardFilter, student.getCid(), ApprovalStatus.VERIFIED));
		if(awards == null || awards.isEmpty())
			throw new NotFoundException(String.format("No Award found for student (%s) after applying filters.",student.getName()));
		
		Set<String> criterionValues = new HashSet<String>();
		awards.stream().forEach(awrd -> {
			if(awrd.getCriterionValue() != null) 
				criterionValues.add(awrd.getCriterionValue());});
		AwardResponse response = new AwardResponse();
//		awards.stream().forEach(awrd -> {awrd.getAwardActivityPerformed().stream().forEach(awrdAct -> {activityTypes.add(awrdAct.getActivityPerformed().getActivity().getName());});});
		
//		for(String act : activityTypes) {
//			awards.stream().filter(awrd -> awrd.getAwardActivityPerformed().)
//		}
		List<AwardResponse> awardResponses = awards.stream().map(AwardResponse::new).collect(Collectors.toList());
		List<GroupResponseByActivityName<AwardResponse>> finalAwardsResponse = new ArrayList<GroupResponseByActivityName<AwardResponse>>();
		GroupResponseByActivityName<AwardResponse> partialAwardsList;
		if(criterionValues !=null && !criterionValues.isEmpty())
		for(String criterionVal : criterionValues) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setCriterionValue(criterionVal);
			partialAwardsList.setResponses(awardResponses.stream().filter(awr -> criterionVal.equalsIgnoreCase(awr.getCriterionValue())).collect(Collectors.toList()));
			partialAwardsList.setCriterion( partialAwardsList.getResponses().get(0).getAwardCriterion());
			finalAwardsResponse.add(partialAwardsList);
		}
		if(finalAwardsResponse.isEmpty()) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setResponses(awardResponses);
			finalAwardsResponse.add(partialAwardsList);
		}
		response.setAwards(finalAwardsResponse);
		return response;
//		return awards.stream().map(AwardResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	public AwardResponse findAllByManagement() {
		Long userId = getUserId();
		Teacher teacher = teacherRepository.getByUserId(userId);
		if (teacher == null) {
			throw new ValidationException("User not login as a management");
		}
		if (teacher.getActivities() == null || teacher.getActivities().isEmpty()) {
			throw new ValidationException("Management not assigned with any activity");
		}
		List<Award> awards = awardRepository.findByActivityInOrActivityNull(teacher.getActivities());
		Set<String> criterionValues = new HashSet<String>();
		awards.stream().forEach(awrd -> {
			if(awrd.getCriterionValue() != null) 
				criterionValues.add(awrd.getCriterionValue());});
		AwardResponse response = new AwardResponse();
//		awards.stream().forEach(awrd -> {awrd.getAwardActivityPerformed().stream().forEach(awrdAct -> {activityTypes.add(awrdAct.getActivityPerformed().getActivity().getName());});});
		
//		for(String act : activityTypes) {
//			awards.stream().filter(awrd -> awrd.getAwardActivityPerformed().)
//		}
		List<AwardResponse> awardResponses = awards.stream().map(AwardResponse::new).collect(Collectors.toList());
		List<GroupResponseByActivityName<AwardResponse>> finalAwardsResponse = new ArrayList<GroupResponseByActivityName<AwardResponse>>();
		GroupResponseByActivityName<AwardResponse> partialAwardsList;
		if(criterionValues !=null && !criterionValues.isEmpty())
		for(String criterionVal : criterionValues) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setCriterionValue(criterionVal);
			partialAwardsList.setResponses(awardResponses.stream().filter(awr -> criterionVal.equalsIgnoreCase(awr.getCriterionValue())).collect(Collectors.toList()));
			partialAwardsList.setCriterion( partialAwardsList.getResponses().get(0).getAwardCriterion());
			finalAwardsResponse.add(partialAwardsList);
		}
		if(finalAwardsResponse.isEmpty()) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setResponses(awardResponses);
			finalAwardsResponse.add(partialAwardsList);
		}
		response.setAwards(finalAwardsResponse);
		return response;
//		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
	}
	
	@Override
	public AwardResponse findAllByManagement(AwardFilter awardFilter) {
		Long userId = getUserId();
		Teacher teacher = teacherRepository.getByUserId(userId);
		if (teacher == null) {
			throw new ValidationException("User not login as a management");
		}
		if (teacher.getActivities() == null || teacher.getActivities().isEmpty()) {
			throw new ValidationException("Management not assigned with any activity");
		}
		List<Award> awards = awardRepository.findAll(new AwardFilterBuilder().build(awardFilter, teacher.getActivities()));
		
		Set<String> criterionValues = new HashSet<String>();
		awards.stream().forEach(awrd -> {
			if(awrd.getCriterionValue() != null) 
				criterionValues.add(awrd.getCriterionValue());});
		AwardResponse response = new AwardResponse();
//		awards.stream().forEach(awrd -> {awrd.getAwardActivityPerformed().stream().forEach(awrdAct -> {activityTypes.add(awrdAct.getActivityPerformed().getActivity().getName());});});
		
//		for(String act : activityTypes) {
//			awards.stream().filter(awrd -> awrd.getAwardActivityPerformed().)
//		}
		List<AwardResponse> awardResponses = awards.stream().map(AwardResponse::new).collect(Collectors.toList());
		List<GroupResponseByActivityName<AwardResponse>> finalAwardsResponse = new ArrayList<GroupResponseByActivityName<AwardResponse>>();
		GroupResponseByActivityName<AwardResponse> partialAwardsList;
		if(criterionValues !=null && !criterionValues.isEmpty())
		for(String criterionVal : criterionValues) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setCriterionValue(criterionVal);
			partialAwardsList.setResponses(awardResponses.stream().filter(awr -> criterionVal.equalsIgnoreCase(awr.getCriterionValue())).collect(Collectors.toList()));
			partialAwardsList.setCriterion( partialAwardsList.getResponses().get(0).getAwardCriterion());
			finalAwardsResponse.add(partialAwardsList);
		}
		if(finalAwardsResponse.isEmpty()) {
			partialAwardsList = new GroupResponseByActivityName<AwardResponse>();
			partialAwardsList.setResponses(awardResponses);
			finalAwardsResponse.add(partialAwardsList);
		}
		response.setAwards(finalAwardsResponse);
		return response;
//		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
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
