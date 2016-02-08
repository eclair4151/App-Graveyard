package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shemeshapps.drexelregistrationassistant.R;

/**
 * Created by tomer on 2/7/16.
 */
public class FilterFragmentDialog extends DialogFragment {

    public FilterFragmentDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_popup_fragment, container);
        getDialog().setTitle("Filter");

        return view;
    }
}