package com.shemeshapps.drexelstudybuddies.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.shemeshapps.drexelstudybuddies.NetworkingServices.RequestUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by tomershemesh on 5/30/14.
 */
public class GenAuthorization {

    public static String GetTokenHeader()
    {
        return GenAuthorization.GetTokenHeader("https://d1m.drexel.edu/API/v2.0/User/Roles");
    }
    public static String GetTokenHeader(String URL)
    {
        SharedPreferences pref = RequestUtil.context.getSharedPreferences("login_data", Context.MODE_PRIVATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String otime = sdf.format(new Date()) + "+00";
        String seed= URL + otime;

        String hash="";
        try
        {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(pref.getString("auth_key","").getBytes("UTF-8"),mac.getAlgorithm()));
            hash = Base64.encodeToString(mac.doFinal(seed.getBytes()),Base64.NO_WRAP);
        }
        catch (Exception e){}

        return (pref.getString("user_id","") + ":" + hash + ":" + otime);
    }

}
