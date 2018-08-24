package com.tomer.drexelgpacalc;

import java.util.ArrayList;

public class ClassList {
    private ArrayList<Course> classes;

    public ClassList() {
        classes = new ArrayList<Course>();
    }

    public void addClass(String name, String grade, double credits) {
        classes.add(new Course(name, grade, credits));
    }

    public void editclass(int position, String name, String grade, double credits) {
        classes.set(position, new Course(name, grade, credits));
    }

    public Course getCourse(int position) {
        return classes.get(position);
    }

    public int getSize() {
        return classes.size();
    }

    public void removeClass(int position) {
        classes.remove(position);
    }

    public void removeAllClasses() {
        classes.clear();
    }

    public double getGPA() {
        double gpa = 0;
        for(Course curclass:classes) {
            double grade = MainActivity.getgpafromgrade(curclass.getgrade());
            if(grade>=0)
            {
                gpa+=grade*curclass.getcredits();
            }
        }
        if(getCredits(false)==0)
        {
            return 0.0;
        }
        gpa = gpa/getCredits(false);
        gpa = (Math.floor(gpa*100))/100;
        return gpa;
    }

    public float getCredits(boolean includeCR)
    {
        float credits = 0.0f;
        for(Course curclass:classes)
        {
            double grade = MainActivity.getgpafromgrade(curclass.getgrade());
            if(grade>0)
            {
                credits += curclass.getcredits();
            }
            else if(grade == -1&&includeCR)
            {
                credits+=curclass.getcredits();
            }

        }
        return credits;
    }
}
