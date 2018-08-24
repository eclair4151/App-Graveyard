package com.shemeshapps.drexeloncampuschallenge.Networking;


import android.content.Context;

import com.shemeshapps.drexeloncampuschallenge.Models.Drexel;
import com.shemeshapps.drexeloncampuschallenge.Models.Participant;
import com.shemeshapps.drexeloncampuschallenge.Models.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Tomer on 1/6/15.
 */
public class Parser {

    public static User ParseUser(String html, Context c)
    {
        if(html.contains("Sign Up or Login"))
        {
            RequestUtil.getInstance(c).logout();
        }
        User u = new User();
        Element body = Jsoup.parse(html).body();
        Elements userInfo = body.getElementById("my-account-info").select("dd");
        u.organization = userInfo.get(0).child(0).text();
        u.name = userInfo.get(1).child(0).text();
        u.email = userInfo.get(2).text();
        u.entries = Integer.parseInt(userInfo.get(3).text());
        u.rank = Integer.parseInt(body.getElementsByClass("user-view-rank").get(0).textNodes().get(0).text());
        u.totalPoints = Integer.parseInt(body.getElementsByClass("user-view-points").get(0).textNodes().get(0).text());
        return u;
    }

    public static Drexel ParseDrexel(String html)
    {
        Drexel d = new Drexel();
        Element body = Jsoup.parse(html).body();
        d.participants = Integer.parseInt(body.getElementsByClass("org-participants").get(0).textNodes().get(0).text().trim());
        d.entries = Integer.parseInt(body.getElementsByClass("org-entries").get(0).textNodes().get(0).text().trim());
        d.position = Integer.parseInt(body.getElementsByClass("org-view-rank").get(0).textNodes().get(0).text());
        d.totalPoints = Integer.parseInt(body.getElementsByClass("org-view-points").get(0).textNodes().get(0).text());
        d.topParticipants = new ArrayList<>();

        Elements rows = body.getElementsByClass("leaders-grid").get(0).select("tr");
        for(Element row:rows)
        {
            Participant p = new Participant();
            p.name = row.select("a").get(0).text();
            p.points = Integer.parseInt(row.getElementsByClass("leaders-activity").get(0).text());
            d.topParticipants.add(p);
        }
        return d;
    }


    public static Boolean LoginCorrect(String html)
    {
        return !html.contains("Sign Up or Login");
    }


}