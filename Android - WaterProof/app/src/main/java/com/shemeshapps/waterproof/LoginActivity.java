package com.shemeshapps.waterproof;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        final Button loginButton = (Button)findViewById(R.id.loginButton);
        final EditText emailBox = (EditText)findViewById(R.id.emailBox);
        final EditText passcodeBox = (EditText)findViewById(R.id.passcodeBox);
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.loginSpinner);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailBox.getText().toString().isEmpty() || passcodeBox.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"Error: Empty Field",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    spinner.setVisibility(View.VISIBLE);
                    loginButton.setEnabled(false);
                    emailBox.setEnabled(false);
                    passcodeBox.setEnabled(false);
                    Map<String,String> things = new HashMap<>();
                    things.put("Email",emailBox.getText().toString());
                    things.put("Passcode",passcodeBox.getText().toString());
                    ParseCloud.callFunctionInBackground("Login", things, new FunctionCallback<Map<String, Object>>() {
                        @Override
                        public void done(Map<String, Object> object, ParseException e) {
                            if(e==null)
                            {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                editor.putString("userid",(String)object.get("objectid"));
                                editor.putLong("expiration",((Date)object.get("date")).getTime());
                                editor.commit();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }
                            else
                            {
                                spinner.setVisibility(View.GONE);
                                loginButton.setEnabled(true);
                                emailBox.setEnabled(true);
                                passcodeBox.setEnabled(true);
                                Toast.makeText(LoginActivity.this,e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
