package com.shayesapps.gifts.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import com.shayesapps.gifts.Models.Gift;
import com.shayesapps.gifts.NetworkingServices.RequestUtil;
import com.shayesapps.gifts.R;

/**
 * Created by tomershemesh on 8/9/14.
 */

//this is the adapter for where ever you have a list of gifts
public class GiftListAdapter extends ArrayAdapter<Gift> {

    private Context context;
    private List<Gift> values;
    private List<Gift> mSelection = new ArrayList<Gift>();

    public GiftListAdapter(Context context,List<Gift> values)
    {
        super(context, R.layout.gift_cell_template,values);
        this.context = context;
        this.values = values;
    }

    //this is used for saved gifts which allows you to long hold a gift to select it and others to send or delete
    public void toggleSelection(Gift gift) {
        if(!mSelection.contains(gift))
        {
            mSelection.add(gift);
        }
       else
        {
            mSelection.remove(gift);
        }
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection.clear();
        notifyDataSetChanged();
    }

    public List<Gift> getSelection()
    {
        return mSelection;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.gift_cell_template, null);
        }

        //put all value for gift in cell
        Gift gift = values.get(position);
        TextView title = (TextView)convertView.findViewById(R.id.giftTitle);
        title.setText(gift.title);

        TextView price = (TextView)convertView.findViewById(R.id.giftPrice);
        price.setText("$"+gift.price);

        TextView merchant = (TextView)convertView.findViewById(R.id.giftMerchant);
        merchant.setText("Merchant: " + gift.merchantName);

        TextView description = (TextView)convertView.findViewById(R.id.giftDescription);
        description.setText(gift.description);

        NetworkImageView imgAvatar = (NetworkImageView) convertView.findViewById(R.id.giftImage);

        imgAvatar.setImageUrl(((gift.mediumProductImageUrl!=null)?gift.mediumProductImageUrl:gift.smallProductImageUrl), RequestUtil.imageLoader);


        //check if the gift is currently selected
        if(mSelection.contains(gift))
        {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.Gifts_Blue));
        }
        else
        {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return values.get(position)._id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
