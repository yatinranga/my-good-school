//package com.nxtlife.mgs.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AppProperties {
//
//	@Autowired
//	private Environment env;
//	
//	public String getTokenSecret()
//	{
//		return env.getProperty("spring.mgs.tokenSecret");
//	}
//	
//	public String getExpirationTime()
//	{
//		return env.getProperty("spring.mgs.expirationTime");
//	}
//	
//	public String getPasswordResetExpirationTime() {
//		return env.getProperty("spring.mgs.passwordResetExpirationTime");
//	}
//	
//	public String getTokenPrefix() {
//		return env.getProperty("spring.mgs.tokenPrefix");
//	}
//	
//	public String getHeaderString() {
//		return env.getProperty("spring.mgs.headerString");
//	}
//	
//	public String getSignUpUrl() {
//		return env.getProperty("spring.mgs.signUpUrl");
//	}
//
//
//	
////	public String getPasswordResetRequestUrl()
////	{
////		return env.getProperty("spring.mgs.passwordResetRequestUrl");
////	}
//}
