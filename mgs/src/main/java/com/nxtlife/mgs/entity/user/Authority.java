package com.nxtlife.mgs.entity.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import com.nxtlife.mgs.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "authority", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class Authority extends BaseEntity implements GrantedAuthority {

	@NotEmpty(message = "name can't be empty")
	private String name;

	@NotEmpty(message = "group name can't be empty")
	private String groupName;

	private String description;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "authority")
	private List<RoleAuthority> authorityRoles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<RoleAuthority> getAuthorityRoles() {
		return authorityRoles;
	}

	public void setAuthorityRoles(List<RoleAuthority> authorityRoles) {
		this.authorityRoles = authorityRoles;
	}

	@Override
	public String getAuthority() {
		return String.format("ROLE_%s", getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorityRoles == null) ? 0 : authorityRoles.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authority other = (Authority) obj;
		if (authorityRoles == null) {
			if (other.authorityRoles != null)
				return false;
		} else if (!authorityRoles.equals(other.authorityRoles))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
