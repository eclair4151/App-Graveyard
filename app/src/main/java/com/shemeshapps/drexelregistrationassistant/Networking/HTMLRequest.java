package com.shemeshapps.drexelregistrationassistant.Networking;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.shemeshapps.drexelregistrationassistant.Helpers.HTMLParser;

import static com.shemeshapps.drexelregistrationassistant.Helpers.HTMLParser.parseTermPage;

/**
 * Created by tomershemesh on 8/9/14.
 */

//this is to parse the json into java object in background thread
public class HTMLRequest<T> extends Request<T> {

    public static enum requestType{
        TERMPAGE,
    };

    private Response.Listener<T> listener;
    private requestType type;

    public HTMLRequest(int method, String url, Response.Listener<T> listener,Response.ErrorListener errorListener,requestType type)
    {
        super(method,url,errorListener);
        setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.type = type;
        this.listener = listener;
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        T parsedResponse = null;
        switch (type){
        case TERMPAGE:
        parsedResponse = (T)HTMLParser.parseTermPage(new String(response.data));
        break;

        }

        return Response.success(parsedResponse, HttpHeaderParser.parseCacheHeaders(response));

    }


    @Override
    protected void deliverResponse(T response)
    {
        listener.onResponse(response);
    }

}




//        extends StringRequest {
//    private Class<T> responseType;
//    private Response.ErrorListener errorListener;
//    private String url;
//
//    public HTMLRequest(int method, String url, Object requestData, Class<T> responseType, Response.Listener listener, Response.ErrorListener errorListener)
//    {
//        super(method, url, listener, errorListener);
//        setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        this.responseType = responseType;
//        this.errorListener = errorListener;
//        this.url = url;
//    }
//
//    @Override
//    protected Response parseNetworkResponse(NetworkResponse response)
//    {
//        try
//        {
//            String jsonString = new String(response.data, "UTF-8");
//            return Response.success(Mapper.objectOrThrow(jsonString, responseType), HttpHeaderParser.parseCacheHeaders(response));
//        }
//        catch (Exception e)
//        {
//            Response<T> res = Response.error(new ParseError(e));
//            return res;
//        }
//    }
//
//
//
//}
