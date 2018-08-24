package com.shemeshapps.drexelregistrationassistant.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shemeshapps.drexelregistrationassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends android.app.Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
