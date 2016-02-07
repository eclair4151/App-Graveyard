package com.shemeshapps.drexelregistrationassistant.Adapters;

/**
 * Created by Tomer on 2/7/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<Term, List<WebtmsClass>> classes = new HashMap<>();
    private List<Term> terms = new ArrayList<>();
    private  ExpandableListView view;
    public TermExpandableListViewAdapter(Activity context, List<Term> terms, ExpandableListView view) {
        this.context = context;
        this.terms = terms;
        this.view = view;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return classes.get(terms.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
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
        WebtmsClass webtmsClass = classes.get(terms.get(groupPosition)).get(childPosition);

        classid.setText(webtmsClass.class_id);
        classType.setText(webtmsClass.instruction_type);
        section.setText("Section " + webtmsClass.section);
        time.setText(webtmsClass.days_time_string);

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

    public int getChildrenCount(int groupPosition) {
        if(classes.containsKey(terms.get(groupPosition)))
        {
           return classes.get(terms.get(groupPosition)).size();
        }
        else
        {
            return 0;
        }
    }

    public Object getGroup(int groupPosition) {
        return terms.get(groupPosition);
    }

    public int getGroupCount() {
        return terms.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.term_group_template, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.term_header);
        item.setText(terms.get(groupPosition).toString());
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void clear()
    {
        terms = new ArrayList<>();
        classes.clear();
        notifyDataSetChanged();
    }

    public void addClassesToTerm(Term t, List<WebtmsClass> class_list)
    {
        classes.put(t,class_list);
        notifyDataSetChanged();
    }

    public void addGroups(List<Term> terms)
    {
        this.terms = terms;
        notifyDataSetChanged();
        view.collapseGroup(0);
    }


}