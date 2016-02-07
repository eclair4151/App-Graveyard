package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomer on 1/22/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Term {
    public Term(String term,String term_type,String term_year)
    {
        this.term = term;
        this.term_type = term_type;
        this.term_year = term_year;
    }
    public String term;
    public String term_type;
    public String term_year;

    @Override
    public String toString() {
        return term + " " + term_type + " " + term_year;
    }
}
