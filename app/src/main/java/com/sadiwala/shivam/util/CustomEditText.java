package com.sadiwala.shivam.util;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.sadiwala.shivam.R;


public class CustomEditText extends AppCompatEditText {
    public CustomEditText(Context context) {
        super(context);
        init(null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        String defaultFontName = "OpenSans-Regular.ttf";

        this.setTextColor(UiUtil.getColor(R.color.dark_grey_323232));
        this.setHintTextColor(UiUtil.getColor(R.color.vymo_color_primary));
    }
}
