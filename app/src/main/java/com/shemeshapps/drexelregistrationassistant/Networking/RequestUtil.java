package com.shemeshapps.drexelregistrationassistant.Networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Tomer on 1/3/16.
 */
public class RequestUtil {
    Context c;
    RequestQueue queue;
    String domain = "http://107.170.133.244/api/v1";
    Response.ErrorListener error =  new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    public static RequestUtil getInstance(Context c)
    {
        if(instance == null) {
            instance = new RequestUtil();
            instance.c = c;
            instance.queue = Volley.newRequestQueue(c);
        }
        return instance;
    }

    private static RequestUtil instance = null;
    private RequestUtil() {
    }

    public void sendPushToken(String token)
    {
        JSONObject phone = new JSONObject();
        try {
            phone.put("device_token",token);
            phone.put("device_id","654");
        } catch (JSONException e) {}
        String url = domain  + "/users";
        queue.add(new JacksonRequest<>(Request.Method.POST, url, phone, Object.class, new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        );
    }

    public void getWebtmsClasses(String classid, Term t, Response.Listener listener)
    {
        String url = domain + "/search_webtms?classid=" + classid + "&term=" + t.term + "&term_type=" + t.term_type +"&term_year=" + t.term_year;
        queue.add(new JacksonRequest<>(Request.Method.GET, url, null, WebtmsClass[].class, listener, error));
    }

    public void autoCompleteClasses(String query, Response.Listener listener)
    {
        try {
            String url = domain + "/search_classes?query=" + URLEncoder.encode(query,"UTF-8");
            queue.add(new JacksonRequest<>(Request.Method.GET, url, null, ClassInfo[].class, listener, error));

        } catch (UnsupportedEncodingException e) {
        }
    }
}
