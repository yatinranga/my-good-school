package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.activity.File;
import com.nxtlife.mgs.view.FileRequest;
import com.nxtlife.mgs.view.FileResponse;

public interface ActivityPerformedService {

	List<FileResponse> getAllFilesOfActivity(String activityCId);

	File saveMediaForActivityPerformed(FileRequest fileRequest, String category, ActivityPerformed activityPerformed);

}