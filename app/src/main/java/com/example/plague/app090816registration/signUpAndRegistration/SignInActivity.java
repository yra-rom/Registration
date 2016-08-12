package com.example.plague.app090816registration.signUpAndRegistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.plague.app090816registration.R;
import com.example.plague.app090816registration.clients.SendKeys;
import com.example.plague.app090816registration.clients.SignThread;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "SignInActivity";

    private EditText etNickOrEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private TextView tvRegister;
    private CheckBox cbKeepLogged;
    private SharedPreferences sharedPrefs;
    private TextView tvRegSucc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        initViews();
    }

    private void initViews() {
        etNickOrEmail = (EditText) findViewById(R.id.sign_etNameOrEMail);
        etPassword = (EditText) findViewById(R.id.sign_etPassword);
        btnSignIn = (Button) findViewById(R.id.sign_btnSignIn);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        cbKeepLogged = (CheckBox) findViewById(R.id.sign_cbKeepLogged);
        tvRegSucc = (TextView) findViewById(R.id.sign_tvRegSucc);

        btnSignIn.setOnClickListener(this);
        tvRegister.setOnClickListener(this);


        etNickOrEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etNickOrEmail.setTextColor(Color.BLACK);
                etNickOrEmail.setHintTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etPassword.setTextColor(Color.BLACK);
                etPassword.setHintTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_btnSignIn:
                logInUser();
                break;
            case R.id.tvRegister:
                //if user want to create a new account
                registerUser();
                break;
        }
    }

    private void registerUser() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, 1);
    }

    private void logInUser() {
        String email = etNickOrEmail.getText().toString();
        String pass = etPassword.getText().toString();
        if(checkEmail(email)){
            Log.d(TAG, "Server checked email: email is correct");
            if(checkPassword(email, pass)){
                Log.d(TAG, "Server checked pass: pass is correct");
                //if user and password are correct and
                if(cbKeepLogged.isChecked()){
                    //remember users nick and pass
                    rememberUser(email, pass);
                }
                Log.d(TAG,"Imitating that user successfully logged in");
                //TO DO Activity when is logged in
            }else{
                Log.d(TAG, "Server checked name: name is NOT correct");
                //if user is correct BUT password is not correct
                etPassword.setTextColor(Color.RED);
            }
        }else{
            Log.d(TAG, "Server checked mail: mail is NOT correct");
            //if user is not correct
            etNickOrEmail.setTextColor(Color.RED);
        }
    }

    private void rememberUser(String key1, String key2) {
        sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPrefs.edit();
        ed.putString(SendKeys.NICK, key1);
        ed.commit();
        ed.putString(SendKeys.PASS, key2);
        ed.commit();
    }

    private boolean checkEmail(String keyWord){
        if(keyWord.equals("")) return false;

        Map map = new HashMap<String, String>();
        map.put(SendKeys.EMAIL, keyWord);

        SignThread signThread = new SignThread(map);
        signThread.start();

        while(signThread.getAnswer()==null); // potential dead loop!!!
        Log.d(TAG, "Server's answer is: " + signThread.getAnswer());

        signThread.close();
        signThread.interrupt();

        Boolean answ = signThread.getAnswer();

        if(!answ){
            etNickOrEmail.setTextColor(Color.RED);
            etNickOrEmail.setHintTextColor(Color.RED);
        }
        return answ;
    }

    private boolean checkPassword(String email, String pass){
        if(pass.equals("")) return false;

        Map map = new HashMap<String, String>();
        map.put(SendKeys.EMAIL, email);
        map.put(SendKeys.PASS, pass);


        SignThread signThread = new SignThread(map);
        signThread.start();

        while(signThread.getAnswer()==null); // potential dead loop!!!
        Log.d(TAG, "Server's answer is: " + signThread.getAnswer());

        signThread.close();
        signThread.interrupt();

        Boolean answ = signThread.getAnswer();

        if(!answ){
            etPassword.setTextColor(Color.RED);
            etPassword.setHintTextColor(Color.RED);
        }
        return answ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null){
            return;
        }
        if(resultCode == RESULT_OK) {
            String nick = data.getStringExtra(SendKeys.NICK);
            //Log.d(TAG, SendKeys.NICK + nick);
            etNickOrEmail.setText(nick);
            tvRegSucc.setText(R.string.regSuccess);
        }
    }
    
    @Override
    public void onBackPressed() {
        //Override to disable back key pressed
    }
}