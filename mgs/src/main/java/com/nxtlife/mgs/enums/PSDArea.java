package com.nxtlife.mgs.enums;

public enum PSDArea {
	PersonalDevelopment("Personal Development"),
	SocialDevelopment("Social Development");
	
	  private final String psdArea;

	  public static boolean matches(String psdArea)
	  {
	    for (PSDArea pa : PSDArea.values())
	    {
	      if (pa.getPsdArea().equalsIgnoreCase(psdArea))
	      {
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  public static PSDArea fromString(String psdArea)
	  {
	    for (PSDArea pa : PSDArea.values())
	    {
	      if (pa.getPsdArea().equalsIgnoreCase(psdArea))
	      {
	        return pa;
	      }
	    }
	    return null;
	  }

	  private PSDArea(String psdArea)
	  {
	    this.psdArea = psdArea;
	  }

	  public String getPsdArea()
	  {
	    return this.psdArea;
	  }

}
