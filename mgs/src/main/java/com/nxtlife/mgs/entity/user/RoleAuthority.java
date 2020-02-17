//package com.nxtlife.mgs.entity.user;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "role_authority")
//public class RoleAuthority {
//
//    @EmbeddedId
//    private RoleAuthorityKey roleAuthorityKey;
//
//    @ManyToOne
//    @MapsId("roleId")
//    private Role role;
//
//    @ManyToOne
//    @MapsId("authorityId")
//    private Authority authority;
//
//	public RoleAuthorityKey getRoleAuthorityKey() {
//		return roleAuthorityKey;
//	}
//
//	public void setRoleAuthorityKey(RoleAuthorityKey roleAuthorityKey) {
//		this.roleAuthorityKey = roleAuthorityKey;
//	}
//
//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
//
//	public Authority getAuthority() {
//		return authority;
//	}
//
//	public void setAuthority(Authority authority) {
//		this.authority = authority;
//	}
//
//	public RoleAuthority(Role role, Authority authority) {
//		this.role = role;
//		this.authority = authority;
//	}
//
//	public RoleAuthority(RoleAuthorityKey roleAuthorityKey, Role role, Authority authority) {
//		this.roleAuthorityKey = roleAuthorityKey;
//		this.role = role;
//		this.authority = authority;
//	}
//	
//	public RoleAuthority() {
//		
//	}
//    
//    
//}
