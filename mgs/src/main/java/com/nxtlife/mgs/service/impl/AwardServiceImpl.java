package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.entity.school.AwardType;
import com.nxtlife.mgs.entity.school.School;
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
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherActivityGradeRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.AwardService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.AwardRequest;
import com.nxtlife.mgs.view.AwardResponse;
import com.nxtlife.mgs.view.PropertyCount;
import com.nxtlife.mgs.view.SuccessResponse;

@Service("awardServiceImpl")
@Transactional
public class AwardServiceImpl extends BaseService implements AwardService {

	@Autowired
	private AwardRepository awardRepository;

	@Autowired
	private ActivityPerformedRepository activityPerformedRepository;

	@Autowired
	private AwardActivityPerformedRepository awardActivityPerformedRepository;

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private FocusAreaRepository focusAreaRepository;

	@Autowired
	private AwardTypeRepository awardTypeRepository;

	@Autowired
	TeacherActivityGradeRepository teacherActivityGradeRepository;

	@PostConstruct
	public void init() {
		String[] awardTypes = { "Ist Position", "IInd Position", "IIIrd Position", "Participation Prize" };

		Set<AwardType> repoAwardTypes = awardTypeRepository.findAll().stream().collect(Collectors.toSet());
		for (String type : awardTypes) {
			AwardType awardType = repoAwardTypes.stream().filter(at -> at.getName().equalsIgnoreCase(type)).findFirst()
					.orElse(null);
			if (awardType == null) {
				repoAwardTypes.add(new AwardType(type));
			} else {
				awardType.setActive(true);
				repoAwardTypes.add(awardType);
			}
		}

		awardTypeRepository.saveAll(repoAwardTypes);
	}

	@Override
	public Set<String> getAwardCriterias() {
		Set<String> awardCriterias = new HashSet<String>();
		Arrays.asList(AwardCriterion.values()).stream().distinct().forEach(ac -> {
			awardCriterias.add(ac.getAwardCriterion());
		});
		return awardCriterias;
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_ASSIGN)
	public AwardResponse createAward(AwardRequest request) {
		if (request == null)
			throw new ValidationException("Request cannot be null.");
		// if (request.getTeacherId() == null)
		// throw new ValidationException("Teacher id cannot be null.");
		// Teacher teacher =
		// teacherRepository.findByCidAndActiveTrue(request.getTeacherId());
		// if (teacher == null)
		// throw new ValidationException(String.format("Teacher (%s) does not exist.",
		// request.getTeacherId()));
		if (request.getAwardType() == null)
			throw new ValidationException("Award Type cannot be null.");
		if (request.getStudentId() == null
				&& (request.getActivityPerformedIds() == null || request.getActivityPerformedIds().isEmpty()))
			throw new ValidationException("Student and ActivityPerformedIds both cannot be empty/null simultaneously.");

		AwardType awardType = awardTypeRepository.getByNameAndActiveTrue(request.getAwardType());
		if (awardType == null)
			throw new ValidationException(String.format("AwardType (%s) not found.", request.getAwardType()));

		Award award = request.toEntity();

		if (request.getStudentId() != null) {
			Long studentId = studentRepository.findIdByCidAndActiveTrue(request.getStudentId());
			if (studentId == null) {
				throw new ValidationException(String.format("Student (%s) doesn't exist.", request.getStudentId()));
			}
			Student student = new Student();
			student.setId(studentId);
			award.setStudent(student);
		}

		if (request.getActivityId() != null) {
			Activity activity = activityRepository.findByCidAndActiveTrue(request.getActivityId());
			if (activity == null) {
				throw new ValidationException(String.format("Activity (%s) doesn't exist", request.getActivityId()));
			}
			award.setActivity(activity);
		}

		award.setAwardType(awardType);
		award.setActive(true);
		award.setCid(Utils.generateRandomAlphaNumString(8));
		Teacher teacher = new Teacher();
		teacher.setId(teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
		award.setTeacher(teacher);
		award.setStatus(ApprovalStatus.PENDING);
		award.setActive(true);
		if (request.getGradeId() != null) {
			if (!gradeRepository.existsByCidAndActiveTrue(request.getGradeId()))
				throw new ValidationException(
						String.format("Grade with id (%s) does not exist.", request.getGradeId()));

			award.setGrade(gradeRepository.getOneByCid(request.getGradeId()));
		}

		if (request.getAwardCriterion() != null) {
			AwardCriterion criterion = AwardCriterion.fromString(request.getAwardCriterion());
			if (!AwardCriterion.matches(request.getAwardCriterion()))
				throw new ValidationException(String.format(
						"Invalid value (%s) for awardCriterion it should be of form : [PSD Area ,Focus Area , 4S ,Activity Type]",
						request.getAwardCriterion()));
			award.setAwardCriterion(criterion);
			if (request.getCriterionValue() == null)
				throw new ValidationException("Please provide criterion value.");
			if (criterion.equals(AwardCriterion.PSDArea)) {
				if (!PSDArea.matches(request.getCriterionValue()))
					throw new ValidationException(String.format(
							"PSD Area (%s) does not matches with available PSD Areas i.e [Personal Development, Social Development]",
							request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			} else if (criterion.equals(AwardCriterion.FocusArea)) {
				if (!focusAreaRepository.existsByNameAndActiveTrue(request.getCriterionValue()))
					throw new ValidationException(
							String.format("Focus Area  (%s) does not exist.", request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			} else if (criterion.equals(AwardCriterion.FourS)) {
				if (!FourS.matches(request.getCriterionValue()))
					throw new ValidationException(String.format(
							"FourS  (%s) does not matches with available values for fourS i.e [Skill , Sport ,Study ,Service]",
							request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			} else if (criterion.equals(AwardCriterion.ActivityType)) {
				if (!activityRepository.existsByNameAndActiveTrue(request.getCriterionValue()))
					throw new ValidationException(
							String.format("Activity with name  (%s) does not exist.", request.getCriterionValue()));
				award.setCriterionValue(request.getCriterionValue());
			} else {
				throw new ValidationException(String.format(
						"Invalid value (%s) for awardCriterion it should be of form : [PSD Area ,Focus Area , 4S ,Activity Type]",
						request.getAwardCriterion()));
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
			award.setAwardActivityPerformed(awardActivityPerformedRepository.saveAll(awardActivityPerformedList));
		}

		return new AwardResponse(award);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_FETCH)
	public List<AwardResponse> findAllByStudent(String studentCid) {
		if (!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Student"))) {
			if (studentCid == null || !studentRepository.existsByCidAndActiveTrue(studentCid)) {
				throw new ValidationException("studentId cannot be null or invalid.");
			}
		} else {
			studentCid = studentRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (studentCid == null)
				throw new ValidationException("Student not found probably userId is not set for student.");
		}
		List<Award> awards = awardRepository.findByStudentCidAndStatus(studentCid, ApprovalStatus.VERIFIED);
		if (awards == null || awards.isEmpty())
			throw new NotFoundException(String.format("No Award given to student (%s) yet.", studentCid));

		return awards.stream().map(AwardResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_FETCH)
	public List<AwardResponse> findAllByStudent(AwardFilter awardFilter, String studentCid) {
		if (!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Student"))) {
			if (studentCid == null || !studentRepository.existsByCidAndActiveTrue(studentCid)) {
				throw new ValidationException("studentId cannot be null or invalid.");
			}
		} else {
			studentCid = studentRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (studentCid == null)
				throw new ValidationException("Student not found probably userId is not set for student.");
		}

		List<Award> awards = awardRepository
				.findAll(new AwardFilterBuilder().build(awardFilter, studentCid, ApprovalStatus.VERIFIED));
		if (awards == null || awards.isEmpty())
			throw new NotFoundException(
					String.format("No Award found for student (%s) after applying filters.", studentCid));

		return awards.stream().map(AwardResponse::new).distinct().collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_FETCH)
	public List<AwardResponse> findAllByManagement(String schoolCid, String teacherCid) {
		List<Award> awards;
		Long teacherId = null;
		List<String> gradeIds = null;
		if (getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor"))) {
			awards = awardRepository
					.findByTeacherCidAndActiveTrue(teacherRepository.findCidByUserIdAndActiveTrue(getUserId()));
			// awards =
			// awardRepository.findByActivityInOrActivityNullAndTeacherCid(teacherActivityGradeRepository.findAllActivityByTeacherCidAndActiveTrue(teacherCid)
			// ,teacherCid);
		} else {
			if (teacherCid == null)
				throw new ValidationException("TeacherId cannot be null.");
			teacherId = teacherRepository.findIdByCidAndActiveTrue(teacherCid);

			if (teacherId == null)
				throw new ValidationException("Teacher id is null or user id not set for teacher");
			if (!teacherActivityGradeRepository.existsByTeacherIdAndActiveTrue(teacherId))
				throw new ValidationException("Teacher not running any clubs or societies.");

			if (!getUser().getRoles().stream()
					.anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))) {
				schoolCid = getUser().getSchool().getCid();
				if (schoolCid == null)
					throw new ValidationException("School id not assigned to user logged in.");

				if (getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("SchoolAdmin"))) {

					if (!teacherRepository.existsByUserIdAndActiveTrue(getUserId()))
						gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue(schoolCid);
					else
						gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue(schoolCid,
								teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
				} else {
					if (!teacherRepository.existsByUserIdAndActiveTrue(getUserId())) {
						throw new ValidationException("Not Authorized to see details.");
					} else {
						gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue(schoolCid,
								teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
					}
				}

			} else {

				if (schoolCid == null)
					throw new ValidationException("School id cannot be null.");
				gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue(schoolCid);
			}

			awards = awardRepository.findByTeacherCidAndStudentGradeCidInAndActiveTrue(teacherCid, gradeIds);
		}

		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_FETCH)
	public List<AwardResponse> findAllByManagement(AwardFilter awardFilter, String teacherCid) {
		if (getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor")))
			teacherCid = teacherRepository.findCidByUserIdAndActiveTrue(getUserId());
		if (teacherCid == null) {
			throw new ValidationException("Teacher id cannot be null.");
		}

		List<Award> awards = awardRepository.findAll(new AwardFilterBuilder().build(awardFilter,
				teacherActivityGradeRepository.findAllActivityByTeacherCidAndActiveTrue(teacherCid)));

		return awards.stream().map(AwardResponse::new).collect(Collectors.toList());
	}

	// change this method to be used by coordinator not for supervisor
	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_REVIEW)
	public AwardResponse updateStatus(String awardId, Boolean isVerified) {

		Award award = awardRepository.findByCidAndActiveTrue(awardId);
		if (award == null) {
			throw new ValidationException(String.format("This award not exist", awardId));
		}
		Boolean headOrAdmin = getUser().getRoles().stream()
				.anyMatch(r -> r.getName().equalsIgnoreCase("Head") || r.getName().equalsIgnoreCase("SchoolAdmin"));
		if (award.getStatus().equals(ApprovalStatus.PENDING)) {
			if (headOrAdmin)
				award.setStatus(isVerified ? ApprovalStatus.VERIFIED : ApprovalStatus.REJECTED);
			else
				award.setStatus(isVerified ? ApprovalStatus.FORWARDED : ApprovalStatus.REJECTED);
			if (award.getStatus().equals(ApprovalStatus.VERIFIED)) {
				award.setDateOfReceipt(new Date());
			}

			award.setStatusModifiedBy(getUser());
			award.setStatusModifiedAt(new Date());
		} else if (award.getStatus().equals(ApprovalStatus.FORWARDED)) {
			if (!headOrAdmin)
				throw new ValidationException("You have already sent this award for review by Head.");
			award.setStatus(isVerified ? ApprovalStatus.VERIFIED : ApprovalStatus.REJECTED);
			award.setDateOfReceipt(award.getStatus().equals(ApprovalStatus.VERIFIED) ? new Date() : null);
			award.setStatusModifiedBy(getUser());
			award.setStatusModifiedAt(new Date());
		} else {
			throw new ValidationException("This award already rejected or verified");
		}
		awardRepository.save(award);
		return new AwardResponse(award);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_FETCH)
	public List<PropertyCount> getCount(String studentCid, String status, String type) {
		if (!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Student"))) {
			if (studentCid == null || !studentRepository.existsByCidAndActiveTrue(studentCid)) {
				throw new ValidationException("studentId cannot be null or invalid.");
			}
		} else {
			studentCid = studentRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (studentCid == null)
				throw new ValidationException("Student not found probably userId is not set for student.");
		}

		if (!ActivityStatus.matches(status))
			throw new ValidationException(String.format("The status (%s) provided by you is invalid.", status));

		if (type.equalsIgnoreCase("fourS"))
			return awardRepository.findFourSCount(studentCid, ActivityStatus.valueOf(status), ApprovalStatus.VERIFIED);
		else if (type.equalsIgnoreCase("focusArea"))
			return awardRepository.findFocusAreaCount(studentCid, ActivityStatus.valueOf(status),
					ApprovalStatus.VERIFIED);
		else if (type.equalsIgnoreCase("psdArea"))
			return awardRepository.findPsdAreaCount(studentCid, ActivityStatus.valueOf(status),
					ApprovalStatus.VERIFIED);
		else
			throw new ValidationException(String.format(
					"invalid type : (%s) , type can have following values [psdArea , focusArea , fourS]", type));
	}

	@Override
	public Set<String> getAllAwardTypes() {
		List<String> awardTypeNames = awardTypeRepository.findAllNameBySchoolIdAndActiveTrue(getUser().gettSchoolId());
		if (awardTypeNames == null || awardTypeNames.isEmpty())
			throw new NotFoundException("No award types found in school.");
		return awardTypeNames.stream().collect(Collectors.toSet());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_CREATE)
	public SuccessResponse createAwardType(String name) {
		if (name == null)
			throw new ValidationException("Name cannot be null");
		Long schoolId = getUser().gettSchoolId();
		if (awardTypeRepository.existsByNameAndSchoolId(name, schoolId))
			throw new ValidationException(
					String.format("AwardType (%s) already exists in school having id (%d)", name, schoolId));
		AwardType awardType = new AwardType();
		awardType.setName(name);
		School school = new School();
		school.setId(schoolId);
		awardType.setSchool(school);
		awardType = awardTypeRepository.save(awardType);

		return new SuccessResponse(200,
				String.format("AwardType (%s) successfully created in school having id (%d)", name, schoolId));
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_AWARD_DELETE)
	public SuccessResponse deleteAwardType(String name) {
		if (name == null)
			throw new ValidationException("Name cannot be null");
		Long schoolId = getUser().gettSchoolId();
		if (!awardTypeRepository.existsByNameAndSchoolId(name, schoolId))
			throw new ValidationException(
					String.format("AwardType (%s) does not exists in school having id (%d)", name, schoolId));
		if (awardTypeRepository.deleteByNameAndSchoolId(name, schoolId) < 1)
			throw new RuntimeException(
					String.format("Something went wrong AwardType (%s) not deleted from school.", name));

		return new SuccessResponse(200,
				String.format("AwardType (%s) successfully deleted from school having id (%d)", name, schoolId));
	}

	@Override
	public Collection<AwardResponse> getAllAwardsForGradesUnderMe(String schoolCid) {

		List<String> gradeIds = null;
		if (!getUser().getRoles().stream()
				.anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin"))) {
			schoolCid = getUser().getSchool().getCid();
			if (schoolCid == null)
				throw new ValidationException("School id not assigned to user logged in.");

			if (getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("SchoolAdmin"))) {

				if (!teacherRepository.existsByUserIdAndActiveTrue(getUserId()))
					gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue(schoolCid);
				else
					gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue(schoolCid,
							teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
			} else {
				if (!teacherRepository.existsByUserIdAndActiveTrue(getUserId())) {
					// if(!studentRepository.existsByUserIdAndActiveTrue(getUserId()))
					throw new ValidationException("Not Authorized to see details.");
					// gradeIds =
					// Arrays.asList(studentRepository.findGradeCidByCidAndActiveTrue(studentRepository.findCidByUserIdAndActiveTrue(getUserId())));
				} else {
					gradeIds = gradeRepository.findAllCidBySchoolsCidAndTeacherIdActiveTrue(schoolCid,
							teacherRepository.getIdByUserIdAndActiveTrue(getUserId()));
				}
			}

		} else {
			if (schoolCid == null)
				throw new ValidationException("School id cannot be null.");
			gradeIds = gradeRepository.findAllCidBySchoolsCidAndActiveTrue(schoolCid);
		}

		Collection<Award> awards;
		if (getUser().getRoles().stream()
				.anyMatch(r -> r.getName().equalsIgnoreCase("SchoolAdmin") || r.getName().equalsIgnoreCase("Head")))
			awards = awardRepository.findAllByStudentSchoolCidAndStudentGradeCidInAndStatusInAndActiveTrue(schoolCid,
					gradeIds,
					Arrays.asList(ApprovalStatus.FORWARDED, ApprovalStatus.VERIFIED, ApprovalStatus.REJECTED));
		else
			awards = awardRepository.findAllByStudentSchoolCidAndStudentGradeCidInAndActiveTrue(schoolCid, gradeIds);
		if (awards == null || awards.isEmpty())
			throw new ValidationException("No awards found for grades assigned to you.");

		return awards.stream().map(AwardResponse::new).distinct().collect(Collectors.toSet());
	}

}
