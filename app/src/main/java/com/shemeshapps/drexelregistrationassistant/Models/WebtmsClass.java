package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public List<Professor> professors;
    public String days_time_string;

    public boolean passesFilter(WebtmsFilter filter)
    {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(begin_time);
        int beg = rightNow.get(Calendar.HOUR_OF_DAY)*100 + rightNow.get(Calendar.MINUTE);
        rightNow.setTime(end_time);
        int end = rightNow.get(Calendar.HOUR_OF_DAY)*100 + rightNow.get(Calendar.MINUTE);

        if(filter.filteredProfessors != null)
        {
            List<Professor> incommon = new ArrayList<>();
            incommon.addAll(professors);
            incommon.retainAll(filter.filteredProfessors);
            if(incommon.size() == 0)
            {
                return false;
            }
        }

        if(filter.filteredCampus!=null && !filter.filteredCampus.contains(campus))
        {
            return false;
        }

        if(filter.filteredInstruction_methods!=null && !filter.filteredInstruction_methods.contains(instruction_method))
        {
            return false;
        }

        if(filter.filteredInstruction_types!=null && !filter.filteredInstruction_types.contains(instruction_type))
        {
            return false;
        }

        List<String> classDays = Arrays.asList(days_time_string.split(" ")[0].split("(?!^)"));
        if(filter.filteredDays!=null && !filter.filteredDays.containsAll(classDays))
        {
            return false;
        }

        if((filter.starttime != -1 && beg < filter.starttime) || (filter.endtime!= -1 && end > filter.endtime))
        {
            return false;
        }

        if(!filter.showFull && current_enroll == max_enroll)
        {
            return false;
        }


        return true;
    }



}
