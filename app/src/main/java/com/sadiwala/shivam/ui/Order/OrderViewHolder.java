package com.sadiwala.shivam.ui.Order;

import static com.sadiwala.shivam.ui.Order.OrderDetailsActivity.ORDER_DATA;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.util.AaryaDateFormats;
import com.sadiwala.shivam.util.Gson;

import java.util.Date;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout rlMain, rlCall;
    private TextView tvName, tvType, tvDate;
    private Order order;
    private Customer customer;

    public OrderViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.name);
        tvType = itemView.findViewById(R.id.type);
        tvDate = itemView.findViewById(R.id.date);
        rlMain = itemView.findViewById(R.id.main_rl);
        rlCall = itemView.findViewById(R.id.rlCall);
    }

    public void setData(Order order, Activity mActivity) {
        this.order = order;
        customer = DataController.getCustomerById(order.getCustomer().getValue());
        tvName.setText(customer.getName().getValue());
        tvType.setText(AppData.getType(mActivity, order.getType()));
        setDate();

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ORDER_DATA, Gson.getInstance().toJson(order));
                OrderDetailsActivity.start(mActivity, bundle);
            }
        });

    }

    private void setDate() {
        Date date = new Date();
        date.setTime(order.getTimestamp());
        String dateAdded = AaryaDateFormats.getFormatddMMMhhmma().format(date);
        tvDate.setText(dateAdded);
    }

}
