package com.shayesapps.gifts.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by tomershemesh on 8/9/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    public int page;
    public int limit;
    public String recipient;
    public String category;
    public String personality;
    public String occasion;
    public String keyword;
    public String lowPrice;
    public String highPrice;
    public String sort;
    public int productsMatched;
    public int productsReturned;
    public String productsMatchedLowPrice;
    public String productsMatchedHighPrice;
    public List<Gift> products;
    public int responseCode;
    public String responseMessage;
    public String responseDetail;
}
