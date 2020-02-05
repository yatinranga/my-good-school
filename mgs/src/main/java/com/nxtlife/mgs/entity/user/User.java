package com.nxtlife.mgs.entity.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.school.School;
import com.nxtlife.mgs.enums.RegisterType;
import com.nxtlife.mgs.enums.UserType;

@Entity
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	@Enumerated(EnumType.STRING)
	private RegisterType registerType;
	
	@NotNull
	@Column(unique=true)
	private String username;
	
	@NotNull
	@Column(unique=true)
	private String cid;
	
    private String passwordHash;
    
    private boolean active;
    
    private Boolean IsOtpVerified = false;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date otpCreatedDate;
    
    private Boolean isPaid = false;
    
    @OneToOne(mappedBy = "user")
    Student student;
    
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    Teacher teacher;
    
    @OneToOne
    SchoolManagementMember schoolManagementMember;
    
    @OneToOne
    School school;
    
    @OneToOne
    LFIN lfin;
    
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "user")
    Guardian guardian;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "USER_ID")}, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")})
    private List<Role> roles = new ArrayList<>();
	
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	public RegisterType getRegisterType() {
		return registerType;
	}
	public void setRegisterType(RegisterType registerType) {
		this.registerType = registerType;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Boolean getIsOtpVerified() {
		return IsOtpVerified;
	}
	public void setIsOtpVerified(Boolean isOtpVerified) {
		IsOtpVerified = isOtpVerified;
	}
	public Date getOtpCreatedDate() {
		return otpCreatedDate;
	}
	public void setOtpCreatedDate(Date otpCreatedDate) {
		this.otpCreatedDate = otpCreatedDate;
	}
	public Boolean getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public SchoolManagementMember getSchoolManagementMember() {
		return schoolManagementMember;
	}
	public void setSchoolManagementMember(SchoolManagementMember schoolManagementMember) {
		this.schoolManagementMember = schoolManagementMember;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public User(UserType userType, RegisterType registerType, @NotNull String username, @NotNull String cid,
			String passwordHash, boolean active, Boolean isOtpVerified, Date otpCreatedDate, Boolean isPaid,
			Student student, Teacher teacher, SchoolManagementMember schoolManagementMember, LFIN lfin,
			Guardian guardian, List<Role> roles) {
		this.userType = userType;
		this.registerType = registerType;
		this.username = username;
		this.cid = cid;
		this.passwordHash = passwordHash;
		this.active = active;
		this.IsOtpVerified = isOtpVerified;
		this.otpCreatedDate = otpCreatedDate;
		this.isPaid = isPaid;
		this.student = student;
		this.teacher = teacher;
		this.schoolManagementMember = schoolManagementMember;
		this.lfin = lfin;
		this.guardian = guardian;
		this.roles = roles;
	}
	
	public User() {
		
	}
    
    
}
