package com.shemeshapps.drexeloncampuschallenge.Networking;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.shemeshapps.drexeloncampuschallenge.Helpers.SessionHelper;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by shemesht on 8/26/15.
 */
public class CustomCookieManager extends java.net.CookieManager {

    Context c;
    public CustomCookieManager(Context c)
    {
        super();
        this.c = c;
    }


    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        super.put(uri, responseHeaders);
        if(responseHeaders.get("Set-Cookie")!=null && responseHeaders.get("Set-Cookie").size() > 0)
        {
            String sessionid = responseHeaders.get("Set-Cookie").get(0).split(";")[0];
            if(sessionid.contains("PHPSESSID"))
            {
                SessionHelper.saveSessionId(c,sessionid.split("=")[1]);
            }
        }
    }
}
