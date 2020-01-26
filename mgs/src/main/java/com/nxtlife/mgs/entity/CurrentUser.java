//package com.nxtlife.mgs.entity;
//
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//
//public class CurrentUser extends User
//{
//
//  private com.nxtlife.mgs.entity.user.User user;
//
//  public CurrentUser(com.nxtlife.mgs.entity.user.User user, String[] roles)
//  {
//    super(user.getUsername(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(roles));
//    this.user = user;
//  }
//
//  public com.nxtlife.mgs.entity.user.User getUser()
//  {
//    return user;
//  }
//
//  public void setUser(com.nxtlife.mgs.entity.user.User user)
//  {
//    this.user = user;
//  }
//
//}
