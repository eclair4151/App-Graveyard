package com.shemeshapps.drexelregistrationassistant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;

/**
 * Created by tomershemesh on 8/8/14.
 */

//this is the adapter for the pull out menu
public class SmallTermSpinnerAdapter extends ArrayAdapter<Term> {

    private Context context;
    private ArrayList<Term> values;

    public SmallTermSpinnerAdapter(Context context, ArrayList<Term> values)
    {
        super(context, android.R.layout.simple_spinner_item,values);
        setDropDownViewResource(R.layout.dropdown_template);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(android.R.layout.simple_spinner_item, null);
        }

        TextView text = (TextView)convertView.findViewById(android.R.id.text1);
        text.setText(values.get(position).term.substring(0,2) + " " + values.get(position).term_year);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.dropdown_template, null);
        }

        TextView text = (TextView)convertView.findViewById(R.id.text1);
        text.setText(values.get(position).term + " " + values.get(position).term_type + " " + values.get(position).term_year);

        return convertView;
    }

}
