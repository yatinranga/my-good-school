package com.nxtlife.mgs.auth;


	public final class MgsAuth {
	    private MgsAuth() {
	        throw new AssertionError("Not allowed");
	    }

	    public static final class Authorities {
//	    	public static final String PROMO_CODE = "ROLE_PROMO_CODE";//for creating Promo Code
	    	public static final String LFIN_WRITE_PRIVILEGE = "ROLE_LFIN_WRITE_PRIVILEGE";
	    	public static final String TRANSACTION_READ_PRIVILEGE ="ROLE_TRANSACTION_READ_PRIVILEGE";
	    	public static final String SELF_READ = "ROLE_SELF_READ";
	        public static final String SELF_WRITE = "ROLE_SELF_WRITE";

	        public static final String READ_ROLE = "ROLE_READ_ROLE";
	        public static final String WRITE_ROLE = "ROLE_WRITE_ROLE";

	        public static final String READ_PRIVILEGE = "ROLE_READ_PRIVILEGE";

	        public static final String READ_USER = "ROLE_READ_USER";
	        public static final String WRITE_USER = "ROLE_WRITE_USER";

	       	public static final String READ_SCHOOL = "ROLE_READ_SCHOOL";
	        public static final String WRITE_SCHOOL = "ROLE_WRITE_SCHOOL";

	       	        public static final String READ_SUBSCRIPTION_KEY = "ROLE_READ_SUBSCRIPTION_KEY";
	        public static final String WRITE_SUBSCRIPTION_KEY = "ROLE_WRITE_SUBSCRIPTION_KEY";

	        public static final String WRITE_MEDIA = "ROLE_WRITE_MEDIA";

	        public static final String WRITE_REPORTS = "ROLE_WRITE_REPORTS";

	       	public static final String SHOW_STUDENT = "ROLE_SHOW_STUDENT";
	        public static final String READ_STUDENT = "ROLE_READ_STUDENT";
	        public static final String WRITE_STUDENT = "ROLE_WRITE_STUDENT";
	        public static final String SHOW_TEACHER = "ROLE_SHOW_TEACHER";
	        public static final String READ_TEACHER = "ROLE_READ_TEACHER";
	        public static final String WRITE_TEACHER = "ROLE_WRITE_TEACHER";
	        public static final String SHOW_SESSION = "ROLE_SHOW_SESSION";
	        public static final String READ_SESSION = "ROLE_READ_SESSION";
	        public static final String WRITE_SESSION = "ROLE_WRITE_SESSION";
	        public static final String SHOW_STUDENT_GRADE = "ROLE_SHOW_STUDENT_GRADE";
	        public static final String READ_STUDENT_GRADE = "ROLE_READ_STUDENT_GRADE";
	        public static final String WRITE_STUDENT_GRADE = "ROLE_WRITE_STUDENT_GRADE";
	        public static final String SHOW_TEACHER_GRADE = "ROLE_SHOW_TEACHER_GRADE";
	        public static final String READ_TEACHER_GRADE = "ROLE_READ_TEACHER_GRADE";
	        public static final String WRITE_TEACHER_GRADE = "ROLE_WRITE_TEACHER_GRADE";
	        public static final String READ_TEACHER_DASHBOARD = "ROLE_READ_TEACHER_DASHBOARD";
	        public static final String READ_PRINCIPAL_DASHBOARD = "ROLE_READ_PRINCIPAL_DASHBOARD";
	        public static final String READ_SCHOOL_DASHBOARD = "ROLE_READ_SCHOOL_DASHBOARD";
	        public static final String SHOW_COMMON_DASHBOARD = "ROLE_SHOW_COMMON_DASHBOARD";
	        public static final String READ_COMMON_DASHBOARD = "ROLE_READ_COMMON_DASHBOARD";
	        public static final String WRITE_COMMON_DASHBOARD = "ROLE_WRITE_COMMON_DASHBOARD";
	        public static final String WRITE_BULK = "ROLE_WRITE_BULK";
	        public static final String READ_STUDENT_REPORT = "ROLE_READ_STUDENT_REPORT";
	        public static final String READ_TEST_REPORT = "ROLE_READ_TEST_REPORT";
	        public static final String READ_REPORTS = "ROLE_READ_REPORTS";
	        public static final String READ_ATTENDANCE_SHEET = "ROLE_READ_ATTENDANCE_SHEET";
	        public static final String WRITE_SYNC_OFFLINE_DATA = "ROLE_WRITE_SYNC_OFFLINE_DATA";
	        public static final String LOGIN_PANEL = "ROLE_LOGIN_PANEL";

	    }

//	    public static final class Dashboard {
//	        public static final String TestTakers = "TestTakers";
//	        public static final String Users = "Users";
//	        public static final String Tests = "Tests";
//	    }
	}

