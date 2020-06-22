package com.nxtlife.mgs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.GuardianRequest;
import com.nxtlife.mgs.view.GuardianResponse;
import com.nxtlife.mgs.view.StudentResponse;

public interface GuardianService {

	GuardianResponse save(GuardianRequest request);

	GuardianResponse setProfilePic(MultipartFile file, String guardianCid);

	GuardianResponse update(String cid, GuardianRequest request);

	GuardianResponse getById(String id);

	List<StudentResponse> getAllChildrenOfGuardian(String guardianId);

}
