package com.nxtlife.mgs.enums;

public enum FourS {

	Skill,
	Sport,
	Study,
	Service;
	
	public static boolean matches(String fourS)
	  {
	    for (FourS fs : FourS.values())
	    {
	      if (fs.name().equals(fourS))
	      {
	        return true;
	      }
	    }
	    return false;
	  }
}
