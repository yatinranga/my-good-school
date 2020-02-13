package com.nxtlife.mgs.enums;

public enum UserType
{
  Student,
  Parent,
  Teacher,
  SchoolManagement,
  School,
  LFIN,
  Coach,
  Nxtlife,
  Admin;
	

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
