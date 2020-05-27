package com.nxtlife.mgs.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.session.Event;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.enums.SessionFetch;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.filtering.filter.SessionFilter;
import com.nxtlife.mgs.filtering.filter.SessionFilterBuilder;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.jpa.FileRepository;
import com.nxtlife.mgs.jpa.GradeRepository;
import com.nxtlife.mgs.jpa.SessionRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileStorageService;
import com.nxtlife.mgs.service.SessionService;
import com.nxtlife.mgs.util.DateUtil;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.GroupResponseBy;
import com.nxtlife.mgs.view.SessionRequest;
import com.nxtlife.mgs.view.SessionResponse;
import com.nxtlife.mgs.view.SuccessResponse;

@Service
public class SessionServiceImpl extends BaseService implements SessionService {

	@Autowired
	SessionRepository sessionRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TeacherRepository teacherRepository;

	@Autowired
	GradeRepository gradeRepository;

	@Autowired
	SessionFilterBuilder builder;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	private FileStorageService<MultipartFile> fileStorageService;

	@Autowired
	FileRepository fileRepository;

	@Override
	public SessionResponse createSession(SessionRequest request) {
		Long userId = getUserId();
		if (userId == null)
			throw new UnauthorizedUserException("Login as a teacher to create session.");
		Teacher teacher = teacherRepository.getByUserId(userId);
		if (teacher == null)
			throw new ForbiddenException("Login as a teacher to create session");
		if (request == null)
			throw new ValidationException("request cannot be null.");
		if (!activityRepository.existsByCidAndActiveTrue(request.getClubId()))
			throw new ValidationException(String.format("Club with id (%s) does not exist.", request.getClubId()));

		Event session = request.toEntity();

		if (request.getGradeIds() != null && !request.getGradeIds().isEmpty()) {

			List<Grade> repoGradeList = gradeRepository.findAllBySchoolsCidAndActiveTrue(teacher.getSchool().getCid());
			List<Grade> finalGradeList = new ArrayList<Grade>();

			for (int i = 0; i < request.getGradeIds().size(); i++) {

				boolean flag = false;

				for (Grade grade : repoGradeList) {

					if (grade.getCid().equals(request.getGradeIds().get(i))) {
						flag = true;
						List<Event> sessions = new ArrayList<Event>();
						sessions = grade.getSessions();
						sessions.add(session);
						finalGradeList.add(grade);
						break;
					}
				}

				if (flag == false) {
					throw new NotFoundException(
							String.format("grade having id [%s] didn't exist", request.getGradeIds().get(i)));
				}

			}
			session.setGrades(finalGradeList);
		}

		if (request.getFileRequests() != null && !request.getFileRequests().isEmpty()) {
			List<File> sessionMedia = new ArrayList<>();
			if (request.getFileRequests().size() > 5)
				throw new ValidationException("Cannot attach more than 5 files to upload.");

			for (FileRequest fileReq : request.getFileRequests()) {
				File file = saveMediaForSession(fileReq, "/session-file/", session);
				if (file != null) {
					if (file.getCid() == null)
						file.setCid(Utils.generateRandomAlphaNumString(8));
					sessionMedia.add(file);
				}
			}
			/*
			 * Assigned the returned files List set to Event entity
			 */
			session.setFiles(sessionMedia);
		}

		session.setCid(Utils.generateRandomAlphaNumString(8));
		session.setTeacher(teacher);
		session.setClub(activityRepository.getOneByCidAndActiveTrue(request.getClubId()));
		session = sessionRepository.save(session);
		return new SessionResponse(session);
	}

	private File saveMediaForSession(FileRequest fileRequest, String category, Event session) {
		File file = fileRequest.toEntity();
		String fileUrl = fileStorageService.storeFile(fileRequest.getFile(),
				fileRequest.getFile().getOriginalFilename(), category, true, fileRequest.getIsImage());
		file.setUrl(fileUrl);

		if (fileRequest.getId() != null)
			file.setCid(fileRequest.getId());
		file.setEvent(session);
		return file;
	}

	@Override
	public SessionResponse updateSession(SessionRequest request) {
		Long userId = getUserId();
		if (userId == null)
			throw new UnauthorizedUserException("Login as a teacher to update session.");
		Teacher teacher = teacherRepository.getByUserId(userId);

		if (request == null)
			throw new ValidationException("request cannot be null.");
		if (request.getId() == null)
			throw new ValidationException("id cannot be null.");
		Event session = sessionRepository.findByCidAndActiveTrue(request.getId());
		if (session == null)
			throw new ValidationException(String.format("Session with id (%s) not found.", request.getId()));

		if (teacher == null || !teacher.getCid().equals(session.getTeacher().getCid()))
			throw new ForbiddenException("To update the session details login as the same teacher who created it.");
		session = request.toEntity(session);
		if (request.getClubId() != null && !session.getClub().getCid().equals(request.getClubId())) {
			if (!activityRepository.existsByCidAndActiveTrue(request.getClubId()))
				throw new ValidationException(String.format("Club with id (%s) does not exist.", request.getClubId()));
			session.setClub(activityRepository.getOneByCidAndActiveTrue(request.getClubId()));
		}

		if (request.getGradeIds() != null && !request.getGradeIds().isEmpty()) {
			List<String> requestGradeIds = request.getGradeIds();
			List<Grade> previousGrades = session.getGrades();

			for (int i = 0; i < previousGrades.size(); i++) {

				if (requestGradeIds.contains(previousGrades.get(i).getCid())) {
					requestGradeIds.remove(previousGrades.get(i).getCid());
				} else {
					previousGrades.remove(i--);
				}
			}
			if (requestGradeIds != null && !requestGradeIds.isEmpty())
				for (String gradeId : requestGradeIds) {
					if (!gradeRepository.existsByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(gradeId,
							teacher.getSchool().getCid()))
						throw new ValidationException(String.format("No grade with id (%s) found in your school (%s)",
								gradeId, teacher.getSchool().getName()));
					Grade grade = gradeRepository.findByCidAndSchoolsCidAndSchoolsActiveTrueAndActiveTrue(gradeId,
							teacher.getSchool().getCid());
					List<Event> sessions = new ArrayList<Event>();
					sessions = grade.getSessions();
					sessions.add(session);
					grade.setSessions(sessions);
					previousGrades.add(grade);
				}
			session.setGrades(previousGrades);
		}

		List<FileRequest> requestFiles = request.getFileRequests();

		if (requestFiles != null && !requestFiles.isEmpty()) {

			List<File> allValidFilesOfActivity = fileRepository.findAllByEventCidAndActiveTrue(request.getId());
			List<File> updatedFiles = new ArrayList<File>();

			for (int itr = 0; itr < requestFiles.size(); itr++) {
				if (requestFiles.get(itr).getId() != null) {
					for (int itr2 = 0; itr2 < allValidFilesOfActivity.size(); itr2++) {
						if (requestFiles.get(itr).getId().equals(allValidFilesOfActivity.get(itr2).getCid())) {
							if (requestFiles.get(itr).getFile() != null) {
								File file = saveMediaForSession(requestFiles.get(itr), "/session-file/", session);
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
			 * logic to delete the files which were previously there but in new
			 * request have been removed.
			 */
			for (File f : allValidFilesOfActivity) {
				fileRepository.updateFileSetActiveByCid(false, f.getCid());
			}

			// Logic to save new files and then add it to List updatedFiles
			if (requestFiles != null && !requestFiles.isEmpty())
				for (FileRequest fileReq : requestFiles) {
					File file = saveMediaForSession(fileReq, "/session-file/", session);
					if (file != null) {
						file.setCid(Utils.generateRandomAlphaNumString(8));
						updatedFiles.add(file);
					}
				}

			if (updatedFiles.size() > 5)
				throw new ValidationException("Cannot attach more than 5 files to upload.");
			// Setting files to session
			session.setFiles(updatedFiles);

		}

		session = sessionRepository.save(session);
		return new SessionResponse(session);
	}

	@Override
	public List<SessionResponse> getSessions(SessionFilter filter) {
		List<SessionResponse> sessions = sessionRepository.findAll(builder.build(filter)).stream()
				.map(SessionResponse::new).collect(Collectors.toList());
		if (sessions == null || sessions.isEmpty())
			throw new ValidationException("No sessions found.");
		return sessions;
	}

	@Override
	public SessionResponse getStudentSessionsOfClubBy(String clubId, String sessionFetch, String teacherId,
			Integer page, Integer pageSize) {
		Long userId = getUserId();
		if (userId == null)
			throw new UnauthorizedUserException("Login as a student to see sessions details.");
		Student student = studentRepository.getByUserIdAndActiveTrue(userId);
		if (student == null)
			throw new ForbiddenException("Login as a student to see sessions details.");
		if (clubId == null)
			throw new ValidationException("Club Id cannot be null.");
		if (!activityRepository.existsByCidAndActiveTrue(clubId))
			throw new ValidationException(String.format("Club with id (%s) does not exist.", clubId));
		// Activity club = activityRepository.getOneByCid(clubId);
		String gradeId = student.getGrade().getCid();
		List<Event> sessions;
		if (sessionFetch == null) {
			sessions = getStudentSessionsOfClub(gradeId, clubId, teacherId, page, pageSize);
		} else {
			Date start = DateTime.now().minusSeconds(60).toDate(), end;// LocalDateTime.now().minusSeconds(60).toDate(),
																		// end;
			if (!SessionFetch.matches(sessionFetch))
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, year}",
						sessionFetch));
			switch (SessionFetch.valueOf(sessionFetch)) {
			case today:
				end = DateUtil.atEndOfDay(start);
				if (teacherId != null) {
					if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
						throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
					sessions = sessionRepository
							.findAllByGradesCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndTeacherCidAndActiveTrue(
									gradeId, clubId, start, end, teacherId,
									Sort.by(Direction.ASC, "startDate"));
				} else {
					sessions = sessionRepository
							.findAllByGradesCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(
									gradeId, clubId, start, end, Sort.by(Direction.ASC, "startDate"));
				}
				break;
			case week:
				end = DateUtil.getlastWorkingDayOfWeek(DateUtil.getLastDayOfWeek());
				if (teacherId != null) {
					if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
						throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
					sessions = sessionRepository
							.findAllByGradesCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndTeacherCidAndActiveTrue(
									gradeId, clubId, start, end, teacherId,
									Sort.by(Direction.ASC, "startDate"));
				} else {
					sessions = sessionRepository
							.findAllByGradesCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(
									gradeId, clubId, start, end, Sort.by(Direction.ASC, "startDate"));
				}
				break;
			case month:
				end = DateUtil.getLastWorkingDayOfMonth(DateUtil.getLastDayOfMonth());
				if (teacherId != null) {
					if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
						throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
					sessions = sessionRepository
							.findAllByGradesCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndTeacherCidAndActiveTrue(
									gradeId, clubId, start, end, teacherId,
									Sort.by(Direction.ASC, "startDate"));
				} else {
					sessions = sessionRepository
							.findAllByGradesCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(
									gradeId, clubId, start, end, Sort.by(Direction.ASC, "startDate"));
				}
				break;
			default:
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, month}",
						sessionFetch));
			}
		}
		if (sessions == null || sessions.isEmpty())
			throw new ValidationException("No sessions found for you.");

		List<LocalDate> startDates = new ArrayList<LocalDate>();
		sessions.stream().forEach(s -> {
			if (!startDates.contains(s.getStartLocalDate()))
				startDates.add(s.getStartLocalDate());
		});

		Collections.sort(startDates);

		SessionResponse response = new SessionResponse();
		List<GroupResponseBy<SessionResponse>> finalSessionList = new ArrayList<GroupResponseBy<SessionResponse>>();
		for (LocalDate startDate : startDates) {
			GroupResponseBy<SessionResponse> tempSessionList = new GroupResponseBy<SessionResponse>();
			tempSessionList.setCriterionAndValue("date",
					DateUtil.formatDate(DateUtil.convertLocalDateToDateAtStartOfDay(startDate)));
			tempSessionList.setResponses(sessions.stream().filter(s -> s.getStartLocalDate().equals(startDate))
					.map(SessionResponse::new).distinct().collect(Collectors.toList()));
			finalSessionList.add(tempSessionList);
		}
		response.setSessions(finalSessionList);
		// return
		// sessions.stream().map(SessionResponse::new).collect(Collectors.toList());
		return response;
	}

	private List<Event> getStudentSessionsOfClub(String gradeId, String clubId, String teacherId, Integer page,
			Integer pageSize) {
		// List<Event> sessions =
		if (teacherId != null) {
			if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
				throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
			return sessionRepository.findAllByGradesCidAndClubCidAndTeacherCidAndActiveTrue(gradeId, clubId, teacherId,
					(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.ASC, "startDate")));
		}
		return sessionRepository.findAllByGradesCidAndClubCidAndActiveTrue(gradeId, clubId,
				(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.ASC, "startDate")));
		//
		// if(sessions == null || sessions.isEmpty())
		// throw new ValidationException("No sessions found for you.");
		// return sessions.stream().map(SessionResponse ::
		// new).distinct().collect(Collectors.toList());
	}

	@Override
	public SessionResponse getStudentSessionsOfClubsBy(String sessionFetch, String teacherId, Integer page,
			Integer pageSize) {
		Long userId = getUserId();
		if (userId == null)
			throw new UnauthorizedUserException("Login as a student to see sessions details.");
		Student student = studentRepository.getByUserIdAndActiveTrue(userId);
		if (student == null)
			throw new ForbiddenException("Login as a student to see sessions details.");
		String gradeId = student.getGrade().getCid();
		List<Activity> clubs = new ArrayList<Activity>();
		student.getStudentClubs().stream().forEach(sc -> {
			if (sc.getMembershipStatus().equals(ApprovalStatus.VERIFIED))
				clubs.add(sc.getActivity());
		});

		List<Event> sessions;
		if (sessionFetch == null) {
			sessions = getStudentSessionsOfClubs(gradeId, clubs, teacherId, page, pageSize);
		} else {
			Date start = DateTime.now().minusSeconds(60).toDate(), end;
			if (!SessionFetch.matches(sessionFetch))
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, year}",
						sessionFetch));
			switch (SessionFetch.valueOf(sessionFetch)) {
			case today:
				end = DateUtil.atEndOfDay(start);
				if (teacherId != null) {
					if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
						throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
					sessions = sessionRepository
							.findAllByGradesCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndTeacherCidAndActiveTrueGroupByClubId(
									gradeId, clubs, start, end, teacherId,
									Sort.by(Direction.ASC, "startDate"));
				} else {
					sessions = sessionRepository
							.findAllByGradesCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(
									gradeId, clubs, start, end, Sort.by(Direction.ASC, "startDate"));
				}
				break;
			case week:
				end = DateUtil.getlastWorkingDayOfWeek(DateUtil.getLastDayOfWeek());
				if (teacherId != null) {
					if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
						throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
					sessions = sessionRepository
							.findAllByGradesCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndTeacherCidAndActiveTrueGroupByClubId(
									gradeId, clubs, start, end, teacherId,
									Sort.by(Direction.ASC, "startDate"));
				} else {
					sessions = sessionRepository
							.findAllByGradesCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(
									gradeId, clubs, start, end, Sort.by(Direction.ASC, "startDate"));
				}
				break;
			case month:
				end = DateUtil.getLastWorkingDayOfMonth(DateUtil.getLastDayOfMonth());
				if (teacherId != null) {
					if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
						throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
					sessions = sessionRepository
							.findAllByGradesCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndTeacherCidAndActiveTrueGroupByClubId(
									gradeId, clubs, start, end, teacherId,
									Sort.by(Direction.ASC, "startDate"));
				} else {
					sessions = sessionRepository
							.findAllByGradesCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(
									gradeId, clubs, start, end, Sort.by(Direction.ASC, "startDate"));
				}
				break;
			default:
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, month}",
						sessionFetch));
			}
		}
		if (sessions == null || sessions.isEmpty())
			throw new ValidationException("No sessions found for you.");

		List<LocalDate> startDates = new ArrayList<LocalDate>();
		sessions.stream().forEach(s -> {
			if (!startDates.contains(s.getStartLocalDate()))
				startDates.add(s.getStartLocalDate());
		});

		Collections.sort(startDates);

		SessionResponse response = new SessionResponse();
		List<GroupResponseBy<SessionResponse>> finalSessionList = new ArrayList<GroupResponseBy<SessionResponse>>();
		for (LocalDate startDate : startDates) {
			GroupResponseBy<SessionResponse> tempSessionList = new GroupResponseBy<SessionResponse>();
			tempSessionList.setCriterionAndValue("date",
					DateUtil.formatDate(DateUtil.convertLocalDateToDateAtStartOfDay(startDate)));
			tempSessionList.setResponses(sessions.stream().filter(s -> s.getStartLocalDate().equals(startDate))
					.map(SessionResponse::new).distinct().collect(Collectors.toList()));
			finalSessionList.add(tempSessionList);
		}
		response.setSessions(finalSessionList);
		// return
		// sessions.stream().map(SessionResponse::new).collect(Collectors.toList());
		return response;
	}

	private List<Event> getStudentSessionsOfClubs(String gradeCid, List<Activity> clubs, String teacherId, Integer page,
			Integer pageSize) {
		if (teacherId != null) {
			if (!teacherRepository.existsByCidAndActiveTrue(teacherId))
				throw new ValidationException(String.format("Teacher with id (%s) not found.", teacherId));
			return sessionRepository.findAllByGradesCidAndClubInAndTeacherCidAndActiveTrueGroupByClubId(gradeCid, clubs,
					teacherId,
					(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.ASC, "startDate")));
		}
		return sessionRepository.findAllByGradesCidAndClubInAndActiveTrueGroupByClubId(gradeCid, clubs,
				(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.ASC, "startDate")));
	}

	@Override
	public SessionResponse getTeacherSessionsOfClubBy(String clubId, String sessionFetch, Integer page,
			Integer pageSize) {
		Long userId = getUserId();
		if (userId == null)
			throw new UnauthorizedUserException("Login as a teacher to see sessions details.");
		Teacher teacher = teacherRepository.findByUserIdAndActiveTrue(userId);
		if (teacher == null)
			throw new ForbiddenException("Login as a teacher to see sessions details.");
		String teacherCid = teacher.getCid();
		if (clubId == null)
			throw new ValidationException("Club Id cannot be null.");
		if (!activityRepository.existsByCidAndActiveTrue(clubId))
			throw new ValidationException(String.format("Club with id (%s) does not exist.", clubId));
		// Activity club = activityRepository.getOneByCid(clubId);

		List<Event> sessions;
		if (sessionFetch == null) {
			sessions = getTeacherSessionsOfClub(teacherCid, clubId, page, pageSize);
		} else {
			Date start = DateTime.now().minusSeconds(60).toDate(), end;
			if (!SessionFetch.matches(sessionFetch))
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, year}",
						sessionFetch));
			switch (SessionFetch.valueOf(sessionFetch)) {
			case today:
				end = DateUtil.atEndOfDay(start);
				sessions = sessionRepository
						.findAllByTeacherCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(
								teacherCid, clubId, start, end, Sort.by(Direction.ASC, "startDate"));
				break;
			case week:
				end = DateUtil.getlastWorkingDayOfWeek(DateUtil.getLastDayOfWeek());
				sessions = sessionRepository
						.findAllByTeacherCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(
								teacherCid, clubId, start, end, Sort.by(Direction.ASC, "startDate"));
				break;
			case month:
				end = DateUtil.getLastWorkingDayOfMonth(DateUtil.getLastDayOfMonth());
				sessions = sessionRepository
						.findAllByTeacherCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(
								teacherCid, clubId, start, end, Sort.by(Direction.ASC, "startDate"));
				break;
			default:
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, month}",
						sessionFetch));
			}
		}
		if (sessions == null || sessions.isEmpty())
			throw new ValidationException("No sessions found for you.");

		List<LocalDate> startDates = new ArrayList<LocalDate>();
		sessions.stream().forEach(s -> {
			if (!startDates.contains(s.getStartLocalDate()))
				startDates.add(s.getStartLocalDate());
		});

		Collections.sort(startDates);

		SessionResponse response = new SessionResponse();
		List<GroupResponseBy<SessionResponse>> finalSessionList = new ArrayList<GroupResponseBy<SessionResponse>>();
		for (LocalDate startDate : startDates) {
			GroupResponseBy<SessionResponse> tempSessionList = new GroupResponseBy<SessionResponse>();
			tempSessionList.setCriterionAndValue("date",
					DateUtil.formatDate(DateUtil.convertLocalDateToDateAtStartOfDay(startDate)));
			tempSessionList.setResponses(sessions.stream().filter(s -> s.getStartLocalDate().equals(startDate))
					.map(SessionResponse::new).distinct().collect(Collectors.toList()));
			finalSessionList.add(tempSessionList);
		}
		response.setSessions(finalSessionList);
		// return
		// sessions.stream().map(SessionResponse::new).collect(Collectors.toList());
		return response;
	}

	private List<Event> getTeacherSessionsOfClub(String teacherCid, String clubId, Integer page, Integer pageSize) {
		return sessionRepository.findAllByTeacherCidAndClubCidAndActiveTrue(teacherCid, clubId,
				(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.ASC, "startDate")));
	}

	@Override
	public SessionResponse getTeacherSessionsOfClubsBy(String sessionFetch, Integer page, Integer pageSize) {
		Long userId = getUserId();
		if (userId == null)
			throw new UnauthorizedUserException("Login as a teacher to see sessions details.");
		Teacher teacher = teacherRepository.findByUserIdAndActiveTrue(userId);
		if (teacher == null)
			throw new ForbiddenException("Login as a teacher to see sessions details.");
		String teacherCid = teacher.getCid();
		List<Activity> clubs = new ArrayList<Activity>();
		teacher.getTeacherActivityGrades().stream().forEach(tag -> {
			if (!clubs.contains(tag.getActivity()))
				clubs.add(tag.getActivity());
		});

		List<Event> sessions;
		if (sessionFetch == null) {
			sessions = getTeacherSessionsOfClubs(teacherCid, clubs, page, pageSize);
		} else {
			Date start = DateTime.now().minusSeconds(60).toDate(), end;
			if (!SessionFetch.matches(sessionFetch))
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, year}",
						sessionFetch));
			switch (SessionFetch.valueOf(sessionFetch)) {
			case today:
				end = DateUtil.atEndOfDay(start);
				sessions = sessionRepository
						.findAllByTeacherCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(
								teacherCid, clubs, start, end, Sort.by(Direction.ASC, "startDate"));
				break;
			case week:
				end = DateUtil.getlastWorkingDayOfWeek(DateUtil.getLastDayOfWeek());
				sessions = sessionRepository
						.findAllByTeacherCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(
								teacherCid, clubs, start, end, Sort.by(Direction.ASC, "startDate"));
				break;
			case month:
				end = DateUtil.getLastWorkingDayOfMonth(DateUtil.getLastDayOfMonth());
				sessions = sessionRepository
						.findAllByTeacherCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(
								teacherCid, clubs, start, end, Sort.by(Direction.ASC, "startDate"));
				break;
			default:
				throw new ValidationException(String.format(
						"Invalid value (%s) for sessionFetch it should be from List : {today, week, month}",
						sessionFetch));
			}
		}
		if (sessions == null || sessions.isEmpty())
			throw new ValidationException("No sessions found for you.");

		List<LocalDate> startDates = new ArrayList<LocalDate>();
		sessions.stream().forEach(s -> {
			if (!startDates.contains(s.getStartLocalDate()))
				startDates.add(s.getStartLocalDate());
		});

		Collections.sort(startDates);

		SessionResponse response = new SessionResponse();
		List<GroupResponseBy<SessionResponse>> finalSessionList = new ArrayList<GroupResponseBy<SessionResponse>>();
		for (LocalDate startDate : startDates) {
			GroupResponseBy<SessionResponse> tempSessionList = new GroupResponseBy<SessionResponse>();
			tempSessionList.setCriterionAndValue("date",
					DateUtil.formatDate(DateUtil.convertLocalDateToDateAtStartOfDay(startDate)));
			tempSessionList.setResponses(sessions.stream().filter(s -> s.getStartLocalDate().equals(startDate))
					.map(SessionResponse::new).distinct().collect(Collectors.toList()));
			finalSessionList.add(tempSessionList);
		}
		response.setSessions(finalSessionList);
		// return
		// sessions.stream().map(SessionResponse::new).collect(Collectors.toList());
		return response;
	}

	private List<Event> getTeacherSessionsOfClubs(String teacherCid, List<Activity> clubs, Integer page,
			Integer pageSize) {
		return sessionRepository.findAllByTeacherCidAndClubInAndActiveTrueGroupByClubId(teacherCid, clubs,
				(Pageable) PageRequest.of(page, pageSize, Sort.by(Direction.ASC, "startDate")));
	}

	@Override
	public SuccessResponse deleteSession(String sessionCid) {
		if (sessionCid == null)
			throw new ValidationException("Session id cannot be null.");

		Event session = sessionRepository.findByCidAndActiveTrue(sessionCid);
		if (session == null)
			throw new ValidationException(String.format("Session with id (%s) not found", sessionCid));
		session.setActive(false);
		session = sessionRepository.save(session);

		return new SuccessResponse(200, String.format("Session titled (%s) deleted.", session.getTitle()));
	}

}
