package com.shemeshapps.drexelregistrationassistant.Networking;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.shemeshapps.drexelregistrationassistant.Helpers.HTMLParser;
import com.shemeshapps.drexelregistrationassistant.Helpers.PreferenceHelper;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLLogin;
import com.shemeshapps.drexelregistrationassistant.Models.TermPage;
import com.tsums.androidcookiejar.PersistentCookieStore;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import static com.shemeshapps.drexelregistrationassistant.Helpers.HTMLParser.parseTermPage;

/**
 * Created by tomershemesh on 8/9/14.
 */
//custom class for making requests to drexel. mainly made for handling request automation and
// authentication in case the session times out it will auto relogin and relaunch the request
public class DrexelRequest<T> extends Request<T> {

    public static enum requestType{
        TERMPAGE,
        LOGIN,
        LOGINPOST
    };

    private Response.Listener<T> listener;
    private requestType type;
    private RequestUtil requestUtil;
    private int method;
    private String url;
    private Response.ErrorListener errorListener;
    private RequestUtil instance;

    public DrexelRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener, requestType type, RequestUtil requestUtil)
    {
        super(method,url,errorListener);
        setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.type = type;
        this.listener = listener;
        this.requestUtil = requestUtil;
        this.errorListener = errorListener;
        this.method = method;
        this.url = url;
        this.instance = requestUtil;
    }


    @Override
    public Request<?> setRequestQueue(RequestQueue requestQueue) {
        if(requestUtil.getCookieNum("https://connect.drexel.edu") == 0 && type != requestType.LOGIN && type != requestType.LOGINPOST)
        {
            //not logged in don't bother sending request
            Log.e("LOGIN FLOW", "no cookies available. redirecting to login");

            requestUtil.loginWithRequest(new DrexelRequest<>(Request.Method.GET,url,listener,errorListener, type,requestUtil));
            cancel();
        }
        return super.setRequestQueue(requestQueue);

    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {

        T parsedResponse = null;
        String html = new String(response.data);
        Log.e("LOGIN FLOW", "Attemping to parse response");

        if (html.contains("NoSuchFlowExecutionException"))
        {
            Log.e("LOGIN FLOW", "OMFG I GOT A FLOW EXCEPTION!");
            cancel();
            return Response.error(new VolleyError("Login Flow Error"));

        }
        else if((type != requestType.LOGIN && type != requestType.LOGINPOST) && (html.contains("Enter your user ID and password.") || html.contains("User Login")))
        {
            Log.e("LOGIN FLOW", "user not logged in. redirecting to login");

            requestUtil.loginWithRequest(new DrexelRequest<>(method,url,listener,errorListener,type,instance));
            cancel();

            return Response.error(new VolleyError("Not logged into Drexel"));
        }
        else
        {
            switch (type){
                case TERMPAGE:
                    Log.e("LOGIN FLOW", "parsing terms");

                    parsedResponse = (T)HTMLParser.parseTermPage(html);
                    break;
                case LOGIN:
                    parsedResponse = (T)HTMLParser.parseLoginPage(html);
                    break;
                case LOGINPOST:
                    parsedResponse = (T)HTMLParser.parseLoginPagePost(html);
                    break;
            }
            return Response.success(parsedResponse, HttpHeaderParser.parseCacheHeaders(response));
        }
    }


    @Override
    protected void deliverResponse(T response)
    {
        listener.onResponse(response);
    }


}