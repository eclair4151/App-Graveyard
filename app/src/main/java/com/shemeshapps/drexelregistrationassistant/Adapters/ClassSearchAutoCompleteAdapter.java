package com.shemeshapps.drexelregistrationassistant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tomer on 1/12/16.
 */
public class ClassSearchAutoCompleteAdapter extends ArrayAdapter<ClassInfo> implements Filterable {
    private ArrayList<ClassInfo> classes;
    private Context context;
    private Handler handler;

    public ClassSearchAutoCompleteAdapter(Context context ,ArrayList<ClassInfo> classes) {
        super(context, R.layout.auto_complete_template, classes);
        this.classes = classes;
        this.context = context;
        handler = new Handler();

    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public ClassInfo getItem(int position) {
        return classes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.auto_complete_template, null);
        }

        TextView text = (TextView)convertView.findViewById(android.R.id.text1);
        text.setText(classes.get(position).class_id + " - " + classes.get(position).title);

        return convertView;
    }





    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    handler.removeMessages(0);
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            RequestUtil.getInstance(context.getApplicationContext()).autoCompleteClasses(constraint.toString(), new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    clear();
                                    addAll(Arrays.asList((ClassInfo[])response));
                                    notifyDataSetChanged();
                                }
                            });
                        }}, 350);
                }
                else
                {
                    clear();
                    notifyDataSetChanged();
                }
                filterResults.count = classes.size();
                filterResults.values = classes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((ClassInfo) resultValue).class_id + " - " + ((ClassInfo) resultValue).title;
            }
        };
        return myFilter;
    }
}
