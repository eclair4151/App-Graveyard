package com.shemeshapps.drexeloncampuschallenge.Models;

/**
 * Created by Tomer on 8/24/15.
 */
public class Participant implements Comparable<Participant>{
    public String name;
    public int points;


    @Override
    public int compareTo(Participant p) {
        if(p.points > points)
        {
            return -1;
        }

        if(p.points == points)
        {
            return 0;
        }

        return 1;
    }
}
