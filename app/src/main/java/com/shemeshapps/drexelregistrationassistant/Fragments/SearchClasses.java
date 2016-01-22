package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;

/**
 * Created by Tomer on 1/11/16.
 */
public class SearchClasses extends Fragment{
    View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.search_classes, container, false);
        Button searchByClass = (Button)parentView.findViewById(R.id.search_by_class_button);
        searchByClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new SearchByClass());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return parentView;
    }
}
