package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.ActivityRepository;
import com.nxtlife.mgs.service.ActivityService;
import com.nxtlife.mgs.view.ActivityResponse;

@Service
public class ActivityServiceImpl implements ActivityService{

	@Autowired
	ActivityRepository activityRepository;
	
	@Override
	public List<ActivityResponse> getAllOfferedActivities(){
		List<Activity> activities = activityRepository.findAll();
		List<ActivityResponse> activityResponses = new ArrayList<ActivityResponse>();
		if(activities == null)
			throw new ValidationException("No activities found.");
		activities.forEach(activity -> {activityResponses.add(new ActivityResponse(activity));});
		return activityResponses;
	}
}
