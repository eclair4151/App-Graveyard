package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Activities.LoginActivity;
import com.shemeshapps.drexelregistrationassistant.Models.ClassRegister;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

/**
 * Created by tomer on 11/23/16.
 */

public class RegistrationFragment extends Fragment {

    View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.registration_fragment, container, false);
        Button login = (Button)parentView.findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),LoginActivity.class),1);
            }
        });
        return parentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK && data!= null)
        {
            RequestUtil.getInstance(getActivity()).getRegisteredClasses(new Response.Listener<ClassRegister>() {
                @Override
                public void onResponse(ClassRegister response) {

                }
            });
        }
    }
}
