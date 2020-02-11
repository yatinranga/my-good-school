package com.nxtlife.mgs.entity.user;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RoleAuthorityKey implements Serializable {

    private Long roleId;

    private Long authorityId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleAuthorityKey)) return false;
        RoleAuthorityKey that = (RoleAuthorityKey) o;
        return getRoleId().equals(that.getRoleId()) &&
                getAuthorityId().equals(that.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getAuthorityId());
    }
}
