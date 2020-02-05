package com.nxtlife.mgs.service.impl;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.service.SchoolService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.SchoolRequest;
import com.nxtlife.mgs.view.SchoolResponse;
import com.nxtlife.mgs.view.StudentResponse;

@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	SchoolRepository schoolRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	Utils utils;
	
	@Override
	public List<SchoolResponse> uploadTeachersFromExcel(MultipartFile file, Integer rowLimit, Boolean isCoach) {
		
		return null;
	}

	@Override
	public SchoolResponse save(SchoolRequest request) {
		if(request==null)
			throw new ValidationException("Request can not be null.");
		if (request.getEmail() == null)
			throw new ValidationException("Email can not be null");
		if (schoolRepository.countByEmail(request.getEmail()) > 0)
			throw new ValidationException("Email already exists");
		if (request.getUsername() == null) {
			request.setUsername(request.getEmail());
			if (schoolRepository.countByUsername(request.getUsername()) > 0)
				throw new ValidationException("Username already exists");
		} else {
			if (schoolRepository.countByUsername(request.getUsername()) > 0)
				throw new ValidationException("Username already exists");
		}
		
		if (request.getName() == null)
			throw new ValidationException("School name can not be null");
		School school = request.toEntity();
		
		try {
			school.setCid(utils.generateRandomAlphaNumString(8));
		} catch (ConstraintViolationException | javax.validation.ConstraintViolationException ce) {
			school.setCid(utils.generateRandomAlphaNumString(8));
		}
		
		User user = userService.createSchoolUser(school);
		if (StringUtils.isEmpty(user))
			throw new ValidationException("User not created successfully");
		school.setUser(user);
		school = schoolRepository.save(school);
		if(school==null)
			throw new RuntimeException("Something went wrong school not saved.");
		
		return new SchoolResponse(school);
		
	}

	@Override
	public SchoolResponse findById(Long id) {
		return null;
	}

	@Override
	public SchoolResponse findByCId(String cId) {
		return null;
	}

}
