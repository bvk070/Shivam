package com.sadiwala.shivam.base;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;


public class VymoFilter<T> extends Filter {
    private final String TAG = this.getClass().getSimpleName();
    VymoBaseAdapter<T> mAdapter;

    public VymoFilter(VymoBaseAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();

        if (charSequence == null || charSequence.length() == 0) {
            filterResults.values = mAdapter.getList();
            filterResults.count = mAdapter.getList().size();
            return filterResults;
        }

        List<T> mResult = new ArrayList<T>();

        for (T input : mAdapter.getList()) {
            if (mAdapter.search(charSequence, input)) {
                mResult.add(input);
            }
        }

        filterResults.values = mResult;
        filterResults.count = mResult.size();
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        mAdapter.updateFilteredList((List<T>) filterResults.values);
    }
}
