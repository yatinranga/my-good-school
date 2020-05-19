package com.nxtlife.mgs.view.user;

import com.nxtlife.mgs.view.SchoolResponse;

public interface UserProjection {
	
	public String getUsername();
	public String getEmail();
	public String getContactNumber();
	public String getPicUrl();
	public SchoolResponse getSchool();

}
