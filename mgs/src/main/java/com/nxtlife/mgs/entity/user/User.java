package com.nxtlife.mgs.entity.user;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
//import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.enums.RegisterType;
import com.nxtlife.mgs.enums.UserType;

@SuppressWarnings("serial")
@Entity
@DynamicInsert
@DynamicUpdate(true)
public class User extends BaseEntity implements UserDetails {

	@Column(unique = true, nullable = false)
	private String cid;

	@Transient
	private Long userId;

	@Column(nullable = false, unique = true)
	private String userName;

	@Column(unique = true,nullable = true ,columnDefinition = "VARCHAR(10)")
//	@Size(min = 10, max = 10)
//	@Pattern(regexp = "^[6-9]{1}[0-9]]{9}$")
	private String mobileNo;

//	@Email
	@Column(unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column
	private String picUrl;

	@Column
	private Boolean active;

	@Column
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@Column
	private String imagePath;

	@Transient
	private Collection<Authority> authorities;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role roleForUser;

	private Boolean isPaid = false;

	@Enumerated(EnumType.STRING)
	private RegisterType registerType;

	@OneToOne(cascade = CascadeType.ALL)
	private Student student;

	@OneToOne(cascade = CascadeType.ALL)
	private School school;

	@OneToOne(cascade = CascadeType.ALL)
	private Teacher teacher;

	@OneToOne(cascade = CascadeType.ALL)
	private LFIN lfin;

	@OneToOne(cascade = CascadeType.ALL)
	private Guardian guardian;

	@PrePersist
	public void prePersist() {
		this.setCreatedDate(DateTime.now());

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = this.getId();
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRoleForUser().getAuthorities();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Role getRoleForUser() {
		return roleForUser;
	}

	public void setRoleForUser(Role roleForUser) {
		this.roleForUser = roleForUser;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public RegisterType getRegisterType() {
		return registerType;
	}

	public void setRegisterType(RegisterType registerType) {
		this.registerType = registerType;
	}

	public String getUserName() {
		return userName;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public LFIN getLfin() {
		return lfin;
	}

	public void setLfin(LFIN lfin) {
		this.lfin = lfin;
	}

	public Guardian getGuardian() {
		return guardian;
	}

	public void setGuardian(Guardian guardian) {
		this.guardian = guardian;
	}

	public User(String cid, String userName,
			@Size(min = 10, max = 10) @Pattern(regexp = "^[6-9]{1}[0-9]]{9}$") String mobileNo, @Email String email,
			String password, String picUrl, Boolean active, UserType userType, String imagePath,
			Collection<Authority> authorities, Role roleForUser, Boolean isPaid, RegisterType registerType,
			Student student, School school, Teacher teacher, LFIN lfin) {
		this.cid = cid;
		this.userName = userName;
		this.mobileNo = mobileNo;
		this.email = email;
		this.password = password;
		this.picUrl = picUrl;
		this.active = active;
		this.userType = userType;
		this.imagePath = imagePath;
		this.authorities = authorities;
		this.roleForUser = roleForUser;
		this.isPaid = isPaid;
		this.registerType = registerType;
		this.student = student;
		this.school = school;
		this.teacher = teacher;
		this.lfin = lfin;
	}

	public User() {

	}

}
