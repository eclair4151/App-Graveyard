package com.shemeshapps.drexeloncampuschallenge.Networking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shemeshapps.drexeloncampuschallenge.Activities.LoginActivity;
import com.shemeshapps.drexeloncampuschallenge.Helpers.SessionHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomer on 8/24/15.
 */
public class RequestUtil {
    private  static RequestUtil instance = null;
    private  Context mContext;
    private  RequestQueue queue;
    private  ImageLoader imageLoader;
    public  String sessionId;

    public static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    protected RequestUtil(Context context)
    {
        this.mContext = context;
        queue = Volley.newRequestQueue(context); //do any cookie loading here
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);
        CustomCookieManager manager = new CustomCookieManager(context);
        CookieHandler.setDefault(manager);
        sessionId = SessionHelper.getSessionId(context);

    }
    public static RequestUtil getInstance(Context context){
        if(instance==null)
        {
            instance = new RequestUtil(context);
        }
        return instance;
    }


    public void GetDrexelInfo(Response.Listener listener)
    {
        String url = "http://www.oncampuschallenge.org/organizations/2028";
        HtmlRequest r = new HtmlRequest(Request.Method.GET,url,listener,errorListener,HtmlRequest.requestType.DREXEL,mContext){

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", "PHPSESSID=" + sessionId + ";");
                return headers;
            }

        };
        queue.add(r);
    }

    public void GetUserInfoWithSavedSession(Response.Listener listener)
    {
        GetUserInfo(listener, sessionId);
    }


    public void GetUserInfo(Response.Listener listener,final String sessionIdOverride)
    {

        String url = "http://www.oncampuschallenge.org/users/account";
        HtmlRequest r = new HtmlRequest(Request.Method.GET,url,listener,errorListener,HtmlRequest.requestType.USER,mContext){

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", "PHPSESSID=" + sessionIdOverride + ";");
                return headers;
            }

        };
        queue.add(r);
    }

    public void Login(final Response.Listener listener, final String email, final String password)
    {
        String url = "http://www.oncampuschallenge.org/users/signup";
        HtmlRequest r = new HtmlRequest(Request.Method.POST, url, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                if(((Boolean)response).booleanValue()) //correct username and pass
                {
                    GetUserInfoWithSavedSession(listener);
                }
                else
                {
                    listener.onResponse(null);
                }
            }
        }, errorListener, HtmlRequest.requestType.LOGIN,mContext){

                    @Override
                    protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("lemail",email);
                    params.put("lpassword",password);
                    params.put("submit", "Log In");
                    params.put("hid_form","login");

                    return params;
                }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
            };
        queue.add(r);
    }



    public void PostPhoto(final Response.Listener listener,final String name,final String activityId,final String description,final String timeId,final String date,final File f)
    {
        new AsyncTask<Void,Void,String>()
        {
            @Override
            protected String doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.oncampuschallenge.org/entry/submit");
                HttpResponse response = null;
                try {
                    MultipartEntity entity = new MultipartEntity();

                    entity.addPart("entryTitle", new StringBody(name));
                    entity.addPart("categoryId", new StringBody(activityId));
                    entity.addPart("entryText", new StringBody(description));
                    entity.addPart("MAX_FILE_SIZE", new StringBody("10485760"));
                    entity.addPart("entryPhoto", new FileBody(f));
                    entity.addPart("custom_activity_time", new StringBody(timeId));
                    entity.addPart("custom_activity_date", new StringBody(date));
                    entity.addPart("custom_terms", new StringBody("1"));
                    httppost.setEntity(entity);
                    httppost.addHeader("Cookie", "PHPSESSID=" + sessionId + ";");
                    response = httpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                try {
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                } catch (IOException e) {
                    return "Please fix the following errors:";
                }
            }

            @Override
            protected void onPostExecute(String r) {
                super.onPostExecute(r);

                if( r.contains("Please fix the following errors:"))
                {
                    listener.onResponse(null);
                }
                else
                {
                    listener.onResponse(r);
                }

            }
        }.execute();

    }

    public void logout()
    {
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        queue.getCache().clear();
        mContext.getSharedPreferences("drexel_data", Context.MODE_PRIVATE).edit().clear().commit();
        mContext.getSharedPreferences("drexel_participants", Context.MODE_PRIVATE).edit().clear().commit();;
        mContext.getSharedPreferences("user_data", Context.MODE_PRIVATE).edit().clear().commit();;
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("finish", true); // if you are checking for this in your other Activities
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        String url = "http://www.oncampuschallenge.org/users/logout";
        StringRequest r = new StringRequest(Request.Method.GET, url, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, errorListener){

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", "PHPSESSID=" + sessionId + ";");
                return headers;
            }

        };
        queue.add(r);
    }

}




