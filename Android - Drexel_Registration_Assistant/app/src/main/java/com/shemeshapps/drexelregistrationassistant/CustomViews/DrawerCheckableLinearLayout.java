package com.shemeshapps.drexelregistrationassistant.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Helpers.ResourceHelper;
import com.shemeshapps.drexelregistrationassistant.R;


/*
this is a class allows android to automatically highlight what ever tab you are currently on in the side menu
 */
public class DrawerCheckableLinearLayout extends LinearLayout implements Checkable {
    private TextView textView;
    private ImageView imageView;
    private LinearLayout background;
    private boolean isChecked = false;
    private Context c;

    public DrawerCheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View layout = getRootView();
        textView = (TextView)layout.findViewById(R.id.drawer_item_text);
        imageView = (ImageView)layout.findViewById(R.id.drawer_item_icon_image);
        background = (LinearLayout)layout.findViewById(R.id.drawer_item_background);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked) {
           // textView.setTextColor(getResources().getColor(android.R.color.white));
            textView.setTypeface(null, Typeface.BOLD);
           // imageView.setColorFilter(getResources().getColor(android.R.color.white));
            background.setBackgroundColor(ResourceHelper.getColor(R.color.drawer_item_grey,c));
        }
        else
        {
           // textView.setTextColor(getResources().getColor(R.color.drawer_item_grey));
            textView.setTypeface(null, Typeface.NORMAL);
           // imageView.setColorFilter(getResources().getColor(R.color.drawer_item_grey));

            background.setBackgroundColor(ResourceHelper.getColor(android.R.color.white,c));
            background.setBackgroundColor(ResourceHelper.getColor(R.color.drexel_blue,c));

        }
    }

    @Override
    public void toggle() {

    }
}