package com.sadiwala.shivam.ui.Customer;

import static com.sadiwala.shivam.ui.Customer.CustomerDetailsActivity.CUSTOMER_DATA;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.inputfields.SelectionInputField;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.PhoneUtil;

public class CustomerViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout rlMain, rlCall;
    private TextView tvName, tvArea;
    private Customer customer;

    public CustomerViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.name);
        tvArea = itemView.findViewById(R.id.area);
        rlMain = itemView.findViewById(R.id.main_rl);
        rlCall = itemView.findViewById(R.id.rlCall);
    }

    public void setData(Customer customer, Activity mActivity) {
        this.customer = customer;
        tvName.setText(customer.getName().getValue());
        tvArea.setText(SelectionInputField.getNameFromJsonValue(customer.getArea().getValue()));

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(CUSTOMER_DATA, Gson.getInstance().toJson(customer));
                CustomerDetailsActivity.start(mActivity, bundle);
            }
        });

        rlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtil.makeCall(mActivity, customer.getMobile().getValue());
            }
        });

    }


}
