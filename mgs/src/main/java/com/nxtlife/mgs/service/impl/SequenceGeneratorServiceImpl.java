package com.nxtlife.mgs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.mgs.enums.UserType;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.SequenceGeneratorRepo;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.SequenceGeneratorService;
import com.nxtlife.mgs.util.SequenceGenerator;

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
	
	@Override
	public SequenceGenerator save(SequenceGenerator sequence) {
		if(sequence==null)
			throw new ValidationException("Invalid request body.");
		sequence = sequenceGeneratorRepo.save(sequence);
		if(sequence==null)
			throw new RuntimeException("Something went wrong sequence not saved.");
		return sequence;
	}

}
