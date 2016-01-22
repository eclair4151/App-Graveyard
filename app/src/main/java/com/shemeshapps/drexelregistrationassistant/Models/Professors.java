package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomer on 1/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Professors {
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
}
