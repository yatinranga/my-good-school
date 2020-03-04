package com.nxtlife.mgs.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractAuditable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nxtlife.mgs.entity.user.User;
import com.nxtlife.mgs.service.BaseService;

@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity extends AbstractAuditable<User, Long>
{
	
//	public User getUser()
//	  {
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    if (authentication == null || !authentication.isAuthenticated())
//	    {
//	      return null;
//	    }
//
//	    if (authentication.getPrincipal() instanceof User)
//	    {
//	      return ((User) authentication.getPrincipal()).getUser();
//	    }
//	    return null;
//	  }


	@PrePersist
	  private void preCreate()
	  {
	    this.setCreatedDate(DateTime.now());

	    User current = BaseService.getUser();
	    if (current != null)
	    {
	      this.setCreatedBy(current);
	    }
	  }

	  @PreUpdate
	  private void preUpdate()
	  {
	    this.setLastModifiedDate(DateTime.now());
	    User current = BaseService.getUser();
	    if (current != null)
	    {
	      this.setLastModifiedBy(current);
	    }

	  }

	  public void setId(Long Id)
	  {
	    super.setId(Id);
	  }

}
