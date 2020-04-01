package com.nxtlife.mgs.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {

	public int countWords(String sentence) {
		if (sentence == null || sentence.isEmpty()) {
			return 0;
		}
		String[] words = sentence.split("\\s+");
		return words.length;
	}
	
	public  boolean isValidMobileNumber(String s) 
    { 
        // The given argument to compile("(0/91)?[7-9][0-9]{9}") method  
        // is regular expression. With the help of  
        // regular expression we can validate mobile 
        // number.  
        // 1) Begins with 0 or 91 
        // 2) Then contains 7 or 8 or 9. 
        // 3) Then contains 9 digits 
        Pattern p = Pattern.compile("[7-9][0-9]{9}"); 
  
        // Pattern class contains matcher() method 
        // to find matching between given number  
        // and regular expression 
        Matcher m = p.matcher(s); 
        return (m.find() && m.group().equals(s)); 
    }

}
