package com.nxtlife.mgs.security;

import com.nxtlife.mgs.SpringApplicationContext;

public class SecurityConstants {
	
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/users/password-reset";
    
    public static String getTokenSecret()
    {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
    
    public static String getExpirationTime()
	{
    	AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getExpirationTime();
	}
	
	public static String getPasswordResetExpirationTime() {

		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getPasswordResetExpirationTime();
	}
	
	public static String getTokenPrefix() {

		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenPrefix();
	}
	
	public static String getHeaderString() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getHeaderString();
	}
	
	public static String getSignUpUrl() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getSignUpUrl();
	}

}