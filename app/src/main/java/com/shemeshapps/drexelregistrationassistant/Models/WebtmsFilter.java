package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tomer on 3/6/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class WebtmsFilter {
    public WebtmsFilter()
    {
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");

    }

    public List<Professor> professors;
    public List<Professor> filteredProfessors;
    public List<String> instruction_types;
    public List<String> filteredInstruction_types;
    public List<String> instruction_methods;
    public List<String> filteredInstruction_methods;
    public List<String> campus;
    public List<String> filteredCampus;

    public int starttime = -1;
    public int endtime = -1;

    public List<String>  days = new ArrayList<>();
    public List<String>  filteredDays;

    public boolean showFull = true;
}
