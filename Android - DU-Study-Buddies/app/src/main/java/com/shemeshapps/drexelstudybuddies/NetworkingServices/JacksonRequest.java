package com.shemeshapps.drexelstudybuddies.NetworkingServices;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.shemeshapps.drexelstudybuddies.Helpers.GenAuthorization;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomershemesh on 8/9/14.
 */

//this is to parse the json into java object in background thread
public class JacksonRequest<T> extends JsonRequest<T> {
    private Class<T> responseType;
    private int method;
    private String url;
    private boolean putAuthKey;

    public JacksonRequest(int method, String url, Object requestData, Class<T> responseType, Response.Listener<T> listener, Response.ErrorListener errorListener,boolean putAuthKey)
    {
        super((method==Method.PUT)?Method.POST:method, url, (requestData == null) ? null : Mapper.string(requestData), listener, errorListener);
        this.responseType = responseType;
        this.method = method;
        this.url = url;
        this.putAuthKey = putAuthKey;
        setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(Mapper.objectOrThrow(jsonString, responseType), HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (Exception e)
        {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders()
    {
        Map<String, String> params = new HashMap<>();
        if(method == Method.PUT)
        {
            params.put("X-Http-Method-Override","PUT");
        }
        if(putAuthKey)
        {
            params.put("Authorization", GenAuthorization.GetTokenHeader(url));
        }
        return params;
    }

}
