package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.Teacher;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityPerformedRepository;
import com.nxtlife.mgs.jpa.FileRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.TeacherRepository;
import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.ActivityPerformedRequest;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;

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

	@Override
	public List<FileResponse> getAllFilesOfActivity(String activityCId) {
		List<FileResponse> fileResponseList = new ArrayList<>();
		List<File> files = fileRepository.findAllByActiveTrueAndActivityPerformedCid(activityCId);
		if(files==null || files.isEmpty())
			throw new NotFoundException("No files found for this activity.");
		for(File file : files) {
			fileResponseList.add(new FileResponse(file));
		}
		return fileResponseList;
	}
	
	@Override
	public File saveMediaForActivityPerformed(FileRequest fileRequest, String category,
			ActivityPerformed activityPerformed) {
		File file = fileService.saveMedia(fileRequest, category);
		if(fileRequest.getId()!=null)
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
		
		Student student = studentRepository.findByCid(request.getStudentId());
		
		if(student == null)
			throw new ValidationException(String.format("Student id is invalid no student with id : %s found.", request.getStudentId()));
		if (request.getActivityId() == null)
			throw new ValidationException("Activity Id can not be null.");
		if (request.getCoachId() == null)
			throw new ValidationException("Coach Id can not be null.");
		
		Teacher coach = teacherRepository.findByCidAndIsCoachTrue(request.getCoachId());
		
		if(coach == null)
			throw new ValidationException(String.format("Coach id is invalid no coach with id : %s found.", request.getCoachId()));

		/* setting those fields in request to null which needs to filled by Coach */
		if (request.getCoachRemark() != null || request.getCoachRemarkDate() != null
				|| request.getAchievementScore() != null || request.getParticipationScore() != null
				|| request.getInitiativeScore() != null || request.getStar() != null) {
			request.setCoachRemark(null);
			request.setCoachRemarkDate(null);
			request.setAchievementScore(null);
			request.setParticipationScore(null);
			request.setInitiativeScore(null);
			request.setStar(null);
		}
//		if(request.getDateOfActivity()==null || request.getDescription()==null || request.getFileRequests()==null|| request.getFileRequests().isEmpty())
//			actStatus = ActivityStatus.SavedByStudent;
//		else
//			actStatus = ActivityStatus.SubmittedByStudent;

		

		/* writing logic to update if activity is already saved and is not new */
		if (request.getId() != null) {
			activityPerformed = activityPerformedRepository.findByCid(request.getId());
			if (activityPerformed == null)
				throw new ValidationException(String.format("Activity having id : %s not found", request.getId()));
			if(!activityPerformed.getActivityStatus().equals(ActivityStatus.SavedByStudent))
				throw new ValidationException(String.format("Activity with the id : %s is already submitted by you and cannot be edited.", request.getId()));
			if (request.getDateOfActivity() == null)
				request.setDateOfActivity(activityPerformed.getDateOfActivity());
			if (request.getDescription() == null)
				request.setDescription(activityPerformed.getDescription());
			
			List<File> allValidFilesOfActivity = fileRepository.findAllByActiveTrueAndActivityPerformedCid(request.getId());
			List<File> copyOfAllValidImagesOfProduct = new ArrayList<File>();
			copyOfAllValidImagesOfProduct.addAll(allValidFilesOfActivity);
			List<FileRequest> requestFiles = request.getFileRequests();
			List<File> updatedFiles = new ArrayList<File>();
			/*
			 * write logic here to add new files if any and remove files which is not
			 * present now but were present earlier
			 */

			if (requestFiles != null && !requestFiles.isEmpty()) {
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

			}
			/* logic to delete the files which were previously there but in new request have
			 been removed. */
			for (File f : allValidFilesOfActivity) {
				fileRepository.updateFileSetActiveByCid(false, f.getCid());
			}
			
			//logic to save updated files
//			if(updatedFiles!=null && !updatedFiles.isEmpty())
//				for(File f : updatedFiles) {
//					fileRepository.saveAndFlush(f);
//				}
//			
			/* clear updatedFiles list and iteratively add new request files to it after saving them in next step */
//			updatedFiles.clear();
			
			// Logic to save new files and then add it to List updatedFiles
			if (requestFiles != null && !requestFiles.isEmpty())
				for(FileRequest fileReq : requestFiles) {
					File file = saveMediaForActivityPerformed(fileReq, "activity", activityPerformed);
					if (file != null){
						file.setCid(utils.generateRandomAlphaNumString(8));
						updatedFiles.add(file);
					}
				}
			
			//Setting files to activityPerformed 
			activityPerformed.setFiles(updatedFiles);
		} else {
			/* Converting request to entity */
			activityPerformed = request.toEntity();

			/* Saving files associated with activity */
			List<File> activityPerformedMedia = new ArrayList<>();
			if (request.getFileRequests() != null && !request.getFileRequests().isEmpty())
				for(FileRequest fileReq : request.getFileRequests())
			     {
					File file = saveMediaForActivityPerformed(fileReq, "activity", activityPerformed);
					if (file != null){
						file.setCid(utils.generateRandomAlphaNumString(8));
						activityPerformedMedia.add(file);
					}
				 }
			/* Assigned the returned files List set to ActivityPerformed entity */
			activityPerformed.setFiles(activityPerformedMedia);

			activityPerformed.setActive(true);
			if (activityPerformed.getCid() == null)
				activityPerformed.setCid(utils.generateRandomAlphaNumString(8));
		}

		actStatus = ActivityStatus.SavedByStudent;
		activityPerformed.setActivityStatus(actStatus);
		activityPerformed.setStudent(student);
		activityPerformed.setTeacher(coach);
		activityPerformed = activityPerformedRepository.save(activityPerformed);
		if(activityPerformed == null)
			throw new RuntimeException("Something went wrong activity not saved.");
		
		return new ActivityPerformedResponse(activityPerformed);
	}
	
	@Override
	public ActivityPerformedResponse submitActivity(String activityPerformedCid) {
		if(activityPerformedCid==null)
			throw new ValidationException("Id cannot be null.");
		ActivityPerformed activity = activityPerformedRepository.findByCid(activityPerformedCid);
		if(activity == null)
			throw new ValidationException(String.format("No activity found for id : %s", activityPerformedCid));
		
		if(!activity.getActivityStatus().equals(ActivityStatus.SavedByStudent))
			throw new ValidationException(String.format("Activity with the id : %s is already submitted by you and cannot be edited.", activityPerformedCid));
		
		if(activity.getDateOfActivity()==null || activity.getDescription()==null || activity.getFiles()==null|| activity.getFiles().isEmpty())
			throw new ValidationException("Activity cannot be submitted first fill all the mandatory fields.");
		
		activity.setActivityStatus(ActivityStatus.SubmittedByStudent);
		activity = activityPerformedRepository.save(activity);
		if(activity == null)
			throw new RuntimeException("Something went wrong activity not submitted.");
		
		return new ActivityPerformedResponse(activity);
	}
	
	@Override
	public List<ActivityPerformedResponse> getAllActivitiesOfStudentByStatus(String status , String studentCid) {
		switch(status) {
		case "saved":
		case "SAVED": 
			return getAllSavedActivitiesOfStudent(studentCid);
		case "submitted":
		case "SUBMITTED":
			return getAllSubmittedActivityOfStudent(studentCid);
		case "reviewed":
		case "REVIEWED":
			return getAllReviewedActivityOfStudent(studentCid);
		default:
			throw new ValidationException("Not a valid status.");
		}
	}
	
	@Override
	public List<ActivityPerformedResponse> getAllSavedActivitiesOfStudent(String studentCid){
		if(studentCid==null)
			throw new ValidationException("School id cannot be null.");
		List<ActivityPerformed> performedActivities = activityPerformedRepository.findAllByStudentCidAndActivityStatus(studentCid,ActivityStatus.SavedByStudent);
		List<ActivityPerformedResponse> performedActivityResponses = new ArrayList<ActivityPerformedResponse>();
		if(performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities saved by student.");
		performedActivities.forEach(act->{performedActivityResponses.add(new ActivityPerformedResponse(act));});
		return performedActivityResponses;
	}
	
	@Override
	public List<ActivityPerformedResponse> getAllSubmittedActivityOfStudent(String studentCid){
		if(studentCid==null)
			throw new ValidationException("School id cannot be null.");
		List<ActivityPerformed> performedActivities = activityPerformedRepository.findAllByStudentCidAndActivityStatus(studentCid,ActivityStatus.SubmittedByStudent);
		List<ActivityPerformedResponse> performedActivityResponses = new ArrayList<ActivityPerformedResponse>();
		if(performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities submitted by student.");
		performedActivities.forEach(act->{performedActivityResponses.add(new ActivityPerformedResponse(act));});
		return performedActivityResponses;
	}
	
	@Override
	public List<ActivityPerformedResponse> getAllReviewedActivityOfStudent(String studentCid){
		if(studentCid==null)
			throw new ValidationException("School id cannot be null.");
		List<ActivityPerformed> performedActivities = activityPerformedRepository.findAllByStudentCidAndActivityStatus(studentCid,ActivityStatus.Reviewed);
		List<ActivityPerformedResponse> performedActivityResponses = new ArrayList<ActivityPerformedResponse>();
		if(performedActivities == null || performedActivities.isEmpty())
			throw new ValidationException("No activities Reviewed yet.");
		performedActivities.forEach(act->{performedActivityResponses.add(new ActivityPerformedResponse(act));});
		return performedActivityResponses;
	}

}
