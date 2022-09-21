package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.ui.main.HomeRecycleviewAdapter.REPORT_TYPE_THIS_MONTH;
import static com.sadiwala.shivam.ui.main.HomeRecycleviewAdapter.REPORT_TYPE_THIS_WEEK;
import static com.sadiwala.shivam.ui.main.HomeRecycleviewAdapter.REPORT_TYPE_TODAY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Report;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private static final String TAG = FragmentHome.class.getName();

    private ProgressBar progressBar;
    private TextView tvError;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.home));
        setData();
    }

    private void initializeViews() {

        tvError = getView().findViewById(R.id.tvError);
        progressBar = getView().findViewById(R.id.progressbar);

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                setData();
            }
        });
    }

    private void setData() {
        ArrayList<Report> reports = new ArrayList<>();
        reports.add(new Report(REPORT_TYPE_TODAY));
        reports.add(new Report(REPORT_TYPE_THIS_WEEK));
        reports.add(new Report(REPORT_TYPE_THIS_MONTH));
        HomeRecycleviewAdapter homeRecycleviewAdapter = new HomeRecycleviewAdapter(reports, getActivity());
        mRecyclerView.setAdapter(homeRecycleviewAdapter);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

}
