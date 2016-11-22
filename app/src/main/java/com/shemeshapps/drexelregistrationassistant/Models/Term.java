package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Tomer on 1/22/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class Term implements Comparable<Term>{
    public Term()
    {

    }


    public Term(String term,String term_type,String term_year)
    {
        this.term = term;
        this.term_type = term_type;
        this.term_year = term_year;
    }

    public int getTermValue()
    {
        int totalVal = Integer.parseInt(term_year.split("-")[0]) * 10;

        switch (term){
            case "Fall":
                totalVal += 1;
                break;
            case "Winter":
                totalVal += 2;
                break;
            case "Spring":
                totalVal += 3;
                break;
            case "Summer":
                totalVal += 4;
                break;
        }
        return totalVal;
    }

    public String term;
    public String term_type;
    public String term_year;

    @Override
    public String toString() {
        return term + " " + term_type + " " + term_year;
    }


    @Override
    public int compareTo(Term another) {
        return  another.getTermValue()- getTermValue();

    }
}
