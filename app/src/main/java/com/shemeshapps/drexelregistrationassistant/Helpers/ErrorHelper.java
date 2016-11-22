package com.shemeshapps.drexelregistrationassistant.Helpers;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tomer on 11/17/16.
 */

public class ErrorHelper {

    public static void LogError(Exception e, String notes)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sw.toString();


    }
}
