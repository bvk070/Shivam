package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_CUSTOMERS;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.inputfields.InputField;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.Customer.AddCustomerActivity;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.UiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class BaseAddActivity extends BaseActivity {

    protected static final String INPUTS = "inputs";
    private EventBus mBus;
    private Map<String, InputField> mInputFields = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputFields.clear();
        mBus = UiUtil.getVymoEventBus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public EventBus getBus() {
        return mBus;
    }

    public void onEvent(InputField inputField) {

    }

    protected void addInputField(String key, InputField inputField) {
        View inputFieldFormView = inputField.getFormView();
        addView(inputFieldFormView);
        addInputFieldWithoutView(key, inputField);
    }

    protected void addInputFieldWithoutView(String key, InputField inputField) {
        mInputFields.put(key, inputField);
    }

    protected void removeInputField(String key) {
        if (mInputFields.get(key) != null) {
            mInputFields.remove(key);
        }
    }

    protected InputField getInputField(String key) {
        return mInputFields.get(key);
    }

    protected String getInputFieldValue(String key) {
        return getInputField(key).getJsonValue();
    }

    protected void addView(View inputFieldFormView) {
        ((ViewGroup) findViewById(R.id.input_fields)).addView(inputFieldFormView);
    }

    public void submit(View view) {

        if (mInputFields != null && mInputFields.size() > 0) {

            boolean validInput = true;
            for (InputField inputField : mInputFields.values()) {
                if (!inputField.validateInput()) {
                    validInput = false;
                }
            }

            if (validInput) {
                if (this instanceof AddCustomerActivity) {
                    disableSubmitButton();
                    addCustomer();
                }
            }

        }

    }

    protected void disableSubmitButton() {
        findViewById(R.id.submit).setEnabled(false);
    }

    protected void enableSubmitButton() {
        findViewById(R.id.submit).setEnabled(true);
    }

    private void addCustomer() {

        HashMap<String, InputFieldValue> hashMap = prepareInputValues();

        Customer customer = new Customer();
        customer.setName(hashMap.get(Customer.NAME));
        customer.setMobile(hashMap.get(Customer.MOBILE));
        customer.setAddress(hashMap.get(Customer.ADDRESS));
        customer.setArea(hashMap.get(Customer.AREA));
        customer.setPincode(hashMap.get(Customer.PINCODE));

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_CUSTOMERS);
        collectionReference.add(customer).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), getString(R.string.customer_added), Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.customer_failed), Toast.LENGTH_LONG).show();
                enableSubmitButton();
            }
        });

    }

    private HashMap<String, InputFieldValue> prepareInputValues() {
        HashMap<String, InputFieldValue> hashMap = AppData.getCustomerEmptyInputFieldValues();
        String inputvalues = getInputFieldValue(INPUTS);
        ArrayList<InputFieldValue> inputFieldValues = Gson.getInstance().fromJson(inputvalues, new TypeToken<List<InputFieldValue>>() {
        }.getType());

        for (InputFieldValue inputFieldValue : inputFieldValues) {
            hashMap.put(inputFieldValue.getCode(), inputFieldValue);
        }
        return hashMap;
    }


}
