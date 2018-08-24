package com.shayesapps.gifts.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by tomershemesh on 8/9/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gift {

    @DatabaseField(generatedId = true)
    public int _id;

    @DatabaseField
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField
    public String smallProductImageUrl;

    @DatabaseField
    public String mediumProductImageUrl;

    @DatabaseField
    public String merchantName;

    @DatabaseField
    public String price;

    @DatabaseField
    public String merchantStoreUrl;

    @DatabaseField
    public String bitlyUrl;

    public Gift(){};

}
