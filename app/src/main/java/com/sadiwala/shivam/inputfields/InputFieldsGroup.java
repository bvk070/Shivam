package com.sadiwala.shivam.inputfields;


import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_DECIMAL;
import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_NUMBER;
import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_SENTENCE;
import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_TEXT;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.common.Data;
import com.sadiwala.shivam.ui.main.BaseAddActivity;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.UiUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.greenrobot.event.EventBus;


public class InputFieldsGroup extends ParentInputField implements ParentInputField.OnRefreshListListener, View.OnClickListener {
    private static final String TAG = "InputFieldsGroup";
    private static final String PREF_IFT_LIST = "ift_list_";
    private LinearLayout mGroupLayout;
    private AppCompatActivity mActivity;
    private List<InputFieldValue> mInputFieldValues;
    private LinearLayout mInputFieldsGroupLayout;
    private LinearLayout mInputFieldsContainerLayout;
    private LinearLayout inputFieldGroupHeader;
    private ImageView ivExpandCollapseIcon;
    private List<InputFieldType> mInputFieldTypes;
    private List<ParentInputField> mInputFields;
    private List<String> mDisabledInputFieldList;

    private String type;
    private boolean mShowDivider;
    private String mCode = "";
    private String mName = "";
    private EventBus mBus;
    private List<InputFieldType> autoComputationInputs;
    private List<InputFieldType> autoEvaluateInputs = new ArrayList<>();
    private HashMap<String, TextView> codeViewMap = new HashMap<>();
    private boolean isExpanded = true;
    private boolean mIsDetect = false;

    public InputFieldsGroup(AppCompatActivity activity, Bundle savedInstanceState, String code, String name,
                            List<InputFieldValue> inputValues, List<InputFieldType> inputs,
                            EditMode mode, boolean showDivider, EventBus bus, String startState, List<String> disabledInputFieldList) {
        super(activity, bus, mode, startState);
        initialise(activity, savedInstanceState, code, name, inputValues, inputs, mode, showDivider, bus,
                startState, disabledInputFieldList, false);
    }

    private void initialise(AppCompatActivity activity, Bundle savedInstanceState, String code, String name,
                            List<InputFieldValue> inputValues, List<InputFieldType> inputs,
                            EditMode mode, boolean showDivider, EventBus bus, String startState, List<String> disabledInputFieldList,
                            boolean isDetect) {
        mCode = code;
        mName = name;
        mActivity = activity;
        mIsDetect = isDetect;
        LayoutInflater inflater = activity.getLayoutInflater();
        mGroupLayout = (LinearLayout) inflater.inflate(R.layout.input_field_group, null);
        mInputFieldsGroupLayout = mGroupLayout.findViewById(R.id.input_field_group_layout);
        mInputFieldsContainerLayout = (LinearLayout) mGroupLayout.findViewById(R.id.input_fields_container);
        inputFieldGroupHeader = (LinearLayout) mGroupLayout.findViewById(R.id.input_field_group_header);
        ivExpandCollapseIcon = (ImageView) mGroupLayout.findViewById(R.id.iv_expand_collapse);
        ivExpandCollapseIcon.setColorFilter(UiUtil.getBrandedPrimaryColorWithDefault());
        LinearLayout divider = mGroupLayout.findViewById(R.id.divider);
        TextView txtTitle = (TextView) mGroupLayout.findViewById(R.id.txt_group_title);
        if (!TextUtils.isEmpty(mName)) {
            txtTitle.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            txtTitle.setText(mName);
            setChevronIcon(activity);
            setHeader(activity);
        } else {
            txtTitle.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            ivExpandCollapseIcon.setVisibility(View.GONE);
        }

        mShowDivider = showDivider;
        refreshListListener = this;
        mSavedInstance = savedInstanceState;
        if (inputs != null && !inputs.isEmpty()) {
            mCode = inputs.get(0).getCode();
        } else {
            // Note: because we send empty list while initializing inputFieldGroup from AddCalendarItemActivity and this code is being used while saving state during config change, OIF etc.
            // generating random code every time so app does not load old field if some two input fields have same code in two diff tasks.
            mCode = UUID.randomUUID().toString();
        }

        mInputFieldTypes = inputs;

        if (mSavedInstance != null) {
            Log.e(TAG, "constructor: mSavedInstance is not null");
            if (mSavedInstance.containsKey(PREF_IFT_LIST + mCode)) {
                Log.e(TAG, "constructor: mSavedInstance is not null and has IFT");
                Type listType = new TypeToken<List<InputFieldType>>() {
                }.getType();

                mInputFieldTypes = Gson.getInstance().fromJson(mSavedInstance.getString(PREF_IFT_LIST + mCode), listType);
                mSavedInstance.remove(PREF_IFT_LIST + mCode);
            }
        }

        mInputFieldValues = inputValues;
        if (mInputFieldValues == null) {
            mInputFieldValues = new ArrayList<>();
        }

        mDisabledInputFieldList = disabledInputFieldList;
        if (mDisabledInputFieldList == null) {
            mDisabledInputFieldList = new ArrayList<>();
        }

        mBus = bus;
        reset(mInputFieldTypes, savedInstanceState, inputValues, mode);
    }


    /**
     * Removes all existing views and add new view from the list.
     */
    public void reset(List<InputFieldType> inputFieldTypes, Bundle savedInstanceState,
                      List<InputFieldValue> inputValues, EditMode mode) {
        if (inputFieldTypes == null) {
            Log.e(TAG, "inputFieldTypes is null while resetting input fields.");
            return;
        }

        mInputFieldsContainerLayout.removeAllViews();
        mInputFieldTypes = inputFieldTypes;
        mInputFields = new ArrayList<ParentInputField>(inputFieldTypes.size());

        String prePopulateValue = null;
        Map<String, InputFieldValue> codeValueMap = getCodeValueMap(inputValues);
        int count = 1;

        for (InputFieldType inputFieldType : inputFieldTypes) {

            if (inputFieldType == null) {
                continue;
            }
            if (InputFieldType.INPUT_FIELD_TYPE_USER_HIERARCHY_INPUT_FIELD.equals(inputFieldType.getType())) {
                // This is to remove Padding from User input field because it will keep adding and after few levels it will not show any dropdown
                mInputFieldsContainerLayout.setPadding(0, 0, 0, 0);
            } else {
                int dpToPixel = UiUtil.getDpToPixel(16);
                mInputFieldsContainerLayout.setPadding(dpToPixel, 0, dpToPixel, dpToPixel);
            }

            autoEvaluateInputs.add(inputFieldType);

            prePopulateValue = getPrepopulateValue(codeValueMap, inputFieldType, inputValues, mode);
            Data data = null;

            ParentInputField inputField = inputFieldType.toInputField(mActivity,
                    savedInstanceState, prePopulateValue, mMode, mBus, getStartState(), inputValues, mDisabledInputFieldList, data, null, mIsDetect);

            if (inputField != null) {
                inputField.setRefreshListListener(this);
                mInputFields.add(inputField);
                if (inputFieldType.getHint() != null && !inputFieldType.getHint().isEmpty()) {
                    if (mMode.equals(EditMode.WRITE)) {
                        LayoutInflater inflater = mActivity.getLayoutInflater();
                        View container = inflater.inflate(R.layout.inputfield_hint_layout, null);

                        if (inputFieldType.getMaxChars() > 0) {
                            TextView tvCount = container.findViewById(R.id.tvCount);
                            tvCount.setText("0/" + inputFieldType.getMaxChars());
                            codeViewMap.put(inputFieldType.getCode(), tvCount);
                        }

                        TextView label = (TextView) container.findViewById(R.id.label);
                        String hint = inputFieldType.getHint();
                        if (inputFieldType.isRequired()) {
                            hint = hint + "*";
                        }
                        label.setText(UiUtil.getHintWithRequiredColor(hint, inputFieldType.isRequired()));
                        mInputFieldsContainerLayout.addView(container);
                    }
                }


                //calling extended view which will add a download button if server validation is required.
                View inputFieldView;

                if (inputFieldType.isReadOnly()) {
                    inputFieldView = inputField.getReadOnlyView();
                } else if (EditMode.WRITE.equals(mMode)) {
                    inputFieldView = inputField.getExtendedView(prePopulateValue);
                } else {
                    mInputFieldsGroupLayout.setPadding(UiUtil.getDpToPixel(1), UiUtil.getDpToPixel(1), UiUtil.getDpToPixel(1), UiUtil.getDpToPixel(1));
                    mInputFieldsGroupLayout.setBackgroundResource(R.drawable.bg_card_grey_border);
                    inputFieldView = inputField.getDisplayView();
                }

                if (inputFieldView != null) {
                    int dpToPixel = UiUtil.getDpToPixel(16);
                    mInputFieldsContainerLayout.addView(inputFieldView);
                }
            }

            boolean isInputFieldWithDivider = INPUT_FIELD_TYPE_TEXT.equals(inputFieldType.getType()) || INPUT_FIELD_TYPE_SENTENCE.equals(inputFieldType.getType()) ||
                    INPUT_FIELD_TYPE_NUMBER.equals(inputFieldType.getType()) || INPUT_FIELD_TYPE_DECIMAL.equals(inputFieldType.getType());
            if (mShowDivider && !isInputFieldWithDivider) {
                LinearLayout divider = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout
                        .horizontal_divider, null).findViewById(R.id.horizontal_divider);

                LinearLayout.LayoutParams dummyParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, 2);
                divider.setLayoutParams(dummyParams);

                //note: UserHierarchyInputField is Group of SIFG and if we add divider for that it will
                // stack them up each time we go deeper into hierarchy.
                if (count < inputFieldTypes.size() && (!inputFieldType.getType().equals(InputFieldType
                        .INPUT_FIELD_TYPE_USER_HIERARCHY_INPUT_FIELD))) {
                    mInputFieldsContainerLayout.addView(divider);
                }
                count++;
            }
        }

        // Will show group only if it has child.
        if (mInputFieldsContainerLayout.getChildCount() > 0) {
            mGroupLayout.setVisibility(View.VISIBLE);
        } else {
            mGroupLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public View getFormView() {
        return mGroupLayout;
    }

    @Override
    public View getDisplayView() {
        return mGroupLayout;
    }

    @Override
    public View getReadOnlyView() {
        return mGroupLayout;
    }

    @Override
    public boolean validateInput() {
        boolean returnValue = true;

        if (mInputFields == null) {
            return returnValue;
        }

        for (InputField input : mInputFields) {
            returnValue &= input.validateInput();
        }
        return returnValue;
    }

    @Override
    public void addFilter(Map<String, String> filters) {
        if (mInputFields == null) {
            return;
        }

        for (InputField inputField : mInputFields) {
            inputField.addFilter(filters);
        }
    }

    @Override
    public String getJsonValue() {
        return Gson.getInstance().toJson(getInputFieldValue());
    }

    @Override
    public String getOnlineValidationValue() {
        return null;
    }

    public List<InputFieldValue> getInputFieldValue() {
        if (mInputFieldTypes == null) {
            return null;
        }

        List<InputFieldValue> inputFieldValues = new ArrayList<InputFieldValue>(mInputFieldTypes.size());

        for (int i = 0; i < mInputFieldTypes.size(); i++) {
            InputFieldType type = mInputFieldTypes.get(i);
            if (type == null) {
                continue;
            }

            String value = mInputFields.get(i).getJsonValue();

            // Handling this because in case of multi_select_auto_complete we get "[]"
            // so we are not sending the inputfield to the server.
            if (InputFieldType.INPUT_FIELD_TYPE_MULTI_SELECT_AUTO_COMPLETE.equals(type.getType()) && value.equals("[]")) {
                value = null;
            }
            if (value != null) {
                if (value.startsWith("\"") || value.startsWith("{")) {
                    value = value.substring(1);  // Strip beginning quote.
                    value = value.substring(0, value.length() - 1);  // String end quote.
                }

                InputFieldValue inputFieldValue = new InputFieldValue();
                if (!TextUtils.isEmpty(type.getDataType())) {
                    // If any input field has data type then that should go ex: Slider will have data type as number, decimal, currency etc.
                    inputFieldValue.setType(type.getDataType());
                } else {
                    inputFieldValue.setType(type.getType());
                }
                inputFieldValue.setCode(type.getCode());
                inputFieldValue.setName(type.getHint());
                inputFieldValue.setValue(value);

                Map<String, Object> meta = mInputFields.get(i).getMeta();
                if (meta != null) {
//                    inputFieldValue.setMeta(meta);
                }
                inputFieldValues.add(inputFieldValue);
            }
        }
        return inputFieldValues;
    }

    public InputField getInputField(String code) {
        if (mInputFieldTypes == null) {
            return null;
        }

        // WARNING: This would lead to unexpected results if the size of mInputFieldTypes is not
        // the same as mInputFields, but the entire validation would fail in that case anyways,
        // because we would be trying to fetch and input field value that doesn't exist.
        for (int i = 0; i < mInputFieldTypes.size(); i++) {
            return mInputFields.get(i);
        }

        return null;
    }

    public List<ParentInputField> getInputFields() {
        return mInputFields;
    }

    /**
     * This is for deleting existing input fields so it can be refreshed. This is helpful in case
     * of few input fields got removed.
     */
    private void deleteInputFields(String parentCode) {
        List<InputFieldType> deletionList = getDeletionList(mInputFieldTypes, parentCode);
        if (deletionList.size() > 0) {
            mInputFieldTypes.removeAll(deletionList);
        }
        for (ParentInputField inputField : mInputFields) {
            //We call saved instance state only for input fields which are not child of this input field.
            if (mInputFieldTypes.contains(inputField.mInputFieldType)) {
                inputField.onSaveInstanceState(mSavedInstance);

                /* TODO: 25/10/16 need to test this if required. moved it from savedInstanceState()
                to fix filter screen filter button is not working for baxa; reason for not working is because of very long user hierarchy data.
                */
                if (mInputFieldTypes != null) {
                    mSavedInstance.putString(PREF_IFT_LIST + mCode, Gson.getInstance().toJson(mInputFieldTypes));
                }
            }
        }
    }

    /**
     * Returns list of all input fields which need to be deleted. This will search for grand child also.
     */
    private List<InputFieldType> getDeletionList(List<InputFieldType> inputFieldTypes, String parentCode) {
        //this will delete existing child input fields.
        List<InputFieldType> deletionList = new ArrayList<>();
        for (int i = 0; i < inputFieldTypes.size(); i++) {
            InputFieldType inputFieldType = inputFieldTypes.get(i);
            if (inputFieldType.getParentCode() != null && inputFieldType.getParentCode().equals(parentCode)) {
                deletionList.add(inputFieldType);


            }
        }
        return deletionList;
    }

    /**
     * This method will add new input field below the parent.
     */
    private boolean addToInputFieldsAfter(String parentCode, List<InputFieldType> newInputFieldTypes) {
        //this map is used for finding the position of parent in original form and append new input fields into that.
        Map<String, InputFieldType> findParentPositionMap = new HashMap<>();
        int pos = -1;
        if (mInputFieldTypes == null || mInputFieldTypes.isEmpty()) {
            return false;
        }

        if (newInputFieldTypes == null || newInputFieldTypes.isEmpty()) {
            //if server does not return input field we should not refresh screen.
            Log.e(TAG, "New input fields are empty while adding it.");
            return false;
        }

        deleteInputFields(parentCode);

        //this will find the position of parent.
        for (int i = 0; i < mInputFieldTypes.size(); i++) {
            InputFieldType inputFieldType = mInputFieldTypes.get(i);
            findParentPositionMap.put(inputFieldType.getCode(), inputFieldType);
            if (inputFieldType.getCode().equals(parentCode)) {
                pos = i;
            }
        }

        //this will add all child inputs after parent.
        for (InputFieldType newInputFieldType : newInputFieldTypes) {
            if (findParentPositionMap.get(newInputFieldType.getCode()) == null) {
                mInputFieldTypes.add(++pos, newInputFieldType);
            }
            if (TextUtils.isEmpty(newInputFieldType.getParentCode())) {
                newInputFieldType.setParentCode(parentCode);
            }
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mInputFields == null) {
            return;
        }

        for (ParentInputField inputField : mInputFields) {
            inputField.onSaveInstanceState(outState);
        }
    }

    @Override
    public void applyContextFilter(Map<String, Set<String>> filters) {

    }

    @Override
    public void onRefresh(String parentCode, List<InputFieldType> list, EditMode mode) {
        if (mSavedInstance == null) {
            mSavedInstance = new Bundle();
        } else {
            mSavedInstance.clear();
        }
        boolean added = addToInputFieldsAfter(parentCode, list);
        if (added) {
            reset(mInputFieldTypes, mSavedInstance, mInputFieldValues, mode);
        }
    }

    @Override
    public void onFailure(String code, EditMode mode) {
        if (mSavedInstance == null) {
            mSavedInstance = new Bundle();
        } else {
            mSavedInstance.clear();
        }
        deleteInputFields(code);
        reset(mInputFieldTypes, mSavedInstance, mInputFieldValues, mode);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected void colorTextView(TextView textView) {
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
    }

    public List<InputFieldType> getInputFieldTypes() {
        return mInputFieldTypes;
    }

    public String getCode() {
        return mCode;
    }

    private void setChevronIcon(AppCompatActivity appCompatActivity) {
        if (appCompatActivity instanceof BaseAddActivity) {
            if (isExpanded) {
                ivExpandCollapseIcon.setImageDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_chevron_up));
            } else {
                ivExpandCollapseIcon.setImageDrawable(ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_chevron_down));
            }
            ivExpandCollapseIcon.setOnClickListener(this);
            ivExpandCollapseIcon.setVisibility(View.VISIBLE);
            inputFieldGroupHeader.setBackgroundResource(R.color.background);
        }
    }


    private void setHeader(AppCompatActivity appCompatActivity) {
        if (appCompatActivity instanceof BaseAddActivity) {
            inputFieldGroupHeader.setBackgroundResource(R.color.background);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_expand_collapse:
                expandCollapseView();
                break;
        }
    }

    private void expandCollapseView() {
        if (isExpanded) {
            mInputFieldsContainerLayout.setVisibility(View.GONE);
        } else {
            mInputFieldsContainerLayout.setVisibility(View.VISIBLE);
        }
        isExpanded = !isExpanded;
        setChevronIcon(mActivity);
    }

    public void addView(View view) {
        if (view != null) {
            mInputFieldsContainerLayout.addView(view);
        }
    }

    public void addPaddingToView(int padding) {
        mInputFieldsContainerLayout.setPadding(padding, 0, padding, 0);
    }

    // inputField util
    public static Map<String, InputFieldValue> getCodeValueMap(List<InputFieldValue> values) {
        if (values == null) {
            return null;
        }
        Map<String, InputFieldValue> codeValueMap = new HashMap<String, InputFieldValue>();

        for (InputFieldValue inputFieldValue : values) {
            codeValueMap.put(inputFieldValue.getCode(), inputFieldValue);
        }
        return codeValueMap;
    }

    public static String getPrepopulateValue(Map<String, InputFieldValue> codeValueMap, InputFieldType inputFieldType, List<InputFieldValue> inputValues, InputField.EditMode mode) {
        String prePopulateValue = null;
        if (inputValues != null && codeValueMap != null) {
            InputFieldValue preInputFieldValue = codeValueMap.get(inputFieldType.getCode());

            if (preInputFieldValue != null) {
                prePopulateValue = preInputFieldValue.getValue();
            } else {
                prePopulateValue = null;
            }
        }
        return prePopulateValue;
    }


}
