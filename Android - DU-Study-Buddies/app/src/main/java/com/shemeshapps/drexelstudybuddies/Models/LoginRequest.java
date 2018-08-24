package com.shemeshapps.drexelstudybuddies.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomer on 2/27/2015.
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class LoginRequest {
    public String UserId;
    public String Password;
}
