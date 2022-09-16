package com.sadiwala.shivam.inputfields;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.sadiwala.shivam.models.common.Data;
import com.sadiwala.shivam.models.common.ICodeName;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class InputFieldType {
    public static final String INPUT_FIELD_DATA_TYPE_NUMBER = "number";
    public static final String INPUT_FIELD_DATA_TYPE_DECIMAL = "decimal";
    public static final String INPUT_FIELD_DATA_TYPE_CURRENCY = "currency";

    public static final String WCI_DESCRIPTION_INPUT_FIELD = "description";

    public static final String INPUT_FIELD_TYPE_TEXT = "text";
    public static final String INPUT_FIELD_TYPE_HTML_TEXT = "html_text";
    public static final String INPUT_FIELD_TYPE_LABEL = "label";
    public static final String INPUT_FIELD_TYPE_SENTENCE = "sentence";
    public static final String INPUT_FIELD_TYPE_NUMBER = "number";
    public static final String INPUT_FIELD_TYPE_DECIMAL = "decimal";
    public static final String INPUT_FIELD_TYPE_PHONE = "phone";
    public static final String INPUT_FIELD_TYPE_EMAIL = "email";
    public static final String INPUT_FIELD_TYPE_JOIN_MEETING = "join_meeting";
    public static final String INPUT_FIELD_TYPE_PAN = "pan";
    public static final String INPUT_FIELD_TYPE_DATE = "date";
    public static final String INPUT_FIELD_TYPE_TIME = "time";
    public static final String INPUT_FIELD_TYPE_DATE_TIME = "datetime";
    public static final String INPUT_FIELD_TYPE_MEETING = "meeting";
    public static final String INPUT_FIELD_TYPE_SPINNER = "spinner";
    public static final String INPUT_FIELD_TYPE_SLIDER = "slider";
    public static final String INPUT_FIELD_TYPE_CODE_NAME_SPINNER = "code_name_spinner";
    public static final String INPUT_FIELD_TYPE_CHECKBOX_GROUP = "checkbox_group";
    public static final String INPUT_FIELD_TYPE_SUPER_INPUT_FIELD = "sifg";
    public static final String INPUT_FIELD_TYPE_USER = "user";
    public static final String INPUT_FIELD_TYPE_USER_HIERARCHY_INPUT_FIELD = "user_hierarchy";
    public static final String INPUT_FIELD_TYPE_AUTO_COMPLETE = "auto";
    public static final String INPUT_FIELD_TYPE_LOCATION = "location";
    public static final String INPUT_FIELD_TYPE_ADDRESS = "address";
    public static final String INPUT_FIELD_TYPE_CUSTOMER = "customer";
    public static final String INPUT_FIELD_TYPE_PHOTO = "photo";
    public static final String INPUT_FIELD_TYPE_MULTI_SELECT_AUTO_COMPLETE = "multi_select_auto_complete";
    public static final String INPUT_FIELD_TYPE_MULTI_SELECT_CHECK_BOX = "multi_select_check_box";
    public static final String INPUT_FIELD_TYPE_MULTI_SELECT_VO = "multi_select_vo";
    public static final String INPUT_FIELD_TYPE_USER_SELECT_VO = "user_select_vo";
    public static final String INPUT_FIELD_TYPE_REFERRAL = "referral";
    public static final String INPUT_FIELD_TYPE_CONTENT = "content";
    public static final String INPUT_FIELD_TYPE_SELECTION = "selection";
    public static final String INPUT_FIELD_TYPE_EXTERNAL_ENTITY = "external_entity";
    public static final String INPUT_FIELD_TYPE_TABLE = "table";
    public static final String INPUT_FIELD_TYPE_CURRENCY = "currency";
    public static final String INPUT_FIELD_TYPE_MULTIMEDIA = "multimedia";
    public static final String INPUT_FIELD_TYPE_CHIP = "chip";
    public static final String INPUT_FIELD_TYPE_PREVIEW = "preview";
    public static final String INPUT_FIELD_TYPE_SWITCH = "switch";

    public static final String LOCATION_SUB_TYPE = "location";
    public static final String LOCATION_SUB_TYPE_CHECK_IN = "check_in";
    public static final String INPUT_FIELD_TYPE_DOCUMENT = "document";

    public static final int FETCH_OIF_NONE = 0;
    public static final int FETCH_OIF_SUCCESS = 1;
    public static final int FETCH_OIF_FAIL = 2;

    private String mClient;
    private String type; // Text, number or phone etc.
    private String code; //unique code for each input field
    private String name;
    private Data data;
    private String parentCode; //unique code which tells who is parent of this inout field. This is
    // used when parent has Online validation and server sends few more inout fields.
    private boolean required; // mandatory field
    private String regex; //regex pattern to verify text field values.
    private String regexHint; //regex pattern error message to display in text field values.
    private String hint; //Hint to be displayed in text field.
    private String value; //used for display text to show label
    private float minValue; //Used for slider's minimum value
    private float maxValue; //Used for slider's maximum value
    private String unit;
    private String dataType;
    private int minChars;
    private int maxChars;
    private int minLines = 1; //default minimum line is 1.
    private boolean readOnly = false;


    public InputFieldType(String type, String code, boolean required, String hint) {
        this.type = type;
        this.code = code;
        this.required = required;
        this.hint = hint;
    }

    public String getmClient() {
        return mClient;
    }

    public void setmClient(String mClient) {
        this.mClient = mClient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegexHint() {
        return regexHint;
    }

    public void setRegexHint(String regexHint) {
        this.regexHint = regexHint;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getValue() {
        return value;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getMinChars() {
        return minChars;
    }

    public void setMinChars(int minChars) {
        this.minChars = minChars;
    }

    public int getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }

    public int getMinLines() {
        return minLines;
    }

    public void setMinLines(int minLines) {
        this.minLines = minLines;
    }

    public boolean isReadOnly() {
        return readOnly ;
    }

    /**
     * This is only begin called in case of suggestion we want some non editble fields.
     *
     * @param isReadOnly
     */
    public void setReadOnly(boolean isReadOnly) {
        this.readOnly = isReadOnly;
    }

    public ParentInputField toInputField(
            AppCompatActivity activity,
            Bundle savedInstanceState,
            String prePopulateValue,
            InputField.EditMode mode,
            EventBus bus,
            String startState) {
        return toInputField(activity, savedInstanceState, prePopulateValue, mode, bus, startState, null, null, null, new ArrayList<ICodeName>(), false);
    }

    public ParentInputField toInputField(
            AppCompatActivity activity,
            Bundle savedInstanceState,
            String prePopulateValue,
            InputField.EditMode mode,
            EventBus bus,
            String startState,
            List<InputFieldValue> inputFieldValueList,
            List<String> nonEditableFields,
            Data data,
            List<ICodeName> leads,
            boolean isDetect) {
        return toInputField(activity, savedInstanceState, prePopulateValue, mode, bus, startState, inputFieldValueList, nonEditableFields, data, leads, isDetect, false);
    }

    public ParentInputField toInputField(
            AppCompatActivity activity,
            Bundle savedInstanceState,
            String prePopulateValue,
            InputField.EditMode mode,
            EventBus bus,
            String startState,
            List<InputFieldValue> inputFieldValueList,
            List<String> nonEditableFields,
            Data data,
            List<ICodeName> leads,
            boolean isDetect,
            boolean isLineworks) {

        if (prePopulateValue == null) {
            prePopulateValue = getValue();
        }


        // Condition to replace the "\n" with a ENTER SPACE line break for sentenceInputFields
        if (INPUT_FIELD_TYPE_SENTENCE.equals(getType()) && !TextUtils.isEmpty(prePopulateValue)) {
            prePopulateValue = prePopulateValue.replace("\\n", System.getProperty("line.separator"));
        }

        if (INPUT_FIELD_TYPE_TEXT.equals(getType())) {
            return new TextInputField(activity, savedInstanceState, prePopulateValue, this, bus, mode, startState);

        } else if (INPUT_FIELD_TYPE_LABEL.equals(getType())) {
            return new LabelInputField(activity, prePopulateValue, this, bus, mode, startState);

        } else if (INPUT_FIELD_TYPE_SENTENCE.equals(getType())) {
            return new TextInputField(activity, savedInstanceState, prePopulateValue, this, bus, mode, startState);

        } else if (INPUT_FIELD_TYPE_NUMBER.equals(getType())) {
            return new TextInputField(activity, savedInstanceState, prePopulateValue, this, bus, mode, startState);

        } else if (INPUT_FIELD_TYPE_DECIMAL.equals(getType())) {
            return new TextInputField(activity, savedInstanceState, prePopulateValue, this, bus, mode, startState);

        } else {
            return null;
        }
    }


}
