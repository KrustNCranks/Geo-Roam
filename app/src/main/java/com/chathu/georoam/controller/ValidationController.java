package com.chathu.georoam.controller;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationController {
    private static ValidationController myInstance = new ValidationController();

    public static ValidationController getInstance() {
        if(myInstance == null){
            return new ValidationController();
        }
        return myInstance;
    }

    private ValidationController(){

    }

    /**
     * Validates a given email address
     *
     * @param email
     * @return boolean
     */
    public boolean isEmail(String email)
    {

        String regex = "^[\\w-\\+]+(\\.[\\\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * Check if the given text field is empty
     *
     * @param text
     * @return
     */
    public boolean isEmpty(String text)
    {
        return text.isEmpty();
    }

    /**
     * Validate the Two Fields
     *
     * @param pass field 1
     * @param pass2 field 2
     * @return boolean
     */
    public boolean confirm(String pass, String pass2)
    {
        return pass.equals(pass2);
    }

}
