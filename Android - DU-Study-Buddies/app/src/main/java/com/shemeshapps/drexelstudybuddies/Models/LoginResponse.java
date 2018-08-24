package com.shemeshapps.drexelstudybuddies.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomer on 3/5/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    public String AuthKey;
    public String Expiration;
    public String UserId;
    public String[] Roles;
}
