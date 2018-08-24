package com.shemeshapps.drexelstudybuddies.Activities;

import android.app.Application;

import com.parse.Parse;
import com.shemeshapps.drexelstudybuddies.NetworkingServices.RequestUtil;

/**
 * Created by Tomer on 2/27/2015.
 */
public class DuApplication extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        RequestUtil.init(this);
    }
}
