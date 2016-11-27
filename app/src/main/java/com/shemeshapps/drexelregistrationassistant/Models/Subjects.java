package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

`import org.parceler.Parcel;

/**
 * Created by Tomer on 1/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class Subjects {
    public String subject;
    public String subject_code;
}
