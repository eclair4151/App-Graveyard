package com.shemeshapps.drexelregistrationassistant.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;


import com.shemeshapps.drexelregistrationassistant.Adapters.WebtmsClassAdapter;
import com.shemeshapps.drexelregistrationassistant.Helpers.PreferenceHelper;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWatchList extends Fragment {

    View parentView;
    WebtmsClassAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.my_watch_list, container, false);
        refreshLayout = (SwipeRefreshLayout)parentView.findViewById(R.id.watch_list_swipe_refresh);
        refreshLayout.setEnabled(false);
        ListView watchList = (ListView)parentView.findViewById(R.id.watch_list_listview);
        adapter = new WebtmsClassAdapter(getActivity(),new ArrayList<WebtmsClass>());
        watchList.setAdapter(adapter);
        refreshWatchList();

        FloatingActionButton fab = (FloatingActionButton)parentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return parentView;
    }

    private void refreshWatchList()
    {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                // refreshLayout.setRefreshing(true);
            }
        });
        adapter.clear();
        adapter.addAll(PreferenceHelper.getWatchList());
        TextView watchlist = (TextView)parentView.findViewById(R.id.watch_list_empty);

        watchlist.setVisibility((adapter.getCount() == 0)?View.VISIBLE:View.GONE);

    }


}
