package com.shemeshapps.drexelstudybuddies.Fragments;

import android.app.Fragment;
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
import android.widget.SearchView;

import com.parse.ParseObject;
import com.shemeshapps.drexelstudybuddies.Activities.MainActivity;
import com.shemeshapps.drexelstudybuddies.Helpers.ListStudyGroupAdapter;
import com.shemeshapps.drexelstudybuddies.R;

import java.util.ArrayList;

public class BrowseGroups extends Fragment {

    View parentView;
    ListStudyGroupAdapter adapter;
    SearchView searchBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.activity_group_calendar, container, false);
        super.onCreate(savedInstanceState);
        final Button crt_grp = (Button)parentView.findViewById(R.id.create_grp);
        ExpandableListView suggestedGroupsList = (ExpandableListView)parentView.findViewById(R.id.suggestedStudyGroupList);
        crt_grp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadScreen(MainActivity.fragments.CREATE);
            }
        });
        searchBox = (SearchView)parentView.findViewById(R.id.browseGroupSearchBox);
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)parentView.findViewById(R.id.suggestedStudyListRefresh);
        adapter = new ListStudyGroupAdapter(getActivity(),new ArrayList<ParseObject>(),refreshLayout,suggestedGroupsList,null);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.loadGroupFromBackend(searchBox.getQuery().toString(),false);
            }
        });

        suggestedGroupsList.setAdapter(adapter);

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.loadGroupFromBackend(query,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                {
                    adapter.loadGroupFromBackend(newText,false);
                }
                return false;
            }
        });
        return parentView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.loadGroupFromBackend(searchBox.getQuery().toString(),true);
    }
}
