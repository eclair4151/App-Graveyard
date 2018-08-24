package com.shemeshapps.drexelregistrationassistant.Helpers;

import android.util.Log;

import com.shemeshapps.drexelregistrationassistant.Models.ClassRegister;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLClass;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLLogin;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLLoginPost;
import com.shemeshapps.drexelregistrationassistant.Models.HTMLOption;
import com.shemeshapps.drexelregistrationassistant.Models.TermPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by tomer on 12/5/16.
 */
//parse responses from drexel
public class HTMLParser {
    public static TermPage parseTermPage(String html)
    {
        TermPage tp =new TermPage();
        Element body = Jsoup.parse(html).body();
        Elements terms = body.select("option");
        for(Element t : terms)
        {
            tp.terms.add(new HTMLOption(t.text(),t.attr("value")));
            t.text();
        }
        return tp;
    }

    public static HTMLLogin parseLoginPage(String html)
    {
        HTMLLogin res = new HTMLLogin(html,"");

        Element body = Jsoup.parse(html).body();
        Elements s = body.select("form");
        if(s.size() == 1)
        {
            res.url = "https://connect.drexel.edu" + s.get(0).attr("action");
        }
        return res;
    }

    public static ClassRegister parseClassPage(String html)
    {
        ClassRegister res = new ClassRegister();

//        Element body = Jsoup.parse(html).body();
//        Elements classes = body.select("table[class=dataentrytable] tr");
//        //remove first element which is a header
//        classes.remove(0);
//        for(Element dclass:classes)
//        {
//            HTMLClass c = new HTMLClass();
//            c.status = dclass.child(0).text();
//            c.subject = dclass.child(3).text();
//            c.course = dclass.child(4).text();
//        }
        return res;
    }


    public static HTMLLoginPost parseLoginPagePost(String html)
    {
        HTMLLoginPost res = new HTMLLoginPost();
        res.success = !(html.contains("The user ID or password you entered is incorrect") || html.contains("DREXEL CONNECT"));
        return res;
    }
}
