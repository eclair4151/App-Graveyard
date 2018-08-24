package com.shayesapps.gifts.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.shayesapps.gifts.Adapters.GiftListAdapter;
import com.shayesapps.gifts.DatabaseHelper.OrmFetcher;
import com.shayesapps.gifts.Models.Gift;
import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomershemesh on 8/8/14.
 */

//saved gift view
public class Saved extends Fragment {
    private View parentView;
    List<Gift> saved;
    GiftListAdapter adapter;
    View headerView;
    ListView giftList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.saved_fragment, container, false);
        saved = OrmFetcher.getGifts();
        headerView = getActivity().getLayoutInflater().inflate(R.layout.list_header, null);
        headerView.setOnClickListener(null);
        TextView headerText = (TextView)headerView.findViewById(R.id.header_view_text);
        headerText.setText("No Saved Gifts.");

        adapter = new GiftListAdapter(getActivity(), saved);
        giftList = (ListView) parentView.findViewById(R.id.giftListView);
        giftList.setAdapter(adapter);
        updateHeader();

        //popup brower when clicked on
        giftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RequestUtil.loadPopupBrowser(getFragmentManager(), saved.get(i).merchantStoreUrl, saved.get(i));
            }
        });

        //allow them to hold down and select multiple gifts
        giftList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        giftList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                adapter.toggleSelection(saved.get(position));
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    //allow to delete or share multiple gifts at once
                    case R.id.item_delete:
                        deleteGifts(adapter.getSelection());
                        adapter.clearSelection();
                        mode.finish();
                        break;
                    case R.id.item_send:
                        shareGifts(adapter.getSelection());
                        adapter.clearSelection();
                        mode.finish();
                        break;

                }
                return false;
            }

            //needed defualt functions for selection menu
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, menu);

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.clearSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

        getActivity().invalidateOptionsMenu();
        return parentView;
    }

    private void updateHeader()
    {
        if (saved.size()==0)
        {
            if(giftList.getHeaderViewsCount()==0)
            {
                try
                {
                    giftList.addHeaderView(headerView);
                }
                catch(Exception e){}
            }
        }
        else
        {
            if(giftList.getHeaderViewsCount()!=0)
            {
                try
                {
                    giftList.removeHeaderView(headerView);
                }
                catch (Exception e){}
            }
        }
    }

    //delete gifts from database as well as list
    private void deleteGifts(List<Gift> gifts)
    {
        for(Gift gift:gifts)
        {
            OrmFetcher.deleteGift(gift);
        }
        saved.removeAll(gifts);
        adapter.notifyDataSetChanged();
        updateHeader();
    }

    //share gifts
    private void shareGifts(List<Gift> gifts)
    {
        String html = "<html><body>";
        for(Gift gift:gifts)
        {
            html+="<p><br><b>" + gift.title + "</b></p>";
            html+="<p>" + "Merchant: " + gift.merchantName + "</p>";
            html+="<p>" + "Price: $" + gift.price + "</p>";
            html+="<p>" + "URL: " + ((gift.bitlyUrl!=null)?gift.bitlyUrl:gift.merchantStoreUrl)+ "</p>";
        }
        html+= "</body></html>";

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out these awesome gifts!");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(html));
        startActivity(Intent.createChooser(emailIntent, "Email:"));
    }
}