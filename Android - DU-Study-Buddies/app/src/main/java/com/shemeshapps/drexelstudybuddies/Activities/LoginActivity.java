package com.shemeshapps.drexelstudybuddies.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shemeshapps.drexelstudybuddies.Models.DrexelClass;
import com.shemeshapps.drexelstudybuddies.Models.LoginResponse;
import com.shemeshapps.drexelstudybuddies.NetworkingServices.RequestUtil;
import com.shemeshapps.drexelstudybuddies.R;

public class LoginActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfLoggedIn();
        setContentView(R.layout.activity_login);

        final ProgressBar loadingBar = (ProgressBar)findViewById(R.id.login_loading);
        final EditText password = (EditText)findViewById(R.id.password_login);
        final EditText username = (EditText)findViewById(R.id.username_login);
        final Button login = (Button)findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                login.setEnabled(false);
                RequestUtil.getAuthCode(username.getText().toString(),password.getText().toString(),new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        saveAuthKey((LoginResponse)response);
                        getUserClasses();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Incorrect Username or Password",Toast.LENGTH_SHORT).show();
                        loadingBar.setVisibility(View.GONE);
                        login.setEnabled(true);
                    }
                });
            }
        });
    }

    private void getUserClasses()
    {
        RequestUtil.getUsersCurrentClasses(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                saveUserClasses((DrexelClass[]) response);
                login();
            }
        });
    }


    private void saveUserClasses(DrexelClass[] classes)
    {
        String classList = "";
        for(int i =0; i < classes.length; i++)
        {
            if(!classes[i].SubjectCode.toLowerCase().equals("exam"))
            {
                classList += (classes[i].SubjectCode + classes[i].CourseNumber);
                if(i != classes.length -1)
                {
                    classList += ",";
                }
            }
        }
        SharedPreferences pref = getSharedPreferences("login_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();
        e.putString("user_classes",classList);
        e.apply();
    }

    private void saveAuthKey(LoginResponse auth)
    {
        SharedPreferences pref = getSharedPreferences("login_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = pref.edit();
        e.putString("auth_key",auth.AuthKey);
        e.putString("user_id",auth.UserId);
        e.apply();
    }

    private void login()
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private void checkIfLoggedIn()
    {
        SharedPreferences pref = getSharedPreferences("login_data", Context.MODE_PRIVATE);
        if(pref.contains("auth_key"))
        {
           login();
        }
    }
}
