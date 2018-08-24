package com.shemeshapps.drexelstudybuddies.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.parse.ParseObject;
import com.shemeshapps.drexelstudybuddies.Activities.MainActivity;
import com.shemeshapps.drexelstudybuddies.Helpers.ListStudyGroupAdapter;
import com.shemeshapps.drexelstudybuddies.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsAttending extends Fragment {

    ExpandableListView suggestedGroupsList;
    SwipeRefreshLayout refreshLayout;
    ListStudyGroupAdapter adapter;
    View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.suggested_groups, container, false);

        final Button crt_grp = (Button)parentView.findViewById(R.id.create_grp);

        crt_grp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadScreen(MainActivity.fragments.CREATE);
            }
        });


        refreshLayout = (SwipeRefreshLayout)parentView.findViewById(R.id.suggestedStudyListRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.loadGroupFromBackend("AttendingStudyGroups",false);
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
        adapter.loadGroupFromBackend("AttendingStudyGroups",true);
    }

}
