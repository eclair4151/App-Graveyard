package com.shemeshapps.drexelregistrationassistant.Helpers;

import com.shemeshapps.drexelregistrationassistant.Models.TermPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Created by tomer on 12/5/16.
 */

public class HTMLParser {
    public static TermPage parseTermPage(String html)
    {
        Element body = Jsoup.parse(html).body();

        return new TermPage();
    }
}
