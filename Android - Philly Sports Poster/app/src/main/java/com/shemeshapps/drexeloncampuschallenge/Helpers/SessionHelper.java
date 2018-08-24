package com.shemeshapps.drexeloncampuschallenge.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.CookieManager;

import com.shemeshapps.drexeloncampuschallenge.Models.Drexel;
import com.shemeshapps.drexeloncampuschallenge.Models.Participant;
import com.shemeshapps.drexeloncampuschallenge.Models.User;
import com.shemeshapps.drexeloncampuschallenge.Networking.RequestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomer on 8/24/15.
 */
public class SessionHelper {
    public static String extractSessionId(){
        String[] cookies = CookieManager.getInstance().getCookie("http://www.oncampuschallenge.org").split("; ");
        for(String cookie:cookies)
        {
            if(cookie.startsWith("PHPSESSID"))
            {
                String sessionId = cookie.split("=")[1]; //finish and return session id
                return sessionId;
            }
        }
        return "";
    }

    public static void saveSessionId(Context c, String sessionId)
    {
        SharedPreferences sharedPref = c.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sessionId", sessionId);
        editor.apply();
        RequestUtil.getInstance(c).sessionId = sessionId;
    }

    public static String getSessionId(Context c)
    {
        SharedPreferences sharedPref = c.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        if(sharedPref.contains("sessionId"))
        {
            return sharedPref.getString("sessionId","");
        }
        return "";
    }

    public static void saveUserInfo(Context c,User u)
    {
        SharedPreferences sharedPref = c.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPref.edit();
        e.putString("name",u.name);
        e.putString("email",u.email);
        e.putInt("entries", u.entries);
        e.putInt("rank", u.rank);
        e.putInt("points", u.totalPoints);
        e.commit();
    }

    public static User getUserInfo(Context c)
    {
        SharedPreferences sharedPref = c.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        User u = new User();
        u.name = sharedPref.getString("name","Name");
        u.email = sharedPref.getString("email","Email");
        u.entries = sharedPref.getInt("entries", 0);
        u.rank = sharedPref.getInt("rank", 0);
        u.totalPoints = sharedPref.getInt("points",0);
        return u;
    }

    public static void saveDrexelInfo(Context c,Drexel d)
    {
        SharedPreferences sharedPref = c.getSharedPreferences("drexel_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPref.edit();
        e.putInt("participants",d.participants);
        e.putInt("entries",d.entries);
        e.putInt("position", d.position);
        e.putInt("points", d.totalPoints);


        SharedPreferences drexelSharedPref = c.getSharedPreferences("drexel_participants", Context.MODE_PRIVATE);
        SharedPreferences.Editor drexelEdit = drexelSharedPref.edit();
        drexelEdit.clear();
        for(Participant p :d.topParticipants)
        {
          drexelEdit.putInt(p.name,p.points);
        }
        drexelEdit.commit();
        e.commit();
    }

    public static Drexel getDrexelInfo(Context c)
    {
        Drexel d = new Drexel();
        SharedPreferences sharedPref = c.getSharedPreferences("drexel_data", Context.MODE_PRIVATE);
        d.participants = sharedPref.getInt("participants",0);
        d.entries = sharedPref.getInt("entries",0);
        d.position = sharedPref.getInt("position",0);
        d.totalPoints = sharedPref.getInt("points",0);
        d.topParticipants = new ArrayList<>();

        SharedPreferences drexelSharedPref = c.getSharedPreferences("drexel_participants", Context.MODE_PRIVATE);
        Map<String,Integer> keys = (Map<String,Integer>)drexelSharedPref.getAll();
        for(Map.Entry<String,Integer> entry : keys.entrySet()){
            Participant p = new Participant();
            p.name = entry.getKey();
            p.points = entry.getValue();
            d.topParticipants.add(p);
        }
        Collections.sort( d.topParticipants);
        return d;
    }

}
