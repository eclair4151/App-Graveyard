package com.shayesapps.gifts.Models;

/**
 * Created by tomershemesh on 8/15/14.
 */
public class BrowseSelection {
    public BrowseSelection(String title, String urlKey, String[] itemList, String[] keyList, int icon)
    {
        this.title = title;
        this.icon = icon;
        this.urlKey = urlKey;
        this.itemList = itemList;
        this.keyList = keyList;
    }
    public String title;
    public int icon;
    public String urlKey;
    public String[] itemList;
    public String[] keyList;
}
