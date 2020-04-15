package com.nxtlife.mgs.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nxtlife.mgs.ex.ValidationException;

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
	
	// Function to check String for only Alphabets 
    public boolean isStringOnlyAlphabet(String str) 
    { 
        return ((str != null) 
                && (!str.equals("")) 
                && (str.matches("^[a-zA-z]+([\\s][a-zA-Z]+)*$")));
//        /^[a-zA-Z ]*$/
    }
    
    public boolean emailValidator(String email) {
    	
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                                "[a-zA-Z0-9_+&*-]+)*@" + 
                                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                                "A-Z]{2,7}$"; 
                                  
            Pattern pat = Pattern.compile(emailRegex); 
            if (email == null) 
                return false; 
            return pat.matcher(email).matches(); 
        
	}
    
    public void validateMobileNumber(String mobile) {
    	if(!isValidMobileNumber(mobile))
    		throw new ValidationException(String.format("Mobile number (%s) is in invalid format, it should be a numeric string with 10 characters only.", mobile));
    }
    
    public void validateEmail(String email) {
    	if(!emailValidator(email))
    		throw new ValidationException(String.format("Email (%s) is in invalid format.", email));
    }
    
    

}
