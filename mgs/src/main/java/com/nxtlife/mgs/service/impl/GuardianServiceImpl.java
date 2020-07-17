package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.common.UserRoleKey;
import com.nxtlife.mgs.entity.user.Guardian;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.entity.user.UserRole;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.GuardianRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.StudentRepository;
import com.nxtlife.mgs.jpa.UserRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.FileStorageService;
import com.nxtlife.mgs.service.GuardianService;
import com.nxtlife.mgs.service.UserService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.util.Utils;
import com.nxtlife.mgs.view.GuardianRequest;
import com.nxtlife.mgs.view.GuardianResponse;
import com.nxtlife.mgs.view.StudentResponse;

@Service
public class GuardianServiceImpl extends BaseService implements GuardianService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserService userService;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	GuardianRepository guardianRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileStorageService<MultipartFile> fileStorageService;

	@Override
	@Secured(AuthorityUtils.SCHOOL_GUARDIAN_CREATE)
	public GuardianResponse save(GuardianRequest request) {
		Guardian guardian = request.toEntity();

		Role role = roleRepository.findBySchoolIdAndName(getUser().gettSchoolId(), "Guardian");
		User user = userService.createUser(guardian.getName(), guardian.getMobileNumber(), guardian.getEmail(),
				getUser().gettSchoolId());

		if (StringUtils.isEmpty(user)) {
			throw new ValidationException("User not created successfully");
		}

		guardian.setUser(user);
		guardian.setUsername(user.getUsername());
		List<Student> students = new ArrayList<Student>();
		setStudentsOfGuardian(guardian, students, request.getStudentIds());
		guardian.setStudents(students);
		guardian.setCid(Utils.generateRandomAlphaNumString(8));
		user.setUserRoles(Arrays.asList(new UserRole(new UserRoleKey(role.getId(), user.getId()), role, user)));

		return new GuardianResponse(guardian = guardianRepository.save(guardian));
	}

	@Override
	public GuardianResponse setProfilePic(MultipartFile file, String guardianCid) {
		if (file == null || file.getSize() == 0)
			throw new ValidationException("profilePic cannot be null or empty.");
		if (!getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Guardian"))) {
			if (guardianCid == null || !guardianRepository.existsByCidAndActiveTrue(guardianCid)) {
				throw new ValidationException("guardian id cannot be null or invalid.");
			}
		} else {
			guardianCid = guardianRepository.findCidByUserIdAndActiveTrue(getUserId());
			if (guardianCid == null)
				throw new ValidationException("Guardian not found probably userId is not set for guardian.");
		}

		String imageUrl = guardianRepository.findImageUrlByCid(guardianCid);

		if (imageUrl != null)
			fileStorageService.delete(imageUrl);
		imageUrl = fileStorageService.storeFile(file, file.getOriginalFilename(), "/profile-image/", true, true);
		int rows = guardianRepository.setImageUrlByCid(guardianCid, imageUrl);
		if (rows == 0)
			throw new RuntimeException("Profile image not set, some error occured.");
		String userCid = guardianRepository.findUserCidByCidAndActiveTrue(guardianCid);
		rows = userRepository.setImageUrlByCid(userCid, imageUrl);
		if (rows == 0)
			throw new RuntimeException(
					String.format("Profile image not set for user with id (%s), some error occured.", userCid));
		GuardianResponse guardian = new GuardianResponse();
		guardian.setId(guardianCid);
		guardian.setImageUrl(imageUrl);
		return guardian;
	}

	@Override
	public GuardianResponse update(String cid, GuardianRequest request) {
		if (getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Guardian")))
			cid = guardianRepository.findCidByUserIdAndActiveTrue(getUserId());
		if (cid == null)
			throw new ValidationException("guardian id cannot be null.");

		Guardian guardian = guardianRepository.findByCidAndActiveTrue(cid);
		if (guardian == null)
			throw new ValidationException(String.format("Guardian with id (%s) does not exist.", cid));

		guardian = request.toEntity(guardian);

		if (request.getMobileNumber() != null) {

			if (!userRepository.existsByContactNumberAndCidNot(request.getMobileNumber(),
					guardian.getUser().getCid())) {
				guardian.setMobileNumber(request.getMobileNumber());
				guardian.getUser().setContactNumber(request.getMobileNumber());
			} else {
				throw new ValidationException(String.format("Mobile Number (%s) already belongs to some other user.",
						request.getMobileNumber()));
			}
		}

		if (request.getEmail() != null) {

			if (!userRepository.existsByEmailAndCidNot(request.getEmail(), guardian.getUser().getCid())) {
				guardian.setEmail(request.getEmail());
				guardian.getUser().setEmail(request.getEmail());
			} else {
				throw new ValidationException(
						String.format("Email (%s) already belongs to some other user.", request.getEmail()));
			}
		}

		if (request.getStudentIds() != null) {
			List<Student> students = studentRepository.findAllDistinctByGuardiansCidAndActive(cid, true);
			students = students == null ? new ArrayList<>() : students;
			List<String> studentIds = students.stream().distinct().map(s -> s.getCid()).collect(Collectors.toList());
			List<String> requestedStudentIds = new ArrayList<>(request.getStudentIds());
			List<String> newStudents = request.getStudentIds();
			newStudents.removeAll(studentIds);

			setStudentsOfGuardian(guardian, students, newStudents);

			studentIds.removeAll(requestedStudentIds);
			for (String studentId : studentIds) {
				students.removeIf(s -> s.getCid().equals(studentId));
			}

			guardian.setStudents(students);
		}
		return new GuardianResponse(guardian = guardianRepository.save(guardian));
	}

	private void setStudentsOfGuardian(Guardian guardian, List<Student> students, List<String> studentIds) {
		for (String studentCid : studentIds) {
			Student student = studentRepository.findByCidAndActiveTrue(studentCid);
			if (student == null)
				throw new ValidationException(String.format("Student with id (%s) does not exist.", studentCid));
			if (student.getGuardians() != null)
				student.getGuardians().add(guardian);
			else
				student.setGuardians(new ArrayList<Guardian>(Arrays.asList(guardian)));
			students.add(student);
		}
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_GUARDIAN_FETCH)
	public GuardianResponse getById(String id) {
		if (id == null || !guardianRepository.existsByCidAndActiveTrue(id))
			throw new ValidationException(String.format("No guardian found with id (%s).", id));
		GuardianResponse guardian = new GuardianResponse(guardianRepository.findByCidAndActiveTrue(id));
		guardian.setStudentIds(studentRepository.findCidByGuardianCid(id));
		return guardian;
	}

	@Override
	@Secured(AuthorityUtils.SCHOOL_STUDENT_FETCH)
	public List<StudentResponse> getAllChildrenOfGuardian(String guardianId) {
		if (getUser().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Guardian")))
			guardianId = guardianRepository.findCidByUserIdAndActiveTrue(getUserId());
		if (guardianId == null || !guardianRepository.existsByCidAndActiveTrue(guardianId))
			throw new ValidationException(String.format("Invalid id (%s) for the guardian", guardianId));

		List<Student> students = studentRepository.findAllDistinctByGuardiansCidAndActive(guardianId, true);

		if (students == null || students.isEmpty())
			throw new NotFoundException(String.format("No children found for the guardian (%s).", guardianId));
		return students.stream().distinct().map(StudentResponse::new).collect(Collectors.toList());
	}
}
