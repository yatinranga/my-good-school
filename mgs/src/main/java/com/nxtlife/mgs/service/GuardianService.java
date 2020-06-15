package com.nxtlife.mgs.service;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.view.GuardianRequest;
import com.nxtlife.mgs.view.GuardianResponse;

public interface GuardianService {

	GuardianResponse save(GuardianRequest request);

	GuardianResponse setProfilePic(MultipartFile file, String guardianCid);

	GuardianResponse update(String cid, GuardianRequest request);

}
