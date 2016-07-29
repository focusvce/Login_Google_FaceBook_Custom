package com.hybrid.freeopensourceusers.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    //
    //private static final String IPADDRESS = "http://www.focusvce.com/api/v1/";
    private static final String IPADDRESS = "http://focusvce.com/api/v1/";

    public static String getIPADDRESS() {
        return IPADDRESS;
    }

    //Email Pattern
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }
}

