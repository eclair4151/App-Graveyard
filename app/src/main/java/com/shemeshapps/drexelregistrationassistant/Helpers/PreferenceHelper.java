package com.shemeshapps.drexelregistrationassistant.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

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


    public static String getDrexelCookie(Context c)
    {
        SharedPreferences sharedPref = c.getSharedPreferences("watchlist", Context.MODE_PRIVATE);
        return sharedPref.getString("drexelCookies","");
    }

    public static void setDrexelCookie(Context c, String cookie)
    {
        SharedPreferences.Editor sharedPref = c.getSharedPreferences("watchlist", Context.MODE_PRIVATE).edit();
        sharedPref.putString("drexelCookies",cookie);
        sharedPref.commit();
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //Log.d(C.TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            //Log.d(C.TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
}
