package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.base.BaseBottomSheet.CLEAR_CLICK;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_CUSTOMERS;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_ORDERS;

import android.app.Activity;
import android.content.Intent;
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
import com.sadiwala.shivam.inputfields.SelectionInputField;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.models.common.IBottomSheetListener;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.Customer.AddCustomerActivity;
import com.sadiwala.shivam.util.AaryaConstants;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.UiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public abstract class BaseAddActivity extends BaseActivity implements IBottomSheetListener {

    protected static final String INPUTS = "inputs";
    private EventBus mBus;
    private Map<String, InputField> mInputFields = new HashMap<>();
    protected Map<String, InputField> mRefreshInputFieldsMap = new HashMap<>();

    protected String productType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputFields.clear();
        mRefreshInputFieldsMap.clear();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AaryaConstants.REQUEST_CODE_INPUTFIELD: {
                if (resultCode == Activity.RESULT_OK) {
                    if (mRefreshInputFieldsMap != null && !mRefreshInputFieldsMap.isEmpty()) {
                        if (data.hasExtra(SelectionInputField.EXTRAS_CODE) && mRefreshInputFieldsMap != null) {
                            mRefreshInputFieldsMap.get(data.getStringExtra(SelectionInputField.EXTRAS_CODE)).updateViewPostSelection(data.getStringExtra(SelectionInputField.EXTRAS_SELECTED_OPTIONS));
                        }
                        if (data.hasExtra(CLEAR_CLICK) && data.getExtras().getBoolean(CLEAR_CLICK)) {
                            mRefreshInputFieldsMap.get(data.getStringExtra(SelectionInputField.EXTRAS_CODE)).clearView();
                        }
                    }
                }
                return;
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addInputFieldInRefreshMap(String code, InputField inputField) {
        mRefreshInputFieldsMap.put(code, inputField);
    }

    public EventBus getBus() {
        return mBus;
    }

    public void onEvent(InputField inputField) {

    }

    public IBottomSheetListener getBottomSheetListener() {
        return this;
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

    protected void disableSubmitButton() {
        findViewById(R.id.submit).setEnabled(false);
    }

    protected void enableSubmitButton() {
        findViewById(R.id.submit).setEnabled(true);
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
                } else {
                    disableSubmitButton();
                    addOrder();
                }
            }

        }

    }

    private void addOrder() {

        HashMap<String, InputFieldValue> hashMap = prepareInputValues();

        Order order = new Order();
        order.setTimestamp(System.currentTimeMillis());
        order.setType(productType);
        order.setCustomer(hashMap.get(Customer.CUSTOMER_CODE));

        order.setShoulder(hashMap.get(Order.SHOULDER));
        order.setChest(hashMap.get(Order.CHEST));
        order.setWaist(hashMap.get(Order.WAIST));
        order.setSleeve(hashMap.get(Order.SLEEVE));
        order.setLength(hashMap.get(Order.LENGTH));
        order.setNeckType(hashMap.get(Order.NECK_TYPE));
        order.setNeckSize(hashMap.get(Order.NECK_SIZE));
        order.setPattern(hashMap.get(Order.PATTERN));
        order.setPocket(hashMap.get(Order.POCKET));
        order.setPocketSize(hashMap.get(Order.POCKET_SIZE));
        order.setMundho(hashMap.get(Order.MUNDHO));
        order.setFitting(hashMap.get(Order.FITTING));

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_ORDERS);
        collectionReference.add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), getString(R.string.order_added), Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.order_failed), Toast.LENGTH_LONG).show();
                enableSubmitButton();
            }
        });

    }

    private void addCustomer() {

        HashMap<String, InputFieldValue> hashMap = prepareInputValues();

        Customer customer = new Customer();
        customer.setTimestamp(System.currentTimeMillis());
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
        HashMap<String, InputFieldValue> hashMap = AppData.getEmptyInputFieldValues();
        String inputvalues = getInputFieldValue(INPUTS);
        ArrayList<InputFieldValue> inputFieldValues = Gson.getInstance().fromJson(inputvalues, new TypeToken<List<InputFieldValue>>() {
        }.getType());

        for (InputFieldValue inputFieldValue : inputFieldValues) {
            hashMap.put(inputFieldValue.getCode(), inputFieldValue);
        }
        return hashMap;
    }


}
