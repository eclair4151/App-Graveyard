package com.shemeshapps.drexelregistrationassistant.Networking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.shemeshapps.drexelregistrationassistant.Helpers.PreferenceHelper;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.ClassRegister;
import com.shemeshapps.drexelregistrationassistant.Models.Colleges;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLLogin;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLLoginPost;
import com.shemeshapps.drexelregistrationassistant.Models.QueryResult;
import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.TermPage;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsFilter;
import com.tsums.androidcookiejar.PersistentCookieStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomer on 1/3/16.
 */

//file for all json routes. this is getting pretty massive...
public class RequestUtil {
    public Context c;
    private RequestQueue queue;
    private String domain = "http://162.243.241.109/api/v1";
    private Term[] termCache;
    private Colleges[] subjectCache;
    private final String queryTag = "QUERY_ROUTE";

    //this should prod do something at some point...
    private Response.ErrorListener error =  new Response.ErrorListener() {
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
            CookieHandler.setDefault(
                    new CookieManager(
                            PersistentCookieStore.getInstance(c),
                            CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        }
        return instance;
    }

    private static RequestUtil instance = null;
    private RequestUtil() {
    }

    public void getTerms(final Response.Listener<Term[]> listener)
    {
        if(termCache != null)
        {
            listener.onResponse(termCache);
        }
        else
        {
            String url = domain + "/terms";
            queue.add(new JacksonRequest<>(Request.Method.GET, url, null, Term[].class, new Response.Listener<Term[]>() {
                @Override
                public void onResponse(Term[] response) {
                    Arrays.sort(response);
                    termCache = response;
                    listener.onResponse(response);
                }
            }, error));
        }
    }

    public void getSubjects(final Response.Listener<Colleges[]> listener)
    {
        if(subjectCache != null)
        {
            listener.onResponse(subjectCache);
        }
        else
        {
            String url = domain + "/subjects";
            queue.add(new JacksonRequest<>(Request.Method.GET, url, null, Colleges[].class, new Response.Listener<Colleges[]>() {
                @Override
                public void onResponse(Colleges[] response) {
                    subjectCache = response;
                    listener.onResponse(response);
                }
            }, error));
        }
    }

    public void getClassTerms(String classid, final Response.Listener<Term[]> listener)
    {
        String url = domain + "/terms?class_id=" + classid;
        queue.add(new JacksonRequest<>(Request.Method.GET, url, null, Term[].class, new Response.Listener<Term[]>() {
            @Override
            public void onResponse(Term[] response) {
                Arrays.sort(response);
                listener.onResponse(response);
            }
        }, error));
    }

    public void getProfTerms(int prof_id, final Response.Listener<Term[]> listener)
    {
        String url = domain + "/terms?prof_id=" + prof_id;
        queue.add(new JacksonRequest<>(Request.Method.GET, url, null, Term[].class, new Response.Listener<Term[]>() {
            @Override
            public void onResponse(Term[] response) {
                Arrays.sort(response);
                listener.onResponse(response);
            }
        }, error));
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

    public void initCache()
    {
        getTerms(new Response.Listener<Term[]>() {
            @Override
            public void onResponse(Term[] response) {

            }
        });

        getSubjects(new Response.Listener<Colleges[]>() {
            @Override
            public void onResponse(Colleges[] response) {
            }
        });
    }

    public void getRegisteredClasses(String term_id, Response.Listener<ClassRegister> listener, Response.ErrorListener error)
    {

    }

    public void getWatchListClasses(Response.Listener<WebtmsClass[]> listener)
    {
        Set<String> classses = PreferenceHelper.getWatchList(c);
        if(classses.isEmpty())
        {
            listener.onResponse(new WebtmsClass[0]);
        }
        else
        {
            JSONObject phone = new JSONObject();
            try {
                JSONArray arr = new JSONArray(classses);
                phone.put("webtms_ids",arr);
            } catch (JSONException e) {}
            String url = domain  + "/search_webtms";
            queue.add(new JacksonRequest<>(Request.Method.POST, url, phone, WebtmsClass[].class, listener, error));
        }
    }

    public void getWebtmsClasses(String classid, Term t, Response.Listener<WebtmsClass[]> listener)
    {
        String url = domain + "/search_webtms?classid=" + classid + "&term=" + t.term + "&term_type=" + t.term_type +"&term_year=" + t.term_year;
        queue.add(new JacksonRequest<>(Request.Method.GET, url, null, WebtmsClass[].class, listener, error));
    }

    public void getWebtmsClassesFromProf(int prof_id, Term t, Response.Listener<WebtmsClass[]> listener)
    {
        String url = domain + "/search_webtms";
        JSONObject data = new JSONObject();
        try {
            JSONArray arr = new JSONArray();
            arr.put(prof_id);
            data.put("professor_ids",arr);
            data.put("term",t.term);
            data.put("term_type",t.term_type);
            data.put("term_year",t.term_year);

        } catch (JSONException e) {}
        queue.add(new JacksonRequest<>(Request.Method.POST, url, data, WebtmsClass[].class, listener, error));
    }


    public void getHtmlTerms(final Response.Listener<TermPage> listener, final Response.ErrorListener errorListener)
    {
        Log.e("LOGIN FLOW", "attemping to get terms in request util");
        String url = "https://bannersso.drexel.edu/ssomanager/c/SSB?pkg=bwszkfrag.P_DisplayFinResponsibility%3Fi_url%3Dbwskfreg.P_AltPin";

        queue.add(new DrexelRequest<>(Request.Method.GET,url,listener,errorListener, DrexelRequest.requestType.TERMPAGE,this));
    }

    public int getCookieNum(String url)
    {
        try {
            return PersistentCookieStore.getInstance(c).get(new URI(url)).size();

        } catch (URISyntaxException e) {
            return 0;
        }
    }

    public void login(final String username, final String password, final Response.Listener<HTMLLoginPost> listener, final Response.ErrorListener errorListener)
    {
        Log.e("LOGIN FLOW", "attemping to start login flow");
        String url = "https://one.drexel.edu/web/university/academics";
        queue.add(new DrexelRequest<>(Request.Method.GET,url,new Response.Listener<HTMLLogin>() {
            @Override
            public void onResponse(HTMLLogin response) {
                Log.e("LOGIN FLOW", "get login page, attemping to post creds");
                //have to get real exceution thing
                String posturl = response.url;
                queue.add(new DrexelRequest<HTMLLoginPost>(Request.Method.POST,posturl,new Response.Listener<HTMLLoginPost>() {
                    @Override
                    public void onResponse(HTMLLoginPost response) {
                        Log.e("LOGIN FLOW", "got response from sending creds. forwarding along");
                        listener.onResponse(response);
                        if(!response.success)
                        {
                            Log.e("LOGIN FLOW", "AHHH THE LOGIN FAILED");
                            //Toast.makeText(c,"Login Failed Invalid username or password",Toast.LENGTH_SHORT).show();
                        }

                    }
                },errorListener, DrexelRequest.requestType.LOGINPOST,instance){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("j_username",username.replace("@drexel.edu",""));
                        params.put("j_password",password);
                        params.put("_eventId_proceed","");
                        return params;
                    }
                });

            }
        },error, DrexelRequest.requestType.LOGIN,instance));

    }

    public void loginWithRequest(final DrexelRequest request)
    {
        if(!PreferenceHelper.getAutoLogin(c))
        {
            Log.e("LOGIN FLOW", "no saved creds. aborting");
            request.getErrorListener().onErrorResponse(new VolleyError("Not Logged In"));
            return;
        }
        Log.e("LOGIN FLOW", "reached redirect. attempting to login");
        String user = PreferenceHelper.getUserName(c);
        String pass = PreferenceHelper.getPassword(c);
        //Toast.makeText(c,"Session Expired. Re-logging in.",Toast.LENGTH_SHORT).show();
        login(user,pass,new Response.Listener<HTMLLoginPost>() {
            @Override
            public void onResponse(HTMLLoginPost response) {
                Log.e("LOGIN FLOW", "logged in, redirecting to terms page");
                if(response.success)
                {
                    queue.add(request);
                }
                else
                {
                    //something went wrong
                    PreferenceHelper.setAutoLogin(c,false);
                    PreferenceHelper.clearCreds(c);
                    request.getErrorListener().onErrorResponse(new VolleyError("Invalid saved creds"));
                }
            }
        },request.getErrorListener());
    }

    public void getClassesInSubjectTerm(String subject, Term t, Response.Listener<ClassInfo[]> listener)
    {
        try
        {
            String url = domain + "/search_classes?subject=" + URLEncoder.encode(subject,"UTF-8") + "&term=" + t.term + "&termType=" + t.term_type +"&termYear=" + t.term_year;
            queue.add(new JacksonRequest<>(Request.Method.GET, url, null, ClassInfo[].class, listener, error));
        }catch (Exception e){}

    }

    public void getClassFilters(String class_id, Response.Listener<WebtmsFilter> listener)
    {
        String url = domain + "/filters?class_id=" +class_id;
        queue.add(new JacksonRequest<>(Request.Method.GET, url, null, WebtmsFilter.class, listener, error));
    }

    public void runSearchQuery(String query, Response.Listener<QueryResult> listener)
    {
        try
        {
            String url = domain + "/query?query=" + URLEncoder.encode(query,"UTF-8");
            JacksonRequest request = new JacksonRequest<>(Request.Method.GET, url, null, QueryResult.class, listener, error);
            request.setTag(queryTag);
            queue.add(request);
        }catch (Exception e){}
    }

    public void cancelQueryCalls()
    {
        queue.cancelAll(queryTag);
    }
}
