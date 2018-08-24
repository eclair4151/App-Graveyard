package com.shemeshapps.drexelregistrationassistant.CustomViews;

/**
 * Created by Tomer on 1/22/16.
 */
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.shemeshapps.drexelregistrationassistant.Helpers.ResourceHelper;
import com.shemeshapps.drexelregistrationassistant.R;


//just a custom autocomplete text view which gives the option to have an X to clear the current text.
//not currently used anymore
public class ClearableAutoCompleteTextView extends AutoCompleteTextView {
    // was the text just cleared?
    //boolean justPressedBack = false;
    // if not set otherwise, the default clear listener clears the text in the
    // text view
    private OnClearListener defaultClearListener = new OnClearListener() {

        @Override
        public void onClear() {
            ClearableAutoCompleteTextView et = ClearableAutoCompleteTextView.this;
            et.setText("");
        }
    };

    private OnClearListener onClearListener = defaultClearListener;

    // The image we defined for the clear button
    public Drawable imgClearButton;

    public interface OnClearListener {
        void onClear();
    }

    /* Required methods, not used in this implementation */
    public ClearableAutoCompleteTextView(Context context) {
        super(context);
        init(context);

    }

    /* Required methods, not used in this implementation */
    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

//    @Override
//    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing() && !justPressedBack) {
//            justPressedBack = true;
//            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//            inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//
//            return true;
//        }
//        return super.onKeyPreIme(keyCode, event);
//    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if(inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS)){
                return true;
            }
        }

        return super.onKeyPreIme(keyCode, event);
    }


    /* Required methods, not used in this implementation */
    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context c) {

        imgClearButton = ResourceHelper.getDrawable(R.drawable.ic_clear_black_24dp, c);


        // Set the bounds of the button
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(getText().length()==0)
                {
                    hideClearButton();
                }
                else
                {
                    showClearButton();
                }
                //justPressedBack = false;
            }
        });
        // if the clear button is pressed, fire up the handler. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ClearableAutoCompleteTextView et = ClearableAutoCompleteTextView.this;
                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight()	- imgClearButton.getIntrinsicWidth()) {
                    onClearListener.onClear();
                }
                return false;
            }
        });
    }

    public void setImgClearButton(Drawable imgClearButton) {
        this.imgClearButton = imgClearButton;
    }

    public void setOnClearListener(final OnClearListener clearListener) {
        this.onClearListener = clearListener;
    }

    public void hideClearButton() {
        this.setCompoundDrawables(null, null, null, null);
    }

    public void showClearButton() {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, imgClearButton, null);
    }

}