package com.nxtlife.mgs.entity.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;
import com.nxtlife.mgs.entity.session.Event;

@SuppressWarnings("serial")
@Entity
public class File extends BaseEntity {

	private String name;

	@NotNull
	@Column(unique = true)
	private String cid;

	@NotNull
	@Column(unique = true)
	private String url;

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
		this.setActive(active);
		this.extension = extension;
		this.activityPerformed = activityPerformed;
	}

	public File() {

	}

}
