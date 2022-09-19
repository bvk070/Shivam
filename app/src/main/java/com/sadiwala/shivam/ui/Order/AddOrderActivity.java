package com.sadiwala.shivam.ui.Order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.inputfields.InputField;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldsGroup;
import com.sadiwala.shivam.inputfields.InputFieldsGroupsContainer;
import com.sadiwala.shivam.ui.main.BaseAddActivity;

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
        }

        init();
        loadGroupView(savedInstanceState);
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadGroupView(Bundle savedInstanceState) {

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();

        if (AppData.PRODUCT_TYPE.ALINE_GOWN.toString().equals(productType)) {
            setTitle(getString(R.string.add_aline));

            ArrayList<InputFieldType> inputFieldTypes1 = AppData.getCustomerSectionInputs();
            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "customer_info", "Customer Info",
                    null, inputFieldTypes1, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            ArrayList<InputFieldType> inputFieldTypes2 = AppData.getAlineGownForm();
            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "aline_gown_info", "Aline Gown Info",
                    null, inputFieldTypes2, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

        } else if (AppData.PRODUCT_TYPE.CHAPATTI_GOWN.toString().equals(productType)) {
            setTitle(getString(R.string.add_chapatti));

            ArrayList<InputFieldType> inputFieldTypes1 = AppData.getCustomerSectionInputs();
            InputFieldsGroup inputFieldsGroup1 = new InputFieldsGroup(this, savedInstanceState, "customer_info", "Customer Info",
                    null, inputFieldTypes1, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup1);

            ArrayList<InputFieldType> inputFieldTypes2 = AppData.getChapatiGownForm();
            InputFieldsGroup inputFieldsGroup2 = new InputFieldsGroup(this, savedInstanceState, "aline_gown_info", "Aline Gown Info",
                    null, inputFieldTypes2, InputField.EditMode.WRITE, false, getBus(), null, null);
            inputFieldsGroups.add(inputFieldsGroup2);

        } else if (AppData.PRODUCT_TYPE.NIGHT_DRESS.toString().equals(productType)) {
            setTitle(getString(R.string.add_nightdress));
        }

        InputFieldsGroupsContainer groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.WRITE, null);
        addInputField(INPUTS, groupView);
    }

}
