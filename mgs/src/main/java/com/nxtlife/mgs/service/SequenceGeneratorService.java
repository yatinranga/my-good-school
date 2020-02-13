package com.nxtlife.mgs.service;

import com.nxtlife.mgs.enums.UserType;

public interface SequenceGeneratorService {

	Long findSequenceByUserType(UserType userType);
	
	int updateSequenceByUserType(Long sequence , UserType userType);
}
