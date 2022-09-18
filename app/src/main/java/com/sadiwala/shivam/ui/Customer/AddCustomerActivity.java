package com.sadiwala.shivam.ui.Customer;

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

public class AddCustomerActivity extends BaseAddActivity {

    private Toolbar mToolbar;

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, AddCustomerActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
        loadGroupView(savedInstanceState);
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.add_customer));
    }

    private void loadGroupView(Bundle savedInstanceState) {
        ArrayList<InputFieldType> inputFieldTypes = AppData.getCustomerForm();
        InputFieldsGroup inputFieldsGroup = new InputFieldsGroup(this, savedInstanceState, "code", "Customer Details",
                null, inputFieldTypes, InputField.EditMode.WRITE, false, getBus(), null, null);

        ArrayList<InputFieldsGroup> inputFieldsGroups = new ArrayList<>();
        inputFieldsGroups.add(inputFieldsGroup);
        InputFieldsGroupsContainer groupView = new InputFieldsGroupsContainer(this, inputFieldsGroups, getBus(), InputField.EditMode.WRITE, null);

        addInputField(INPUTS, groupView);
    }


}
