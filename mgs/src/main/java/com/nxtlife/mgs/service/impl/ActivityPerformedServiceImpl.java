package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.jpa.FileRepository;
import com.nxtlife.mgs.service.ActivityPerformedService;
import com.nxtlife.mgs.service.FileService;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;

@Service
public class ActivityPerformedServiceImpl implements ActivityPerformedService {

	@Autowired
	FileRepository fileRepository;
	
	@Autowired 
	FileService fileService;

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
}
