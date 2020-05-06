package com.nxtlife.mgs.service;

import java.util.List;

import com.nxtlife.mgs.filtering.filter.SessionFilter;
import com.nxtlife.mgs.view.SessionRequest;
import com.nxtlife.mgs.view.SessionResponse;
import com.nxtlife.mgs.view.SuccessResponse;

public interface SessionService {

	SessionResponse createSession(SessionRequest request);

	List<SessionResponse> getSessions(SessionFilter filter);

	SessionResponse updateSession(SessionRequest request);

	SuccessResponse deleteSession(String sessionCid);

	SessionResponse getStudentSessionsOfClubBy(String clubId, String sessionFetch ,String teacherId , Integer page,
			Integer pageSize);

	SessionResponse getStudentSessionsOfClubsBy(String sessionFetch,String teacherId , Integer page, Integer pageSize);

	SessionResponse getTeacherSessionsOfClubsBy(String sessionFetch, Integer page, Integer pageSize);

	SessionResponse getTeacherSessionsOfClubBy(String clubId, String sessionFetch, Integer page,
			Integer pageSize);

}
