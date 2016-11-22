package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Activities.ViewClassActivity;
import com.shemeshapps.drexelregistrationassistant.Activities.ViewProfessorActivity;
import com.shemeshapps.drexelregistrationassistant.Activities.WebtmsClassActivity;
import com.shemeshapps.drexelregistrationassistant.Adapters.SearchExpandableListAdapter;
import com.shemeshapps.drexelregistrationassistant.Helpers.UIHelper;
import com.shemeshapps.drexelregistrationassistant.Models.QueryResult;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

import org.parceler.Parcels;

/**
 * Created by tomer on 11/18/16.
 */

public class SearchFragment extends Fragment {
    View parentView;
    Handler mHandler = new Handler();
    String mQueryString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.search_fragment, container, false);
        final ProgressBar loading = (ProgressBar)parentView.findViewById(R.id.search_loading);
        final SearchView search = (SearchView)parentView.findViewById(R.id.search_fragment_view);
        ExpandableListView searchResultsView = (ExpandableListView)parentView.findViewById(R.id.search_expandable_list);
        final SearchExpandableListAdapter adapter = new SearchExpandableListAdapter(getActivity(),searchResultsView);
        searchResultsView.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mQueryString = s;
                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(loading.getVisibility() == View.VISIBLE)
                        {
                            RequestUtil.getInstance(getActivity().getApplicationContext()).cancelQueryCalls();
                        }

                        if(!mQueryString.isEmpty())
                        {
                            loading.setVisibility(View.VISIBLE);
                            RequestUtil.getInstance(getActivity().getApplicationContext()).runSearchQuery(mQueryString, new Response.Listener<QueryResult>() {
                                @Override
                                public void onResponse(QueryResult response) {
                                    adapter.setResults(response);
                                    loading.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                        else
                        {
                            RequestUtil.getInstance(getActivity().getApplicationContext()).cancelQueryCalls();
                            loading.setVisibility(View.INVISIBLE);
                            adapter.clear();
                            adapter.removeHeader();
                            adapter.notifyDataSetChanged();
                        }

                    }
                }, 350);
                return true;
            }
        });

        int searchCloseButtonId = search.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView)search.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestUtil.getInstance(getActivity().getApplicationContext()).cancelQueryCalls();
                loading.setVisibility(View.INVISIBLE);
                adapter.clear();
                adapter.removeHeader();
                adapter.notifyDataSetChanged();
                search.setQuery("",false);
            }
        });

        searchResultsView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String group = adapter.getGroup(i);
                if(group.equals(SearchExpandableListAdapter.SearchType.Classes.name()))
                {
                    Intent intent = new Intent(getActivity(), ViewClassActivity.class);
                    intent.putExtra("ClassInfo", Parcels.wrap(adapter.getChild(i,i1)));
                    startActivity(intent);
                }
                else if(group.equals(SearchExpandableListAdapter.SearchType.Professors.name()))
                {
                    Intent intent = new Intent(getActivity(), ViewProfessorActivity.class);
                    intent.putExtra("professor", Parcels.wrap(adapter.getChild(i,i1)));
                    startActivity(intent);
                }
                else if(group.equals(SearchExpandableListAdapter.SearchType.CRNs.name()))
                {
                    Intent intent = new Intent(getActivity(), WebtmsClassActivity.class);
                    intent.putExtra("webtms_class", Parcels.wrap(adapter.getChild(i,i1)));
                    startActivity(intent);
                }
                return false;
            }
        });
        return parentView;
    }


}
