package com.shemeshapps.drexeloncampuschallenge.Helpers;

import com.shemeshapps.drexeloncampuschallenge.Models.SpinnerItem;

import java.util.ArrayList;

/**
 * Created by Tomer on 9/7/15.
 */
public class GetSpinnerItemHelper {

    public static ArrayList<SpinnerItem> getActivities()
    {
        ArrayList<SpinnerItem> s = new ArrayList<>();
        s.add(new SpinnerItem("Adventure Racing","385"));
        s.add(new SpinnerItem("Backpacking","393"));
        s.add(new SpinnerItem("Biking","386"));
        s.add(new SpinnerItem("Birdwatching/wildlife watching","387"));
        s.add(new SpinnerItem("Camping","389"));
        s.add(new SpinnerItem("Climbing","390"));
        s.add(new SpinnerItem("Fishing","391"));
        s.add(new SpinnerItem("Gardening (Chores don't count!)","395"));
        s.add(new SpinnerItem("Geocaching","388"));
        s.add(new SpinnerItem("Hammocking","407"));
        s.add(new SpinnerItem("Hiking","392"));
        s.add(new SpinnerItem("Outdoor clinics/training/events","403"));
        s.add(new SpinnerItem("Outdoor games (frisbee, horseshoes, etc.)","404"));
        s.add(new SpinnerItem("Paddling","398"));
        s.add(new SpinnerItem("Restoration and trail maintenance","394"));
        s.add(new SpinnerItem("Running/jogging","397"));
        s.add(new SpinnerItem("Skateboarding","402"));
        s.add(new SpinnerItem("Snowsports","400"));
        s.add(new SpinnerItem("Stargazing","405"));
        s.add(new SpinnerItem("Walking - fun and fitness","396"));
        s.add(new SpinnerItem("Water activities (outdoor)","399"));
        s.add(new SpinnerItem("Yoga (outdoor)","401"));

        return s;
    }

    public static ArrayList<SpinnerItem> getTimes()
    {
        ArrayList<SpinnerItem> s = new ArrayList<>();
        s.add(new SpinnerItem("30 mins","30min"));
        s.add(new SpinnerItem("60 mins","60min"));
        s.add(new SpinnerItem("90 mins","90min"));
        s.add(new SpinnerItem("150 mins","150min"));
        s.add(new SpinnerItem("180 mins","18min"));
        s.add(new SpinnerItem("2+ hours","2+hours"));

        return s;
    }

}
