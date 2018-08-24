package com.tomer.drexelgpacalc;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by tomershemesh on 6/3/14.
 */
public class ItEventParser {

    public static String parseClasses(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements tbod = doc.getElementsByClass("datadisplaytable").get(0).child(1).children();
        String total = "";
        int index =0;

        for(ClassList[] year:MainActivity.terms)
        {
            for(ClassList term:year)
            {
                term.removeAllClasses();
            }
        }

        while(!(tbod.get(index).children().size() >0 && tbod.get(index).child(0).childNode((0)).toString().contains("INSTITUTION CREDIT")))
        {
            index++;
        }
        //now we are at the top of the document may not be needed

        int year =-1;
        String currentYear ="";
        while(!(tbod.get(index).children().size() >0 && tbod.get(index).child(0).childNode(0).toString().contains("COURSES IN PROGRESS")))
        {
            if(tbod.get(index).children().size() >0 && tbod.get(index).child(0).childNode(0).attr("class").equals("fieldOrangetextbold"))
            {
                int term =0;
                String yearTerm = tbod.get(index).child(0).childNode(0).childNode(0).toString();
                total+= "\n\n"+ yearTerm;
                if (yearTerm.contains("Fall"))
                    term = 0;
                else if(yearTerm.contains("Winter"))
                    term = 1;
                else if(yearTerm.contains("Spring"))
                    term = 2;
               else if(yearTerm.contains("Summer"))
                    term = 3;

                if(!currentYear.equals(yearTerm.substring(Math.max(yearTerm.length() - 2, 0))))
                {
                    year++;
                    currentYear=yearTerm.substring(Math.max(yearTerm.length() - 2, 0));
                }
                while(!tbod.get(index).child(0).childNode(0).toString().contains("Subject"))
                {
                    index++;

                }
                index++;

                while(!(tbod.get(index).child(0).childNode(0).toString().contains("Term Totals")))
                {
                    String name = tbod.get(index).child(0).childNode(0).toString();
                    String num =  tbod.get(index).child(1).childNode(0).toString();
                    //String def =  tbod.get(index).child(3).childNode(0).toString();
                    String grade =  tbod.get(index).child(4).childNode(0).toString();
                    String credits = tbod.get(index).child(5).childNode(0).childNode(0).toString().replaceAll(" ","").replaceAll("\n","");
                    MainActivity.terms[year][term].addClass(name+num , grade, Float.parseFloat(credits));
                    //MainActivity.terms[year][term].addClass("test", "A-", 4);


                    MainActivity.refreshview();
                    total+="\n" + name+" "+num+" "+" "+credits+" "+ grade;
                    index++;
                }
            }
            else
            {
                index++;
            }


        }
        MainActivity.refreshview();
        return total;
    }
}

