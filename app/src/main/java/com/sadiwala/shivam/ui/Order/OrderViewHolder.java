package com.sadiwala.shivam.ui.Order;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Order;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout rlMain, rlCall;
    private TextView tvName;
    private Order order;

    public OrderViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.name);
        rlMain = itemView.findViewById(R.id.main_rl);
        rlCall = itemView.findViewById(R.id.rlCall);
    }

    public void setData(Order order, Activity mActivity) {
        this.order = order;
        tvName.setText(order.getChest().getValue());

//        rlMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(CUSTOMER_DATA, Gson.getInstance().toJson(customer));
//                CustomerDetailsActivity.start(mActivity, bundle);
//            }
//        });


    }


}
