package com.nxtlife.mgs.service;

public interface SequenceGeneratorService {

	Long findSequenceByUserType(String userType);
	
	int updateSequenceByUserType(Long sequence , String userType);
}
