package com.shayesapps.gifts.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shayesapps.gifts.Adapters.BrowseSelectionAdapter;
import com.shayesapps.gifts.Models.BrowseSelection;
import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

/**
 * Created by tomershemesh on 8/8/14.
 */
public class Browse extends ParentListFragment {

    BrowseSelectionAdapter browseSelectionAdapter;
    ArrayAdapter browseListAdapter;
    ArrayList<String> browseList = new ArrayList<String>();
    List<BrowseSelection> selections = new ArrayList<BrowseSelection>();
    ListView browseListView;
    int browseSelectionItem;
    String browseSelectionKey;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.browse_fragment, container, false);
        browseListView = (ListView)parentView.findViewById(R.id.browseListView);
        setupView();

        //this adds all menus to the initial browse page
        selections.clear();
        selections.add(new BrowseSelection("Categories", "category", RequestUtil.categories, RequestUtil.categoriesCodes, R.drawable.browse_catagory_icon));
        selections.add(new BrowseSelection("Recipients", "recipient", RequestUtil.recipients, RequestUtil.recipientsCodes, R.drawable.browse_recipients_icon));
        selections.add(new BrowseSelection("Personalities","personality", RequestUtil.personalities, RequestUtil.personalitiesCodes, R.drawable.browse_personalities_icon));
        selections.add(new BrowseSelection("Occasions","occasion",RequestUtil.occasions,RequestUtil.occasionsCodes, R.drawable.browse_occasion_icon));
        browseSelectionAdapter = new BrowseSelectionAdapter(getActivity(),selections);
        browseListView.setAdapter(browseSelectionAdapter);

        browseListAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, browseList);


        //when a browse item is clicked if you are in the first page show all the items within that list
        //other wise we are in the secondary selection in which case make a request to get all gifts in this category
        browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(browseListView.getAdapter() == browseSelectionAdapter)
                {
                    browseSelectionClicked(i);
                }
                else if (browseListView.getAdapter() == browseListAdapter)
                {
                    browseSelectionKey = selections.get(browseSelectionItem).keyList[i];
                    gifts.clear();
                    adapter.notifyDataSetChanged();
                    try
                    {
                        giftListView.removeFooterView(footerView);
                    }
                    catch (Exception e){}

                    currentPage = 0;
                    browseListView.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    makeRequest();
                    getActivity().invalidateOptionsMenu();
                }
            }
        });
        return parentView;
    }

    //clicked main browse page item so show more specific list
    private void browseSelectionClicked(int position)
    {
        browseList.clear();
        browseList.addAll(Arrays.asList(selections.get(position).itemList));
        browseListView.setAdapter(browseListAdapter);
        browseListAdapter.notifyDataSetChanged();
        browseSelectionItem = position;
    }


    //if we are at a gift go to the last page
    //if we are at the specific gift options go back to the first browse page
    //other wise we are at the first page, return true let android run default behavior
    @Override
    public boolean backPressed() {
        if (browseListView.getVisibility()==View.VISIBLE && browseListView.getAdapter() == browseSelectionAdapter) {
            return true;
        }
        else if (browseListView.getVisibility()==View.VISIBLE && browseListView.getAdapter() == browseListAdapter) {
            browseListView.setAdapter(browseSelectionAdapter);
        }
        else{
            browseListView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
            getActivity().invalidateOptionsMenu();
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(refreshLayout.getVisibility() == View.VISIBLE)
        {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    //make request for gifts
    protected void makeRequest()
    {

        refreshLayout.setRefreshing(true);
        List<NameValuePair> BrowseParameters = new ArrayList<NameValuePair>();
        BrowseParameters.add(new BasicNameValuePair(selections.get(browseSelectionItem).urlKey, browseSelectionKey));
        BrowseParameters.add(new BasicNameValuePair("limit", Integer.toString(pageLimit)));
        BrowseParameters.add(new BasicNameValuePair("page", Integer.toString(currentPage)));
        BrowseParameters.add(new BasicNameValuePair("sort", sortOrder));

        RequestUtil.getSearchResult(BrowseParameters, listener);
    }


}