package com.sadiwala.shivam.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    int left = 0, top = 0, right = 0, bottom = 0;

    public MarginItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = Util.getDpToPixel(top);
        }
        outRect.bottom = Util.getDpToPixel(bottom);
        outRect.left = Util.getDpToPixel(left);
        outRect.right = Util.getDpToPixel(right);
    }
}
