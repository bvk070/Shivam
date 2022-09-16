package com.sadiwala.shivam.ui.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.ui.main.BaseAddActivity;

public class AddCustomerActivity extends BaseAddActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Customer");
    }

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, AddCustomerActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


}
