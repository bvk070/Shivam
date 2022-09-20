package com.sadiwala.shivam.ui.Customer;

import static com.sadiwala.shivam.base.AppData.prepareCustomerGroups;
import static com.sadiwala.shivam.ui.Customer.CustomerDetailsActivity.CUSTOMER_DATA;
import static com.sadiwala.shivam.util.AaryaConstants.REQUEST_CODE_ADD_CUSTOMER;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.inputfields.InputField;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.inputfields.InputFieldsGroup;
import com.sadiwala.shivam.inputfields.InputFieldsGroupsContainer;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.ui.main.BaseAddActivity;
import com.sadiwala.shivam.util.Gson;

import java.util.ArrayList;

public class AddCustomerActivity extends BaseAddActivity {

    private Toolbar mToolbar;

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, AddCustomerActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, AddCustomerActivity.class);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQUEST_CODE_ADD_CUSTOMER);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (getIntent().getExtras() != null) {
            customer = Gson.getInstance().fromJson(getIntent().getStringExtra(CUSTOMER_DATA), Customer.class);
        }

        init();
        loadGroupView(savedInstanceState);
    }

    private void init() {
        btnSubmit = findViewById(R.id.btnSubmit);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (customer != null) {
            setTitle(getString(R.string.update_customer));
            btnSubmit.setText(getString(R.string.update));
        } else {
            setTitle(getString(R.string.add_customer));
            btnSubmit.setText(getString(R.string.add));
        }
    }

    private void loadGroupView(Bundle savedInstanceState) {

        ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();
        if (customer != null) {
            prepareCustomerGroups(null, inputFieldValues, customer);
        }

        ArrayList<InputFieldType> inputFieldTypes = AppData.getCustomerForm();
        InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", "Customer Details",
                inputFieldValues, inputFieldTypes, InputField.EditMode.WRITE, false, getBus(), null, null);

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();
        inputFieldsGroups.add(inputFieldsGroup);
        InputFieldsGroupsContainer groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.WRITE, null);

        addInputField(INPUTS, groupView);
    }


}
