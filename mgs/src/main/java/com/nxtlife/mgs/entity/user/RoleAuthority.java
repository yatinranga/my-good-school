package com.nxtlife.mgs.entity.user;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.nxtlife.mgs.entity.common.RoleAuthorityKey;

@SuppressWarnings("serial")
@Entity
@Table(name = "role_authority")
public class RoleAuthority implements Serializable {

	@EmbeddedId
	private RoleAuthorityKey roleAuthorityKey;

	@MapsId("roleId")
	@ManyToOne
	private Role role;

	@MapsId("authorityId")
	@ManyToOne
	private Authority authority;

	public RoleAuthority() {
		super();
	}

	public RoleAuthority(RoleAuthorityKey roleAuthorityKey, Role role, Authority authority) {
		super();
		this.roleAuthorityKey = roleAuthorityKey;
		this.role = role;
		this.authority = authority;
	}

	public RoleAuthority(Long roleId, Long authorityId) {
		super();
		this.roleAuthorityKey = new RoleAuthorityKey(roleId, authorityId);
		if (roleId != null) {
			this.role = new Role();
			this.role.setId(roleId);
		}
		if (authorityId != null) {
			this.authority = new Authority();
			this.authority.setId(authorityId);
		}
	}

	public RoleAuthorityKey getRoleAuthorityKey() {
		return roleAuthorityKey;
	}

	public void setRoleAuthorityKey(RoleAuthorityKey roleAuthorityKey) {
		this.roleAuthorityKey = roleAuthorityKey;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((roleAuthorityKey == null) ? 0 : roleAuthorityKey.hashCode());
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
		RoleAuthority other = (RoleAuthority) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (roleAuthorityKey == null) {
			if (other.roleAuthorityKey != null)
				return false;
		} else if (!roleAuthorityKey.equals(other.roleAuthorityKey))
			return false;
		return true;
	}

}
