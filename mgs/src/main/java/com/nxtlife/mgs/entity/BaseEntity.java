package com.nxtlife.mgs.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.jpa.domain.AbstractAuditable;

import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.service.BaseService;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity extends AbstractAuditable<User, Long> implements Serializable {

	@PrePersist
	private void preCreate() {
		this.setCreatedDate(LocalDateTime.now());

		User current = BaseService.getUser();
		if (current != null) {
			this.setCreatedBy(current);
		}
	}

	@PreUpdate
	private void preUpdate() {
		this.setLastModifiedDate(LocalDateTime.now());
		User current = BaseService.getUser();
		if (current != null) {
			this.setLastModifiedBy(current);
		}

	}

	public void setId(Long Id) {
		super.setId(Id);
	}

}
