package com.nxtlife.mgs.entity.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.enums.FourS;

@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title","description","student_id"}))
public class Certificate extends BaseEntity{

	@NotNull
	@Column(unique = true)
	private String cid;
	
	@NotNull
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private FourS fourS;
	
	@NotNull
	private String certificationAuthority;
	
	@NotNull
	private String imageUrl;
	
	@NotNull
	@ManyToOne
//	@Column(name = "student_id")
	Student student;
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FourS getFourS() {
		return fourS;
	}

	public void setFourS(FourS fourS) {
		this.fourS = fourS;
	}

	public String getCertificationAuthority() {
		return certificationAuthority;
	}

	public void setCertificationAuthority(String certificationAuthority) {
		this.certificationAuthority = certificationAuthority;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Certificate(String title, String description, FourS fourS, String certificationAuthority, String imageUrl) {
		this.title = title;
		this.description = description;
		this.fourS = fourS;
		this.certificationAuthority = certificationAuthority;
		this.imageUrl = imageUrl;
	}
	
	public Certificate() {
		
	}
	
}
