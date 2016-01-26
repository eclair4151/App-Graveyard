package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Tomer on 1/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class WebtmsClass {
    public int id;
    public String crn;
    public String class_id;
    public String instruction_type;
    public String instruction_method;
    public String section;
    public Date begin_time;
    public Date end_time;
    public int max_enroll;
    public int current_enroll;
    public float credits;
    public String campus;
    public String section_comments;
    public String textbook_link;
    public String building;
    public String room;
    public String term;
    public String term_type;
    public String term_year;
    public Date updated_at;
    public Professors[] professors;
    public WebtmsDays[] webtms_days;
    public String days_time_string;



}
