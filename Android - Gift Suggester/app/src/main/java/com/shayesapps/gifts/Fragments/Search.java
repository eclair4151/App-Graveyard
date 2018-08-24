package com.shayesapps.gifts.Fragments;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tomershemesh on 8/8/14.
 */

//search function. i tried to make it cleaner but unfortunatly they were to different to do in one way
public class Search extends ParentListFragment {
    Spinner categorySpinner, recipientSpinner, personalitySpinner, occasionSpinner;
    EditText keyword;
    LinearLayout searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.search_fragment, container, false);

        searchView = (LinearLayout)parentView.findViewById(R.id.search_view);
        setHasOptionsMenu(true);
        setupView();

        //all of these set up the drop down menus on the search page
        categorySpinner = (Spinner)parentView.findViewById(R.id.search_category_spinner);
        List<String> categoryItems = new ArrayList<String>(Arrays.asList(RequestUtil.categories));
        categoryItems.add(0, "Any");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, categoryItems);
        categorySpinner.setAdapter(categoryAdapter);

        recipientSpinner = (Spinner)parentView.findViewById(R.id.search_recipient_spinner);
        List<String> recipientItems = new ArrayList<String>(Arrays.asList(RequestUtil.recipients));
        recipientItems.add(0,"Any");
        ArrayAdapter<String> recipientAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, recipientItems);
        recipientSpinner.setAdapter(recipientAdapter);

        personalitySpinner = (Spinner)parentView.findViewById(R.id.search_personality_spinner);
        List<String> personalityItems = new ArrayList<String>(Arrays.asList(RequestUtil.personalities));
        personalityItems.add(0,"Any");
        ArrayAdapter<String> personalityAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, personalityItems);
        personalitySpinner.setAdapter(personalityAdapter);

        occasionSpinner = (Spinner)parentView.findViewById(R.id.search_occasion_spinner);
        List<String> occasionItems = new ArrayList<String>(Arrays.asList(RequestUtil.occasions));
        occasionItems.add(0,"Any");
        ArrayAdapter<String> occasionAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, occasionItems);
        occasionSpinner.setAdapter(occasionAdapter);

        keyword = (EditText)parentView.findViewById(R.id.search_keyword_edittext);


        Button searchButton = (Button)parentView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage =0;
                try
                {
                    giftListView.removeFooterView(footerView);
                }
                catch (Exception e){}
                adapter.clear();
                adapter.notifyDataSetChanged();
                searchView.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                getActivity().invalidateOptionsMenu();
                makeRequest();
            }
        });
        return parentView;
    }


    //in make request we check if they click on any of the drop down and if they did we need to add it to out parameters
    protected void makeRequest() {
        refreshLayout.setRefreshing(true);
        List<NameValuePair> BrowseParameters = new ArrayList<NameValuePair>();
        if(categorySpinner.getSelectedItemPosition()!=0)
        {
            BrowseParameters.add(new BasicNameValuePair("category",RequestUtil.categoriesCodes[categorySpinner.getSelectedItemPosition()-1]));
        }
        if(recipientSpinner.getSelectedItemPosition()!=0)
        {
            BrowseParameters.add(new BasicNameValuePair("recipient",RequestUtil.recipientsCodes[recipientSpinner.getSelectedItemPosition()-1]));
        }
        if(personalitySpinner.getSelectedItemPosition()!=0)
        {
            BrowseParameters.add(new BasicNameValuePair("personality",RequestUtil.personalitiesCodes[personalitySpinner.getSelectedItemPosition()-1]));
        }
        if(occasionSpinner.getSelectedItemPosition()!=0)
        {
            BrowseParameters.add(new BasicNameValuePair("occasion",RequestUtil.occasionsCodes[occasionSpinner.getSelectedItemPosition()-1]));
        }
        if(!keyword.getText().toString().isEmpty())
        {
            BrowseParameters.add(new BasicNameValuePair("keyword",keyword.getText().toString().replace(" ", "%20")));
        }

        if(BrowseParameters.size()==0)
        {
            BrowseParameters.add(new BasicNameValuePair("category","Rpa"));
        }

        BrowseParameters.add(new BasicNameValuePair("limit", Integer.toString(pageLimit)));
        BrowseParameters.add(new BasicNameValuePair("page", Integer.toString(currentPage)));
        BrowseParameters.add(new BasicNameValuePair("sort", sortOrder));

        RequestUtil.getSearchResult(BrowseParameters, listener);
    }

    //if they pressed back while in the gift list take them back to the search page
    @Override
    public boolean backPressed()
    {
        if(searchView.getVisibility() == View.GONE)
        {
            searchView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
            getActivity().invalidateOptionsMenu();
            return false;
        }
        else
        {
            return true;
        }
    }

    /// add option to clear search items
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(refreshLayout!=null && refreshLayout.getVisibility()==View.GONE && menu!=null)
        {
            menu.add("Clear");
        }
        else
        {
            super.onCreateOptionsMenu(menu, inflater);
        }

    }

    //on clear clicked reset all dropdowns
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(refreshLayout.getVisibility()==View.GONE)
        {
            categorySpinner.setSelection(0);
            recipientSpinner.setSelection(0);
            personalitySpinner.setSelection(0);
            occasionSpinner.setSelection(0);
            keyword.setText("");
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}