package com.sadiwala.shivam.ui.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Report;

import java.util.ArrayList;

public class HomeRecycleviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String REPORT_TYPE_TODAY = "report_type_today";
    public static final String REPORT_TYPE_THIS_WEEK = "report_type_this_week";
    public static final String REPORT_TYPE_THIS_MONTH = "report_type_this_month";

    private ArrayList<Report> reports;
    private Activity mActivity;

    public HomeRecycleviewAdapter(ArrayList<Report> reports, Activity mActivity) {
        this.reports = reports;
        this.mActivity = mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item, parent, false);
        return new HomeViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
        homeViewHolder.setData(reports.get(position), mActivity);

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

}
