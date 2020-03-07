package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.AwardStatus;
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
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;

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

	@Override
	public AwardResponse createAward(AwardRequest request) {
		if (request == null)
			throw new ValidationException("Request cannot be null.");
		if (request.getTeacherId() == null)
			throw new ValidationException("Teacher id cannot be null.");
		Teacher teacher = teacherRepository.findByCidAndActiveTrue(request.getTeacherId());
		if (teacher == null)
			throw new ValidationException(String.format("Teacher (%s) does not exist.", request.getTeacherId()));
		Student student = studentRepository.findByCidAndActiveTrue(request.getStudentId());
		if (student == null) {
			throw new ValidationException(String.format("Student (%s) doesn't exist.", request.getStudentId()));
		}
		Activity activity = activityRepository.findByCidAndActiveTrue(request.getActivityId());
		if (activity == null) {
			throw new ValidationException(String.format("Activity (%s) doesn't exist", request.getActivityId()));
		}
		Award award = request.toEntity();
		award.setActive(true);
		award.setCid(utils.generateRandomAlphaNumString(8));
		award.setTeacher(teacher);
		award.setStatus(AwardStatus.PENDING);
		award.setStudent(student);
		award.setActivity(activity);
		award = awardRepository.save(award);
		List<AwardActivityPerformed> awardActivityPerformedList = new ArrayList<>();
		ActivityPerformed activityPerformed;
		if (request.getActivityPerformedIds() != null && !request.getActivityPerformedIds().isEmpty()) {
			for (String activityPerformedId : request.getActivityPerformedIds()) {
				activityPerformed = activityPerformedRepository.findByCidAndActiveTrue(activityPerformedId);
				if (activityPerformed == null) {
					throw new ValidationException(
							String.format("This performed activity (%s) not found", activityPerformedId));
				}
				awardActivityPerformedList.add(new AwardActivityPerformed(award.getId(), activityPerformed.getId()));
			}
		}
		award.setAwardActivityPerformed(awardActivityPerformedRepository.save(awardActivityPerformedList));
		return new AwardResponse(award);
	}

	@Override
	public List<AwardResponse> findAllByStudent() {
		Long userId = getUserId();
		Student student = studentRepository.getByUserId(userId);
		if (student == null) {
			throw new ValidationException("User not login as a student");
		}
		List<Award> awards = awardRepository.findByStudentIdAndStatus(student.getId(), AwardStatus.VERIFIED);
		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<AwardResponse> findAllByManagement(){
		Long userId = getUserId();
		Teacher teacher = teacherRepository.getByUserId(userId);
		if(teacher==null){
			throw new ValidationException("User not login as a management");
		}
		if(teacher.getActivities()==null || teacher.getActivities().isEmpty()){
			throw new ValidationException("Management not assigned with any activity");
		}
		List<Award> awards = awardRepository.findByActivity(teacher.getActivities());
		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
	}
}
