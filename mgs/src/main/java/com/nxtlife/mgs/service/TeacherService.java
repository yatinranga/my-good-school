package com.nxtlife.mgs.service;

import javax.validation.Valid;

import com.nxtlife.mgs.view.TeacherRequest;
import com.nxtlife.mgs.view.TeacherResponse;

public interface TeacherService {

	TeacherResponse addTeacher(@Valid TeacherRequest request);

}
