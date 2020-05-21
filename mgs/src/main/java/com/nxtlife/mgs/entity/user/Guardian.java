package com.nxtlife.mgs.entity.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.engine.FetchTiming;
import org.hibernate.validator.constraints.Email;

import com.nxtlife.mgs.entity.BaseEntity;

@Entity
@DynamicUpdate(true)
public class Guardian extends BaseEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	Long id;

	@NotNull
	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	@NotNull
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	@Column(unique = true)
	private String username;

//	private Date dob;

	private String imageUrl;

	@Email
	@Column(unique = true)
	private String email;

	@Size(min = 10, max = 10)
	@Pattern(regexp = "^[0-9]*$", message = "Mobile no. should contain only digit")
	@Column(unique = true)
	private String mobileNumber;

	private Boolean active;

	private String gender;

	private String relationship;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	User user;

	/*
	 * @ManyToOne(cascade = CascadeType.PERSIST) private Student student;
	 */

	@ManyToMany()
	@JoinTable(name = "student_guardian", joinColumns = { @JoinColumn(name = "guardian_id") }, inverseJoinColumns = {
			@JoinColumn(name = "student_id") })
	private List<Student> students;

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

//	public Date getDob() {
//		return dob;
//	}
//
//	public void setDob(Date dob) {
//		this.dob = dob;
//	}

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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Guardian(@NotNull String name, @NotNull String cid, String imageUrl, String email, String mobileNumber,
			Boolean active, String gender, User user, List<Student> students) {
		this.name = name;
		this.cid = cid;
		this.imageUrl = imageUrl;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.active = active;
		this.gender = gender;
		this.user = user;
		this.students = students;
	}

	public Guardian() {

	}

}
