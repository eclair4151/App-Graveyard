package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by tomer on 11/23/16.
 */
@Parcel
public class ClassRegister {
    ArrayList<HTMLClass> classes = new ArrayList<>();
}
