package com.shemeshapps.drexelstudybuddies.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomer on 3/12/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrexelClass {
    public String CourseNumber;
    public String CourseTitle;
    public String SubjectCode;
}
