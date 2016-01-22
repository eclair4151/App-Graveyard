package com.shemeshapps.drexelregistrationassistant.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomer on 1/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ClassInfo {
    public String class_id;
    public String title;
    public String id;
    public String subject_code;
    public String subject;
    public String course_number;
    public float credits_lower_range;
    public float credits_upper_range;
    public String description;
    public boolean writing_intensive;
    public String college;
    public String repeat_status;
    public String prereqs;
    public String restrictions;
    public String coreqs;
    public String term_type;
    public String student_type;
}
