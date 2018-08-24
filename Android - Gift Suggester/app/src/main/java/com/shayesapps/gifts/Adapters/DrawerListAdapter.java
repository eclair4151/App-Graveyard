package com.shayesapps.gifts.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.shayesapps.gifts.Models.DrawerItem;
import com.shayesapps.gifts.R;

/**
 * Created by tomershemesh on 8/8/14.
 */

//this is the adapter for the pull out menu
public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {

    private Context context;
    private ArrayList<DrawerItem> values;

    public DrawerListAdapter(Context context,ArrayList<DrawerItem> values)
    {
        super(context, R.layout.drawer_item_template,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_item_template, null);
        }
        ImageView icon = (ImageView)convertView.findViewById(R.id.drawer_item_icon_image);
        icon.setImageResource(values.get(position).icon);

        TextView text = (TextView)convertView.findViewById(R.id.drawer_item_text);
        text.setText(values.get(position).title);

        return convertView;
    }



}
