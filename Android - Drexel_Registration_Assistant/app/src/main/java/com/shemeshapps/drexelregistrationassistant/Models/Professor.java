package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

/**
 * Created by Tomer on 1/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class Professor {
    public int id;
    public String name;
    public float rate_my_prof_rating;
    public int rate_my_prof_num_ratings;
    public String rate_my_prof_url;
    public String google_url;
    public String first_last_name;
    public float koofers_rating;
    public int koofers_num_ratings;
    public String koofers_url;
    public float total_rating;

    @Override
    public String toString()
    {
        return first_last_name;
    }

    @Override
    public boolean equals(Object p)
    {
        return (p instanceof Professor && ((Professor)p).id == id);
    }

    @Override
    public int hashCode() {
        return id;
    }




}
