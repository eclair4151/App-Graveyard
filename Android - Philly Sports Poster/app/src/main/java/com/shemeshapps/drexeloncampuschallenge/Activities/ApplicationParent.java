package com.shemeshapps.drexeloncampuschallenge.Activities;

import android.app.Application;

import com.shemeshapps.drexeloncampuschallenge.Networking.RequestUtil;

/**
 * Created by Tomer on 8/24/15.
 */
public class ApplicationParent extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        RequestUtil.getInstance(getApplicationContext());
    }
}
