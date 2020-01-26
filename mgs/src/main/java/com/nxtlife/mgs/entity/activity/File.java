package com.nxtlife.mgs.entity.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;

@Entity
public class File extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cId;
	
	@NotNull
	@Column(unique = true)
	private String url;
	
	private Boolean active;
	
	@NotNull
	private String extension;
	
	@ManyToOne
	private ActivityPerformed activityPerformed;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public ActivityPerformed getActivityPerformed() {
		return activityPerformed;
	}

	public void setActivityPerformed(ActivityPerformed activityPerformed) {
		this.activityPerformed = activityPerformed;
	}
	
	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public File(String name, String url, Boolean active, String extension, ActivityPerformed activityPerformed) {
		this.name = name;
		this.url = url;
		this.active = active;
		this.extension = extension;
		this.activityPerformed = activityPerformed;
	} 
	
	public File() {
		
	}
	

}
