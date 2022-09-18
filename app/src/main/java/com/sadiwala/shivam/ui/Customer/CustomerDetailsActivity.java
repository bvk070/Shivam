package com.sadiwala.shivam.ui.Customer;

import static com.sadiwala.shivam.base.AppData.prepareCustomerGroups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.inputfields.InputField;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.inputfields.InputFieldsGroup;
import com.sadiwala.shivam.inputfields.InputFieldsGroupsContainer;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.PhoneUtil;
import com.sadiwala.shivam.util.UiUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class CustomerDetailsActivity extends BaseActivity {

    public static final String CUSTOMER_DATA = "customer_data";

    private Toolbar mToolbar;
    private CustomTextView tvName;
    private RelativeLayout rlCall;

    private EventBus mBus;
    private Customer customer;

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, CustomerDetailsActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        init();
        loadGroupView(savedInstanceState);
    }

    public EventBus getBus() {
        return mBus;
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.customer_details));
        mBus = UiUtil.getVymoEventBus();

        if (getIntent().getExtras() != null) {
            customer = Gson.getInstance().fromJson(getIntent().getStringExtra(CUSTOMER_DATA), Customer.class);
        }

        tvName = findViewById(R.id.name);
        tvName.setText(customer.getName().getValue());

        rlCall = findViewById(R.id.rlCall);
        rlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtil.makeCall(CustomerDetailsActivity.this, customer.getMobile().getValue());
            }
        });
    }

    private void loadGroupView(Bundle savedInstanceState) {

        ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
        ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();
        prepareCustomerGroups(inputFieldTypes, inputFieldValues, customer);

        InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", "Other Info",
                inputFieldValues, inputFieldTypes, InputField.EditMode.READ, false, getBus(), null, null);

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();
        inputFieldsGroups.add(inputFieldsGroup);
        InputFieldsGroupsContainer groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.READ, null);

        ((ViewGroup) findViewById(R.id.input_fields)).addView(groupView.getDisplayView());
    }

}
