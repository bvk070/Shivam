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
import com.sadiwala.shivam.ui.main.BaseDetailsActivity;
import com.sadiwala.shivam.util.AaryaConstants;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.PhoneUtil;
import com.sadiwala.shivam.util.UiUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class CustomerDetailsActivity extends BaseDetailsActivity {

    public static final String CUSTOMER_DATA = "customer_data";

    private Toolbar mToolbar;
    private CustomTextView tvName;
    private RelativeLayout rlCall;

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
        refreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AaryaConstants.REQUEST_CODE_UPDATE_CUSTOMER:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.hasExtra(CUSTOMER_DATA)) {
                        customer = Gson.getInstance().fromJson(data.getStringExtra(CUSTOMER_DATA), Customer.class);
                        refreshData();
                    }
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
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
        rlCall = findViewById(R.id.rlCall);
        rlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtil.makeCall(CustomerDetailsActivity.this, customer.getMobile().getValue());
            }
        });
    }

    private void refreshData() {
        tvName.setText(customer.getName().getValue());
        loadGroupView(null);
    }

    private void loadGroupView(Bundle savedInstanceState) {
        ((ViewGroup) findViewById(R.id.input_fields)).removeAllViews();
        ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
        ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();
        prepareCustomerGroups(inputFieldTypes, inputFieldValues, customer);

        InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", "Other Info",
                inputFieldValues, inputFieldTypes, InputField.EditMode.READ, false, getBus(), null, null);

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();
        inputFieldsGroups.add(inputFieldsGroup);
        groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.READ, null);

        ((ViewGroup) findViewById(R.id.input_fields)).addView(groupView.getDisplayView());
    }

}
