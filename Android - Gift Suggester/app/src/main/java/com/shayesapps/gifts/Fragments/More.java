package com.shayesapps.gifts.Fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tomershemesh on 8/8/14.
 */
public class More extends Fragment {
    private  View parentView;

    //this class is the more page, which are all just links to facebook, twitter, email, as versoin number
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.more_fragment, container, false);
        TextView versionNumber = (TextView)parentView.findViewById(R.id.more_version_text);
        try
        {
            versionNumber.setText(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            versionNumber.setText("Error: Unknown Version");
        }


        LinearLayout visitWebsite = (LinearLayout)parentView.findViewById(R.id.more_visit_out_website);
        visitWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestUtil.loadPopupBrowser(getFragmentManager(), "http://shayesapps.com",null);
            }
        });

        //check if they have the facebook app to take them to it other wise go to the website
        LinearLayout visitFacebook = (LinearLayout)parentView.findViewById(R.id.more_like_on_facebook);
        visitFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 try{
                    final String url = "fb://page/180816541943369";
                    Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    facebookAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(facebookAppIntent);
                } catch( ActivityNotFoundException e ){
                    RequestUtil.loadPopupBrowser(getFragmentManager(),"https://www.facebook.com/shayesapps",null);
                }
            }
        });

        //same with twitter check if they have the app first
        LinearLayout visitTwitter = (LinearLayout)parentView.findViewById(R.id.more_follow_on_twitter);
        visitTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("twitter://user?screen_name=shayesapps"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(intent);

                }catch (ActivityNotFoundException e) {
                    RequestUtil.loadPopupBrowser(getFragmentManager(),"https://mobile.twitter.com/shayesapps",null);
                }
            }
        });

        //go to amazon store or play store
        LinearLayout visitReviewPage = (LinearLayout)parentView.findViewById(R.id.more_write_review);
        visitReviewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=" + appPackageName)));
                }
            }
        });

        //send email
        LinearLayout tellAFriend = (LinearLayout)parentView.findViewById(R.id.more_tell_friend);
        tellAFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                String body = convertStreamToString(getResources().openRawResource(R.raw.share_email_body));
                emailIntent.setType("text/html");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out Gift Suggester for iOS and Android!");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
                startActivity(Intent.createChooser(emailIntent, "Email:"));

            }
        });

        LinearLayout visitContactUs = (LinearLayout)parentView.findViewById(R.id.more_more_contact_us);
        visitContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String version = "";
                try
                {
                    version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName;
                }
                catch (PackageManager.NameNotFoundException e)
                {
                   version = "error";
                }

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","support@shayesapps.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Gift Suggester for Android Build: " + Build.VERSION.SDK_INT + " v" +version);
                startActivity(Intent.createChooser(emailIntent, ""));
            }
        });
        return parentView;
    }

    //used for email format
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}