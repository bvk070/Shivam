package com.sadiwala.shivam.ui.Order;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Order;

import java.util.ArrayList;


public class OrdersRecycleviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Order> orders;
    private Activity mActivity;

    public OrdersRecycleviewAdapter(ArrayList<Order> orders, Activity mActivity) {
        this.orders = orders;
        this.mActivity = mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        return new OrderViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        orderViewHolder.setData(orders.get(position), mActivity);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}
