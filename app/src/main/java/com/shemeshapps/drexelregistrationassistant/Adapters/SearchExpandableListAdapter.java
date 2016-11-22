package com.shemeshapps.drexelregistrationassistant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Helpers.ErrorHelper;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.Professor;
import com.shemeshapps.drexelregistrationassistant.Models.QueryResult;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by tomer on 11/18/16.
 */

public class SearchExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ExpandableListView view;
    View header = null;
    private List<String> groups = new ArrayList<>();
    private Map<String,List<?>> results = new HashMap<>();
    LayoutInflater mInflater;

    public static enum SearchType{
        Classes,Professors,CRNs,
    }

    public SearchExpandableListAdapter(Context context,  ExpandableListView view) {
        this.context = context;
        view.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Doing nothing
                return true;
            }
        });
        mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.view = view;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return results.get(groups.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return results.get(groups.get(groupPosition)).get(childPosition).hashCode();
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.search_item_template, null);
        }
        TextView text = (TextView)convertView.findViewById(R.id.search_result_text);
        if(groups.get(groupPosition).equals(SearchType.Classes.name()))
        {
            ClassInfo item = (ClassInfo)(results.get(groups.get(groupPosition)).get(childPosition));
            text.setText(item.class_id + " - " + item.title);
        }
        else if(groups.get(groupPosition).equals(SearchType.Professors.name()))
        {
            Professor item = (Professor)(results.get(groups.get(groupPosition)).get(childPosition));
            text.setText(item.first_last_name);
        }
        else if(groups.get(groupPosition).equals(SearchType.CRNs.name()))
        {
            WebtmsClass item = (WebtmsClass)(results.get(groups.get(groupPosition)).get(childPosition));
            text.setText(item.crn + " - " + item.class_id + " " + item.instruction_type + " Section " + item.section);
        }
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {

        return results.get(groups.get(groupPosition)).size();

    }


    public String getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groups.get(groupPosition).hashCode();
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.search_header, null);
        }

        TextView text = (TextView)convertView.findViewById(R.id.search_header_text);
        text.setText(groups.get(groupPosition));
        view.expandGroup(groupPosition);

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
        groups.clear();
        results.clear();
    }

    public void removeHeader()
    {
        view.removeHeaderView(header);
    }

    public void setResults(QueryResult results)
    {
        clear();
        if(results.Classes.size() > 0)
        {
            groups.add(SearchType.Classes.name());
            this.results.put(SearchType.Classes.name(),results.Classes);
        }

        if(results.Professors.size() > 0)
        {
            groups.add(SearchType.Professors.name());
            this.results.put(SearchType.Professors.name(),results.Professors);
        }

        if(results.CRNS.size() > 0)
        {
            groups.add(SearchType.CRNs.name());
            this.results.put(SearchType.CRNs.name(),results.CRNS);
        }

        if(groups.size() == 0)
        {

            if(view.getHeaderViewsCount() == 0)
            {
                if(header == null)
                {
                    header = mInflater.inflate(R.layout.no_result_header, null);
                }
                try
                {
                    view.addHeaderView(header,null, false);
                }catch (Exception e)
                {
                    ErrorHelper.LogError(e,"add no result header to search");
                }

            }
        }
        else
        {
            removeHeader();
        }
        notifyDataSetChanged();
    }
}