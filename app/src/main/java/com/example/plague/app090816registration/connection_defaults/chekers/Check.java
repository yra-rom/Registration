package com.example.plague.app090816registration.connection_defaults.chekers;

import android.util.Log;

import com.example.plague.app090816registration.connection_defaults.Constants.SendKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {
    public static final String TAG = "Check";

    private static Check instance = new Check();
    public static Check getInstance() {
        return instance;
    }

    private Check(){}

    public boolean checkEmail(String email){
        return checkEmailLocal(email) && checkEmailDB(email);
    }

    public boolean checkEmailLocal(String email){
        int l = email.length();
        if(l < 3 || l > 32){
            return false;
        }

        //checking with regex
        Pattern p = Pattern.compile(".+@.+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean checkEmailDB(String email) {
        HashMap map = new HashMap<>();
        map.put(SendKeys.TITLE, SendKeys.CHECK_MAIL);
        map.put(SendKeys.EMAIL, email);

        CheckThread checkThread = new CheckThread(map);
        checkThread.start();

        while(checkThread.getAnswer()==null); // potential dead loop!!!
        Log.d(TAG, "Server's answer is: " + checkThread.getAnswer());

        checkThread.close();
        checkThread.interrupt();

        return checkThread.getAnswer();
    }

    public boolean checkPassword(String email, String pass){
        return checkPasswordLocal(pass) && checkPasswordDB(email,pass);
    }

    public boolean checkPasswordDB(String email, String pass) {
        HashMap map = new HashMap<>();
        map.put(SendKeys.TITLE, SendKeys.CHECK_PASS);
        map.put(SendKeys.EMAIL, email);
        map.put(SendKeys.PASS, pass);


        CheckThread signThread = new CheckThread(map);
        signThread.start();

        while(signThread.getAnswer()==null); // potential dead loop!!!
        Log.d(TAG, "Server's answer is: " + signThread.getAnswer());

        signThread.close();
        signThread.interrupt();

        return signThread.getAnswer();
    }

    public boolean checkPasswordLocal(String pass) {
        int l = pass.length();
        return l > 4 && l < 17;
    }

    public boolean checkEmailIsFree(String email) {
        HashMap map = new HashMap<>();
        map.put(SendKeys.TITLE, SendKeys.CHECK_MAIL);
        map.put(SendKeys.EMAIL, email);

        CheckThread signThread = new CheckThread(map);
        signThread.start();
        while(signThread.getAnswer()==null); // potential dead loop!!!
        Log.d(TAG, "Server's answer is: " + signThread.getAnswer());

        signThread.close();
        signThread.interrupt();

        return !signThread.getAnswer();
    }

}
