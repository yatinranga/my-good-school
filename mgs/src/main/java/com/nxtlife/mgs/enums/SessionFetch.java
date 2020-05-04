package com.nxtlife.mgs.enums;

public enum SessionFetch {

	today,week,month;
	
//	private final String fetch;
	public static boolean matches(String sessionFetch)
	  {
	    for (SessionFetch sf : SessionFetch.values())
	    {
	      if (sf.name().equals(sessionFetch))
	      {
	        return true;
	      }
	    }
	    return false;
	  }
	
//	public static SessionFetch fromString(String sessionFetch) {
//		  for (SessionFetch sf : SessionFetch.values())
//		    {
//		      if (sf.getFetch().equals(sessionFetch))
//		      {
//		        return sf;
//		      }
//		    }
//		    return null;
//	  }
//	
//	  private SessionFetch(String fetch)
//	  {
//	    this.fetch = fetch;
//	  }
//
//	  public String getFetch()
//	  {
//	    return this.fetch;
//	  }
}
