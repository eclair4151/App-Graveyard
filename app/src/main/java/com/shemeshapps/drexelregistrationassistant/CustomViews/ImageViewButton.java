package com.shemeshapps.drexelregistrationassistant.CustomViews;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.shemeshapps.drexelregistrationassistant.R;

/**
 * Created by shemesht on 5/4/15.
 */
public class ImageViewButton extends ImageView {

    private Context c;
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setColorFilter(getResources().getColor(R.color.imageButtonGrey,c.getTheme()));
                    } else {
                        setColorFilter(getResources().getColor(R.color.imageButtonGrey));
                    }

                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                        if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
                        {
                            //not in
                            if(touchedInside)
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    setColorFilter(getResources().getColor(android.R.color.transparent,c.getTheme()));
                                } else {
                                    setColorFilter(getResources().getColor(android.R.color.transparent));
                                }
                            }
                            touchedInside = false;
                        }
                        else
                        {
                            if(!touchedInside)
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    setColorFilter(getResources().getColor(R.color.imageButtonGrey,c.getTheme()));
                                } else {
                                    setColorFilter(getResources().getColor(R.color.imageButtonGrey));
                                }
                            }
                            touchedInside = true;
                        }
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setColorFilter(getResources().getColor(android.R.color.transparent,c.getTheme()));
                    } else {
                        setColorFilter(getResources().getColor(android.R.color.transparent));
                    }
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
