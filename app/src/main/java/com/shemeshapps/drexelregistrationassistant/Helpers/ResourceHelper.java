package com.shemeshapps.drexelregistrationassistant.Helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.shemeshapps.drexelregistrationassistant.R;

/**
 * Created by Tomer on 1/23/16.
 */
public class ResourceHelper {
    public static int getColor(int res, Context c)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return c.getResources().getColor(res,c.getTheme());
        } else {
           return c.getResources().getColor(res);
        }
    }


    public static Drawable getDrawable(int res, Context c)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return c.getResources().getDrawable(res, c.getTheme());
        } else {
            return  c.getResources().getDrawable(res);
        }
    }

}
