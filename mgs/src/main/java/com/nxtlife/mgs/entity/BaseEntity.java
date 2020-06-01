package com.nxtlife.mgs.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractAuditable;

import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.service.BaseService;
import com.nxtlife.mgs.util.DateUtil;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity extends AbstractAuditable<User, Long> implements Serializable {

	@NotNull
	@Column(name = "active", columnDefinition = "boolean default true")
	private Boolean active = true;
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@PrePersist
	private void preCreate() {
		this.setCreatedDate(LocalDateTime.now(DateUtil.defaultTimeZone.toZoneId()));
		this.setActive(true);
		User current = BaseService.getUser();
		if (current != null) {
			this.setCreatedBy(current);
		}
	}

	@PreUpdate
	private void preUpdate() {
		this.setLastModifiedDate(LocalDateTime.now(DateUtil.defaultTimeZone.toZoneId()));
		User current = BaseService.getUser();
		if (current != null) {
			this.setLastModifiedBy(current);
		}

	}

	public void setId(Long Id) {
		super.setId(Id);
	}

}
