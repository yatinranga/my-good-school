package com.nxtlife.mgs.entity.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.userdetails.UserDetails;
import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.view.user.security.RoleResponse;

@SuppressWarnings("serial")
@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username", "school_id" }) })
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class User extends BaseEntity implements UserDetails, Serializable {

	@NotNull
	@Column(unique = true)
	private String cid;
	
	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "username can't be null")
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	private String username;

	@NotNull(message = "password can't be null")
	private String password;

	private String generatedPassword;
	
	@Transient
	private String rawPassword;

	private String email;

	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Contact no should contain only digit")
	private String contactNumber;

	private String notificationToken;

	private String picUrl;

	@Column(columnDefinition = "boolean default false")
	private boolean accountExpired = false;

	@Column(columnDefinition = "boolean default false")
	private boolean accountLocked = false;

	@Column(columnDefinition = "boolean default false")
	private boolean credentialsExpired = false;

	@Column(columnDefinition = "boolean default false")
	private boolean enabled = true;

	@ManyToOne
	private School school;

	@Transient
	private Long tschoolId;
	
	@Transient
	private String tschoolCid;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<UserRole> userRoles;

	@Transient
	private Collection<Authority> authorities;

	@Transient
	private Collection<Role> roles;

	@Transient
	private Long userId;

	public User() {

	}

	public User(
			@NotNull @Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20") String username,
			@NotNull String password, Collection<Authority> authorities) {
		super();
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getNotificationToken() {
		return notificationToken;
	}

	public void setNotificationToken(String notificationToken) {
		this.notificationToken = notificationToken;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !isAccountExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !isAccountLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !isCredentialsExpired();
	}

	public String getGeneratedPassword() {
		return generatedPassword;
	}

	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long gettSchoolId() {
		return tschoolId;
	}

	public void settSchoolId(Long schoolId) {
		if (schoolId != null) {
			this.school = this.school == null ? new School() : this.school;
			this.school.setId(schoolId);
		}
		this.tschoolId = schoolId;
	}
	
	public String gettSchoolCid() {
		return tschoolCid;
	}
	
	public void settSchoolCid(String schoolCid) {
		if (schoolCid != null) {
			this.school = this.school == null ? new School() : this.school;
			this.school.setId(this.gettSchoolId());
			this.school.setCid(schoolCid);
		}
		this.tschoolCid = schoolCid;
	}
	

	public Collection<Role> getRoles() {
		return roles = roles == null || roles.isEmpty() ? this.getUserRoles().stream().map(ur -> ur.getRole()).collect(Collectors.toSet()) : this.roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public String getRawPassword() {
		return rawPassword;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

}
