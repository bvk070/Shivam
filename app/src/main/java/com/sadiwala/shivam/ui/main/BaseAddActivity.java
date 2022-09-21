package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.base.BaseBottomSheet.CLEAR_CLICK;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_CUSTOMERS;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_ORDERS;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.addCustomerInCache;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.addOrderInCache;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.updateCustomerInCache;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.updateOrderInCache;
import static com.sadiwala.shivam.ui.Customer.CustomerDetailsActivity.CUSTOMER_DATA;
import static com.sadiwala.shivam.ui.Order.OrderDetailsActivity.ORDER_DATA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

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
import com.sadiwala.shivam.models.common.CodeName;
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

    protected AppCompatButton btnSubmit;
    protected Customer customer;
    protected Order order;
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
            case AaryaConstants.REQUEST_CODE_ADD_CUSTOMER: {
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
        findViewById(R.id.btnSubmit).setEnabled(false);
    }

    protected void enableSubmitButton() {
        findViewById(R.id.btnSubmit).setEnabled(true);
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
                    if (customer != null) {
                        updateCustomer();
                    } else {
                        addCustomer();
                    }
                } else {
                    disableSubmitButton();
                    if (order != null) {
                        updateOrder();
                    } else {
                        addOrder();
                    }
                }
            }

        }

    }

    private void addOrder() {

        HashMap<String, InputFieldValue> hashMap = prepareInputValues();

        Order order = new Order();
        order.setTimestamp(System.currentTimeMillis());
        order.setType(productType);

        order.setCustomer(hashMap.get(Order.CUSTOMER));
        order.setShoulder(hashMap.get(Order.SHOULDER));
        order.setChest(hashMap.get(Order.CHEST));
        order.setWaist(hashMap.get(Order.WAIST));
        order.setSleeve(hashMap.get(Order.SLEEVE));
        order.setLength(hashMap.get(Order.LENGTH));
        order.setNeckType(hashMap.get(Order.NECK_TYPE));
        order.setNeckSize(hashMap.get(Order.NECK_SIZE));
        order.setPattern(hashMap.get(Order.PATTERN));
        order.setPocket(hashMap.get(Order.POCKET));
        order.setLenghaPocket(hashMap.get(Order.LENGHA_POCKET));
        order.setPocketSize(hashMap.get(Order.POCKET_SIZE));
        order.setMundho(hashMap.get(Order.MUNDHO));
        order.setFitting(hashMap.get(Order.FITTING));
        order.setKotho(hashMap.get(Order.KOTHO));
        order.setHips(hashMap.get(Order.HIPS));
        order.setGher(hashMap.get(Order.GHER));
        order.setCut(hashMap.get(Order.CUT));
        order.setMori(hashMap.get(Order.MORI));
        order.setKhristak(hashMap.get(Order.KHRISTAK));
        order.setIlastic(hashMap.get(Order.ILASTIC));
        order.setClothDesign(hashMap.get(Order.CLOTH_DESIGN));
        order.setClothColor(hashMap.get(Order.CLOTH_COLOR));

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_ORDERS);
        collectionReference.add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                order.setId(documentReference.getId());
                addOrderInCache(order);

                Toast.makeText(getApplicationContext(), getString(R.string.order_added), Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.add_order_failed), Toast.LENGTH_LONG).show();
                enableSubmitButton();
            }
        });

    }

    private void updateOrder() {

        HashMap<String, InputFieldValue> hashMap = prepareInputValues();

        Map<String, Object> map = new HashMap<>();
        map.put(Order.CUSTOMER, hashMap.get(Order.CUSTOMER));
        map.put(Order.SHOULDER, hashMap.get(Order.SHOULDER));
        map.put(Order.CHEST, hashMap.get(Order.CHEST));
        map.put(Order.WAIST, hashMap.get(Order.WAIST));
        map.put(Order.SLEEVE, hashMap.get(Order.SLEEVE));
        map.put(Order.LENGTH, hashMap.get(Order.LENGTH));
        map.put(Order.NECK_TYPE, hashMap.get(Order.NECK_TYPE));
        map.put(Order.NECK_SIZE, hashMap.get(Order.NECK_SIZE));
        map.put(Order.PATTERN, hashMap.get(Order.PATTERN));
        map.put(Order.POCKET, hashMap.get(Order.POCKET));
        map.put(Order.LENGHA_POCKET, hashMap.get(Order.LENGHA_POCKET));
        map.put(Order.POCKET_SIZE, hashMap.get(Order.POCKET_SIZE));
        map.put(Order.MUNDHO, hashMap.get(Order.MUNDHO));
        map.put(Order.FITTING, hashMap.get(Order.FITTING));
        map.put(Order.KOTHO, hashMap.get(Order.KOTHO));
        map.put(Order.HIPS, hashMap.get(Order.HIPS));
        map.put(Order.GHER, hashMap.get(Order.GHER));
        map.put(Order.CUT, hashMap.get(Order.CUT));
        map.put(Order.MORI, hashMap.get(Order.MORI));
        map.put(Order.KHRISTAK, hashMap.get(Order.KHRISTAK));
        map.put(Order.ILASTIC, hashMap.get(Order.ILASTIC));
        map.put(Order.CLOTH_DESIGN, hashMap.get(Order.CLOTH_DESIGN));
        map.put(Order.CLOTH_COLOR, hashMap.get(Order.CLOTH_COLOR));

        order.setCustomer(hashMap.get(Order.CUSTOMER));
        order.setShoulder(hashMap.get(Order.SHOULDER));
        order.setChest(hashMap.get(Order.CHEST));
        order.setWaist(hashMap.get(Order.WAIST));
        order.setSleeve(hashMap.get(Order.SLEEVE));
        order.setLength(hashMap.get(Order.LENGTH));
        order.setNeckType(hashMap.get(Order.NECK_TYPE));
        order.setNeckSize(hashMap.get(Order.NECK_SIZE));
        order.setPattern(hashMap.get(Order.PATTERN));
        order.setPocket(hashMap.get(Order.POCKET));
        order.setLenghaPocket(hashMap.get(Order.LENGHA_POCKET));
        order.setPocketSize(hashMap.get(Order.POCKET_SIZE));
        order.setMundho(hashMap.get(Order.MUNDHO));
        order.setFitting(hashMap.get(Order.FITTING));
        order.setKotho(hashMap.get(Order.KOTHO));
        order.setHips(hashMap.get(Order.HIPS));
        order.setGher(hashMap.get(Order.GHER));
        order.setCut(hashMap.get(Order.CUT));
        order.setMori(hashMap.get(Order.MORI));
        order.setKhristak(hashMap.get(Order.KHRISTAK));
        order.setIlastic(hashMap.get(Order.ILASTIC));
        order.setClothDesign(hashMap.get(Order.CLOTH_DESIGN));
        order.setClothColor(hashMap.get(Order.CLOTH_COLOR));

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_ORDERS);
        collectionReference.document(order.getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                updateOrderInCache(order);
                Toast.makeText(getApplicationContext(), getString(R.string.order_updated), Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.putExtra(ORDER_DATA, Gson.getInstance().toJson(order));
                setResult(RESULT_OK, intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.update_order_failed), Toast.LENGTH_LONG).show();
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
                customer.setId(documentReference.getId());
                addCustomerInCache(customer);

                Toast.makeText(getApplicationContext(), getString(R.string.customer_added), Toast.LENGTH_LONG).show();
                if (getIntent().getExtras() != null && getIntent().hasExtra(SelectionInputField.EXTRAS_CODE)) {
                    ArrayList<CodeName> codeNames = new ArrayList<>();
                    codeNames.add(new CodeName(customer.getId(), customer.getName().getValue()));
                    Intent intent = new Intent();
                    intent.putExtra(SelectionInputField.EXTRAS_CODE, getIntent().getStringExtra(SelectionInputField.EXTRAS_CODE));
                    intent.putExtra(SelectionInputField.EXTRAS_SELECTED_OPTIONS, Gson.getInstance().toJson(codeNames));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.add_customer_failed), Toast.LENGTH_LONG).show();
                enableSubmitButton();
            }
        });

    }

    private void updateCustomer() {

        HashMap<String, InputFieldValue> hashMap = prepareInputValues();

        Map<String, Object> map = new HashMap<>();
        map.put(Customer.NAME, hashMap.get(Customer.NAME));
        map.put(Customer.MOBILE, hashMap.get(Customer.MOBILE));
        map.put(Customer.ADDRESS, hashMap.get(Customer.ADDRESS));
        map.put(Customer.AREA, hashMap.get(Customer.AREA));
        map.put(Customer.PINCODE, hashMap.get(Customer.PINCODE));

        customer.setName(hashMap.get(Customer.NAME));
        customer.setMobile(hashMap.get(Customer.MOBILE));
        customer.setAddress(hashMap.get(Customer.ADDRESS));
        customer.setArea(hashMap.get(Customer.AREA));
        customer.setPincode(hashMap.get(Customer.PINCODE));

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_CUSTOMERS);
        collectionReference.document(customer.getId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                updateCustomerInCache(customer);
                Toast.makeText(getApplicationContext(), getString(R.string.customer_updated), Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.putExtra(CUSTOMER_DATA, Gson.getInstance().toJson(customer));
                setResult(RESULT_OK, intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.update_customer_failed), Toast.LENGTH_LONG).show();
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
