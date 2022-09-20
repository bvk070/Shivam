package com.sadiwala.shivam.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.network.FirebaseDatabaseController;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.Customer.CustomersRecycleviewAdapter;
import com.sadiwala.shivam.ui.Order.OrdersRecycleviewAdapter;
import com.sadiwala.shivam.util.CustomDividerItemDecoration;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {

    private static final String TAG = SearchActivity.class.getName();

    public static final String DATA_TYPE = "data_type";
    public static final int DATA_TYPE_ORDERS = 100;
    public static final int DATA_TYPE_CUSTOMERS = 101;

    private Toolbar mToolbar;
    private TextView tvError;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private SearchView searchView;

    private int dataType;

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dataType = getIntent().getExtras().getInt(DATA_TYPE);
        init();
    }

    private void init() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Orders");

        tvError = findViewById(R.id.tvError);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mRecyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Add custom divider to recyclerview
        Drawable mDivider = ContextCompat.getDrawable(mRecyclerView.getContext(), R.drawable.divider);
        CustomDividerItemDecoration hItemDecoration = new CustomDividerItemDecoration(mDivider, false, false);
        mRecyclerView.addItemDecoration(hItemDecoration);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (dataType == DATA_TYPE_ORDERS) {
                    setOrdersData(newText);
                } else if (dataType == DATA_TYPE_CUSTOMERS) {
                    setCustomerData(newText);
                }
                return true;
            }
        });

        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

    }

    private void setOrdersData(String query) {
        ArrayList<Order> orders = new ArrayList<>();
        if (!TextUtils.isEmpty(query)) {
            orders = DataController.getPrefOrders();
            orders = FirebaseDatabaseController.getOrdersByName(orders, query);
        }

        OrdersRecycleviewAdapter ordersRecycleviewAdapter = new OrdersRecycleviewAdapter(orders, this);
        mRecyclerView.setAdapter(ordersRecycleviewAdapter);

    }

    private void setCustomerData(String query) {
        ArrayList<Customer> customers = new ArrayList<>();
        if (!TextUtils.isEmpty(query)) {
            customers = DataController.getPrefCustomers();
            customers = FirebaseDatabaseController.getCustomerByName(customers, query);
        }

        CustomersRecycleviewAdapter customersRecycleviewAdapter = new CustomersRecycleviewAdapter(customers, this);
        mRecyclerView.setAdapter(customersRecycleviewAdapter);

    }


}
