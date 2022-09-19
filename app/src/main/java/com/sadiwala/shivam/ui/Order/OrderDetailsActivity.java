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

        if (customer != null) {
            loadData();
        } else {
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

        loadGroupView(null);
    }

    private void loadGroupView(Bundle savedInstanceState) {

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();

        if (AppData.PRODUCT_TYPE.ALINE_GOWN.toString().equals(order.getType())) {

            ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
            ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();

            prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

            InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", customer.getName().getValue() + " / " + AppData.getType(this, order.getType()),
                    inputFieldValues, AppData.getAlineGownForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup);

        } else if (AppData.PRODUCT_TYPE.CHAPATTI_GOWN.toString().equals(order.getType())) {

            ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
            ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();

            prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

            InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", customer.getName().getValue() + " / " + AppData.getType(this, order.getType()),
                    inputFieldValues, AppData.getChapatiGownForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup);

        } else if (AppData.PRODUCT_TYPE.NIGHT_DRESS.toString().equals(order.getType())) {

            ArrayList<InputFieldType> inputFieldTypes = new ArrayList<>();
            ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();

            prepareOrderGroups(inputFieldTypes, inputFieldValues, order);

            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "night_shoot_top", customer.getName().getValue() + " / " + AppData.getType(this, order.getType()) + " / " + getString(R.string.night_shoot_top),
                    inputFieldValues, AppData.getNightShootTopForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "night_shoot_bottom", customer.getName().getValue() + " / " + AppData.getType(this, order.getType()) + " / " + getString(R.string.night_shoot_bottom),
                    inputFieldValues, AppData.getNightShootBottomForm(), InputField.EditMode.READ, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

        }

        groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.READ, null);
        ((ViewGroup) findViewById(R.id.input_fields)).addView(groupView.getDisplayView());
    }

}
