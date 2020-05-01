package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.filtering.filter.ActivityPerformedFilter;
import com.nxtlife.mgs.filtering.filter.ActivityPerformedFilterBuilder;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.FileRepository;
import com.nxtlife.mgs.jpa.FocusAreaRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.StudentClubRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;
import com.nxtlife.mgs.view.PropertyCount;
import com.nxtlife.mgs.view.SuccessResponse;

@Service
public class ActivityPerformedServiceImpl extends BaseService implements ActivityPerformedService {

	@Autowired
	FileRepository fileRepository;

	@Autowired
	FileService fileService;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	TeacherRepository teacherRepository;

	@Autowired
	Utils utils;

	@Autowired
	ActivityPerformedRepository activityPerformedRepository;

	@Autowired
	FocusAreaRepository focusAreaRepository;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	StudentClubRepository studentClubRepository;

	@Override
	public List<FileResponse> getAllFilesOfActivity(String activityCId) {
		List<File> files = fileRepository.findAllByActiveTrueAndActivityPerformedCidAndActiveTrue(activityCId);
		if (files == null || files.isEmpty())
			throw new NotFoundException("No files found for this activity.");

		return files.stream().map(FileResponse::new).collect(Collectors.toList());
	}

	@Override
	public File saveMediaForActivityPerformed(FileRequest fileRequest, String category,
			ActivityPerformed activityPerformed) {
		File file = fileService.saveMedia(fileRequest, category);
		if (fileRequest.getId() != null)
			file.setCid(fileRequest.getId());
		file.setActivityPerformed(activityPerformed);
		return file;
	}

	@Override
	public ActivityPerformedResponse saveActivity(ActivityPerformedRequest request) {

		if (request == null)
			throw new ValidationException("Request can not be null.");

		ActivityStatus actStatus = ActivityStatus.InProgressAtStudent;

		ActivityPerformed activityPerformed;

		if (request.getStudentId() == null)
			throw new ValidationException("Student Id can not be null.");

		Student student = studentRepository.findByCidAndActiveTrue(request.getStudentId());

		if (student == null)
			throw new ValidationException(
					String.format("Student id is invalid no student with id : %s found.", request.getStudentId()));
		if (request.getActivityId() == null)
			throw new ValidationException("Activity Id can not be null.");

		Activity activity = activityRepository.findByCidAndActiveTrue(request.getActivityId());
		if (activity == null)
			throw new ValidationException(
					String.format("Activity with id : %s does not exist.", request.getActivityId()));
		
		if(!studentClubRepository.existsByStudentIdAndActivityIdAndMembershipStatusAndActiveTrue(student.getId(), activity.getId(), ApprovalStatus.VERIFIED))
			throw new ValidationException(String.format("You are not a member of %s : %s please apply for membership first.", activity.getClubOrSociety().toString() , activity.getName()));

		if (request.getCoachId() == null)
			throw new ValidationException("Coach Id can not be null.");

		Teacher coach = teacherRepository.findByCidAndIsCoachTrueAndActiveTrue(request.getCoachId());

		if (coach == null)
			throw new ValidationException(
					String.format("Coach id is invalid no coach with id : %s found.", request.getCoachId()));

		/*
		 * setting those fields in request to null which needs to filled by Coach
		 */
		if (request.getCoachRemark() != null || request.getAchievementScore() != null
				|| request.getParticipationScore() != null || request.getInitiativeScore() != null) {
			throw new ValidationException("student can't set remarks or fields which are meant to be set by coach");

		}

		/*
		 * writing logic to update if activity is already saved and is not new
		 */

		if (request.getId() != null) {
			activityPerformed = activityPerformedRepository.findByCidAndActiveTrue(request.getId());
			if (activityPerformed == null)
				throw new ValidationException(String.format("Activity having id : %s not found", request.getId()));
			if (!activityPerformed.getActivityStatus().equals(ActivityStatus.SavedByStudent))
				throw new ValidationException(
						String.format("Activity with the id : %s is already submitted by you and cannot be edited.",
								request.getId()));
			if (request.getDateOfActivity() != null) {
				if (LocalDateTime.now().toDate().before(DateUtil.convertStringToDate(request.getDateOfActivity())))
					throw new ValidationException("Date of activity cannot be a future date.");
				activityPerformed.setDateOfActivity(DateUtil.convertStringToDate(request.getDateOfActivity()));
			}
			if (request.getDescription() != null)
				activityPerformed.setDescription(request.getDescription());

			
			/*
			 * write logic here to add new files if any and remove files which is not
			 * present now but were present earlier
			 */

			List<FileRequest> requestFiles = request.getFileRequests();
			
			if (requestFiles != null && !requestFiles.isEmpty()) {
				
				List<File> allValidFilesOfActivity = fileRepository
						.findAllByActiveTrueAndActivityPerformedCidAndActiveTrue(request.getId());
				List<File> updatedFiles = new ArrayList<File>();
				
				for (int itr = 0; itr < requestFiles.size(); itr++) {
					if (requestFiles.get(itr).getId() != null) {
						for (int itr2 = 0; itr2 < allValidFilesOfActivity.size(); itr2++) {
							if (requestFiles.get(itr).getId().equals(allValidFilesOfActivity.get(itr2).getCid())) {
								if (requestFiles.get(itr).getFile() != null) {
									File file = saveMediaForActivityPerformed(requestFiles.get(itr), "activity",
											activityPerformed);
									file.setId(allValidFilesOfActivity.get(itr2).getId());
									file.setCid(allValidFilesOfActivity.get(itr2).getCid());
									if (file != null)
										allValidFilesOfActivity.set(itr2, file);

								}
								updatedFiles.add(allValidFilesOfActivity.get(itr2));
								allValidFilesOfActivity.remove(itr2);
								itr2--;
								requestFiles.remove(itr);
								itr--;
								break;
							}
						}

					}
				}
				
				/*
				 * logic to delete the files which were previously there but in new request have
				 * been removed.
				 */
				for (File f : allValidFilesOfActivity) {
					fileRepository.updateFileSetActiveByCid(false, f.getCid());
				}

				// Logic to save new files and then add it to List updatedFiles
				if (requestFiles != null && !requestFiles.isEmpty())
					for (FileRequest fileReq : requestFiles) {
						File file = saveMediaForActivityPerformed(fileReq, "activity", activityPerformed);
						if (file != null) {
							file.setCid(utils.generateRandomAlphaNumString(8));
							updatedFiles.add(file);
						}
					}

				if(updatedFiles.size() > 5)
					throw new ValidationException("Cannot attach more than 5 files to upload.");
				// Setting files to activityPerformed
				activityPerformed.setFiles(updatedFiles);

			}
			
		} else {
			/* Converting request to entity */
			activityPerformed = request.toEntity();

			/* Saving files associated with activity */
			List<File> activityPerformedMedia = new ArrayList<>();
			if (request.getFileRequests() != null && !request.getFileRequests().isEmpty()) {
				if(request.getFileRequests().size() > 5)
					throw new ValidationException("Cannot attach more than 5 files to upload.");
			
				for (FileRequest fileReq : request.getFileRequests()) {
					File file = saveMediaForActivityPerformed(fileReq, "activity", activityPerformed);
					if (file != null) {
						file.setCid(utils.generateRandomAlphaNumString(8));
						activityPerformedMedia.add(file);
					}
				}
			/*
			 * Assigned the returned files List set to ActivityPerformed entity
			 */
			activityPerformed.setFiles(activityPerformedMedia);
			}

			activityPerformed.setActive(true);
			if (activityPerformed.getCid() == null)
				activityPerformed.setCid(utils.generateRandomAlphaNumString(8));
		}

		actStatus = ActivityStatus.SavedByStudent;
		activityPerformed.setActivityStatus(actStatus);
		activityPerformed.setStudent(student);
		activityPerformed.setTeacher(coach);
		activityPerformed.setActivity(activity);
		activityPerformed = activityPerformedRepository.save(activityPerformed);
		if (activityPerformed == null)
			throw new RuntimeException("Something went wrong activity not saved.");

		return new ActivityPerformedResponse(activityPerformed);
	}

	@Override
	public ActivityPerformedResponse submitActivity(String activityPerformedCid) {
		if (activityPerformedCid == null)
			throw new ValidationException("Id cannot be null.");
		ActivityPerformed activity = activityPerformedRepository.findByCidAndActiveTrue(activityPerformedCid);
		if (activity == null)
			throw new ValidationException(String.format("No activity found for id : %s", activityPerformedCid));

		if (!activity.getActivityStatus().equals(ActivityStatus.SavedByStudent))
			throw new ValidationException(
					String.format("Activity with the id : %s is already submitted by you and cannot be edited.",
							activityPerformedCid));

		if (activity.getDateOfActivity() == null
				|| activity.getDescription() == null/*
													 * || activity.getFiles() == null || activity.getFiles().isEmpty()
													 */)
			throw new ValidationException("Activity cannot be submitted first fill all the mandatory fields.");

		activity.setActivityStatus(ActivityStatus.SubmittedByStudent);
		activity.setSubmittedOn(LocalDateTime.now().toDate());
		activity = activityPerformedRepository.save(activity);
		if (activity == null)
			throw new RuntimeException("Something went wrong activity not submitted.");

		return new ActivityPerformedResponse(activity);
	}

	@Override
	public ActivityPerformedResponse saveActivityByCoach(ActivityPerformedRequest request) {

		if (request.getId() == null)
			throw new ValidationException("Activity id cannot be null.");

		ActivityPerformed activity = activityPerformedRepository.findByCidAndActiveTrue(request.getId());

		if (activity == null)
			throw new ValidationException(String.format("No activity found with id : %s", request.getId()));

		if (!(activity.getActivityStatus().equals(ActivityStatus.SubmittedByStudent))
				&& !(activity.getActivityStatus().equals(ActivityStatus.SavedByTeacher)))
			throw new ValidationException("Activity cannot be reviewed yet.");

		activity = request.toEntity(activity);

		if (activity.getCoachRemark() != null)
			activity.setCoachRemarkDate(LocalDateTime.now().toDate());
		// DateUtil.convertStringToDate(LocalDate.now().toString())

		activity.setActivityStatus(ActivityStatus.SavedByTeacher);

		activity = activityPerformedRepository.save(activity);

		if (activity == null)
			throw new RuntimeException("Something went wrong activity not submitted.");

		return new ActivityPerformedResponse(activity);
	}

	@Override
	public ActivityPerformedResponse submitActivityByCoach(String activityPerformedCid) {

		if (activityPerformedCid == null)
			throw new ValidationException("Id cannot be null.");

		ActivityPerformed activity = activityPerformedRepository.findByCidAndActiveTrue(activityPerformedCid);

		if (activity == null)
			throw new ValidationException(String.format("No activity found for id : %s", activityPerformedCid));

		if (!(activity.getActivityStatus().equals(ActivityStatus.SavedByTeacher)))
			throw new ValidationException(String.format(
					"Activity with the id : %s cannot be submitted first fill all mandatory fields and submit activity.",
					activityPerformedCid));

		if (activity.getCoachRemark() == null || activity.getAchievementScore() == null
				|| activity.getInitiativeScore() == null || activity.getParticipationScore() == null)
			throw new ValidationException("Activity cannot be submitted first fill all the mandatory fields.");

		activity.setActivityStatus(ActivityStatus.Reviewed);
		activity.setReviewedOn(LocalDateTime.now().toDate());

		activity = activityPerformedRepository.save(activity);

		if (activity == null)
			throw new RuntimeException("Something went wrong activity not submitted.");

		return new ActivityPerformedResponse(activity);
	}

	@Override
	public List<ActivityPerformedResponse> getAllActivitiesOfStudentByStatus(String status, String studentCid,
			Integer page, Integer pageSize) {
		page = page == null ? 0 : page;
		pageSize = pageSize == null ? 10 : pageSize;
		if (status == null) {
			return getAllPerformedActivitiesOfStudent(studentCid, page, pageSize);
		} else {
			switch (status) {
			case "saved":
			case "SAVED":
				return getAllSavedActivitiesOfStudent(studentCid, page, pageSize);
			case "submitted":
			case "SUBMITTED":
				return getAllSubmittedActivityOfStudent(studentCid, page, pageSize);
			case "reviewed":
			case "REVIEWED":
				return getAllReviewedActivityOfStudent(studentCid, page, pageSize);
			default:
				throw new ValidationException(String
						.format("[%s] is not a valid status. Valid status are : SAVED, SUBMITTED, REVIEWED  ", status));
			}
		}
	}

	@Override
	public List<ActivityPerformedResponse> getAllActivitiesAssignedToCoachforReview(String coachCid, String status) {

		if (coachCid == null)
			throw new ValidationException("coach id cannot be null.");

		Teacher coach = teacherRepository.findByCidAndIsCoachTrueAndActiveTrue(coachCid);

		if (coach == null)
			throw new ValidationException(String.format("Coach with id : %s not found.", coachCid));

		List<ActivityPerformed> submittedActivities = null;
		if (status == null) {
			submittedActivities = activityPerformedRepository
					.findAllByTeacherCidAndActivityStatusOrActivityStatusOrActivityStatusAndActiveTrue(coachCid,
							ActivityStatus.SubmittedByStudent, ActivityStatus.SavedByTeacher, ActivityStatus.Reviewed);

			if (submittedActivities == null || submittedActivities.isEmpty())
				throw new ValidationException("No activities assignet to teacher yet.");
		} else {
			if (status.equalsIgnoreCase("pending")) {
				submittedActivities = activityPerformedRepository
						.findAllByTeacherCidAndActivityStatusOrActivityStatusAndActiveTrue(coachCid,
								ActivityStatus.SubmittedByStudent, ActivityStatus.SavedByTeacher);

				if (submittedActivities == null || submittedActivities.isEmpty())
					throw new ValidationException("No activities pending to review yet.");
			} else if (status.equalsIgnoreCase("reviewed")) {
				submittedActivities = activityPerformedRepository
						.findAllByTeacherCidAndActivityStatusAndActiveTrue(coachCid, ActivityStatus.Reviewed);

				if (submittedActivities == null || submittedActivities.isEmpty())
					throw new ValidationException("No activities reviewed yet.");
			}
		}

		return submittedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllPendingActivitiesByClass(String coachCid, String gradeCid) {
		if (coachCid == null)
			throw new ValidationException("Coach id cannot be null.");
		Teacher coach = teacherRepository.findByCidAndIsCoachTrueAndActiveTrue(coachCid);
		if (gradeCid == null)
			throw new ValidationException("Grade id cannot be null.");
		if (coach == null)
			throw new ValidationException(String.format("Coach with id : %s not found.", coachCid));
		Grade grade = gradeRepository.findByCidAndActiveTrue(gradeCid);
		if (grade == null)
			throw new ValidationException(String.format("Grade with id : %s not found.", gradeCid));
		List<ActivityPerformed> submittedActivities = activityPerformedRepository
				.findAllByTeacherCidAndStudentGradeCidAndActivityStatusOrActivityStatusAndActiveTrue(coachCid, gradeCid,
						ActivityStatus.SubmittedByStudent, ActivityStatus.SavedByTeacher);
		if (submittedActivities == null || submittedActivities.isEmpty())
			throw new ValidationException("No activities assigned to review yet.");

		return submittedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllPendingActivitiesByService(String coachCid, String activityCid) {
		if (coachCid == null)
			throw new ValidationException("Coach id cannot be null.");
		Teacher coach = teacherRepository.findByCidAndIsCoachTrueAndActiveTrue(coachCid);
		if (activityCid == null)
			throw new ValidationException("activity id cannot be null.");
		if (coach == null)
			throw new ValidationException(String.format("Coach with id : %s not found.", coachCid));
		Activity activity = activityRepository.findByCidAndActiveTrue(activityCid);
		if (activity == null)
			throw new ValidationException(String.format("activity with id : %s not found.", activityCid));
		List<ActivityPerformed> submittedActivities = activityPerformedRepository
				.findAllByTeacherCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(coachCid, activityCid,
						ActivityStatus.SubmittedByStudent, ActivityStatus.SavedByTeacher);
		if (submittedActivities == null || submittedActivities.isEmpty())
			throw new ValidationException("No activities assigned to review yet.");

		return submittedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllPendingActivitiesByClassAndService(String coachCid, String gradeCid,
			String activityCid) {
		if (coachCid == null)
			throw new ValidationException("Coach id cannot be null.");
		Teacher coach = teacherRepository.findByCidAndIsCoachTrueAndActiveTrue(coachCid);
		if (activityCid == null)
			throw new ValidationException("activity id cannot be null.");
		if (gradeCid == null)
			throw new ValidationException("Grade id cannot be null.");
		if (coach == null)
			throw new ValidationException(String.format("Coach with id : %s not found.", coachCid));
		Grade grade = gradeRepository.findByCidAndActiveTrue(gradeCid);
		if (grade == null)
			throw new ValidationException(String.format("Grade with id : %s not found.", gradeCid));
		Activity activity = activityRepository.findByCidAndActiveTrue(activityCid);
		if (activity == null)
			throw new ValidationException(String.format("activity with id : %s not found.", activityCid));
		List<ActivityPerformed> submittedActivities = activityPerformedRepository
				.findAllByTeacherCidAndStudentGradeCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(
						coachCid, gradeCid, activityCid, ActivityStatus.SubmittedByStudent,
						ActivityStatus.SavedByTeacher);
		if (submittedActivities == null || submittedActivities.isEmpty())
			throw new ValidationException("No activities assigned to review yet.");

		return submittedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(
			String studentCid, String fourS, String activityStatus) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (studentRepository.findByCidAndActiveTrue(studentCid) == null)
			throw new ValidationException("No student found with id : " + studentCid);
		List<ActivityPerformed> performedActivities;
		switch (activityStatus) {
		case "saved":
		case "SAVED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(studentCid, FourS.valueOf(fourS),
							ActivityStatus.SavedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities saved by student.");

			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "submitted":
		case "SUBMITTED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(studentCid, FourS.valueOf(fourS),
							ActivityStatus.SubmittedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities submitted by student.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "reviewed":
		case "REVIEWED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(studentCid, FourS.valueOf(fourS),
							ActivityStatus.Reviewed);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities reviewed yet.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		default:
			throw new ValidationException("Not a valid status.");
		}

	}

	@Override
	public List<ActivityPerformedResponse> findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(
			String studentCid, String focusAreaCid, String activityStatus) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (studentRepository.findByCidAndActiveTrue(studentCid) == null)
			throw new ValidationException("No student found with id : " + studentCid);
		if (focusAreaCid == null)
			throw new ValidationException("FocusArea id cannot be null.");
		if (focusAreaRepository.getOneByCidAndActiveTrue(focusAreaCid) == null)
			throw new ValidationException("No focus area with id : " + focusAreaCid + " found");
		List<ActivityPerformed> performedActivities;
		switch (activityStatus) {
		case "saved":
		case "SAVED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(studentCid, focusAreaCid,
							ActivityStatus.SavedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities saved by student.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "submitted":
		case "SUBMITTED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(studentCid, focusAreaCid,
							ActivityStatus.SubmittedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities submitted by student.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "reviewed":
		case "REVIEWED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(studentCid, focusAreaCid,
							ActivityStatus.Reviewed);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities reviewed yet.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		default:
			throw new ValidationException("Not a valid status.");
		}

	}

	@Override
	public List<ActivityPerformedResponse> findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(
			String studentCid, String psdArea, String activityStatus) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (studentRepository.findByCidAndActiveTrue(studentCid) == null)
			throw new ValidationException("No student found with id : " + studentCid);
		if (psdArea == null)
			throw new ValidationException("PSD Area cannot be null.");
		if (!PSDArea.matches(psdArea))
			throw new ValidationException("Not a valid PSD Area.");
		List<ActivityPerformed> performedActivities;
		switch (activityStatus) {
		case "saved":
		case "SAVED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(studentCid,
							PSDArea.valueOf(psdArea), ActivityStatus.SavedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities saved by student.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "submitted":
		case "SUBMITTED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(studentCid,
							PSDArea.valueOf(psdArea), ActivityStatus.SubmittedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities submitted by student.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "reviewed":
		case "REVIEWED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(studentCid,
							PSDArea.valueOf(psdArea), ActivityStatus.Reviewed);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities reviewed yet.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		default:
			throw new ValidationException("Not a valid status.");
		}
	}

	@Override
	public List<ActivityPerformedResponse> findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(
			String studentCid, String teacherCid, String activityStatus) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (studentRepository.findByCidAndActiveTrue(studentCid) == null)
			throw new ValidationException("No student found with id : " + studentCid);
		if (teacherCid == null)
			throw new ValidationException("Teacher Id cannot be null.");
		if (teacherRepository.findByCidAndActiveTrue(teacherCid) == null)
			throw new ValidationException("No teacher found with id : " + teacherCid);
		List<ActivityPerformed> performedActivities;
		switch (activityStatus) {
		case "saved":
		case "SAVED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(studentCid, teacherCid,
							ActivityStatus.SavedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities saved by student.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "submitted":
		case "SUBMITTED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(studentCid, teacherCid,
							ActivityStatus.SubmittedByStudent);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities submitted by student.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		case "reviewed":
		case "REVIEWED":
			performedActivities = activityPerformedRepository
					.findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(studentCid, teacherCid,
							ActivityStatus.Reviewed);
			if (performedActivities == null || performedActivities.isEmpty())
				throw new ValidationException("No activities reviewed yet.");
			return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
		default:
			throw new ValidationException("Not a valid status.");
		}

	}

	@Override
	public List<ActivityPerformedResponse> filterActivityByYearPerformed(String year, String studentCid) {
		if (year == null)
			throw new ValidationException("year cannot be null.");
		if (!(new Integer(Integer.parseInt(year)) instanceof Integer))
			throw new ValidationException("Year is in invalid format.");
		if (studentCid == null)
			throw new ValidationException("student id cannot be null.");
		Student student = studentRepository.findByCidAndActiveTrue(studentCid);
		if (student == null)
			throw new ValidationException("No student found with id : " + studentCid);
		List<ActivityPerformed> performedActivities = activityPerformedRepository.findAllByYearOfActivity(year,
				student.getId());
		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities performed in year : " + year);
		return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllSavedActivitiesOfStudent(String studentCid, Integer page,
			Integer pageSize) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (studentRepository.findByCidAndActiveTrue(studentCid) == null)
			throw new ValidationException("No student found with id : " + studentCid);

		List<ActivityPerformed> performedActivities = activityPerformedRepository
				.findAllByStudentCidAndActivityStatusAndActiveTrue(studentCid, ActivityStatus.SavedByStudent,
						(Pageable) new PageRequest(page, pageSize,
								new Sort(Direction.DESC, Arrays.asList("dateOfActivity"))));
		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities saved by student.");
		return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllPerformedActivitiesOfStudent(String studentCid, Integer page,
			Integer pageSize) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (studentRepository.findByCidAndActiveTrue(studentCid) == null)
			throw new ValidationException("No student found with id : " + studentCid);

		List<ActivityPerformed> performedActivities = activityPerformedRepository.findAllByStudentCidAndActiveTrue(
				studentCid,
				(Pageable) new PageRequest(page, pageSize, new Sort(Direction.DESC, Arrays.asList("dateOfActivity"))));
		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities performed by student.");

//		performedActivities.stream().forEach(act -> {
//			if (act.getActivityStatus()!=null && !act.getActivityStatus().equals(ActivityStatus.SavedByStudent)
//					&& !act.getActivityStatus().equals(ActivityStatus.SubmittedByStudent)
//					&& !act.getActivityStatus().equals(ActivityStatus.Reviewed))
//				performedActivities.remove(act);
//			else
//				performedActivityResponses.add(new ActivityPerformedResponse(act));
//		});
		performedActivities.removeIf(act -> 
			act.getActivityStatus()!=null && !act.getActivityStatus().equals(ActivityStatus.SavedByStudent)
					&& !act.getActivityStatus().equals(ActivityStatus.SubmittedByStudent)
					&& !act.getActivityStatus().equals(ActivityStatus.Reviewed));
		
		return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllSubmittedActivityOfStudent(String studentCid, Integer page,
			Integer pageSize) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		List<ActivityPerformed> performedActivities = activityPerformedRepository
				.findAllByStudentCidAndActivityStatusAndActiveTrue(studentCid, ActivityStatus.SubmittedByStudent,
						(Pageable) new PageRequest(page, pageSize,
								new Sort(Direction.DESC, Arrays.asList("dateOfActivity"))));
		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities submitted by student.");
		return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllReviewedActivityOfStudent(String studentCid, Integer page,
			Integer pageSize) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		List<ActivityPerformed> performedActivities = activityPerformedRepository
				.findAllByStudentCidAndActivityStatusAndActiveTrue(studentCid, ActivityStatus.Reviewed,
						(Pageable) new PageRequest(page, pageSize,
								new Sort(Direction.DESC, Arrays.asList("dateOfActivity"))));
		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities Reviewed yet.");
		return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<ActivityPerformedResponse> getAllActivityOfStudentByActivityId(String studentCid, String activityCid,
			String activityStatus) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");
		if (activityCid == null)
			throw new ValidationException("Activity id cannot be null.");
		Student student = studentRepository.findByCidAndActiveTrue(studentCid);
		if (student == null)
			throw new ValidationException(String.format("Student with id : %s not found.", studentCid));
		Activity activity = activityRepository.findByCidAndActiveTrue(activityCid);
		if (activity == null)
			throw new ValidationException(String.format("No activity found having id : %s.", activityCid));
		if (activityStatus == null)
			activityStatus = ActivityStatus.Reviewed.toString();

		List<ActivityPerformed> activitiesPerformed = activityPerformedRepository
				.findAllByStudentCidAndStudentActiveTrueAndActivityCidAndActivityActiveTrueAndActivityStatusAndActiveTrue(
						studentCid, activityCid, ActivityStatus.valueOf(activityStatus));

		if (activitiesPerformed == null || activitiesPerformed.isEmpty())
			throw new ValidationException(String.format("No activity reviewed for category : %s of Student : %s ",
					activity.getName(), student.getName()));

		return activitiesPerformed.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public SuccessResponse deleteActivityOfStudent(String activityPerformedCid) {
		if (activityPerformedCid == null)
			throw new ValidationException("Activity Performed id cannot be null.");
		ActivityPerformed activityPerformed = activityPerformedRepository.findByCidAndActiveTrue(activityPerformedCid);
		if (activityPerformed == null)
			throw new ValidationException(String.format("No activity found with id : %s .", activityPerformedCid));

		if (activityPerformed.getActivityStatus().equals(ActivityStatus.SubmittedByStudent)
				|| activityPerformed.getActivityStatus().equals(ActivityStatus.SavedByTeacher)
				|| activityPerformed.getActivityStatus().equals(ActivityStatus.Reviewed))
			throw new ValidationException("Activity already submitted by you for review now it cannot be deleted.");

		activityPerformed.setActive(false);
		activityPerformed = activityPerformedRepository.save(activityPerformed);
		if (activityPerformed == null)
			throw new RuntimeException("Something went wrong , Activity not deleted");
		return new SuccessResponse(200, "Activity deleted successfuly.");
	}

	@Override
	public List<ActivityPerformedResponse> filter(ActivityPerformedFilter filterRequest) {
		if (filterRequest.getStudentId() != null) {
			Boolean studentFlag = studentRepository.existsByCidAndActiveTrue(filterRequest.getStudentId());
			if (!studentFlag)
				throw new ValidationException(
						String.format("Student with id : %s does not exist.", filterRequest.getStudentId()));
		}

		if (filterRequest.getTeacherId() != null) {
			if (!teacherRepository.existsByCidAndActiveTrue(filterRequest.getTeacherId()))
				throw new ValidationException(
						String.format("Teacher with id : %s does not exist.", filterRequest.getTeacherId()));
		}

		List<ActivityPerformed> performedActivities = activityPerformedRepository
				.findAll(new ActivityPerformedFilterBuilder().build(filterRequest));

		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities found after applying filter.");
		return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	public List<PropertyCount> getCount(String studentCid, String status, String type) {
		if (studentCid == null)
			throw new ValidationException("Student id cannot be null.");

		if (!studentRepository.existsByCidAndActiveTrue(studentCid))
			throw new ValidationException(String.format("Student with id (%s) does not exist.", studentCid));

		if (!ActivityStatus.matches(status))
			throw new ValidationException(String.format(
					"The status (%s) provided by you is invalid it should be from [fourS ,psdArea ,focusArea] .",
					status));

		if (type.equalsIgnoreCase("fourS"))
			return activityPerformedRepository.findFourSCount(studentCid, ActivityStatus.valueOf(status));
		else if (type.equalsIgnoreCase("focusArea"))
			return activityPerformedRepository.findFocusAreaCount(studentCid, ActivityStatus.valueOf(status));
		else if (type.equalsIgnoreCase("psdArea"))
			return activityPerformedRepository.findPsdAreaCount(studentCid, ActivityStatus.valueOf(status));
		else
			throw new ValidationException(String.format(
					"invalid type : (%s) , type can have following values [psdArea , focusArea , fourS]", type));
	}

}
