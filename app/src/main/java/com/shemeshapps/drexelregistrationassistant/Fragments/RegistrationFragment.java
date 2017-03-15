package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shemeshapps.drexelregistrationassistant.Activities.LoginActivity;
import com.shemeshapps.drexelregistrationassistant.Helpers.PreferenceHelper;
import com.shemeshapps.drexelregistrationassistant.Models.ClassRegister;
import com.shemeshapps.drexelregistrationassistant.Models.TermPage;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;
import com.tsums.androidcookiejar.PersistentCookieStore;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by tomer on 11/23/16.
 */

public class RegistrationFragment extends Fragment {

    View parentView;
    Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //in progress. currently just knows if your logged in and gets the term page or offers to log in
        parentView = inflater.inflate(R.layout.registration_fragment, container, false);
        login = (Button)parentView.findViewById(R.id.loginButton);




        RequestUtil.getInstance(getActivity().getApplicationContext()).getHtmlTerms(new Response.Listener<TermPage>() {
            @Override
            public void onResponse(final TermPage response) {
                Log.e("LOGIN FLOW", "got terms");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOGIN FLOW","received user not logged in error successfully");
                loadLoginScreen();
            }
        });


        return parentView;
    }

    private void loadLoginScreen()
    {
        login.setVisibility(View.VISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),LoginActivity.class);
                startActivityForResult(i,1);
            }
        });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK)
        {
//            RequestUtil.getInstance(getActivity()).getHtmlTerms(new Response.Listener<TermPage>() {
//                @Override
//                public void onResponse(TermPage response) {
//                    RequestUtil.getInstance(getActivity()).getRegisteredClasses("", new Response.Listener<ClassRegister>() {
//                        @Override
//                        public void onResponse(ClassRegister response) {
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    });
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });

        }
    }
}
