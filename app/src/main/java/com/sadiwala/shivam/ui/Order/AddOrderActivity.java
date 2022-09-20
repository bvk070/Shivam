package com.sadiwala.shivam.ui.Order;

import static com.sadiwala.shivam.base.AppData.prepareCustomerGroups;
import static com.sadiwala.shivam.base.AppData.prepareOrderGroups;
import static com.sadiwala.shivam.ui.Order.OrderDetailsActivity.ORDER_DATA;

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
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.ui.main.BaseAddActivity;
import com.sadiwala.shivam.util.Gson;

import java.util.ArrayList;

public class AddOrderActivity extends BaseAddActivity {

    public static final String PRODUCT_TYPE_DATA = "product_type_data";

    private Toolbar mToolbar;

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, AddOrderActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (getIntent().getExtras() != null) {
            productType = getIntent().getStringExtra(PRODUCT_TYPE_DATA);
            order = Gson.getInstance().fromJson(getIntent().getStringExtra(ORDER_DATA), Order.class);
            if (order != null) {
                productType = order.getType();
            }
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

        if (order != null) {
            btnSubmit.setText(getString(R.string.update));
        } else {
            btnSubmit.setText(getString(R.string.add));
        }
    }

    private void loadGroupView(Bundle savedInstanceState) {

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();

        ArrayList<InputFieldValue> inputFieldValues = new ArrayList<>();
        if (order != null) {
            prepareOrderGroups(null, inputFieldValues, order);
        }

        if (AppData.PRODUCT_TYPE.ALINE_GOWN.toString().equals(productType)) {
            if (order != null) {
                setTitle(getString(R.string.update_aline));
            } else {
                setTitle(getString(R.string.add_aline));
            }

            ArrayList<InputFieldType> inputFieldTypes1 = AppData.getCustomerSectionInputs();
            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "customer_info", "Customer Info",
                    inputFieldValues, inputFieldTypes1, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            ArrayList<InputFieldType> inputFieldTypes2 = AppData.getAlineGownForm();
            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "aline_gown_info", "Aline Gown Info",
                    inputFieldValues, inputFieldTypes2, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

        } else if (AppData.PRODUCT_TYPE.CHAPATTI_GOWN.toString().equals(productType)) {
            if (order != null) {
                setTitle(getString(R.string.update_chapatti));
            } else {
                setTitle(getString(R.string.add_chapatti));
            }

            ArrayList<InputFieldType> inputFieldTypes1 = AppData.getCustomerSectionInputs();
            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "customer_info", "Customer Info",
                    inputFieldValues, inputFieldTypes1, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            ArrayList<InputFieldType> inputFieldTypes2 = AppData.getChapatiGownForm();
            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "aline_gown_info", "Aline Gown Info",
                    inputFieldValues, inputFieldTypes2, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

        } else if (AppData.PRODUCT_TYPE.NIGHT_DRESS.toString().equals(productType)) {
            if (order != null) {
                setTitle(getString(R.string.update_nightdress));
            } else {
                setTitle(getString(R.string.add_nightdress));
            }

            ArrayList<InputFieldType> inputFieldTypes1 = AppData.getCustomerSectionInputs();
            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "customer_info", "Customer Info",
                    inputFieldValues, inputFieldTypes1, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            ArrayList<InputFieldType> inputFieldTypes2 = AppData.getNightShootTopForm();
            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "top_info", getString(R.string.night_shoot_top),
                    inputFieldValues, inputFieldTypes2, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

            ArrayList<InputFieldType> inputFieldTypes3 = AppData.getNightShootBottomForm();
            InputFieldsGroup inputFieldsGroup3 = new InputFieldsGroup(this, savedInstanceState, "bottom_info", getString(R.string.night_shoot_bottom),
                    inputFieldValues, inputFieldTypes3, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup3);
        } else if (AppData.PRODUCT_TYPE.LENGHI.toString().equals(productType)) {
            if (order != null) {
                setTitle(getString(R.string.update_lenghi));
            } else {
                setTitle(getString(R.string.add_lenghi));
            }

            ArrayList<InputFieldType> inputFieldTypes1 = AppData.getCustomerSectionInputs();
            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "customer_info", "Customer Info",
                    inputFieldValues, inputFieldTypes1, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            ArrayList<InputFieldType> inputFieldTypes3 = AppData.getNightShootBottomForm();
            InputFieldsGroup inputFieldsGroup3 = new InputFieldsGroup(this, savedInstanceState, "bottom_info", getString(R.string.night_shoot_bottom),
                    inputFieldValues, inputFieldTypes3, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup3);
        }

        InputFieldsGroupsContainer groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.WRITE, null);
        addInputField(INPUTS, groupView);
    }

}
