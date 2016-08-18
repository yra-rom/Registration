package com.example.plague.app090816registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.plague.app090816registration.LogInAndRegistration.activities.LogInActivity;
import com.example.plague.app090816registration.Tabs.TabsActivity;
import com.example.plague.app090816registration.connection_defaults.SendKeys;

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Where everything begins
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String s1 = preferences.getString(SendKeys.EMAIL, "");
        String s2 = preferences.getString(SendKeys.PASS, "");
//        String s1 = "";
//        String s2 = "";
        if(s1.equals("") || s2.equals("")){
            startActivity(new Intent(this, LogInActivity.class));
        }else {
            startActivity(new Intent(this, TabsActivity.class));
        }
    }

}