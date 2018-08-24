package com.shemeshapps.drexeloncampuschallenge.Helpers;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shemeshapps.drexeloncampuschallenge.R;

/**
 * Created by shemesht on 5/4/15.
 */
public class TextViewButton extends TextView {
    public TextViewButton(Context context) {
        super(context);
        onTouch();
    }

    public TextViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        onTouch();
    }

    public TextViewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onTouch();
    }

    int origTextColor;
    private Rect rect;
    boolean touchedInside = true;


    private void onTouch()
    {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    origTextColor = getCurrentTextColor();
                    setTextColor(getResources().getColor(R.color.imageButtonGrey));
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
                    {
                        //not in
                        if(touchedInside)
                        {
                            setTextColor(origTextColor);
                        }
                        touchedInside = false;
                    }
                    else
                    {
                        if(!touchedInside)
                        {
                            setTextColor(getResources().getColor(R.color.imageButtonGrey));
                        }
                        touchedInside = true;
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    setTextColor(origTextColor);
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
