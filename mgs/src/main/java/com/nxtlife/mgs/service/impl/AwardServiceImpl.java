package com.nxtlife.mgs.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.AwardActivityPerformedRepository;
import com.nxtlife.mgs.jpa.AwardRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.util.AwardActivityPerformedId;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.AwardActivityPerformedCid;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.entity.school.Award;

@Service
public class AwardServiceImpl implements AwardService{

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
	
	@Override
	public AwardResponse createAward(AwardRequest request) {
		if(request==null)
			throw new ValidationException("Request cannot be null.");
		if(request.getName()!=null) {
			Award award =awardRepository.getOneByNameAndActiveTrue(request.getName());
			if(award!=null)
				throw new ValidationException(String.format("Award with name : %s already exist.", request.getName()));
		}
		if(request.getTeacherId() == null)
			throw new ValidationException("Teacher id cannot be null.");
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(request.getTeacherId());
		if(teacher==null)
			throw new ValidationException(String.format("Teacher with id : %s does not exist.", request.getTeacherId()));
		Award award = request.toEntity();
		award.setActive(true);
		award.setCid(utils.generateRandomAlphaNumString(8));
		award.setTeacher(teacher);
		award = awardRepository.save(award);
		if(award == null)
			throw new RuntimeException("Something went wrong award not created.");
		
		return new AwardResponse(award);
	}
	
	@Override
	public List<AwardResponse> assignAward(AwardRequest request) {
		if(request==null)
			throw new ValidationException("Request cannot be null.");
		if(request.getId()==null)
			throw new ValidationException("Award id cannot be null.");
		Award award = awardRepository.getOneByCidAndActiveTrue(request.getId());
		if(award == null)
			throw new ValidationException("Award does not exist.");
		if(request.getActivityPerformedIds()==null || request.getActivityPerformedIds().isEmpty())
			throw new ValidationException("Please provide activity performed ids to assign award.");
		List<String> requestActivityPerformedIds = request.getActivityPerformedIds();
		
		if(request.getTeacherId()==null)
			throw new ValidationException("teacher id who is assigning the award cannot be null.");
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(request.getTeacherId());
		if(teacher == null)
			throw new ValidationException(String.format("Teacher with id : %s not found",request.getTeacherId()));
		
//		if(award.getTeacher()!=null && award.getTeacher().getSchool()!=null)
//		    repoActivityPerformedList = activityPerformedRepository.findAllByStudentSchoolCidAndStudentSchoolActiveTrueAndActiveTrue(award.getTeacher().getSchool().getCid());
//		else
//		    repoActivityPerformedList = activityPerformedRepository.findAllByActiveTrue();
		if(request.getSchoolId()==null)
			throw new ValidationException("school id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(request.getSchoolId());
		if(school == null)
			throw new ValidationException("No school found with id : "+request.getSchoolId());
		if(request.getGradeId()==null)
			throw new ValidationException("Grade id cannot be null.");
		Grade grade = gradeRepository.findByCidAndActiveTrue(request.getGradeId());
		if(grade==null)
			throw new ValidationException(String.format("No grade found with id : %s", request.getGradeId()));
		if(request.getActivityId() == null)
		  throw new ValidationException("activity id cannot be null.");
		Activity activity = activityRepository.findByCidAndActiveTrue(request.getActivityId());
		if(activity == null)
			throw new ValidationException(String.format("Activity with id : %s not found.", request.getActivityId()));
		
		
		List<ActivityPerformed> repoActivityPerformedList=activityPerformedRepository.findAllByStudentSchoolCidAndStudentGradeCidAndActivityCidAndAndActivityStatusAndStudentSchoolActiveTrueAndStudentGradeActiveTrueAndActivityActiveTrueAndActiveTrue(request.getSchoolId(), request.getGradeId(), request.getActivityId(), ActivityStatus.Reviewed);
		if(repoActivityPerformedList==null||repoActivityPerformedList.isEmpty()) {
			throw new ValidationException("No activities performed yet.");
		}
		List<ActivityPerformed> copyOfrepoActivityPerformedList = repoActivityPerformedList;
		
		//logic to retain valid ids in repoActivityPerformedList and invalid ids in requestActivityPerformedIds
		for(int i=0;i< repoActivityPerformedList.size() ;i++) {
			if(!requestActivityPerformedIds.contains(repoActivityPerformedList.get(i).getCid()))
				repoActivityPerformedList.remove(i--);
			else
				requestActivityPerformedIds.remove(repoActivityPerformedList.get(i).getCid());
		}
		
		if (requestActivityPerformedIds != null && !requestActivityPerformedIds.isEmpty())
			for (String id : requestActivityPerformedIds) {
				throw new ValidationException(String.format("Activity Performed with id : %s not found.", id));
			}
		
		//Write logic here to check if award is already assigned to activities present in  repoActivityPerformedList
		List<AwardActivityPerformed> awardActivityPerformedList  = awardActivityPerformedRepository.findAllByAwardCidAndIsVerifiedTrueAndActiveTrue(award.getCid());
		if (awardActivityPerformedList != null && !awardActivityPerformedList.isEmpty() && repoActivityPerformedList!=null && !repoActivityPerformedList.isEmpty()) {
			for (ActivityPerformed act : repoActivityPerformedList) {
				for (int i = 0; i < awardActivityPerformedList.size(); i++) {
					if (act.getCid().equals(awardActivityPerformedList.get(i).getActivityPerformed().getCid()))
						throw new ValidationException(String.format(
								"Award : %s already awarded to activity having id : %s on  %s", award.getName(),
								act.getCid(), awardActivityPerformedList.get(i).getDateOfReceipt()));
				}
			}
		}
		
			//directly save the entries because there are no awards allocated yet.
			List<AwardActivityPerformed> awardActivityPerformedToSaveList = new ArrayList<AwardActivityPerformed>();
			for(ActivityPerformed act : repoActivityPerformedList) {
				AwardActivityPerformed awrdActivity = new AwardActivityPerformed(new AwardActivityPerformedId(award.getId(),act.getId()), award, act);
				/*Setting mapping OneToMany side for ActivityPerformed and List<AwardActivityPerformed> */
				List<AwardActivityPerformed> toSetForActivityPerformed = act.getAwardActivityPerformed();
				toSetForActivityPerformed.add(awrdActivity);
				act.setAwardActivityPerformed(toSetForActivityPerformed);
				//check if dateOfreceipt will be set here or when verified
				awardActivityPerformedToSaveList.add(awrdActivity);
			}
			
			/*Setting mapping OneToMany side for Award and List<AwardActivityPerformed> */
			List<AwardActivityPerformed> toSetForAwards = award.getAwardActivityPerformed();
			toSetForAwards.addAll(awardActivityPerformedToSaveList);
			award.setAwardActivityPerformed(toSetForAwards);
			
			awardActivityPerformedToSaveList = awardActivityPerformedRepository.save(awardActivityPerformedToSaveList);
			if(awardActivityPerformedToSaveList == null)
				throw new RuntimeException("Something went wrong , entries not saved to AwardActivityPerformed Table.");
	
		List<AwardResponse> awardResponseList = new ArrayList<AwardResponse>();
		awardActivityPerformedToSaveList.forEach(awrdAct->{AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
		awardResponse.setIsVerified(awrdAct.getIsVerified());
		awardResponse.setDateOfReceipt(awrdAct.getDateOfReceipt());
		awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
		awardResponseList.add(awardResponse);
		});
		
		return awardResponseList;
	}
	
	@Override
	public List<AwardResponse> getAllAwardsBySchool(String schoolCid){
		if(schoolCid==null)
			throw new ValidationException("school id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);
		if(school == null)
			throw new ValidationException(String.format("No school found with id : %s.", schoolCid));
		List<Award> awards = awardRepository.findByTeacherSchoolCidAndActiveTrue(schoolCid);
		if(awards==null)
			throw new ValidationException(String.format("No awards found for the school id : %s", schoolCid));
		List<AwardResponse> awardResponses = new ArrayList<AwardResponse>();
		awards.forEach(awrd->{awardResponses.add(new AwardResponse(awrd));});
		
		return awardResponses;
	}
	
	@Override
	public List<AwardResponse> getAllUnverifiedAwardsOfSchool(String schoolCid){
		if(schoolCid==null)
			throw new ValidationException("school id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);
		if(school == null)
			throw new ValidationException(String.format("No school found with id : %s.", schoolCid));
		
		List<AwardActivityPerformed> awardActivityPerformedList = awardActivityPerformedRepository.findAllByAwardTeacherSchoolCidAndIsVerifiedFalseAndAwardTeacherSchoolActiveTrueAndActiveTrue(schoolCid);
		
		if(awardActivityPerformedList==null || awardActivityPerformedList.isEmpty())
			throw new ValidationException(String.format("No unverified awards found for the school id : %s", schoolCid));
		
		List<AwardResponse> awardResponseList = new ArrayList<AwardResponse>();
		awardActivityPerformedList.forEach(awrdAct->{AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
		awardResponse.setIsVerified(awrdAct.getIsVerified());
		awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
		awardResponseList.add(awardResponse);
		});
		
		return awardResponseList;
	}
	
	@Override
	public List<AwardResponse> verifyAwards(AwardRequest request){
		if(request.getAwardActivityPerformedList()==null || request.getAwardActivityPerformedList().isEmpty())
			throw new ValidationException(String.format("AwardActivityPerformedList cannot be empty."));
		List<AwardActivityPerformedCid> awardActivityPerformedIdRequestList = request.getAwardActivityPerformedList();
		List<AwardActivityPerformed> awardActivityPerformedRepoList = awardActivityPerformedRepository.findAllByActiveTrue();
		List<AwardActivityPerformedCid> awardActivityPerformedCidRepoList = new ArrayList<AwardActivityPerformedCid>();
		
		for(AwardActivityPerformed awardAct : awardActivityPerformedRepoList) {
			awardActivityPerformedCidRepoList.add(new AwardActivityPerformedCid(awardAct.getAward().getCid(),awardAct.getActivityPerformed().getCid()));
		}
		
		/*Filtering out valid and invalid (award,activity) pairs in awardActivityPerformedCidRepoList and awardActivityPerformedIdRequestList respectively. */
		for(int i=0 ;i<awardActivityPerformedCidRepoList.size();i++) {
			if(!awardActivityPerformedIdRequestList.contains(awardActivityPerformedCidRepoList.get(i)))
				awardActivityPerformedCidRepoList.remove(i--);
			else
				awardActivityPerformedIdRequestList.remove(awardActivityPerformedCidRepoList.get(i));
		}
		
		/*Throwing validation exception for invalid (award,activity) pair.*/
		if(awardActivityPerformedIdRequestList!=null && !awardActivityPerformedIdRequestList.isEmpty())
			for(AwardActivityPerformedCid awrdAct : awardActivityPerformedIdRequestList)
			{
				throw new ValidationException(String.format("No award with id : %s awaded to activity having id : %s.", awrdAct.getAwardId(),awrdAct.getActivityPerformedId()));
			}
		
		/*Retaining all those entries in awardActivityPerformedRepoList which needs to be set true based on all entries left in awardActivityPerformedCidRepoList.*/
		for(int i =0; i< awardActivityPerformedRepoList.size();i++) {
			Boolean valid = false;
			for(int j=0;j<awardActivityPerformedCidRepoList.size();j++) {
				if(awardActivityPerformedRepoList.get(i).getAward().getCid().equals(awardActivityPerformedCidRepoList.get(j).getAwardId()) && awardActivityPerformedRepoList.get(i).getActivityPerformed().getCid().equals(awardActivityPerformedCidRepoList.get(j).getActivityPerformedId()))
					valid=true;
			}
			if(!valid) {
				awardActivityPerformedRepoList.remove(i--);
			}else {
				/*Validating award allocation and setting the dateOfReceipt to be current instant of time.*/
				awardActivityPerformedRepoList.get(i).setIsVerified(true);
				awardActivityPerformedRepoList.get(i).setDateOfReceipt(LocalDate.now().toDate());
			}
		}
		
		awardActivityPerformedRepoList = awardActivityPerformedRepository.save(awardActivityPerformedRepoList);
		
		if(awardActivityPerformedRepoList==null || awardActivityPerformedRepoList.isEmpty())
			throw new RuntimeException("Something went wrong awards not verfied successfully.");
		
		List<AwardResponse> awardResponseList = new ArrayList<AwardResponse>();
		awardActivityPerformedRepoList.forEach(awrdAct->{AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
		awardResponse.setDateOfReceipt(awrdAct.getDateOfReceipt());
		awardResponse.setIsVerified(awrdAct.getIsVerified());
		awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
		awardResponseList.add(awardResponse);
		});
		
		return awardResponseList;
	}
	
	@Override
	public List<AwardResponse> filterAwardByYearPerformed(String year, String studentCid) {
		if (year == null)
			throw new ValidationException("year cannot be null.");
		if(!(new Integer(Integer.parseInt(year)) instanceof Integer))
			throw new ValidationException("Year is in invalid format.");
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		Student student = studentRepository.findByCidAndActiveTrue(studentCid);
		if (student == null)
			throw new ValidationException("No student found with id : " + studentCid);
		List<AwardActivityPerformed> awardActivityperformedList = awardActivityPerformedRepository.findAllByYearOfActivity(year);
		if (awardActivityperformedList == null || awardActivityperformedList.isEmpty())
			throw new ValidationException(String.format("No award allocated to any student in year : %s", year));
		
		for(AwardActivityPerformed awrdAct : awardActivityperformedList) {
			if(!awrdAct.getActivityPerformed().getStudent().getCid().equals(studentCid))
				awardActivityperformedList.remove(awrdAct);
		}
		
		if (awardActivityperformedList == null || awardActivityperformedList.isEmpty())
			throw new ValidationException(String.format("No award allocated to student having id : %s  performed in year : %s", studentCid,year));
		
		List<AwardResponse> awardResponses = new ArrayList<AwardResponse>();
		awardActivityperformedList.forEach(awrdAct->{AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
		awardResponse.setDateOfReceipt(awrdAct.getDateOfReceipt());
		awardResponse.setIsVerified(awrdAct.getIsVerified());
		awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
		awardResponses.add(awardResponse);
		});
		return awardResponses;
	}
	
	
}
