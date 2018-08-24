package com.shayesapps.gifts.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rosaloves.bitlyj.Url;
import com.shayesapps.gifts.DatabaseHelper.OrmFetcher;
import com.shayesapps.gifts.Models.Gift;
import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

import java.sql.SQLException;

/**
 * Created by tomershemesh on 8/14/14.
 */
public class WebViewFragment extends DialogFragment{

    public Gift gift;

    //custom web popup to allow users to see all gift info without having to leave the actual app
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.web_view,container);
        final ProgressBar progressbar = (ProgressBar)parentView.findViewById(R.id.web_view_progress_loader);

        //set web settings
        final WebView wv = (WebView)parentView.findViewById(R.id.web_view);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);

        final  String url = getArguments().getString("url","");

        //check if we are actually connected to the internet
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //if we are connected create a bitly link in the background for sharing
        if(gift!=null && isConnected)
        {
            new bitlyAsyncTask().execute(url);
        }

        //load website
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        //when done loading remove loading bar
        wv.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                progressbar.setVisibility(View.GONE);
                wv.setVisibility(View.VISIBLE);
            }
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view,url,favicon);
            }
        });

        //on save save the gift to database
        ImageView save = (ImageView)parentView.findViewById(R.id.web_view_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gift!=null)
                {
                    OrmFetcher.saveGift(gift);
                    Toast.makeText(getActivity(), "Gift Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //if its not a gift view dont show save button
        if(gift==null)
        {
            save.setVisibility(View.GONE);
        }

        //back button
        ImageView previous = (ImageView)parentView.findViewById(R.id.web_view_back);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wv.canGoBack())
                {
                    wv.goBack();
                }
            }
        });

        //share current gift with someone
        ImageView share = (ImageView)parentView.findViewById(R.id.web_view_cancel);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlToSend = (gift!=null && gift.bitlyUrl!=null) ? gift.bitlyUrl : url;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String message = "Check out: " + gift.title + " on " + gift.merchantName + "\n\n" + urlToSend;
                intent.putExtra(Intent.EXTRA_TEXT, message);
                //intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check Out This Awesome Gift!");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        //reload page
        ImageView reload = (ImageView)parentView.findViewById(R.id.web_view_reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wv.reload();
            }
        });

        ImageView forward = (ImageView)parentView.findViewById(R.id.web_view_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wv.canGoForward()) {
                    wv.goForward();
                }
            }
        });

        return parentView;
    }


    //class to retrieve bitly link in background
    private class bitlyAsyncTask extends AsyncTask<String, Void, Url> {

        @Override
        protected Url doInBackground(String... params) {
            return RequestUtil.shortenUrl(params[0]);
        }

        @Override
        protected void onPostExecute(Url result) {
            gift.bitlyUrl = result.getShortUrl();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onViewCreated(view, savedInstanceState);
    }


}
