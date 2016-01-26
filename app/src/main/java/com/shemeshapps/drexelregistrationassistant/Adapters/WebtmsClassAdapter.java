package com.shemeshapps.drexelregistrationassistant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Models.Professors;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsDays;
import com.shemeshapps.drexelregistrationassistant.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tomer on 1/11/16.
 */
public class WebtmsClassAdapter extends ArrayAdapter<WebtmsClass> {

    private Context context;
    private ArrayList<WebtmsClass> values;

    public WebtmsClassAdapter(Context context, ArrayList<WebtmsClass> values)
    {
        super(context, R.layout.webtms_row_template,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.webtms_row_template, null);
        }

        TextView classid = (TextView)convertView.findViewById(R.id.classid);
        TextView classType = (TextView)convertView.findViewById(R.id.class_type);
        TextView section = (TextView)convertView.findViewById(R.id.class_section);
        TextView time = (TextView)convertView.findViewById(R.id.class_time);
        TextView enrolled = (TextView)convertView.findViewById(R.id.class_enrollment);
        TextView prof = (TextView)convertView.findViewById(R.id.class_prof);
        WebtmsClass webtmsClass = values.get(position);

        classid.setText(webtmsClass.class_id);
        classType.setText(webtmsClass.instruction_type);
        section.setText("Section " + webtmsClass.section);
        time.setText(webtmsClass.getFormatedTime());

        enrolled.setText(webtmsClass.current_enroll + "/" + webtmsClass.max_enroll);
        String profsText = "";

        for(int i =0; i <webtmsClass.professors.length; i++)
        {
            profsText += webtmsClass.professors[i].first_last_name;
            if(i < webtmsClass.professors.length-1)
            {
                profsText+= " ,";
            }
        }
        prof.setText(profsText);

        return convertView;
    }

}
