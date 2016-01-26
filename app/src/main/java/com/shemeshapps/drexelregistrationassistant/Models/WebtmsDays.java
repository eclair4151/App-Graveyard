package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomer on 1/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class WebtmsDays implements Comparable<WebtmsDays> {
    public String day;

    @Override
    public int compareTo(WebtmsDays webtmsDays) {
        return dayOrder.get(day).compareTo(dayOrder.get(webtmsDays.day));
    }

    public String getDayChar()
    {
        return dayCharMap.get(day);
    }

    public WebtmsDays()
    {
        dayOrder.put("Monday",1);
        dayOrder.put("Tuesday",2);
        dayOrder.put("Wednesday",3);
        dayOrder.put("Thursday",4);
        dayOrder.put("Friday",5);
        dayOrder.put("Saturday",6);

        dayCharMap.put("Monday","M");
        dayCharMap.put("Tuesday","T");
        dayCharMap.put("Wednesday","W");
        dayCharMap.put("Thursday","R");
        dayCharMap.put("Friday","F");
        dayCharMap.put("Saturday","S");
    }
    private Map<String,Integer> dayOrder = new HashMap<String,Integer>();
    private Map<String,String> dayCharMap = new HashMap<String,String>();

}
