package com.shemeshapps.waterproof;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Long expiration = PreferenceManager.getDefaultSharedPreferences(this).getLong("expiration",0);
        if((new Date().getTime()) > expiration)
        {
            //logout
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(expiration);
        final TextView exp = (TextView)findViewById(R.id.expirationText);
        exp.setText("Account Expiration: " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR));


        String userid = PreferenceManager.getDefaultSharedPreferences(this).getString("userid","");
        if(!userid.equals(""))
        {
            Map<String,String> things = new HashMap<>();
            things.put("objectId",userid);
            ParseCloud.callFunctionInBackground("GetExpiration", things, new FunctionCallback<Object>() {
                @Override
                public void done(Object object, ParseException e) {
                    if(e==null)
                    {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editor.putLong("expiration",((Date) object).getTime()).apply();
                        if((new Date().getTime()) > ((Date) object).getTime())
                        {
                            editor.clear().commit();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            Calendar c = new GregorianCalendar();
                            c.setTime((Date) object);
                            exp.setText("Account Expiration: " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR));
                        }
                    }
                }
            });
        }

        Button encryptMenuButton = (Button)findViewById(R.id.encryptMenuButton);
        encryptMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,EncryptGPS.class));
            }
        });

        Button decryptMenuButton = (Button)findViewById(R.id.decryptMenuButton);
        decryptMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DecryptGPS.class));
            }
        });

        Button logout = (Button)findViewById(R.id.logoutMenuButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to logout? you wont be able to login with the same code as before.").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editor.clear().commit();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

    }
}
