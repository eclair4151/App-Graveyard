package com.shemeshapps.drexeloncampuschallenge.Helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.isseiaoki.simplecropview.CropImageView;

/**
 * Created by Tomer on 8/28/15.
 */
public class CustomCropView extends CropImageView {
    boolean isEnabled = true;

    public CustomCropView(Context context) {
        super(context);
    }

    public CustomCropView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        isEnabled = enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isEnabled && super.onTouchEvent(event);
    }
}
