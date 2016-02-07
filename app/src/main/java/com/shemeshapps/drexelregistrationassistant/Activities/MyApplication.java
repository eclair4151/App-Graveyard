package com.shemeshapps.drexelregistrationassistant.Activities;

import android.app.Application;

import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;

//import com.triggerads.SDK;

/**
 * Created by Tomer on 2/13/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        RequestUtil.getInstance(getApplicationContext()).initCache();

    }


}
