package com.sadiwala.shivam.ui.Order;

import static com.sadiwala.shivam.base.AppData.prepareOrderGroups;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_CUSTOMERS;
import static com.sadiwala.shivam.ui.Customer.CustomerDetailsActivity.CUSTOMER_DATA;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.base.ShivamApplication;
import com.sadiwala.shivam.inputfields.InputField;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.inputfields.InputFieldsGroup;
import com.sadiwala.shivam.inputfields.InputFieldsGroupsContainer;
import com.sadiwala.shivam.inputfields.SelectionInputField;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.ui.Customer.CustomerDetailsActivity;
import com.sadiwala.shivam.ui.main.BaseDetailsActivity;
import com.sadiwala.shivam.util.AaryaConstants;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.PhoneUtil;
import com.sadiwala.shivam.util.UiUtil;
import com.sadiwala.shivam.util.Util;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class OrderDetailsActivity extends BaseDetailsActivity {

    public static final String ORDER_DATA = "order_data";

    private Toolbar mToolbar;
    private CustomTextView tvName;
    private RelativeLayout rlCall, rlWhatsApp, rlDownload, profileView;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AaryaConstants.REQUEST_CODE_UPDATE_ORDER:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.hasExtra(ORDER_DATA)) {
                        order = Gson.getInstance().fromJson(data.getStringExtra(ORDER_DATA), Order.class);
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
        setTitle(getString(R.string.order_details));
        mBus = UiUtil.getVymoEventBus();

        if (getIntent().getExtras() != null) {
            order = Gson.getInstance().fromJson(getIntent().getStringExtra(ORDER_DATA), Order.class);
            customer = DataController.getCustomerById(SelectionInputField.getCodeFromJsonValue(order.getCustomer().getValue()));
        }

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

        tvName = findViewById(R.id.name);

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

        refreshData();
    }

    private void loadCustomerRelatedInfo() {
        rlCall = findViewById(R.id.rlCall);
        rlCall.setVisibility(View.VISIBLE);
        rlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtil.makeCall(OrderDetailsActivity.this, customer.getMobile().getValue());
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

    private void refreshData() {
        if (customer == null) {
            tvName.setText(getString(R.string.customer_not_exists));

            showUpdate = false;
            invalidateOptionsMenu();

        } else {
            tvName.setText(customer.getName().getValue());
            loadCustomerRelatedInfo();

            showUpdate = true;
            invalidateOptionsMenu();
        }

        loadGroupView(null);

    }

    private void loadGroupView(Bundle savedInstanceState) {

        ((ViewGroup) findViewById(R.id.input_fields)).removeAllViews();

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();
        String customerName = "";
        if (customer != null) {
            customerName = customer.getName().getValue();
        }

        if (AppData.PRODUCT_TYPE.ALINE_GOWN.toString().equals(order.getType())) {

            ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
            ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();

            prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

            InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", customerName + " / " + AppData.getType(this, order.getType()),
                    inputFieldValues, AppData.getAlineGownForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup);

        } else if (AppData.PRODUCT_TYPE.CHAPATTI_GOWN.toString().equals(order.getType())) {

            ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
            ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();

            prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

            InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", customerName + " / " + AppData.getType(this, order.getType()),
                    inputFieldValues, AppData.getChapatiGownForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup);

        } else if (AppData.PRODUCT_TYPE.NIGHT_DRESS.toString().equals(order.getType())) {

            ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
            ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();

            prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "night_shoot_top", customerName + " / " + AppData.getType(this, order.getType()) + " / " + getString(R.string.night_shoot_top),
                    inputFieldValues, AppData.getNightShootTopForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "night_shoot_bottom", customerName + " / " + AppData.getType(this, order.getType()) + " / " + getString(R.string.night_shoot_bottom),
                    inputFieldValues, AppData.getNightShootBottomForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

        } else if (AppData.PRODUCT_TYPE.LENGHI.toString().equals(order.getType())) {

            ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
            ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();

            prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "night_shoot_bottom", customerName + " / " + AppData.getType(this, order.getType()),
                    inputFieldValues, AppData.getNightShootBottomForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

        }

        groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.READ, null);
        ((ViewGroup) findViewById(R.id.input_fields)).addView(groupView.getDisplayView());
    }

}
