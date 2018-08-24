package com.shemeshapps.waterproof;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by Tomer on 3/18/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("aLPXpgbcYgQ5Eh0wx9jVqRByXx5ivKp6hakSA0ia")
                .clientKey("gLwDMNGYcVaGBn99rt3GduJdzn15DRFEiEqDCqj7").build());

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
