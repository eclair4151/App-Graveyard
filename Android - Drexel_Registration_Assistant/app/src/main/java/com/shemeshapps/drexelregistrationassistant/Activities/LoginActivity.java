package com.shemeshapps.drexelregistrationassistant.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shemeshapps.drexelregistrationassistant.Helpers.PreferenceHelper;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLLoginPost;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                RequestUtil.getInstance(LoginActivity.this).login(username.getText().toString(),password.getText().toString(),new Response.Listener<HTMLLoginPost>() {
                    @Override
                    public void onResponse(HTMLLoginPost response) {
                        if(response.success)
                        {
                            //success! save creds and close activity
                            setResult(RESULT_OK);
                            PreferenceHelper.storeUserCreds(username.getText().toString().replace("@drexel.edu",""),password.getText().toString(),LoginActivity.this);
                            PreferenceHelper.setAutoLogin(LoginActivity.this,true);
                            finish();
                        }
                        else
                        {
                            //login failed re-enable fields
                            loadingBar.setVisibility(View.GONE);
                            login.setEnabled(true);
                            Toast.makeText(LoginActivity.this,"Username or Password Incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        }});

    }
}