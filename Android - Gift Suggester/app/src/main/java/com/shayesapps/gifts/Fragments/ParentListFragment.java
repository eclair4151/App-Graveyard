package com.shayesapps.gifts.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shayesapps.gifts.Adapters.GiftListAdapter;
import com.shayesapps.gifts.DatabaseHelper.OrmFetcher;
import com.shayesapps.gifts.MainActivity;
import com.shayesapps.gifts.Models.Gift;
import com.shayesapps.gifts.Models.SearchResult;
import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

/**
 * Created by tomershemesh on 8/14/14.
 */

//abstarct fragment because many of the pages have the same format. this is to avoid duplication of request code and gift code etc
public abstract class ParentListFragment extends Fragment {

    protected View parentView;
    protected int pageLimit = 25;

    protected GiftListAdapter adapter;
    protected ListView giftListView;
    protected Response.Listener<SearchResult> listener;
    protected TextView footerPageInfo;
    protected View footerView;
    protected SwipeRefreshLayout refreshLayout;
    protected List<Gift> gifts = new ArrayList<Gift>();
    protected int totalResults = 0;
    protected int currentPage = 0;
    protected String sortOrder = "popularity";
    protected int sortOrderIndex = 0;
    protected View headerView;

    //when a list of gifts is sent back populate the list he wave
    protected void listOfGiftsReturned(SearchResult response)
    {
        if(response!=null)
        {
            gifts.clear();
            gifts.addAll(response.products);
            for(Gift gift:gifts)
            {
                gift.title = RequestUtil.removeHTMLCodes(gift.title);
                gift.description = RequestUtil.removeHTMLCodes(gift.description);
            }

            adapter.notifyDataSetChanged();

            //if no gifts say no gifts
            if(adapter.getCount()==0)
            {
                if(giftListView.getFooterViewsCount()!=0)
                {
                    try
                    {
                        giftListView.removeFooterView(footerView);
                    }
                    catch (Exception e){}
                }
                if(giftListView.getHeaderViewsCount()==0)
                {
                    try
                    {
                       giftListView.addHeaderView(headerView);
                    }
                    catch (Exception e){}
                }

            }
            else
            {
                if(giftListView.getFooterViewsCount()==0)
                {
                    giftListView.addFooterView(footerView);
                    totalResults = response.productsMatched;
                }

                if(giftListView.getHeaderViewsCount()!=0)
                {
                    try
                    {
                        giftListView.removeHeaderView(headerView);
                    }
                    catch(Exception e)
                    {

                    }
                }

                //set up bottom paging
                int resultLow = (response.page*response.limit)+1;
                int resultHigh = resultLow+response.productsReturned-1;
                footerPageInfo.setText(resultLow + "-" + resultHigh + " of " + response.productsMatched);
                giftListView.setSelectionAfterHeaderView();
            }
        }

        refreshLayout.setRefreshing(false);
    }


    //setup initial view
    protected void setupView()
    {
        currentPage =0;
        giftListView = (ListView) parentView.findViewById(R.id.giftListView);
        adapter = new GiftListAdapter(getActivity(), gifts);
        giftListView.setAdapter(adapter);

        //load popup brower when gift is clicked on
        giftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<gifts.size())
                {
                    RequestUtil.loadPopupBrowser(getFragmentManager(), gifts.get(i).merchantStoreUrl, gifts.get(i));
                }
            }
        });
        setupFooterView();
        refreshLayout = (SwipeRefreshLayout)parentView.findViewById(R.id.giftListSwipeRefresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeRequest();
            }
        });


        listener = new Response.Listener<SearchResult>() {
            @Override
            public void onResponse(SearchResult response) {
                listOfGiftsReturned(response);
            }
        };
        setHasOptionsMenu(true);
    }


    //setup the menu for going back and forth between pages
    private void setupFooterView()
    {
        headerView = getActivity().getLayoutInflater().inflate(R.layout.list_header, null);
        headerView.setOnClickListener(null);
        footerView = getActivity().getLayoutInflater().inflate(R.layout.page_navigation_footer, null);
        footerPageInfo = (TextView)footerView.findViewById(R.id.page_footer_page_info);
        ImageView nextButton = (ImageView)footerView.findViewById(R.id.page_footer_next_button);
        ImageView previousButton = (ImageView)footerView.findViewById(R.id.page_footer_previous_button);
        ImageView firstButton = (ImageView)footerView.findViewById(R.id.page_footer_first_button);
        ImageView lastButton = (ImageView)footerView.findViewById(R.id.page_footer_last_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage<totalResults/pageLimit)
                {
                    currentPage++;
                    makeRequest();
                }
            }
        });

        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage=totalResults/pageLimit;
                makeRequest();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage>0)
                {
                    currentPage--;
                    makeRequest();
                }
            }
        });

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage=0;
                makeRequest();
            }
        });
        footerView.setOnClickListener(null);
    }

    public boolean backPressed(){

        return true;
    }

    //add option on all windows to sort the gifts
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("Sort");
        super.onCreateOptionsMenu(menu, inflater);
    }

    //menu to sort gifts base on different factors
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final String[] sortOptions = {"Popularity","Relevance","Price"};
        final ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice, sortOptions);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Sort by:");
        alertDialogBuilder.setSingleChoiceItems(sortAdapter, sortOrderIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sortOrderIndex = i;
                sortOrder = sortOptions[i].toLowerCase();
                makeRequest();
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return super.onOptionsItemSelected(item);
    }

    protected abstract void makeRequest();
}
