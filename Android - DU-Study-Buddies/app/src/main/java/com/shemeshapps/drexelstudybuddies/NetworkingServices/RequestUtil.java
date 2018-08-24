package com.shemeshapps.drexelstudybuddies.NetworkingServices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shemeshapps.drexelstudybuddies.Activities.LoginActivity;
import com.shemeshapps.drexelstudybuddies.Helpers.GenAuthorization;
import com.shemeshapps.drexelstudybuddies.Helpers.Utils;
import com.shemeshapps.drexelstudybuddies.Models.DrexelClass;
import com.shemeshapps.drexelstudybuddies.Models.Group;
import com.shemeshapps.drexelstudybuddies.Models.LoginRequest;
import com.shemeshapps.drexelstudybuddies.Models.LoginResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by tomer on 8/9/14.
 */
public class RequestUtil {
    public static RequestQueue queue;
    public static ImageLoader imageLoader;
    private static Response.ErrorListener errorListener;
    public static Context context;

    public static void init(final Context context)
    {
        RequestUtil.context = context;
        queue = Volley.newRequestQueue(context);
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError.networkResponse!=null)
                {
                        Log.e("Volley Error", Arrays.toString(volleyError.networkResponse.data));
                        Toast.makeText(context, "An error has occurred while making the request", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Please connect to the internet to use this app", Toast.LENGTH_LONG).show();
                }
            }
        };
    }


    public static void getAuthCode(String userid, String password, Response.Listener listener , Response.ErrorListener errorListener)
    {
        String url = "https://d1m.drexel.edu/API/v2.0/Authentication/";
        LoginRequest r = new LoginRequest();
        r.Password = password;
        r.UserId = userid;

        RequestUtil.queue.add(new JacksonRequest<>(Request.Method.PUT,url,r, LoginResponse.class,listener,errorListener,false));
    }

    public static void getUsersCurrentClasses(Response.Listener listener)
    {
        String url = "https://d1m.drexel.edu/API/v2.0/Student/CourseSections";
        RequestUtil.queue.add(new JacksonRequest<>(Request.Method.GET,url,null, DrexelClass[].class,listener,errorListener,true));
    }

    public static void postStudyGroup(Group g,SaveCallback callback)
    {
        ParseObject study = Utils.GroupToParseObject(g);
        study.saveInBackground(callback);
    }


    //takes a comma delimeted list ex cs275,cs283,cs260 or leave empty for all groups
    public static void getStudyGroups(String classNames,FunctionCallback callback)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("classes", classNames);
        params.put("Authorization", GenAuthorization.GetTokenHeader());
        ParseCloud.callFunctionInBackground("QueryGroupsByClass", params, callback);
    }

    public static void deleteStudyGroup(String groupId,FunctionCallback callback)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("GroupId", groupId);
        params.put("Authorization", GenAuthorization.GetTokenHeader());
        ParseCloud.callFunctionInBackground("DeleteStudyGroup", params, callback);
    }

    public static void getMyStudyGroups(FunctionCallback callback)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Authorization", GenAuthorization.GetTokenHeader());
        ParseCloud.callFunctionInBackground("MyStudyGroups", params, callback);
    }

    public static void getAttendingStudyGroups(FunctionCallback callback)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Authorization", GenAuthorization.GetTokenHeader());
        ParseCloud.callFunctionInBackground("AttendingStudyGroups", params, callback);
    }

    public static void joinStudyGroup(ParseObject p, SaveCallback callback)
    {
        List<String> attending = (List<String>) p.get("UsersAttending");
        SharedPreferences pref = context.getSharedPreferences("login_data", Context.MODE_PRIVATE);
        attending.add(pref.getString("user_id",""));
        p.put("UsersAttending",attending);
        p.saveInBackground(callback);
    }

    public static void leaveStudyGroup(ParseObject p, SaveCallback callback)
    {
        List<String> attending = (List<String>) p.get("UsersAttending");
        SharedPreferences pref = context.getSharedPreferences("login_data", Context.MODE_PRIVATE);
        attending.remove(pref.getString("user_id",""));
        p.put("UsersAttending",attending);
        p.saveInBackground(callback);
    }

    public static void logout()
    {
        SharedPreferences pref = context.getSharedPreferences("login_data", Context.MODE_PRIVATE);
        pref.edit().clear().commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
