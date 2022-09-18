package com.sadiwala.shivam.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.sadiwala.shivam.R;

import java.util.List;


public abstract class VymoBaseAdapter<T> extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<T> mList;
    private List<T> mFilteredList;
    private VymoFilter<T> vymoFilter;
    //this is to check whether view is inflated view or footer view
    private String INFLATED_VIEW = "inflated_view";

    public VymoBaseAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
        mFilteredList = list;
    }

    public void setList(List<T> list) {
        mList = list;
        mFilteredList = list;
    }

    protected Context getContext() {
        return mContext;
    }

    public List<T> getList() {
        return mList;
    }

    public List<T> getFilteredList() {
        return mFilteredList;
    }

    @Override
    public int getCount() {
        return mFilteredList != null ? mFilteredList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mFilteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        String tag = null;

        if (convertView != null) {
            tag = (String) convertView.getTag(R.integer.footer_id + i);
        }

        if (convertView == null || parent.getChildCount() == 0 || tag == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(getLayout(), parent, false);
            view.setTag(R.integer.footer_id + i, INFLATED_VIEW);
        } else {
            view = convertView;
        }

        populateViewForItem(view, mFilteredList.get(i));
        return view;
    }

    protected abstract int getLayout();

    protected abstract void populateViewForItem(View view, T t);

    public Filter getFilter() {
        if (vymoFilter == null) {
            vymoFilter = new VymoFilter<T>(this);
        }

        return vymoFilter;
    }

    protected boolean search(CharSequence searchTerm, T t) {
        return false;
    }

    public void updateFilteredList(List<T> searchList) {
        mFilteredList = searchList;
        notifyDataSetChanged();
    }
}
