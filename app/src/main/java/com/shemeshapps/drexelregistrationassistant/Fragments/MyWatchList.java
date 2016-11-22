package com.shemeshapps.drexelregistrationassistant.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Activities.WebtmsClassActivity;
import com.shemeshapps.drexelregistrationassistant.Adapters.WebtmsClassAdapter;
import com.shemeshapps.drexelregistrationassistant.Helpers.PreferenceHelper;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWatchList extends Fragment {

    View parentView;
    WebtmsClassAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK)
        {
            refreshWatchList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.my_watch_list, container, false);
        refreshLayout = (SwipeRefreshLayout)parentView.findViewById(R.id.watch_list_swipe_refresh);
        refreshLayout.setEnabled(false);
        refreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        ListView watchList = (ListView)parentView.findViewById(R.id.watch_list_listview);
        adapter = new WebtmsClassAdapter(getActivity(),new ArrayList<WebtmsClass>());
        watchList.setAdapter(adapter);
        refreshWatchList();
        watchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebtmsClassActivity.class);
                intent.putExtra("webtms_class", Parcels.wrap(adapter.getItem(position)));
                startActivityForResult(intent,1);
            }
        });


       /* FloatingActionButton fab = (FloatingActionButton)parentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        return parentView;
    }

    private void refreshWatchList()
    {
        adapter.clear();
        if(!PreferenceHelper.getWatchList(getActivity()).isEmpty())
        {
            refreshLayout.setRefreshing(true);
            RequestUtil.getInstance(getActivity()).getWatchListClasses(new Response.Listener<WebtmsClass[]>() {
                @Override
                public void onResponse(WebtmsClass[] response) {
                    adapter.addAll(Arrays.asList(response));
                    TextView watchlist = (TextView)parentView.findViewById(R.id.watch_list_empty);
                    watchlist.setVisibility((adapter.getCount() == 0)?View.VISIBLE:View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            });
        }
        else
        {
            TextView watchlist = (TextView)parentView.findViewById(R.id.watch_list_empty);
            watchlist.setVisibility(View.VISIBLE);
        }
    }


}
