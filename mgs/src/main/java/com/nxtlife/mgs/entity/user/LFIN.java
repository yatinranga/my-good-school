package com.nxtlife.mgs.entity.user;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class LFIN extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cid;
	
	private String username;
	
	private Date dob;
	
	private String imageUrl;
	
	@NotNull
	@Column(unique = true)
	private String email;
	
	@Column(unique = true)
	private String contactNumber;
	
	private Boolean active;
	
	private String gender;
	
	@OneToOne(cascade = CascadeType.ALL)
	User user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LFIN(@NotNull String name, @NotNull String cid, String username, Date dob, String imageUrl,
			@NotNull String email, String mobileNumber, Boolean active, String gender) {
		super();
		this.name = name;
		this.cid = cid;
		this.username = username;
		this.dob = dob;
		this.imageUrl = imageUrl;
		this.email = email;
		this.contactNumber = mobileNumber;
		this.active = active;
		this.gender = gender;
	}

	public LFIN() {
		
	}

	
}
