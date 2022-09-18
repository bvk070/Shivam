package com.sadiwala.shivam.ui.Order;

import static com.sadiwala.shivam.base.AppData.prepareOrderGroups;
import static com.sadiwala.shivam.ui.Customer.CustomerDetailsActivity.CUSTOMER_DATA;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.base.ShivamApplication;
import com.sadiwala.shivam.inputfields.InputField;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.inputfields.InputFieldsGroup;
import com.sadiwala.shivam.inputfields.InputFieldsGroupsContainer;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.Customer.CustomerDetailsActivity;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.PhoneUtil;
import com.sadiwala.shivam.util.UiUtil;
import com.sadiwala.shivam.util.Util;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class OrderDetailsActivity extends BaseActivity {

    public static final String ORDER_DATA = "order_data";

    private Toolbar mToolbar;
    private CustomTextView tvName;
    private RelativeLayout rlCall, rlWhatsApp, rlDownload, profileView;

    private EventBus mBus;
    private Order order;
    private Customer customer;
    private InputFieldsGroupsContainer groupView;

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
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
        setTitle(getString(R.string.order_details));
        mBus = UiUtil.getVymoEventBus();

        if (getIntent().getExtras() != null) {
            order = Gson.getInstance().fromJson(getIntent().getStringExtra(ORDER_DATA), Order.class);
            customer = DataController.getCustomerById(order.getCustomer().getValue());
        }

        tvName = findViewById(R.id.name);
        tvName.setText(customer.getName().getValue());

        rlCall = findViewById(R.id.rlCall);
        rlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtil.makeCall(OrderDetailsActivity.this, customer.getMobile().getValue());
            }
        });

        rlWhatsApp = findViewById(R.id.rlWhatsApp);
        rlWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Util.getScreenshot(OrderDetailsActivity.this, groupView.getDisplayView());
                if (path != null) {
                    Uri uriFileProvider = FileProvider.getUriForFile(
                            ShivamApplication.getAppContext(),
                            ShivamApplication.getAppContext()
                                    .getPackageName() + ".provider", new File(path));
                    Util.sendWhatsAppMessage(OrderDetailsActivity.this, "Hi", uriFileProvider);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.soemthing_wrong), Toast.LENGTH_LONG).show();
                }
            }
        });

        rlDownload = findViewById(R.id.rlDownload);
        rlDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Util.getScreenshot(OrderDetailsActivity.this, groupView.getDisplayView());
                if (path != null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.screenshot_saved), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.soemthing_wrong), Toast.LENGTH_LONG).show();
                }
            }
        });

        profileView = findViewById(R.id.profileView);
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(CUSTOMER_DATA, Gson.getInstance().toJson(customer));
                CustomerDetailsActivity.start(OrderDetailsActivity.this, bundle);
            }
        });
    }

    private void loadGroupView(Bundle savedInstanceState) {

        ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
        ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();
        prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

        InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", customer.getName().getValue() + " / " + AppData.getType(this, order.getType()),
                inputFieldValues, inputFieldTypes, InputField.EditMode.READ, false, getBus(), null, null);

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();
        inputFieldsGroups.add(inputFieldsGroup);
        groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.READ, null);

        ((ViewGroup) findViewById(R.id.input_fields)).addView(groupView.getDisplayView());
    }

}
