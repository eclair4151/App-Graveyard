package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by tomer on 11/18/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class QueryResult {
    public List<WebtmsClass> CRNS;
    public List<Professor> Professors;
    public List<ClassInfo> Classes;
}
