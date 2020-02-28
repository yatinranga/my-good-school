package com.nxtlife.mgs.service.impl;

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
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.AwardActivityPerformedRepository;
import com.nxtlife.mgs.jpa.AwardRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.util.AwardActivityPerformedId;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;

@Service
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

	@Override
	public AwardResponse createAward(AwardRequest request) {
		if (request == null)
			throw new ValidationException("Request cannot be null.");
		if (request.getName() != null) {
			Award award = awardRepository.getOneByNameAndActiveTrue(request.getName());
			if (award != null)
				throw new ValidationException(String.format("Award with name : %s already exist.", request.getName()));
		}
		if (request.getTeacherId() == null)
			throw new ValidationException("Teacher id cannot be null.");
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(request.getTeacherId());
		if (teacher == null)
			throw new ValidationException(
					String.format("Teacher with id : %s does not exist.", request.getTeacherId()));
		Award award = request.toEntity();
		award.setActive(true);
		award.setCid(utils.generateRandomAlphaNumString(8));
		award.setTeacher(teacher);
		award = awardRepository.save(award);
		if (award == null)
			throw new RuntimeException("Something went wrong award not created.");

		return new AwardResponse(award);
	}

	@Override
	public List<AwardResponse> assignAward(AwardRequest request) {

		if (request == null)
			throw new ValidationException("Request cannot be null.");

		if (request.getId() == null)
			throw new ValidationException("Award id cannot be null.");
		Award award = awardRepository.getOneByCidAndActiveTrue(request.getId());

		if (award == null)
			throw new ValidationException("Award does not exist.");

		if (request.getActivityPerformedIds() == null || request.getActivityPerformedIds().isEmpty())
			throw new ValidationException("Please provide activity performed ids to assign award.");

		List<String> requestActivityPerformedIds = request.getActivityPerformedIds();

		if (request.getTeacherId() == null)
			throw new ValidationException("teacher id who is assigning the award cannot be null.");
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(request.getTeacherId());

		if (teacher == null)
			throw new ValidationException(String.format("Teacher with id : %s didn't exist", request.getTeacherId()));

		if (request.getSchoolId() == null)
			throw new ValidationException("school id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(request.getSchoolId());

		if (school == null)
			throw new ValidationException("No school found with id : " + request.getSchoolId());

		if (request.getGradeId() == null)
			throw new ValidationException("Grade id cannot be null.");
		Grade grade = gradeRepository.findByCidAndActiveTrue(request.getGradeId());

		if (grade == null)
			throw new ValidationException(String.format("No grade found with id : %s", request.getGradeId()));

		if (request.getActivityId() == null)
			throw new ValidationException("activity id cannot be null.");
		Activity activity = activityRepository.findByCidAndActiveTrue(request.getActivityId());

		if (activity == null)
			throw new ValidationException(
					String.format("Activity with id : %s didn't exist.", request.getActivityId()));

		List<ActivityPerformed> repoActivityPerformedList = activityPerformedRepository
				.findAllByStudentSchoolCidAndStudentGradeCidAndActivityCidAndAndActivityStatusAndStudentSchoolActiveTrueAndStudentGradeActiveTrueAndActivityActiveTrueAndActiveTrue(
						request.getSchoolId(), request.getGradeId(), request.getActivityId(), ActivityStatus.Reviewed);

		if (repoActivityPerformedList == null || repoActivityPerformedList.isEmpty()) {
			throw new ValidationException("No activities performed yet.");
		}

		// logic to retain valid ids in repoActivityPerformedList and invalid ids in
		// requestActivityPerformedIds
		for (int i = 0; i < repoActivityPerformedList.size(); i++) {

			if (!requestActivityPerformedIds.contains(repoActivityPerformedList.get(i).getCid()))
				repoActivityPerformedList.remove(i--);
			else
				requestActivityPerformedIds.remove(repoActivityPerformedList.get(i).getCid());
		}

		if (requestActivityPerformedIds != null && !requestActivityPerformedIds.isEmpty())
			for (String id : requestActivityPerformedIds) {
				throw new ValidationException(String.format("Activity Performed with id : %s not found.", id));
			}

		// Write logic here to check if award is already assigned to activities present
		// in repoActivityPerformedList

		List<AwardActivityPerformed> awardActivityPerformedList = awardActivityPerformedRepository
				.findAllByAwardCidAndIsVerifiedTrueAndActiveTrue(award.getCid());

		if (awardActivityPerformedList != null && !awardActivityPerformedList.isEmpty()
				&& repoActivityPerformedList != null && !repoActivityPerformedList.isEmpty()) {

			for (ActivityPerformed act : repoActivityPerformedList) {
				for (int i = 0; i < awardActivityPerformedList.size(); i++) {
					if (act.getCid().equals(awardActivityPerformedList.get(i).getActivityPerformed().getCid()))
						throw new ValidationException(String.format(
								"Award : %s already awarded to activity having id : %s on  %s", award.getName(),
								act.getCid(), awardActivityPerformedList.get(i).getDateOfReceipt()));
				}
			}
		}

		// directly save the entries because there are no awards allocated yet.
		List<AwardActivityPerformed> awardActivityPerformedToSaveList = new ArrayList<AwardActivityPerformed>();

		for (ActivityPerformed act : repoActivityPerformedList) {
			AwardActivityPerformed awrdActivity = new AwardActivityPerformed(
					new AwardActivityPerformedId(award.getId(), act.getId()), award, act);

			awardActivityPerformedToSaveList.add(awrdActivity);
		}

		awardActivityPerformedToSaveList = awardActivityPerformedRepository.save(awardActivityPerformedToSaveList);
		if (awardActivityPerformedToSaveList == null)
			throw new RuntimeException("Something went wrong , entries not saved to AwardActivityPerformed Table.");

		List<AwardResponse> awardResponseList = new ArrayList<AwardResponse>();
		awardActivityPerformedToSaveList.forEach(awrdAct -> {
			AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
			awardResponse.setIsVerified(awrdAct.getIsVerified());
			awardResponse.setDateOfReceipt(awrdAct.getDateOfReceipt());
			awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
			awardResponseList.add(awardResponse);
		});

		return awardResponseList;
	}

	@Override
	public List<AwardResponse> getAllAwardsBySchool(String schoolCid) {
		if (schoolCid == null)
			throw new ValidationException("school id cannot be null.");
		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);
		if (school == null)
			throw new ValidationException(String.format("No school found with id : %s.", schoolCid));
		List<Award> awards = awardRepository.findByTeacherSchoolCidAndActiveTrue(schoolCid);
		if (awards == null)
			throw new ValidationException(String.format("No awards found for the school id : %s", schoolCid));
		List<AwardResponse> awardResponses = new ArrayList<AwardResponse>();
		awards.forEach(awrd -> {
			awardResponses.add(new AwardResponse(awrd));
		});

		return awardResponses;
	}

	@Override
	public List<AwardResponse> getAllUnverifiedAwardsOfSchool(String schoolCid) {

		if (schoolCid == null)
			throw new ValidationException("school id cannot be null.");

		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);

		if (school == null)
			throw new ValidationException(String.format("No school found with id : %s.", schoolCid));

		List<AwardActivityPerformed> awardActivityPerformedList = awardActivityPerformedRepository
				.findAllByAwardTeacherSchoolCidAndIsVerifiedFalseAndAwardTeacherSchoolActiveTrueAndActiveTrue(
						schoolCid);

		if (awardActivityPerformedList == null || awardActivityPerformedList.isEmpty())
			throw new ValidationException(
					String.format("No unverified awards found for the school id : %s", schoolCid));

		List<AwardResponse> awardResponseList = new ArrayList<AwardResponse>();
		awardActivityPerformedList.forEach(awrdAct -> {
			AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
			awardResponse.setIsVerified(awrdAct.getIsVerified());
			awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
			awardResponseList.add(awardResponse);
		});

		return awardResponseList;
	}

	@Override
	public List<AwardResponse> getAllSoloUnverifiedAwardsOfSchool(String schoolCid, String awardCid) {

		if (schoolCid == null)
			throw new ValidationException("school id cannot be null.");

		School school = schoolRepository.findByCidAndActiveTrue(schoolCid);

		if (school == null)
			throw new ValidationException(String.format("No school found with id : %s.", schoolCid));

		if (awardCid == null) {
			throw new ValidationException("award id can't be null");
		}

		Award award = awardRepository.findByCidAndActiveTrue(awardCid);

		if (award == null) {
			throw new NotFoundException(String.format("Award having id [%s] didn't exist", awardCid));
		}

		List<AwardActivityPerformed> awardActivityPerformedList = awardActivityPerformedRepository
				.findAllByAwardCidAndAwardTeacherSchoolCidAndIsVerifiedFalseAndAwardActiveTrueAndAwardTeacherSchoolActiveTrueAndActiveTrue(
						awardCid, schoolCid);

		if (awardActivityPerformedList == null || awardActivityPerformedList.isEmpty())
			throw new ValidationException(String
					.format("No unverified awards having id %s found for the school id : %s", awardCid, schoolCid));

		List<AwardResponse> responseList = new ArrayList<AwardResponse>();

		awardActivityPerformedList.forEach(awrdAct -> {
			AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
			awardResponse.setIsVerified(awrdAct.getIsVerified());
			awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
			responseList.add(awardResponse);
		});

		return responseList;
	}

	@Override
	public List<AwardResponse> verifyAwards(AwardRequest request) {

		if (request.getId() == null) {
			throw new NotFoundException("award id can't be null/empty");
		}

		Award award = awardRepository.findByCidAndActiveTrue(request.getId());

		if (award == null) {
			throw new NotFoundException(String.format("award having id [%s] didn't exist", request.getId()));
		}

		if (request.getSchoolId() == null)
			throw new NotFoundException("school id can't be null/empty");

		School school = schoolRepository.findByCidAndActiveTrue(request.getSchoolId());

		if (school == null)
			throw new ValidationException(String.format("school havid id [%s] didn't exist", request.getSchoolId()));

		if (request.getActivityPerformedIds() == null || request.getActivityPerformedIds().isEmpty()) {

			throw new NotFoundException("activity performed list can't be empty");
		}

		// finding the list of activities from school id and activity id whose status is
		// not verified

		List<AwardActivityPerformed> awardActivityPerformedList = awardActivityPerformedRepository
				.findAllByAwardCidAndAwardTeacherSchoolCidAndIsVerifiedFalseAndAwardActiveTrueAndAwardTeacherSchoolActiveTrueAndActiveTrue(
						request.getId(), request.getSchoolId());

		List<AwardActivityPerformed> aapFinalList = new ArrayList<AwardActivityPerformed>();

		for (String apid /* apid = activityPerformedId */ : request.getActivityPerformedIds()) {

			boolean flag = false;

			// checking the ids which comes in request is present in
			// awardActivityPerformedList or not

			for (int i = 0; i < awardActivityPerformedList.size(); i++) {

				if (awardActivityPerformedList.get(i).getActivityPerformed().getCid().equals(apid)) {
					flag = true;
					aapFinalList.add(awardActivityPerformedList.get(i));
					awardActivityPerformedList.remove(i--);

					// Validating award allocation and setting the dateOfReceipt to be current
					// instant of time.

					if (aapFinalList.get(i).getIsVerified()) {
						throw new ValidationException(
								String.format("activity performed havig id [%s] is already verified", apid));
					} else {
						aapFinalList.get(i).setIsVerified(true);
						aapFinalList.get(i).setDateOfReceipt(LocalDate.now().toDate());
					}

				}

			}

			if (flag == false) {

				throw new NotFoundException(String.format(
						"activity performed havind id [%s] didn't exist in award activity performed list", apid));
			}

		}

		aapFinalList = awardActivityPerformedRepository.save(aapFinalList);

		if (aapFinalList == null || aapFinalList.isEmpty())
			throw new RuntimeException("Something went wrong awards not verfied successfully.");

		List<AwardResponse> awardResponseList = new ArrayList<AwardResponse>();
		aapFinalList.forEach(awrdAct -> {
			AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
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

		if (!(new Integer(Integer.parseInt(year)) instanceof Integer))
			throw new ValidationException("Year is in invalid format.");

		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");

		Student student = studentRepository.findByCidAndActiveTrue(studentCid);

		if (student == null)
			throw new ValidationException("No student found with id : " + studentCid);

		List<AwardActivityPerformed> awardActivityperformedList = awardActivityPerformedRepository
				.findAllByYearOfActivity(year);

		if (awardActivityperformedList == null || awardActivityperformedList.isEmpty())
			throw new ValidationException(String.format("No award allocated to any student in year : %s", year));

		for (AwardActivityPerformed awrdAct : awardActivityperformedList) {
			if (!awrdAct.getActivityPerformed().getStudent().getCid().equals(studentCid))
				awardActivityperformedList.remove(awrdAct);
		}

		if (awardActivityperformedList == null || awardActivityperformedList.isEmpty())
			throw new ValidationException(String
					.format("No award allocated to student having id : %s  performed in year : %s", studentCid, year));

		List<AwardResponse> awardResponses = new ArrayList<AwardResponse>();

		awardActivityperformedList.forEach(awrdAct -> {
			AwardResponse awardResponse = new AwardResponse(awrdAct.getAward());
			awardResponse.setDateOfReceipt(awrdAct.getDateOfReceipt());
			awardResponse.setIsVerified(awrdAct.getIsVerified());
			awardResponse.setActivityPerformedResponse(new ActivityPerformedResponse(awrdAct.getActivityPerformed()));
			awardResponses.add(awardResponse);
		});

		return awardResponses;
	}

}
