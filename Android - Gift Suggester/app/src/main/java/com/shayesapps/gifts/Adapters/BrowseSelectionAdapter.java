package com.shayesapps.gifts.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.shayesapps.gifts.Models.BrowseSelection;
import com.shayesapps.gifts.R;

/**
 * Created by tomershemesh on 8/15/14.
 */
public class BrowseSelectionAdapter extends ArrayAdapter<BrowseSelection> {

    private Context context;
    private List<BrowseSelection> values;

    //takes in the list of browse selections
    public BrowseSelectionAdapter(Context context,List<BrowseSelection> values)
    {
        super(context, R.layout.gift_cell_template,values);
        this.context = context;
        this.values = values;
    }

    //this is the view for the tiles in the first screen on the browse tab
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.browse_selection, null);
        }
        TextView title = (TextView)convertView.findViewById(R.id.browseSelectionTitle);
        title.setText(values.get(position).title);

        ImageView icon = (ImageView)convertView.findViewById(R.id.browseSelectionIcon);
        icon.setImageResource(values.get(position).icon);

        return convertView;
    }



}