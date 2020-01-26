package com.nxtlife.mgs.entity;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.jpa.domain.AbstractAuditable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

import com.nxtlife.mgs.entity.user.User;

@MappedSuperclass
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
//	    if (authentication.getPrincipal() instanceof CurrentUser)
//	    {
//	      return ((CurrentUser) authentication.getPrincipal()).getUser();
//	    }
//	    return null;
//	  }

	@PrePersist
	  private void preCreate()
	  {
	    this.setCreatedDate(LocalDateTime.now());

//	    User current = getUser();
//	    if (current != null)
//	    {
//	      this.setCreatedBy(current);
//	    }
	  }

	  @PreUpdate
	  private void preUpdate()
	  {
	    this.setLastModifiedDate(LocalDateTime.now());
//	    User current = getUser();
//	    if (current != null)
//	    {
//	      this.setLastModifiedBy(current);
//	    }

	  }

	  public void setId(Long Id)
	  {
	    super.setId(Id);
	  }

}
