package com.nxtlife.mgs.enums;

public enum AwardCriterion {

	PSDArea("PSD Area"),
	FocusArea("Focus Area"),
	FourS("4S"),
	ActivityType("Activity Type");
	
	  private final String awardCriterion;

	  public static boolean matches(String awardCriterion)
	  {
	    for (AwardCriterion ac : AwardCriterion.values())
	    {
	      if (ac.getAwardCriterion().equalsIgnoreCase(awardCriterion))
	      {
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  public static AwardCriterion fromString(String awardCriterion)
	  {
	    for (AwardCriterion ac : AwardCriterion.values())
	    {
	      if (ac.getAwardCriterion().equalsIgnoreCase(awardCriterion))
	      {
	        return ac;
	      }
	    }
	    return null;
	  }

	  private AwardCriterion(String awardCriterion)
	  {
	    this.awardCriterion = awardCriterion;
	  }

	  public String getAwardCriterion()
	  {
	    return this.awardCriterion;
	  }

}
