package com.shemeshapps.drexelstudybuddies.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.parse.ParseObject;
import com.shemeshapps.drexelstudybuddies.Activities.MainActivity;
import com.shemeshapps.drexelstudybuddies.Helpers.ListStudyGroupAdapter;
import com.shemeshapps.drexelstudybuddies.NetworkingServices.RequestUtil;
import com.shemeshapps.drexelstudybuddies.R;

import java.util.ArrayList;


public class SuggestedGroups extends Fragment {
    ExpandableListView suggestedGroupsList;
    SwipeRefreshLayout refreshLayout;
    ListStudyGroupAdapter adapter;
    View parentView;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.suggested_groups, container, false);

        final Button crt_grp = (Button)parentView.findViewById(R.id.create_grp);

        crt_grp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadScreen(MainActivity.fragments.CREATE);
            }
        });



        pref = getActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE);
        refreshLayout = (SwipeRefreshLayout)parentView.findViewById(R.id.suggestedStudyListRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.loadGroupFromBackend(pref.getString("user_classes",""),false);
            }
        });
        suggestedGroupsList = (ExpandableListView)parentView.findViewById(R.id.suggestedStudyGroupList);
        adapter = new ListStudyGroupAdapter(getActivity(),new ArrayList<ParseObject>(),refreshLayout,suggestedGroupsList,null);
        suggestedGroupsList.setAdapter(adapter);
        return parentView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.loadGroupFromBackend(pref.getString("user_classes",""),true);
    }
}
