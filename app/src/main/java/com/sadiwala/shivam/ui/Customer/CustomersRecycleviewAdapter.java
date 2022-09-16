package com.sadiwala.shivam.ui.Customer;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Customer;

import java.util.ArrayList;


public class CustomersRecycleviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Customer> customers;
    private Activity mActivity;

    public CustomersRecycleviewAdapter(ArrayList<Customer> customers, Activity mActivity) {
        this.customers = customers;
        this.mActivity = mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_item, parent, false);
        return new CustomerViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CustomerViewHolder customerViewHolder = (CustomerViewHolder) holder;
        customerViewHolder.setData(customers.get(position), mActivity);

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

}
