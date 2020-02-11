package com.nxtlife.mgs.entity.user;

import javax.persistence.*;

@Entity
@Table(name = "role_authority")
public class RoleAuthority {

    @EmbeddedId
    private RoleAuthorityKey roleAuthorityKey;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name ="role_id")
    private Role role;

    @ManyToOne
    @MapsId("authorityId")
    @JoinColumn(name ="authority_id")
    private Authority authority;
}
