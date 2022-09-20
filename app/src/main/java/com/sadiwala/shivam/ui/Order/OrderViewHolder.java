package com.sadiwala.shivam.ui.Order;

import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_CUSTOMERS;
import static com.sadiwala.shivam.ui.Order.OrderDetailsActivity.ORDER_DATA;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.inputfields.SelectionInputField;
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
    private Activity mActivity;

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
        this.mActivity = mActivity;

        customer = DataController.getCustomerById(SelectionInputField.getCodeFromJsonValue(order.getCustomer().getValue()));
        loadData();

        if (customer == null) {
            fetchCustomerById(SelectionInputField.getCodeFromJsonValue(order.getCustomer().getValue()));
        }

    }

    private void fetchCustomerById(String id) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_CUSTOMERS);
        DocumentReference documentReference = collectionReference.document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    customer = documentSnapshot.toObject(Customer.class);
                    loadData();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void loadData() {
        if (customer == null) {
            tvName.setText(mActivity.getString(R.string.customer_not_exists));
        } else {
            tvName.setText(customer.getName().getValue());
        }
        tvType.setText(AppData.getType(mActivity, order.getType()));
        setDate();
//        setBackgroundColor();

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ORDER_DATA, Gson.getInstance().toJson(order));
                OrderDetailsActivity.start(mActivity, bundle);
            }
        });
    }

    private void setBackgroundColor() {
        if (AppData.PRODUCT_TYPE.ALINE_GOWN.toString().equals(order.getType())) {
            rlMain.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.aline_gown_bg_color));
        } else if (AppData.PRODUCT_TYPE.CHAPATTI_GOWN.toString().equals(order.getType())) {
            rlMain.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.chapati_gown_bg_color));
        } else if (AppData.PRODUCT_TYPE.NIGHT_DRESS.toString().equals(order.getType())) {
            rlMain.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.night_shoot_bg_color));
        } else if (AppData.PRODUCT_TYPE.LENGHI.toString().equals(order.getType())) {
            rlMain.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.lenghi_bg_color));
        }
    }

    private void setDate() {
        Date date = new Date();
        date.setTime(order.getTimestamp());
        String dateAdded = AaryaDateFormats.getFormatddMMMhhmma().format(date);
        tvDate.setText(dateAdded);
    }

}
