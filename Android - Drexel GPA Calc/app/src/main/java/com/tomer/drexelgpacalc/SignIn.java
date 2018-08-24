package com.tomer.drexelgpacalc;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tomer.drexelgpacalc.R;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tomershemesh on 7/16/14.
 */
//                String test  =ItEventParser.parseClasses(response);

public class SignIn extends Activity {
Spinner yearSpinner;
    private EditText passwordbox, usernamebox;
    private TextView resultbox;
    ProgressBar progressBar;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        yearSpinner = (Spinner)findViewById(R.id.sign_in_spinner_year);
        queue = Volley.newRequestQueue(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.yearArray));
        yearSpinner.setAdapter(adapter);

        passwordbox = (EditText) findViewById(R.id.password);
        usernamebox = (EditText) findViewById(R.id.username);
        resultbox = (TextView) findViewById(R.id.resulttext);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        DefaultHttpClient httpclient = new DefaultHttpClient();

        CookieStore cookieStore = new BasicCookieStore();
        httpclient.setCookieStore( cookieStore );

        HttpStack httpStack = new HttpClientStack( httpclient );
        queue = Volley.newRequestQueue( getApplicationContext(), httpStack  );
    }




    public void buttonClick(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        resultbox.setText("");
        imm.hideSoftInputFromWindow(passwordbox.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(usernamebox.getWindowToken(), 0);
        progressBar.setVisibility(View.VISIBLE);



        final StringRequest sr2 = new StringRequest(Request.Method.POST,"https://connect.drexel.edu/idp/profile/cas/login?execution=e1s1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getGrades();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("j_username",usernamebox.getText().toString());
                params.put("j_password",passwordbox.getText().toString());
                params.put("_eventId_proceed","");


                return params;
            }};

        StringRequest sr1 = new StringRequest(Request.Method.GET,"https://one.drexel.edu/web/university/academics?gpi=10230", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                queue.add(sr2);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(sr1);

    }


    public void getGrades()
    {

        final StringRequest sr2 = new StringRequest(Request.Method.POST,"https://banner.drexel.edu/pls/duprod/bwskotrn.P_ViewTran", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                resultbox.setText(ItEventParser.parseClasses(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                String str = "levl=&tprt=STUD";
                return str.getBytes();
            };

            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };


        String url = "https://bannersso.drexel.edu/ssomanager/c/SSB?pkg=bwszkfrag.P_DisplayFinResponsibility%3Fi_url%3Dbwskotrn.P_ViewTermTran";
        StringRequest sr1 = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                queue.add(sr2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(sr1);
    }


}

