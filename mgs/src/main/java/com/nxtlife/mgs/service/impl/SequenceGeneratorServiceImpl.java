package com.nxtlife.mgs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.mgs.jpa.SequenceGeneratorRepo;
import com.nxtlife.mgs.service.SequenceGeneratorService;

@Service
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService{

	@Autowired
	private SequenceGeneratorRepo sequenceGeneratorRepo;
	
	@Override
	public Long findSequenceByUserType(String userType) {
		return sequenceGeneratorRepo.findSequenceByUserType(userType);
	}

	@Override
	@Transactional
	public int updateSequenceByUserType(Long sequence, String userType) {
		return sequenceGeneratorRepo.updateSequenceByUserType(sequence, userType);
	}

}
