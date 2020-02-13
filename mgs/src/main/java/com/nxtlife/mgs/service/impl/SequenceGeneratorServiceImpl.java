package com.nxtlife.mgs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.jpa.SequenceGeneratorRepo;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.SequenceGeneratorService;

@Service
public class SequenceGeneratorServiceImpl extends BaseService implements SequenceGeneratorService{

	@Autowired
	private SequenceGeneratorRepo sequenceGeneratorRepo;
	
	@Override
	public Long findSequenceByUserType(UserType userType) {
		return sequenceGeneratorRepo.findSequenceByUserType(userType);
	}

	@Override
	public int updateSequenceByUserType(Long sequence, UserType userType) {
		return sequenceGeneratorRepo.updateSequenceByUserType(sequence, userType);
	}

}
