package com.shemeshapps.drexelregistrationassistant.CustomViews;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.shemeshapps.drexelregistrationassistant.Helpers.ResourceHelper;
import com.shemeshapps.drexelregistrationassistant.R;

/**
 * Created by shemesht on 5/4/15.
 */
//custom jenky class to make an imageview clickable with animation.
public class ImageViewButton extends ImageView {

    public ImageViewButton(Context context) {
        super(context);
        onTouch();
    }

    public ImageViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        onTouch();
    }

    public ImageViewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onTouch();
    }


    private Rect rect;
    boolean touchedInside = true;
    private void onTouch()
    {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    touchedInside = true;
                    setColorFilter(ResourceHelper.getColor(R.color.imageButtonGrey,getContext()));
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                        if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
                        {
                            //not in
                            if(touchedInside)
                            {
                                setColorFilter(ResourceHelper.getColor(android.R.color.transparent,getContext()));
                            }
                            touchedInside = false;
                        }
                        else
                        {
                            if(!touchedInside)
                            {
                                setColorFilter(ResourceHelper.getColor(R.color.imageButtonGrey,getContext()));
                            }
                            touchedInside = true;
                        }
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    setColorFilter(ResourceHelper.getColor(android.R.color.transparent,getContext()));
                    if(touchedInside)
                    {
                        callOnClick();
                    }
                }
                return true;
            }
        });
    }
}
