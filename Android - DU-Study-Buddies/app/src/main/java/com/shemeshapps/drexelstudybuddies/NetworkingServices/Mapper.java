package com.shemeshapps.drexelstudybuddies.NetworkingServices;

/**
 * Created by tomershemesh on 8/9/14.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Singleton wrapper class which configures the Jackson JSON parser.
 */

//map json to java class
public final class Mapper
{
    private static ObjectMapper MAPPER;

    public static ObjectMapper get()
    {
        if (MAPPER == null)
        {
            MAPPER = new ObjectMapper();
            MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }

        return MAPPER;
    }

    public static String string(Object data)
    {
        try
        {
            if(data instanceof String)
            {
                return (String)data;
            }
            else if(data instanceof JSONObject)
            {
                return data.toString();
            }
            else
            {
                return get().writeValueAsString(data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectOrThrow(String data, Class<T> type) throws JsonParseException, JsonMappingException, IOException
    {
        return get().readValue(data, type);
    }

    public static <T> T object(String data, Class<T> type)
    {
        try
        {
            return objectOrThrow(data, type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}