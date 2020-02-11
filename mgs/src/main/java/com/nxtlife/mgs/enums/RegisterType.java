package com.nxtlife.mgs.enums;

public enum RegisterType
{
  GOOGLE("Google"), // In case further required for Oauth2 i.e third party verification on signup
  FACEBOOK("Facebook"), // In case further required for Oauth2 i.e third party verification on signup
  SELF("Self"), // Self sign up
  MANUALLY("Manually"); // In case of bulk sign up by school

  private final String type;

  public static boolean matches(String registerType)
  {
    for (RegisterType rt : RegisterType.values())
    {
      if (rt.getType().equals(registerType))
      {
        return true;
      }
    }
    return false;
  }
  
  public static RegisterType fromString(String registerType) {
	  for (RegisterType rt : RegisterType.values())
	    {
	      if (rt.getType().equals(registerType))
	      {
	        return rt;
	      }
	    }
	    return null;
  }
  private RegisterType(String type)
  {
    this.type = type;
  }

  public String getType()
  {
    return this.type;
  }
}