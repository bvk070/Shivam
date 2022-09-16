package com.sadiwala.shivam.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


public class CustomTextView extends AppCompatTextView {

    private static Typeface myTypeface;

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }


    private void init(AttributeSet attrs) {


    }

}
