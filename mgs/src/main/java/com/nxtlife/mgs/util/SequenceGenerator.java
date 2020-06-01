//package com.nxtlife.mgs.util;
//
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.UniqueConstraint;
//import javax.validation.constraints.NotNull;
//
//import com.nxtlife.mgs.enums.UserType;
////import com.sun.istack.NotNull;
//
//@Entity
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userType", "sequence"})})
//public class SequenceGenerator {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//	
//	@NotNull
//	@Enumerated(EnumType.STRING)
//	UserType userType;
//	
//	@NotNull
//	private Long sequence;
//
//	public Long getId() {
//		return id;
//	}
//
//	public UserType getUserType() {
//		return userType;
//	}
//
//	public void setUserType(UserType userType) {
//		this.userType = userType;
//	}
//
//	public Long getSequence() {
//		return sequence;
//	}
//
//	public void setSequence(Long sequence) {
//		this.sequence = sequence;
//	}
//
//	public SequenceGenerator(UserType userType, Long sequence) {
//		this.userType = userType;
//		this.sequence = sequence;
//	}
//	
//	public SequenceGenerator() {
//		
//	}
//	
//}
