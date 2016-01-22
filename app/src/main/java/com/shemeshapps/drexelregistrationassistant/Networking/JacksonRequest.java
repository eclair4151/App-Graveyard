package com.shemeshapps.drexelregistrationassistant.Networking;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

/**
 * Created by tomershemesh on 8/9/14.
 */

//this is to parse the json into java object in background thread
public class JacksonRequest<T> extends JsonRequest<T> {
    private Class<T> responseType;
    private Response.ErrorListener errorListener;
    private String url;

    public JacksonRequest(int method, String url, Object requestData, Class<T> responseType, Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        super(method, url, (requestData == null) ? null : Mapper.string(requestData), listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.responseType = responseType;
        this.errorListener = errorListener;
        this.url = url;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, "UTF-8");
            return Response.success(Mapper.objectOrThrow(jsonString, responseType), HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (Exception e)
        {
            return Response.error(new ParseError(e));
        }
    }



}
