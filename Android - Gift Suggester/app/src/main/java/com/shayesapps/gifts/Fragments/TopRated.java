package com.shayesapps.gifts.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

/**
 * Created by tomershemesh on 8/8/14.
 */
public class TopRated extends ParentListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.top_rated_fragment, container, false);
        setupView();
        gifts.clear();
        adapter.notifyDataSetChanged();
        makeRequest();
        return parentView;
    }




//make generic request
    protected void makeRequest()
    {

        refreshLayout.setRefreshing(true);
        List<NameValuePair> topRatedParameters = new ArrayList<NameValuePair>();
        topRatedParameters.add(new BasicNameValuePair("category","Rpa"));
        topRatedParameters.add(new BasicNameValuePair("limit",Integer.toString(pageLimit)));
        topRatedParameters.add(new BasicNameValuePair("page",Integer.toString(currentPage)));
        topRatedParameters.add(new BasicNameValuePair("sort",sortOrder));

        RequestUtil.getSearchResult(topRatedParameters, listener);
    }

}