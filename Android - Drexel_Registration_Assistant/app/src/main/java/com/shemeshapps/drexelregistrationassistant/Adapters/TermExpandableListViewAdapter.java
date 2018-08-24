package com.shemeshapps.drexelregistrationassistant.Adapters;

/**
 * Created by Tomer on 2/7/16.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Models.Professor;
import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsFilter;
import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//view used to show all terms with a drop down to show more classes
public class TermExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Activity context;
    //all classes and then a copy for caching filtered classes
    private Map<Term, List<WebtmsClass>> classes = new HashMap<>();
    private Map<Term, List<WebtmsClass>> filteredClasses = new HashMap<>();

    private List<Term> terms = new ArrayList<>();
    private Set<Term> loadingTerms = new HashSet<>();
    private  ExpandableListView view;
    public TermExpandableListViewAdapter(Activity context, List<Term> terms, ExpandableListView view) {
        this.context = context;
        this.terms = terms;
        this.view = view;
    }

    public WebtmsClass getChild(int groupPosition, int childPosition) {
        return filteredClasses.get(terms.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    //load class into row
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
        WebtmsClass webtmsClass = filteredClasses.get(terms.get(groupPosition)).get(childPosition);

        classid.setText(webtmsClass.class_id);
        classType.setText(webtmsClass.instruction_type);
        section.setText("Section " + webtmsClass.section);
        time.setText(webtmsClass.days_time_string);

        enrolled.setText(webtmsClass.current_enroll + "/" + webtmsClass.max_enroll);
        String profsText = "";

        for(int i =0; i <webtmsClass.professors.size(); i++)
        {
            profsText += webtmsClass.professors.get(i).first_last_name;
            if(i < webtmsClass.professors.size()-1)
            {
                profsText+= ", ";
            }
        }
        prof.setText(profsText);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        if(filteredClasses.containsKey(terms.get(groupPosition)))
        {
           return filteredClasses.get(terms.get(groupPosition)).size();
        }
        else
        {
            return 0;
        }
    }

    //need to show loading for a term while getting classes
    public void addLoading(Term t)
    {
        loadingTerms.add(t);
        notifyDataSetChanged();
    }

    public void removeLoading(Term t)
    {
        loadingTerms.remove(t);
    }

    public Term getGroup(int groupPosition) {
        return terms.get(groupPosition);
    }

    public int getGroupCount() {
        return terms.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //drop down terms view
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.term_group_template, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.term_header);
        item.setText(terms.get(groupPosition).toString());

        ProgressBar loading = (ProgressBar)convertView.findViewById(R.id.term_loading_bar);
        if(loadingTerms.contains(terms.get(groupPosition)))
        {
            loading.setVisibility(View.VISIBLE);
        }
        else
        {
            loading.setVisibility(View.GONE);
        }
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
        filteredClasses.clear();
        notifyDataSetChanged();
    }

    //whe classes are downloaded save them and filter them if one exists
    public void addClassesToTerm(Term t, List<WebtmsClass> class_list)
    {
        classes.put(t,class_list);
        if(filter==null)
        {
            filteredClasses.put(t,new ArrayList<>(class_list));
        }
        else
        {
            List<WebtmsClass> filteredTerm = new ArrayList<>();
            for(WebtmsClass wc:class_list)
            {
                if(wc.passesFilter(filter))
                {
                    filteredTerm.add(wc);
                }
            }
            filteredClasses.put(t,filteredTerm);

            //run new classes throug filter
        }
        notifyDataSetChanged();
    }

    public boolean hasTermClasses(Term t)
    {
        return classes.get(t) != null;
    }

    //add terms when loaded
    public void addGroups(List<Term> terms)
    {
        this.terms = terms;
        notifyDataSetChanged();
        view.collapseGroup(0);
    }

    public void removeFilter()
    {
        filter = null;
        runFilter();
    }

    public void addFilter(WebtmsFilter filter)
    {
        this.filter = filter;
        runFilter();
    }

    private WebtmsFilter filter;

    //go through each term and each class and check if it fits into the filter. if so add it to the classes showing
    public void runFilter()
    {
        if(filter != null) {

            for (Map.Entry<Term, List<WebtmsClass>> entry : classes.entrySet()) {
                filteredClasses.get(entry.getKey()).clear();
                for (WebtmsClass wc : entry.getValue()) {
                    if (wc.passesFilter(filter)) {
                        filteredClasses.get(entry.getKey()).add(wc);
                    }
                }
            }
            notifyDataSetChanged();
        }
        else
        {
            for (Map.Entry<Term, List<WebtmsClass>> entry : classes.entrySet()) {
                filteredClasses.put(entry.getKey(),new ArrayList<>(entry.getValue()));
            }
            notifyDataSetChanged();
        }
    }


}