package com.sadiwala.shivam.util;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class VymoNestedScrollView extends NestedScrollView {

    private int maxHeight = -1;

    public VymoNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public VymoNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    }

    public VymoNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMaxHeightDensity(int dps){
        this.maxHeight = (int)(dps * getContext().getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}