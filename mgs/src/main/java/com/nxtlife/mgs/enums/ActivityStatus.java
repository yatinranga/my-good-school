package com.nxtlife.mgs.enums;

public enum ActivityStatus {

	SavedByStudent,SubmittedByStudent,InReviewStage,Reviewed;
	
	public static boolean matches(String activityStatus)
	  {
	    for (ActivityStatus as : ActivityStatus.values())
	    {
	      if (as.name().equals(activityStatus))
	      {
	        return true;
	      }
	    }
	    return false;
	  }
}
