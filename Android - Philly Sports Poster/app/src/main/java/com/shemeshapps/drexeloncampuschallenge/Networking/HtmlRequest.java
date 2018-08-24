package com.shemeshapps.drexeloncampuschallenge.Networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.shemeshapps.drexeloncampuschallenge.Helpers.SessionHelper;
import com.shemeshapps.drexeloncampuschallenge.Models.Drexel;
import com.shemeshapps.drexeloncampuschallenge.Models.User;


/**
 * Created by Tomer on 2/16/15.
 */
public class HtmlRequest<T> extends Request<T> {
    public static enum requestType{
        USER,
        LOGIN,
        DREXEL
    };

    private Context mContext;
    Response.Listener listener;
    requestType type;

    public HtmlRequest(int method, String url, Response.Listener listener,Response.ErrorListener errorListener,requestType type, Context c)
    {
        super(method,url,errorListener);
        this.type = type;
        this.listener = listener;
        this.mContext = c;
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        T parsedResponse = null;
        switch (type){
            case USER:
                parsedResponse = (T)Parser.ParseUser(new String(response.data),mContext);
                SessionHelper.saveUserInfo(mContext, (User)parsedResponse);
                break;
            case LOGIN:
                parsedResponse = (T)Parser.LoginCorrect(new String(response.data));

                break;
            case DREXEL:
                parsedResponse = (T)Parser.ParseDrexel(new String(response.data));
                SessionHelper.saveDrexelInfo(mContext, (Drexel) parsedResponse);
                break;


        }
        return Response.success(parsedResponse, HttpHeaderParser.parseCacheHeaders(response));
    }


    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}