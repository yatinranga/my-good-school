package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.session.Event;
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
import com.nxtlife.mgs.service.FileStorageService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;
import com.nxtlife.mgs.view.GroupResponseBy;
import com.nxtlife.mgs.view.PropertyCount;
import com.nxtlife.mgs.view.SuccessResponse;

@Service
public class ActivityPerformedServiceImpl extends BaseService implements ActivityPerformedService {

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private FileStorageService<MultipartFile> fileStorageService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ActivityPerformedRepository activityPerformedRepository;

	@Autowired
	private FocusAreaRepository focusAreaRepository;

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private StudentClubRepository studentClubRepository;

	@Override
	public List<FileResponse> getAllFilesOfActivity(String activityCId) {
		List<File> files = fileRepository.findAllByActivityPerformedCidAndActiveTrue(activityCId);
		if (files == null || files.isEmpty())
			throw new NotFoundException("No files found for this activity.");

		return files.stream().map(FileResponse::new).collect(Collectors.toList());
	}

	@Override
	public File saveMediaForActivityPerformed(FileRequest fileRequest, String category,
			ActivityPerformed activityPerformed) {
		// File file = fileService.saveMedia(fileRequest, category);
		File file = fileRequest.toEntity();
		String fileUrl = fileStorageService.storeFile(fileRequest.getFile(),
				fileRequest.getFile().getOriginalFilename(), category, true, fileRequest.getIsImage());
		file.setUrl(fileUrl);
		if (fileRequest.getId() != null)
			file.setCid(fileRequest.getId());
		file.setActivityPerformed(activityPerformed);
		return file;
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_CREATE)
	public ActivityPerformedResponse saveActivity(ActivityPerformedRequest request) {

		if (request == null)
			throw new ValidationException("Request can not be null.");

		ActivityPerformed activityPerformed;

		Long studentId = studentRepository.findIdByUserIdAndActiveTrue(getUserId());

		if (studentId == null)
			throw new ValidationException(
					String.format("User not logged in as student."));
					

		/*
		 * setting those fields in request to null which needs to filled by
		 * Coach
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
			
			if(!activityPerformed.getStudent().getId().equals(studentId))
				throw new ValidationException("You cannot edit anyone else's activity.");
			
			if (!activityPerformed.getActivityStatus().equals(ActivityStatus.SavedByStudent))
				throw new ValidationException(
						String.format("Activity with the id : %s is already submitted by you and cannot be edited.",
								request.getId()));
			if (request.getDateOfActivity() != null) {
				if (LocalDateTime.now(DateTimeZone.forTimeZone(DateUtil.defaultTimeZone)).toDate().before(DateUtil.convertStringToDate(request.getDateOfActivity())))
					throw new ValidationException("Date of activity cannot be a future date.");
				activityPerformed.setDateOfActivity(DateUtil.convertStringToDate(request.getDateOfActivity()));
			}
			if (request.getDescription() != null)
				activityPerformed.setDescription(request.getDescription());

			/*
			 * write logic here to add new files if any and remove files which
			 * is not present now but were present earlier
			 */

			List<FileRequest> requestFiles = request.getFileRequests();
			
//			if (requestFiles != null && !requestFiles.isEmpty()) {
//				List<String> repoFiles = fileRepository.findAllCidByActivityPerformedCidAndActiveTrue(request.getId());
//				List<String> repoCopy = new ArrayList<>(repoFiles); // copy of repofile ids.
//				List<String> requestFileIds = requestFiles.stream().filter(f -> f.getId() != null).map(f -> f.getId()).distinct().collect(Collectors.toList());
//				repoCopy.removeAll(requestFileIds); // repoCopy now has fileIds that needs to be deleted.
//				if(repoFiles != null && requestFileIds != null ) {
//					 requestFileIds.removeAll(repoFiles); // if requestFileIds not empty then few ids are invalid
//					 if(!requestFileIds.isEmpty())
//						 throw new ValidationException(String.format("Some file ids are invalid (%s) .",requestFileIds));
//				}
//				
//				repoFiles.removeAll(repoCopy); //repoFiles now have files that remained after deleting files.
//				int fileLimit = repoFiles.size();
//				if(fileLimit > 5)
//					throw new ValidationException("Cannot attach more than 5 files to upload.");
//				
//				fileRepository.updateFileSetActiveByCidIn(repoCopy, false); // delete files from repo i.e, set their active false.
//				
//				if(activityPerformed.getFiles() != null)
//					activityPerformed.getFiles().removeIf(f -> repoCopy.contains(f.getCid()));
//				
//				List<File> newFilesToInsert = new ArrayList<>();
//				Long sessionId = activityPerformed.getId();
//				requestFiles.stream().filter(f -> f.getId() == null).forEach(f ->{ 
//					if(f.getFile() == null)
//						throw new ValidationException("Please provide file where id is null.");
//					
//					if(fileLimit + 1 > 5)
//						throw new ValidationException("Cannot attach more than 5 files to upload.");
//					ActivityPerformed finalSession = new ActivityPerformed();
//					finalSession.setId(sessionId);
//					File file = saveMediaForActivityPerformed(f, "/activity-file/", finalSession);
//					if (file != null) {
//						file.setCid(Utils.generateRandomAlphaNumString(8));
//						newFilesToInsert.add(file);
//					}
//				});
//
//				activityPerformed.getFiles().addAll(newFilesToInsert);
//			}

			if (requestFiles != null && !requestFiles.isEmpty()) {

				List<File> allValidFilesOfActivity = fileRepository
						.findAllByActivityPerformedCidAndActiveTrue(request.getId());
				List<File> updatedFiles = new ArrayList<File>();

				for (int itr = 0; itr < requestFiles.size(); itr++) {
					if (requestFiles.get(itr).getId() != null) {
						for (int itr2 = 0; itr2 < allValidFilesOfActivity.size(); itr2++) {
							if (requestFiles.get(itr).getId().equals(allValidFilesOfActivity.get(itr2).getCid())) {
								if (requestFiles.get(itr).getFile() != null) {
									File file = saveMediaForActivityPerformed(requestFiles.get(itr), "/activity-file/",
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
				 * logic to delete the files which were previously there but in
				 * new request have been removed.
				 */
				for (File f : allValidFilesOfActivity) {
					fileRepository.updateFileSetActiveByCid(false, f.getCid());
				}

				// Logic to save new files and then add it to List updatedFiles
				if (requestFiles != null && !requestFiles.isEmpty())
					for (FileRequest fileReq : requestFiles) {
						File file = saveMediaForActivityPerformed(fileReq, "/activity-file/", activityPerformed);
						if (file != null) {
							file.setCid(Utils.generateRandomAlphaNumString(8));
							updatedFiles.add(file);
						}
					}

				if (updatedFiles.size() > 5)
					throw new ValidationException("Cannot attach more than 5 files to upload.");
				// Setting files to activityPerformed
				activityPerformed.setFiles(updatedFiles);

			}else {
				if(activityPerformed.getFiles() != null && !activityPerformed.getFiles().isEmpty())
					activityPerformed.getFiles().stream().forEach(f -> f.setActive(false));
			}

		} else {
			
			if (request.getActivityId() == null)
				throw new ValidationException("Activity Id can not be null.");

			Activity activity = activityRepository.findByCidAndActiveTrue(request.getActivityId());
			if (activity == null)
				throw new ValidationException(
						String.format("Activity with id : %s does not exist.", request.getActivityId()));

			if (!studentClubRepository.existsByStudentIdAndActivityIdAndMembershipStatusAndActiveTrue(studentId,
					activity.getId(), ApprovalStatus.VERIFIED))
				throw new ValidationException(
						String.format("You are not a member of %s : %s please apply for membership first.",
								activity.getClubOrSociety().toString(), activity.getName()));

			if (request.getCoachId() == null || !teacherRepository.existsByCidAndActiveTrue(request.getCoachId()))
				throw new ValidationException(
						String.format("Coach id is invalid no coach with id : (%s) found.", request.getCoachId()));

			 
			/* Converting request to entity */
			activityPerformed = request.toEntity();

			/* Saving files associated with activity */

			if (request.getFileRequests() != null && !request.getFileRequests().isEmpty()) {
				List<File> activityPerformedMedia = new ArrayList<>();
				if (request.getFileRequests().size() > 5)
					throw new ValidationException("Cannot attach more than 5 files to upload.");

				for (FileRequest fileReq : request.getFileRequests()) {
					File file = saveMediaForActivityPerformed(fileReq, "/activity-file/", activityPerformed);
					if (file != null) {
						if (file.getCid() == null)
							file.setCid(Utils.generateRandomAlphaNumString(8));
						activityPerformedMedia.add(file);
					}
				}
				/*
				 * Assigned the returned files List set to ActivityPerformed
				 * entity
				 */
				activityPerformed.setFiles(activityPerformedMedia);
			}

			activityPerformed.setActive(true);
			if (activityPerformed.getCid() == null)
				activityPerformed.setCid(Utils.generateRandomAlphaNumString(8));
			
			activityPerformed.setStudent(studentRepository.findByIdAndActiveTrue(studentId));
			activityPerformed.setTeacher(teacherRepository.findByCidAndActiveTrue(request.getCoachId()));
			activityPerformed.setActivity(activity);
		}

		activityPerformed.setActivityStatus(ActivityStatus.SavedByStudent);
		activityPerformed = activityPerformedRepository.save(activityPerformed);
		if (activityPerformed == null)
			throw new RuntimeException("Something went wrong activity not saved.");

		return new ActivityPerformedResponse(activityPerformed);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_CREATE)
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

		if (activity.getDateOfActivity() == null || activity
				.getDescription() == null/*
											 * || activity.getFiles() == null ||
											 * activity.getFiles().isEmpty()
											 */)
			throw new ValidationException("Activity cannot be submitted first fill all the mandatory fields.");

		activity.setActivityStatus(ActivityStatus.SubmittedByStudent);
		activity.setSubmittedOn(LocalDateTime.now(DateTimeZone.forTimeZone(DateUtil.defaultTimeZone)).toDate());
		activity = activityPerformedRepository.save(activity);
		if (activity == null)
			throw new RuntimeException("Something went wrong activity not submitted.");

		return new ActivityPerformedResponse(activity);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_REVIEW)
	public ActivityPerformedResponse saveActivityByCoach(ActivityPerformedRequest request) {

		if (request.getId() == null)
			throw new ValidationException("Activity id cannot be null.");

		ActivityPerformed activity = activityPerformedRepository.findByCidAndActiveTrue(request.getId());

		if (activity == null)
			throw new ValidationException(String.format("No activity found with id : %s", request.getId()));
		
		if(!activity.getTeacher().getUser().getId().equals(getUserId()))
			throw new ValidationException("Please review only those activities that are assigned to you.");

		if (!(activity.getActivityStatus().equals(ActivityStatus.SubmittedByStudent))
				&& !(activity.getActivityStatus().equals(ActivityStatus.SavedByTeacher)))
			throw new ValidationException("Activity cannot be reviewed yet.");

		activity = request.toEntity(activity);

		if (activity.getCoachRemark() != null)
			activity.setCoachRemarkDate(LocalDateTime.now(DateTimeZone.forTimeZone(DateUtil.defaultTimeZone)).toDate());
		// DateUtil.convertStringToDate(LocalDate.now().toString())

		activity.setActivityStatus(ActivityStatus.SavedByTeacher);

		activity = activityPerformedRepository.save(activity);

		if (activity == null)
			throw new RuntimeException("Something went wrong activity not submitted.");

		return new ActivityPerformedResponse(activity);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_REVIEW)
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
			throw new ValidationException("Activity cannot be submitted ,  fill all the mandatory fields first.");

		activity.setActivityStatus(ActivityStatus.Reviewed);
		activity.setReviewedOn(LocalDateTime.now(DateTimeZone.forTimeZone(DateUtil.defaultTimeZone)).toDate());

		activity = activityPerformedRepository.save(activity);

		if (activity == null)
			throw new RuntimeException("Something went wrong activity not submitted.");

		return new ActivityPerformedResponse(activity);
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_FETCH)
	public List<ActivityPerformedResponse> getAllActivitiesOfStudentByStatus(String status, String studentCid,
			Integer page, Integer pageSize) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Student") )) {
			if (studentCid == null || !studentRepository.existsByCidAndActiveTrue(studentCid)) {
				throw new ValidationException("studentId cannot be null or invalid.");
			}
			}else {
				 studentCid = studentRepository.findCidByUserIdAndActiveTrue(getUserId());
				if (studentCid == null) 
					throw new ValidationException("Student not found probably userId is not set for student.");
			}
		
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
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_FETCH)
	public List<ActivityPerformedResponse> getAllActivitiesAssignedToCoachforReview(String teacherCid, String status) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Supervisor") || r.getName().equalsIgnoreCase("Coordinator") || r.getName().equalsIgnoreCase("Head") )) {
			if (teacherCid == null || !teacherRepository.existsByCidAndActiveTrue(teacherCid)) {
				throw new ValidationException("teacherId cannot be null or invalid.");
			}
		}else {
			teacherCid = teacherRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (teacherCid == null) 
				throw new ValidationException("Teacher not found probably userId is not set for student.");
		}

		List<ActivityPerformed> submittedActivities = null;
		if (status == null) {
			submittedActivities = activityPerformedRepository
					.findAllByTeacherCidAndActivityStatusOrActivityStatusOrActivityStatusAndActiveTrue(teacherCid,
							ActivityStatus.SubmittedByStudent, ActivityStatus.SavedByTeacher, ActivityStatus.Reviewed);

			if (submittedActivities == null || submittedActivities.isEmpty())
				throw new ValidationException("No activities assignet to teacher yet.");
		} else {
			if (status.equalsIgnoreCase("pending")) {
				submittedActivities = activityPerformedRepository
						.findAllByTeacherCidAndActivityStatusOrActivityStatusAndActiveTrue(teacherCid,
								ActivityStatus.SubmittedByStudent, ActivityStatus.SavedByTeacher);

				if (submittedActivities == null || submittedActivities.isEmpty())
					throw new ValidationException("No activities pending to review yet.");
			} else if (status.equalsIgnoreCase("reviewed")) {
				submittedActivities = activityPerformedRepository
						.findAllByTeacherCidAndActivityStatusAndActiveTrue(teacherCid, ActivityStatus.Reviewed);

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
						(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.DESC, "dateOfActivity")));
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
				studentCid, (Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.DESC, "dateOfActivity")));
		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities performed by student.");

		// performedActivities.stream().forEach(act -> {
		// if (act.getActivityStatus()!=null &&
		// !act.getActivityStatus().equals(ActivityStatus.SavedByStudent)
		// && !act.getActivityStatus().equals(ActivityStatus.SubmittedByStudent)
		// && !act.getActivityStatus().equals(ActivityStatus.Reviewed))
		// performedActivities.remove(act);
		// else
		// performedActivityResponses.add(new ActivityPerformedResponse(act));
		// });
		performedActivities.removeIf(
				act -> act.getActivityStatus() != null && !act.getActivityStatus().equals(ActivityStatus.SavedByStudent)
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
						(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.DESC, "dateOfActivity")));
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
						(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.DESC, "dateOfActivity")));
		if (performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities Reviewed yet.");
		return performedActivities.stream().map(ActivityPerformedResponse::new).collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_FETCH)
	public List<ActivityPerformedResponse> getAllActivityOfStudentByActivityId(String studentCid, String activityCid,
			String activityStatus) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Student") )) {
			if (studentCid == null || !studentRepository.existsByCidAndActiveTrue(studentCid)) {
				throw new ValidationException("studentId cannot be null or invalid.");
			}
			}else {
				 studentCid = studentRepository.findCidByUserIdAndActiveTrue(getUserId());
				if (studentCid == null) 
					throw new ValidationException("Student not found probably userId is not set for student.");
			}
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
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_DELETE)
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
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_FETCH)
	public List<ActivityPerformedResponse> filter(ActivityPerformedFilter filterRequest) {
		if (filterRequest.getStudentId() != null) {
			if (!studentRepository.existsByCidAndActiveTrue(filterRequest.getStudentId()))
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
	@Secured(AuthorityUtils.SCHOOL_PERFORMED_ACTIVITY_FETCH)
	public List<PropertyCount> getCount(String studentCid, String status, String type) {
		if(!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Student") )) {
			if (studentCid == null || !studentRepository.existsByCidAndActiveTrue(studentCid)) {
				throw new ValidationException("studentId cannot be null or invalid.");
			}
			}else {
				 studentCid = studentRepository.findCidByUserIdAndActiveTrue(getUserId());
				if (studentCid == null) 
					throw new ValidationException("Student not found probably userId is not set for student.");
			}

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

//	public Set<GroupResponseBy<ActivityPerformedResponse>> getAllActivityPerformedForCoordinator(String schoolCid ,String gradeId ,String clubId ,String status){
//		
//		if(getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("MainAdmin") || r.getName().equalsIgnoreCase("Lfin")) ) {
//			if(schoolCid == null || gradeId == null)
//				throw new ValidationException("SchoolId and Grade id cannot be null.");
//		}
//		schoolCid = getUser().getSchool().getCid();
//		List<Long> grades = null;
//		if(gradeId != null) {
//			grades = Arrays.asList(gradeRepository.findIdByCidAndActiveTrue(gradeId));
//		}else {
//			Long tId = teacherRepository.getIdByUserIdAndActiveTrue(getUserId());
//			if(tId != null)
//				grades = gradeRepository.findGradeIdsOfTeacher(tId);
//		}
//		List<ActivityStatus> statuses = null;
//		if(status == null) {
//			statuses = Arrays.asList(ActivityStatus.SubmittedByStudent, ActivityStatus.SavedByTeacher, ActivityStatus.Reviewed);
//		}else {
//			if(!ActivityStatus.matches(status))
//				throw new ValidationException(String.format("Invalid value (%s) for field status", status));
//			statuses = Arrays.asList(ActivityStatus.valueOf(status));
//		}
//		Set<GroupResponseBy<ActivityPerformed>> response = null;
//		
//		if(clubId == null) {
//			response = activityPerformedRepository.findAllBySchoolCidAndGradesIdInAndActivityStatusIn(schoolCid, grades, statuses);
//		}else {
//			response = activityPerformedRepository.findAllBySchoolCidAndGradesIdInAndActivityStatusInAndClubId(schoolCid, grades, statuses, clubId);
//		}
//		
//		Set<GroupResponseBy<ActivityPerformedResponse>> finalResponse = new HashSet<GroupResponseBy<ActivityPerformedResponse>>();
//		response.stream().forEach(r -> {
//			GroupResponseBy<ActivityPerformedResponse> member = new GroupResponseBy<>();
//			member.setCount(r.getCount());
//			member.setCriterionAndValue("activityName", r.getCriterionValue());
//			member.setResponses(r.getResponses().stream().map(ActivityPerformedResponse :: new).collect(Collectors.toList()));
//			finalResponse.add(member);
//		});
//		return finalResponse;
//	}
}
