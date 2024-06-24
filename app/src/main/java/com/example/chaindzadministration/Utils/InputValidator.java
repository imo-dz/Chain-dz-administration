package com.example.chaindzadministration.Utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    private static final String PHONE_NUMBER_PATTERN = "^(07|06|05)\\d{8}$";
    public static boolean isEmpty(String input)
    {
        if(TextUtils.isEmpty(input) || input==null)
            return true;
        return false;
    }
    public static boolean isEmail(String email)
    {

        if(TextUtils.isEmpty(email) || email==null)
        {
            return false;
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);

        if(!m.matches())
        {
            return false;
        }
        return true;
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber) || phoneNumber==null)
        {
            return false;
        }
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}

