package com.nxtlife.mgs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.entity.user.Role;
import com.nxtlife.mgs.entity.user.RoleAuthority;
import com.nxtlife.mgs.ex.NotFoundException;
import com.nxtlife.mgs.ex.ValidationException;
import com.nxtlife.mgs.jpa.AuthorityRepository;
import com.nxtlife.mgs.jpa.RoleAuthorityRepository;
import com.nxtlife.mgs.jpa.RoleRepository;
import com.nxtlife.mgs.jpa.SchoolRepository;
import com.nxtlife.mgs.jpa.UserRoleRepository;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.service.RoleService;
import com.nxtlife.mgs.util.AuthorityUtils;
import com.nxtlife.mgs.view.SuccessResponse;
import com.nxtlife.mgs.view.user.security.RoleRequest;
import com.nxtlife.mgs.view.user.security.RoleResponse;

@Service("roleServiceImpl")
@Transactional
public class RoleServiceImpl extends BaseService implements RoleService {

	private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private RoleRepository roleDao;

	@Autowired
	private RoleAuthorityRepository roleAuthorityJpaDao;

	@Autowired
	private AuthorityRepository authorityDao;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private UserRoleRepository userRoleJpaDao;

	private void validateAuthorityIds(Set<Long> requestAuthorityIds) {
		List<Long> authorityIds = authorityDao.findAllIds();
		requestAuthorityIds.removeAll(authorityIds);
		if (!requestAuthorityIds.isEmpty()) {
			throw new ValidationException(
					String.format("Some of the authorities (%s) are not valid", requestAuthorityIds));
		}
	}

	/**
	 * this method used to validate request
	 * 
	 * @param request
	 * @param schoolId
	 */
	private void validateRequest(RoleRequest request, Long schoolId) {
		validateAuthorityIds(request.getAuthorityIds());
		if (roleDao.existsRoleByNameAndSchoolId(request.getName(), schoolId)) {
			throw new ValidationException(
					String.format("This role (%s) is already exists for this organization", request.getName()));
		}
	}

	@Secured(AuthorityUtils.ROLE_FETCH)
	@Override
	public List<RoleResponse> getAllRoles() {
		Long schoolId = getUser().gettSchoolId();
		if (schoolRepository.findResponseById(schoolId) == null) {
			throw new ValidationException("Organization not found");
		}
		List<RoleResponse> roles = roleDao.findBySchoolId(schoolId);
		roles.stream().map(role -> {
			role.setAuthorities(authorityDao.findByAuthorityRolesRoleId(role.getId()));
			return role;
		}).collect(Collectors.toList());
		return roles;

	}

	@Secured(AuthorityUtils.ROLE_FETCH)
	@Override
	public RoleResponse findById(Long id) {
		RoleResponse role = roleDao.findById(id);
		if (role == null) {
			throw new NotFoundException(String.format("Role(%s) not found", id));
		}
		role.setAuthorities(authorityDao.findByAuthorityRolesRoleId(role.getId()));
		return role;
	}

	/**
	 * save the Role <tt>size()</tt>tells no of elements in the list
	 *
	 * <tt>toEntity()</tt> tranform the request into entity
	 *
	 * throw a validation exception when size is less than 1.
	 *
	 * <tt>getUser()</tt>return the user that will login at that instance
	 *
	 * <findById>return an object by particular id</findById>
	 *
	 *
	 * <saveAll>method save the list of objects in the database</saveAll>
	 * 
	 * @Param request call save method of jpa
	 * @return <tt>RoleResponse</tt>
	 */
	@Secured(AuthorityUtils.ROLE_CREATE)
	@Override
	public RoleResponse save(RoleRequest request) {
		Long schoolId = getUser().gettSchoolId();
		validateRequest(request, schoolId);
		Role role = request.toEntity();
		School school = new School();
		school.setId(schoolId);
		role.setSchool(school);
		roleDao.save(role);
		List<RoleAuthority> roleAuthorities = new ArrayList<>();
		for (Long authorityId : request.getAuthorityIds()) {
			if (authorityDao.findResponseById(authorityId) != null) {
				roleAuthorities.add(new RoleAuthority(role.getId(), authorityId));
			}
		}
		roleAuthorityJpaDao.save(roleAuthorities);
		RoleResponse roleResponse = roleDao.findById(role.getId());
		roleResponse.setAuthorities(authorityDao.findByAuthorityRolesRoleId(role.getId()));
		return roleResponse;
	}

	@Override
	@Secured(AuthorityUtils.ROLE_UPDATE)
	public RoleResponse update(Long id, RoleRequest request) {
		Long schoolId = getUser().gettSchoolId();
		RoleResponse role = roleDao.findById(id);
		if (role == null) {
			throw new NotFoundException(String.format("Role (%s) not found", id));
		}
		if (role.getName().equalsIgnoreCase("SuperAdmin")) {
			throw new ValidationException("Superadmin role can't be updated");
		}
		validateAuthorityIds(request.getAuthorityIds());
		Long existRoleId = roleDao.findIdByNameAndSchoolId(request.getName(), schoolId);
		if (existRoleId != null && !existRoleId.equals(id)) {
			throw new ValidationException("This role already exists for this organization");
		}
		List<Long> requestAuthorityIds = new ArrayList<>(request.getAuthorityIds());
		List<Long> roleAuthorityIds = roleAuthorityJpaDao.getAllAuthorityIdsByRoleId(id);
		List<RoleAuthority> roleAuthorities = new ArrayList<>();
		if (request.getName() != null && !role.getName().equals(request.getName())) {
			roleDao.updateName(request.getName(), id, getUserId(), new Date());
		}
		requestAuthorityIds.removeAll(roleAuthorityIds);
		for (Long authorityId : requestAuthorityIds) {
			roleAuthorities.add(new RoleAuthority(id, authorityId));
		}
		roleAuthorityIds.removeAll(request.getAuthorityIds());
		if (!roleAuthorities.isEmpty())
			roleAuthorityJpaDao.save(roleAuthorities);
		if (!roleAuthorityIds.isEmpty()) {
			roleAuthorityJpaDao.deleteByRoleIdAndAuthorityIds(id, roleAuthorityIds);
		}
		logger.info("Role {} updated successfully", request.getName());
		RoleResponse roleResponse = roleDao.findById(id);
		roleResponse.setAuthorities(authorityDao.findByAuthorityRolesRoleId(id));
		return roleResponse;

	}

	@Override
	@Secured(AuthorityUtils.ROLE_UPDATE)
	public SuccessResponse activate(Long id) {
		RoleResponse role = roleDao.findById(id);
		if (role == null) {
			throw new NotFoundException(String.format("Role (%s) not found", id));
		}
		int rows = roleDao.activate(id, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Role {} activated successfully", id);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Role activated successfully");
	}

	@Override
	@Secured(AuthorityUtils.ROLE_DELETE)
	public SuccessResponse delete(Long id) {
		RoleResponse role = roleDao.findById(id);
		if (role == null) {
			throw new NotFoundException(String.format("Role (%s) not found", id));
		}
		if (role.getName().equalsIgnoreCase("SuperAdmin")) {
			throw new ValidationException("Superadmin role can't be deleted");
		}
		Set<Long> userIds = userRoleJpaDao.findUserIdsByRoleId(id);
		if (userIds.isEmpty()) {
			int rows = roleDao.delete(id, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Role {} deleted successfully", id);
			}
		} else {
			throw new ValidationException("Some of the users are assigned with this role.");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Role deleted successfully");
	}

}
