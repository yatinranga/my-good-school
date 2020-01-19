package com.nxtlife.mgs.enums;

public enum UserType
{
  Student,
  Parent,
  Teacher,
  SchoolManagement,
  LFIN,
  Coach,
  Nxtlife;

  public static boolean matches(String userType)
  {
    for (UserType ut : UserType.values())
    {
      if (ut.name().equals(userType))
      {
        return true;
      }
    }
    return false;
  }
}
