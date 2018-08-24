package com.shemeshapps.drexeloncampuschallenge.Helpers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shemeshapps.drexeloncampuschallenge.Models.SpinnerItem;
import com.shemeshapps.drexeloncampuschallenge.R;

import java.util.ArrayList;

/**
 * Created by Tomer on 9/7/15.
 */
public class SimpleSpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    Context c;
    ArrayList<SpinnerItem> items;
    public SimpleSpinnerAdapter(Context ctx,  ArrayList<SpinnerItem> objects) {
        super(ctx, R.layout.spinner_cell, objects);
        this.c = ctx;
        this.items = objects;
    }

    @Override public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
    {
        return getCustomView(position, cnvtView,false);
    }



    @Override public View getView(int pos, View cnvtView, ViewGroup prnt)
    {
        return getCustomView(pos, cnvtView,true);
    }



    public View getCustomView(int position, View convertView,boolean hasArrow) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_cell, null);
        }
        TextView t = (TextView)convertView.findViewById(R.id.spinner_text);
        t.setText(items.get(position).display);
        ImageView arrow = (ImageView)convertView.findViewById(R.id.downArrow);

        if(hasArrow)
        {
            arrow.setVisibility(View.VISIBLE);
        }
        else
        {
            arrow.setVisibility(View.GONE);
        }
        return convertView;
    }

    public String getSpinnerItemId(int pos)
    {
        return items.get(pos).item_id;
    }

}
