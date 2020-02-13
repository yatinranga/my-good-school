package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.FileRepository;
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
	Utils utils;

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
		file.setActivityPerformed(activityPerformed);
		return file;
	}
	
	@Override
	public ActivityPerformedResponse saveActivity(ActivityPerformedRequest request) {
		if (request == null)
			throw new ValidationException("Request can not be null.");
		ActivityStatus actStatus = ActivityStatus.InProgressAtStudent;

//		if(!getCurrentUser().getRoles().contains(new Role("Student")))
//			throw new AccessDeniedException("user is not authorized to submit activity because he is not a student.");
		if(request.getStudentId()==null)
			throw new ValidationException("Student Id can not be null.");
		if (request.getActivityId() == null)
			throw new ValidationException("Activity Id can not be null.");
		if(request.getCoachId()==null)
			throw new ValidationException("Coach Id can not be null.");
		
		/* setting those fields in request to null which needs to filled by Coach */
		request.setCoachRemark(null);
		request.setCoachRemarkDate(null);
		request.setAchievementScore(null);
		request.setParticipationScore(null);
		request.setInitiativeScore(null);
		request.setStar(null);
		
		if(request.getDateOfActivity()==null || request.getDescription()==null || request.getFileRequests()==null|| request.getFileRequests().isEmpty())
			actStatus = ActivityStatus.SavedByStudent;
		else
			actStatus = ActivityStatus.SubmittedByStudent;

		/* Converting request to entity */
		ActivityPerformed activityPerformed = request.toEntity();
		

		/* Saving files associated with activity */
		List<File> activityPerformedMedia = new ArrayList<>();
		if (request.getFileRequests() != null && !request.getFileRequests().isEmpty())
			request.getFileRequests().forEach(fileReq -> {
				File file = saveMediaForActivityPerformed(fileReq, "activity",
						activityPerformed);
				if (file != null)
					activityPerformedMedia.add(file);
			});
		/* Assigned the returned files List set to ActivityPerformed entity */
		activityPerformed.setFiles(activityPerformedMedia);
		
		activityPerformed.setActive(true);
		try {
			activityPerformed.setCid(utils.generateRandomAlphaNumString(8));
		} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
			activityPerformed.setCid(utils.generateRandomAlphaNumString(8));
		}
		activityPerformed.setActivityStatus(actStatus);
		return null;
	}

}
