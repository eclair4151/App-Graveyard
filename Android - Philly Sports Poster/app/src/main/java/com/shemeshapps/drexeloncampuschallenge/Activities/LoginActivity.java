package com.shemeshapps.drexeloncampuschallenge.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.shemeshapps.drexeloncampuschallenge.Helpers.SessionHelper;
import com.shemeshapps.drexeloncampuschallenge.R;
import com.shemeshapps.drexeloncampuschallenge.Networking.RequestUtil;
import com.shemeshapps.drexeloncampuschallenge.Models.User;


public class LoginActivity extends AppCompatActivity {


    private RelativeLayout loginBox;
    private ProgressBar loginLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        checkIfLoggedIn();
        TextView register = (TextView)findViewById(R.id.register_button);
        final EditText emailBox = (EditText)findViewById(R.id.email_box);
        final EditText passwordBox = (EditText)findViewById(R.id.password_box);
        final Button loginButton = (Button)findViewById(R.id.login_button);
        emailBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        passwordBox.setImeOptions(EditorInfo.IME_ACTION_GO);

        emailBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    passwordBox.requestFocus();
                    return true;
                }
                return false;
            }
        });

        passwordBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO)
                {
                    loginButton.callOnClick();
                    return true;
                }
                return false;
            }
        });

        loginBox = (RelativeLayout)findViewById(R.id.login_box);
        loginLoader = (ProgressBar)findViewById(R.id.login_spinner);
        Button facebookButton = (Button)findViewById(R.id.facebook_login);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean failedlogin = false;
                if(emailBox.getText().toString().isEmpty())
                {
                    emailBox.setError("Required Field");
                    failedlogin = true;
                }

                if(passwordBox.getText().toString().isEmpty())
                {
                    passwordBox.setError("Required Field");
                    failedlogin = true;
                }

                if(failedlogin)
                    return;


                hideSoftKeyBoard();
                loginBox.setVisibility(View.GONE);
                loginLoader.setVisibility(View.VISIBLE);
                RequestUtil.getInstance(LoginActivity.this).Login(new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        if(response==null) //invalid launchApp
                        {
                            loginBox.setVisibility(View.VISIBLE);
                            loginLoader.setVisibility(View.GONE);
                            purgeSessionId();
                            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                        }
                        else if(((User)response).organization.equals("Drexel University"))
                        {
                            //SessionHelper.saveSessionId(LoginActivity.this, SessionHelper.extractSessionId());
                            launchApp(true);
                        }
                        else
                        {
                            loginBox.setVisibility(View.VISIBLE);
                            loginLoader.setVisibility(View.GONE);
                            purgeSessionId();
                            Toast.makeText(getApplicationContext(), "This app is for users who post for Drexel University only.", Toast.LENGTH_LONG).show();
                        }
                    }
                },emailBox.getText().toString(),passwordBox.getText().toString());
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,LoginWebviewActivity.class);
                i.putExtra("facebook_login",true);
                startActivityForResult(i,0);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,LoginWebviewActivity.class));
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,final Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK)
        {
            loginBox.setVisibility(View.GONE);
            loginLoader.setVisibility(View.VISIBLE);
            SessionHelper.saveSessionId(LoginActivity.this, data.getStringExtra("sessionId"));

            RequestUtil.getInstance(LoginActivity.this).GetUserInfo(new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    if (((User) response).organization.equals("Drexel University")) {
                        launchApp(true);
                    } else {
                        loginBox.setVisibility(View.VISIBLE);
                        loginLoader.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "This app is for users who post for Drexel University only.", Toast.LENGTH_LONG).show();
                        purgeSessionId();
                    }
                }
            }, data.getStringExtra("sessionId"));
        }
    }


    private void checkIfLoggedIn()
    {
        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        if(sharedPref.contains("sessionId"))
        {
            launchApp(false);
        }
    }

    private void launchApp(boolean justLoggedIn)
    {
        Intent i = new Intent(this,MainActivity.class);
        if(justLoggedIn)
        {
            i.putExtra("justLoggedIn", true);
        }
        startActivity(i);
        finish();
    }

    private void purgeSessionId()
    {
        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
