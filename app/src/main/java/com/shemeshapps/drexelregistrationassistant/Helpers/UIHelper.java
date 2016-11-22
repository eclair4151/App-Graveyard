package com.shemeshapps.drexelregistrationassistant.Helpers;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by tomer on 11/19/16.
 */

public class UIHelper {
    public static void hideSoftKeyBoard(Activity c) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
