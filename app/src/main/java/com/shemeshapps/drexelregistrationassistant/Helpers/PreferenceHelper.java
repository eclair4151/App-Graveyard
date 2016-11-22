package com.shemeshapps.drexelregistrationassistant.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomer on 1/11/16.
 */
public class PreferenceHelper {
    public static Set<String> getWatchList(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        return sharedPref.getStringSet("watchlist_ids",new HashSet<String>());
    }

    public static void AddToWatchList(WebtmsClass webtmsClass,Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> classes = sharedPref.getStringSet("watchlist_ids",new HashSet<String>());
        classes.add(Integer.toString(webtmsClass.id));
        editor.putStringSet("watchlist_ids",classes);
        editor.commit();
    }

    public static void RemoveFromWatchList(WebtmsClass webtmsClass,Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> classes = sharedPref.getStringSet("watchlist_ids",new HashSet<String>());
        classes.remove(Integer.toString(webtmsClass.id));
        editor.putStringSet("watchlist_ids",classes);
        editor.commit();
    }


    public static boolean IsInWatchList(WebtmsClass webtmsClass,Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        return sharedPref.getStringSet("watchlist_ids",new HashSet<String>()).contains(Integer.toString(webtmsClass.id));
    }


}
