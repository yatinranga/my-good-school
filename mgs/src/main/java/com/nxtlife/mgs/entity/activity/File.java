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
import com.nxtlife.mgs.entity.session.Event;

@Entity
public class File extends BaseEntity{
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	Long id;
	
	private String name;
	
	@NotNull
	@Column(unique = true)
	private String cid;
	
	@NotNull
	@Column(unique = true)
	private String url;
	
	private Boolean active;
	
	@NotNull
	private String extension;
	
	@ManyToOne
	private ActivityPerformed activityPerformed;
	
	@ManyToOne
	private Event event;

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
	
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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
