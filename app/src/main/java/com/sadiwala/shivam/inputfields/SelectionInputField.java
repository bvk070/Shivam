package com.sadiwala.shivam.inputfields;

import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_CODE_NAME_SPINNER;
import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_SPINNER;
import static com.sadiwala.shivam.util.AaryaConstants.REQUEST_CODE;
import static io.reactivex.annotations.SchedulerSupport.NONE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.SourceRouteUtil;
import com.sadiwala.shivam.inputfields.searchinputfield.SelectionFragment;
import com.sadiwala.shivam.inputfields.searchinputfield.SelectionInputfieldSearchResultsCallback;
import com.sadiwala.shivam.models.common.CodeName;
import com.sadiwala.shivam.models.common.CodeValue;
import com.sadiwala.shivam.models.common.ICodeName;
import com.sadiwala.shivam.models.common.OnItemSelectedListener;
import com.sadiwala.shivam.util.AaryaConstants;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.StringUtils;
import com.sadiwala.shivam.util.UiUtil;
import com.sadiwala.shivam.util.Util;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by ankit on 30/11/16.
 * This class do following:
 * 1. Show hint default value is select
 * 2. Add click listener which will start another activity/fragment
 * 3.Send options(CodeName) to the called screen
 * 4.in OnActivityResult update hint with first selected option and say "X more" in next line(separated by new line char)
 */
public class SelectionInputField extends ParentInputField {
    public static final String EXTRAS_CODE = "code";
    public static final String EXTRAS_SELECTED_OPTIONS = "selected_options";
    public static final String EXTRAS_SELECTED_OPTIONS_CLASS_TYPE = "selected_options_class_type";
    public static final String EXTRAS_SINGLE_SELECT = "single_select";
    public static final String EXTRAS_MAX_ITEMS_SELECTION = "max_items_selection";
    public static final String EXTRAS_MIN_ITEMS_SELECTION = "min_items_selection";
    public static final String EXTRAS_TYPE = "type";
    private static final String EXTRAS_SOURCE = "source";
    public static final String EXTRA_DATA_PROVIDER = "data_provider";
    public static final String EXTRAS_SECTIONED_LIST = "sectioned_list";
    public static final String EXTRAS_SECTIONED_LIST_DATA = "sectioned_list_data";
    private static final String TAG = "SIF";
    private static final int SHOW_MAX_SELECTED_NAME = 2;
    private static final int MAX_CHAR_TO_SHOW = 8;
    private AppCompatActivity mActivity;
    private View view;
    private TextView txtValue;
    private ImageView imgArrow;
    private CustomTextView errorText;
    protected List<ICodeName> mSelectedOptions;
    private List<ICodeName> mAvailableOptions;
    private List<CodeValue> mAvailableOptionsForMiEChips;
    private DataSelectionListener mListener;
    private String mPrepopulateValue;
    private int mMinSelectionCount;
    private int mMaxSelectionCount;
    private String hint;
    private boolean mIsDetect;
    private boolean enableTeamsSwitch = false;
    private StaticOptionsDataProvider staticOptionsDataProvider;
    public static final String HINT = "hint";
    public static final String POSITIVE_BUTTON_NAME = "positive_button_name";
    private OnItemSelectedListener onItemSelectedListener;

    public SelectionInputField(AppCompatActivity activity, Bundle savedInstanceState, String prePopulateValue,
                               final InputFieldType inputFieldType, EventBus bus, EditMode mode, String startState, boolean isDetect) {
        super(activity, bus, mode, startState);
        mActivity = activity;
        SourceRouteUtil.addInputFieldInRefreshMap(mActivity, inputFieldType, this);
        mInputFieldType = inputFieldType;
        view = activity.getLayoutInflater().inflate(R.layout.selection_input_field, null);
        txtValue = view.findViewById(R.id.txt_selected_values);
        imgArrow = view.findViewById(R.id.img_right_arrow);
        imgArrow.setColorFilter(UiUtil.getBrandedPrimaryColorWithDefault());
        errorText = view.findViewById(R.id.error_text);

        mSavedInstance = savedInstanceState;
        mPrepopulateValue = prePopulateValue;
        mSelectedOptions = new ArrayList<>();
        mAvailableOptions = new ArrayList<>();
        mAvailableOptionsForMiEChips = new ArrayList<>();
        initializeAvailableOptions(activity);

        staticOptionsDataProvider = new StaticOptionsDataProvider(mAvailableOptions);

        if (savedInstanceState != null && savedInstanceState.containsKey(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode())) {
            initSelectedOptions(savedInstanceState.getString(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode()));
        } else if (savedInstanceState != null && savedInstanceState.containsKey(mInputFieldType.getCode())) {
            initSelectedOptions(getCode(savedInstanceState.getInt(mInputFieldType.getCode())));
        } else if (!TextUtils.isEmpty(mPrepopulateValue)) {
            initSelectedOptions(mPrepopulateValue);
        }

        setHint(mInputFieldType.getHint());
        updateHintText(mSelectedOptions);
        if (!Util.isListEmpty(mSelectedOptions)) {
            setSelectionOptions(mSelectedOptions);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectionFragment(inputFieldType);
            }
        });

        // If there is some pre-populated value in the inputField then do not display the chips for it
        if (isInputFieldAlreadyPrePopulated(savedInstanceState)) {
            mIsInputFieldPopulated = true;
        }

        txtValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validateInput();
                }
            }
        });
    }

    // Method to tell if the selectionInputField is already populated or not
    protected boolean isInputFieldAlreadyPrePopulated(Bundle savedInstanceState) {
        if (savedInstanceState != null && mInputFieldType != null && savedInstanceState.containsKey(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode())) {
            return true;
        } else if (savedInstanceState != null && mInputFieldType != null && savedInstanceState.containsKey(mInputFieldType.getCode())) {
            return true;
        } else if (!Util.isListEmpty(mSelectedOptions)) {
            return true;
        }
        return false;
    }

    private boolean isInputFieldTypeMultiSelectInputField(InputFieldType inputFieldType) {
        return inputFieldType.getType().equals(InputFieldType.INPUT_FIELD_TYPE_MULTI_SELECT_CHECK_BOX) ||
                inputFieldType.getType().equals(InputFieldType.INPUT_FIELD_TYPE_MULTI_SELECT_AUTO_COMPLETE) ||
                inputFieldType.getType().equals(InputFieldType.INPUT_FIELD_TYPE_MULTI_SELECT_VO);
    }

    // To handle initial selection when DataProvider exists
    protected void initiateSelectionFromSavedInstanceState(Bundle savedInstanceState, String prePopulateValue) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode())) {
            initSelectedOptions(savedInstanceState.getString(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode()));
            updateHintText(mSelectedOptions);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(mInputFieldType.getCode())) {
            initSelectedOptions(getCode(savedInstanceState.getInt(mInputFieldType.getCode())));
            updateHintText(mSelectedOptions);
        } else if (!TextUtils.isEmpty(prePopulateValue)) {
            initSelectedOptions(prePopulateValue);
            updateHintText(mSelectedOptions);
        }
    }

    private List<ICodeName> getValidPrePopulateValue() {
        Map<String, String> prepopulateOptionMap = new HashMap<>();
        for (ICodeName iCodeName : mSelectedOptions) {
            if (iCodeName != null) {
                prepopulateOptionMap.put(iCodeName.getCode(), iCodeName.getName());
            }
        }
        return mSelectedOptions;
    }


    protected void startSelectionFragment(InputFieldType inputFieldType) {
        // check for the type and based on it send either spinnerOptions() or codeNameOptions()
        // or append dynamic options(see MSCBIF) and send it to list activity.
        inputFieldType.getSpinnerOptions();
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_CODE, inputFieldType.getCode());
        intent.putExtra(EXTRAS_TYPE, inputFieldType.getType());
        intent.putExtra(EXTRAS_SOURCE, inputFieldType.getSource());
        intent.putExtra(EXTRA_DATA_PROVIDER, getDataProvider());

        if (!Util.isListEmpty(mSelectedOptions)) {
            intent.putExtra(EXTRAS_SELECTED_OPTIONS, Gson.getInstance().toJson(mSelectedOptions));
        }

        boolean isSingleSelection = inputFieldType.isSingleSelect();

        if (INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(inputFieldType.getType()) ||
                InputFieldType.INPUT_FIELD_TYPE_SUPER_INPUT_FIELD.equals(inputFieldType.getType()) ||
                INPUT_FIELD_TYPE_SPINNER.equals(inputFieldType.getType())) {
            isSingleSelection = true;
        }

        intent.putExtra(EXTRAS_SINGLE_SELECT, isSingleSelection);
        intent.putExtra(EXTRAS_MIN_ITEMS_SELECTION, mMinSelectionCount);
        intent.putExtra(EXTRAS_MAX_ITEMS_SELECTION, mMaxSelectionCount);
        intent.putExtra(EXTRAS_SELECTED_OPTIONS_CLASS_TYPE, getClass().getSimpleName());
        intent.putExtra(HINT, inputFieldType.getHint());
        intent.putExtra(REQUEST_CODE, AaryaConstants.REQUEST_CODE_INPUTFIELD);

        SelectionFragment.showSelectionFragment((AppCompatActivity) mActivity, intent.getExtras());
    }

    public void setMinSelectionCount(int minSelectionCount) {
        this.mMinSelectionCount = minSelectionCount;
    }

    public void setMaxSelectionCount(int maxSelectionCount) {
        this.mMaxSelectionCount = maxSelectionCount;
    }

    public void setHint(String hint) {
        this.hint = hint;
        setSelectedValueText(hint);
    }

    public void setSelectedValueText(String hintText) {
        txtValue.setText(hintText);
        if (Util.isListEmpty(getSelectedValues())) {
            hint = hintText;
            txtValue.setTextColor(mActivity.getResources().getColor(R.color.vymo_text_light));
        } else {
            txtValue.setTextColor(mActivity.getResources().getColor(R.color.black));
            errorText.setVisibility(View.GONE);
            txtValue.setBackgroundResource(0);
        }
    }

    public String getSelectedValueText() {
        return txtValue.getText().toString();
    }

    @Override
    public View getFormView() {
        return view;
    }

    @Override
    public View getDisplayView() {
        if (InputFieldType.INPUT_FIELD_TYPE_MULTI_SELECT_CHECK_BOX.equals(mInputFieldType.getType())
                || InputFieldType.INPUT_FIELD_TYPE_MULTI_SELECT_AUTO_COMPLETE.equals(mInputFieldType.getType())) {

            String valueString = processMultilinePrepopulateValue(mPrepopulateValue);
            View displayView = getReadOnlyView(mInputFieldType.getHint(), valueString.trim());
            TextView textView = (TextView) displayView.findViewById(R.id.value);
            //setting multi line because we need to show multiple selected options in each line.
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setSingleLine(false);
            return displayView;
        } else if (INPUT_FIELD_TYPE_SPINNER.equals(mInputFieldType.getType())
                || INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(mInputFieldType.getType())) {
            return getReadOnlyView(mInputFieldType.getHint(), mPrepopulateValue);
        }
        return null;
    }

    @Override
    public View getReadOnlyView() {
        //this will be disabled in cased where we prepopulate the value and does not want user to
        //change those value such as in case of suggestions
        if (mInputFieldType.isReadOnly()) {
            view.setEnabled(false);
            view.setAlpha(0.8f);
        }
        return view;
    }

    private String processMultilinePrepopulateValue(String value) {
        CodeName[] values = Gson.getInstance().fromJson(value, CodeName[].class);

        String valueString = "";
        if (values == null) {
            return valueString;
        } else {
            for (ICodeName individualValue : values) {
                valueString = valueString + individualValue.getName() +
                        System.getProperty("line.separator");
            }
        }

        return valueString;
    }

    @Override
    public boolean validateInput() {
        if (!mInputFieldType.isRequired()) {
            return true;
        }

        if (mSelectedOptions != null && !mSelectedOptions.isEmpty()) {
            if (INPUT_FIELD_TYPE_SPINNER.equals(mInputFieldType.getType()) || INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(mInputFieldType.getType())) {
                if (NONE.equalsIgnoreCase(mSelectedOptions.get(0).getCode())) {
                    return false;
                } else {
                    errorText.setBackgroundResource(R.drawable.edit_text_background);
                    errorText.setVisibility(View.GONE);
                    return true;
                }
            } else {
                errorText.setBackgroundResource(R.drawable.edit_text_background);
                errorText.setVisibility(View.GONE);
                return true;
            }
        }

        showError();
        //Notify that this input field is not valid to listeners.
        EventBus.getDefault().post(this);

        return false;
    }

    @Override
    public void addFilter(Map<String, String> filters) {
        if (mSelectedOptions == null || mSelectedOptions.isEmpty()) {
            return;
        }

        String value = null;
        if (INPUT_FIELD_TYPE_SPINNER.equals(mInputFieldType.getType()) || INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(mInputFieldType.getType())) {
            if (mSelectedOptions != null && !mSelectedOptions.isEmpty()) {
                if (mSelectedOptions.get(0).getCode() != null && !NONE.equalsIgnoreCase(mSelectedOptions.get(0).getCode())) {
                    value = mSelectedOptions.get(0).getCode();
                }
            }
        } else {
            //here we get array of code name
            value = Gson.getInstance().toJson(mSelectedOptions);
        }

        if (value != null) {
            filters.put(mInputFieldType.getCode(), value);
        }
    }

    public List<ICodeName> getSelectedValues() {
        return mSelectedOptions;
    }

    private int getSelectedItemPosition() {
        // migration of spinner to selected_inputfield
        if (!Util.isListEmpty(mSelectedOptions)) {
            for (int i = 0; i < mAvailableOptions.size(); i++) {
                if (mAvailableOptions.get(i).getCode().equals(mSelectedOptions.get(0).getCode())) {
                    return i;
                }
            }
        }
        return -1;  // default value of spinner is -1;
    }

    private String getCode(int pos) {
        if (pos >= 0 && !Util.isListEmpty(mAvailableOptions) && mAvailableOptions.size() > pos) {
            return mAvailableOptions.get(pos).getCode();
        }
        return null;
    }


    public void showError() {
        String error = getErrorMessage();
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(error);
        txtValue.setBackgroundResource(R.drawable.edittext_error_background);
    }

    private String getErrorMessage() {
        if (mInputFieldType.getRegexHint() != null) {
            return mInputFieldType.getRegexHint();
        }
        return mActivity.getString(R.string.error_required);
    }

    @Override
    public String getJsonValue() {

        if (INPUT_FIELD_TYPE_SPINNER.equals(mInputFieldType.getType())
                || INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(mInputFieldType.getType())) {

            if (!Util.isListEmpty(mSelectedOptions)) {
                return Gson.getInstance().toJson(mSelectedOptions.get(0).getCode());
            }
            return null;

        }

        try {
            if (mSelectedOptions == null || mSelectedOptions.isEmpty()) {
                return Gson.getInstance().toJson(Collections.emptyList());
            } else {
                List<CodeName> selectedOptions = new ArrayList<>();
                for (ICodeName codeName : mSelectedOptions) {
                    selectedOptions.add(new CodeName(codeName.getCode(), codeName.getName()));
                }
                if (InputFieldType.INPUT_FIELD_TYPE_REFERRAL.equals(mInputFieldType.getType())) {
                    return "{" + Gson.getInstance().toJson(selectedOptions.get(0)) + "}";
                } else {
                    return Gson.getInstance().toJson(selectedOptions);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public String getOnlineValidationValue() {
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (INPUT_FIELD_TYPE_SPINNER.equals(mInputFieldType.getType())
                || INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(mInputFieldType.getType())) {
            outState.putInt(mInputFieldType.getCode(), getSelectedItemPosition());
        } else {
            outState.putString(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode(),
                    Gson.getInstance().toJson(mSelectedOptions));
        }
    }

    @Override
    public void applyContextFilter(Map<String, Set<String>> filters) {

    }

    @Override
    public void updateViewPostSelection(String selectedValues) {
        if (!TextUtils.isEmpty(selectedValues) || TextUtils.isEmpty(getSelectedValueText())) {
            List<ICodeName> selectedOptions = Gson.getInstance().fromJson(selectedValues, getClassType());
            if (!Util.isListEmpty(selectedOptions)) {
                mSelectedOptions = new ArrayList<>(selectedOptions);
            } else {
                mSelectedOptions = new ArrayList<>();
            }
            updateHintText(mSelectedOptions);
        }

        if (mListener != null) {
            mListener.onRefreshData();
        }
//
//        if (onItemSelectedListener != null && !Util.isListEmpty(mSelectedOptions)) {
//            onItemSelectedListener.onItemSelected(mSelectedOptions);
//        } else {
//            if (!Util.isListEmpty(mSelectedOptions)) {
//                // migrating GenericSpinnerInputField selection
//                getBus().postSticky(new CodeName(mInputFieldType.getCode(), mSelectedOptions.get(0).getCode()));
//            }
//        }

        // validate this field on value change.
        validateInput();
    }

    @Override
    public void updateErrorWithUserText(boolean showError, String errorText) {

    }

    @Override
    public void clearView() {

        if (!Util.isListEmpty(mSelectedOptions)) {
            mSelectedOptions.clear();
        }

        setHint(hint);
        if (mInputFieldType.isRequired()) {
            showError();
        }
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onClearClicked();
        }

    }

    protected void updateHintText(List<? extends ICodeName> selectedOptions) {
        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            String selectedValue;
            if (selectedOptions.size() > SHOW_MAX_SELECTED_NAME) {
                selectedValue = StringUtils.trimString(selectedOptions.get(0).getName(), MAX_CHAR_TO_SHOW) + ", " + StringUtils.trimString(selectedOptions.get(1).getName(), MAX_CHAR_TO_SHOW)
                        + getActivity().getString(R.string.and_x_more, String.valueOf(selectedOptions.size() - SHOW_MAX_SELECTED_NAME));
            } else if (selectedOptions.size() == SHOW_MAX_SELECTED_NAME) {
                selectedValue = StringUtils.trimString(selectedOptions.get(0).getName(), MAX_CHAR_TO_SHOW) + " and " + StringUtils.trimString(selectedOptions.get(1).getName(), MAX_CHAR_TO_SHOW);
            } else {
                selectedValue = selectedOptions.get(0).getName();
            }
            setSelectedValueText(selectedValue);
        } else {
            if (mInputFieldType.getSelectionHint() != null) {
                setSelectedValueText(mInputFieldType.getSelectionHint());
            } else if (mAvailableOptions != null && !mAvailableOptions.isEmpty() && !TextUtils.isEmpty(mAvailableOptions.get(0).getName())) {
                setSelectedValueText(mActivity.getString(R.string.select));
            } else {
                setSelectedValueText(mActivity.getString(R.string.select));
            }
        }
    }

    private String getHintText(String hintText, int selectedOptionsCount) {
        String requiredSuffix = mInputFieldType.isRequired() ? "*" : "";
        String selectedCountString = selectedOptionsCount > 0 ? " (" + selectedOptionsCount + ")" : "";
        return hintText + requiredSuffix + selectedCountString;
    }

    protected void setSelectionOptions(List<? extends ICodeName> selectionOptions) {
        mSelectedOptions = (List<ICodeName>) selectionOptions;
    }

    /**
     * This will reinitialize all selected options.
     */
    protected void initSelectedOptions(String json) {
        if (json == null) {
            Log.e(TAG, "Null JSON");
        }

        if (INPUT_FIELD_TYPE_SPINNER.equals(mInputFieldType.getType())
                || INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(mInputFieldType.getType())) {

            mSelectedOptions = new ArrayList<>();
            for (int i = 0; i < mAvailableOptions.size(); i++) {
                if (mAvailableOptions.get(i).getCode().equals(json)) {
                    mSelectedOptions.add(mAvailableOptions.get(i));
                }
            }

            mSelectedOptions = getValidPrePopulateValue();

            if (onItemSelectedListener != null && !Util.isListEmpty(mSelectedOptions)) {
                onItemSelectedListener.onItemSelected(mSelectedOptions);
            } else {
                if (!Util.isListEmpty(mSelectedOptions)) {
                    // migrating GenericSpinnerInputField selection
                    getBus().postSticky(new CodeName(mInputFieldType.getCode(), mSelectedOptions.get(0).getCode()));
                }
            }

            return;
        }

        try {
            mSelectedOptions = Gson.getInstance().fromJson(json, getClassType());
            mSelectedOptions = mSelectedOptions == null ? null : getValidPrePopulateValue();
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Invalid JSON while init data: " + json);
        }
    }

    /**
     * This will be called from all child classes to update available options.
     * See # HdfcCustomesInputField.
     */
    public void resetAvailableOptions(List<ICodeName> options) {
        if (mAvailableOptions != null && !mAvailableOptions.isEmpty()) {
            mAvailableOptions.clear();
        }

        if (options != null && !options.isEmpty()) {
            mAvailableOptions.addAll(options);
        }

        // remove select from options
        if (!Util.isListEmpty(mAvailableOptions)) {
            if (mAvailableOptions.get(0).getName().toLowerCase().contains(mActivity.getResources().getString(R.string.select).toLowerCase()) ||
                    NONE.equals(mAvailableOptions.get(0).getCode())) {
                mAvailableOptions.remove(0);
            }
        }

        if (staticOptionsDataProvider != null) {
            staticOptionsDataProvider.setAvailableOptions(mAvailableOptions);
        }
    }

    public void resetSelectedOptions() {
        if (!Util.isListEmpty(mSelectedOptions)) {
            mSelectedOptions.clear();
        }
    }

    private void initializeAvailableOptions(Activity activity) {
        ICodeName[] staticOptions = mInputFieldType.getCodeNameSpinnerOptions();

        if (staticOptions == null && mInputFieldType.getSpinnerOptions() != null) {
            String[] spinnerOptions = mInputFieldType.getSpinnerOptions();
            ArrayList<String> codeNames = new ArrayList<>(Arrays.asList(spinnerOptions));

            if (!Util.isListEmpty(codeNames)) {
                staticOptions = new ICodeName[codeNames.size()];
                for (int i = 0; i < codeNames.size(); i++) {
                    CodeName codeName = new CodeName();
                    codeName.setCode(codeNames.get(i));
                    codeName.setName(codeNames.get(i));
                    staticOptions[i] = codeName;
                }
            }
        }

        if (staticOptions != null && staticOptions.length > 0) {
            mAvailableOptions.addAll(new ArrayList<>(Arrays.asList(staticOptions)));
        }

        // remove select from options
        if (!Util.isListEmpty(mAvailableOptions)) {
            if (mAvailableOptions.get(0).getName().toLowerCase().contains(mActivity.getResources().getString(R.string.select).toLowerCase()) ||
                    NONE.equals(mAvailableOptions.get(0).getCode())) {
                mAvailableOptions.remove(0);
            }
        }

    }

    protected Type getClassType() {
        return new TypeToken<List<CodeName>>() {
        }.getType();
    }

    public interface DataSelectionListener {
        void onRefreshData();
    }

    public void setListener(DataSelectionListener listener) {
        this.mListener = listener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;

        if (mSavedInstance != null && mSavedInstance.containsKey(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode())) {
            initSelectedOptions(mSavedInstance.getString(SelectionInputField.EXTRAS_SELECTED_OPTIONS + mInputFieldType.getCode()));
        } else if (mSavedInstance != null && mSavedInstance.containsKey(mInputFieldType.getCode())) {
            initSelectedOptions(getCode(mSavedInstance.getInt(mInputFieldType.getCode())));
        } else if (!TextUtils.isEmpty(mPrepopulateValue)) {
            initSelectedOptions(mPrepopulateValue);
        }
    }

    protected DataProvider getDataProvider() {
        return staticOptionsDataProvider;
    }

    protected static class StaticOptionsDataProvider implements DataProvider, Serializable {
        private static final long serialVersionUID = 104453443343L;
        private List<ICodeName> mAvailableOptions;

        public StaticOptionsDataProvider(List<ICodeName> availableOptions) {
            this.mAvailableOptions = availableOptions;
        }

        public void setAvailableOptions(List<ICodeName> availableOptions) {
            this.mAvailableOptions = availableOptions;
        }

        @Override
        public List<ICodeName> getOptions() {
            if (mAvailableOptions == null) {
                mAvailableOptions = new ArrayList<>();
            }
            return mAvailableOptions;
        }

        @Override
        public String getOnlineSearchUrl() {
            return null;
        }

        @Override
        public void searchOnline(Activity activity, String searchQuery, SelectionInputfieldSearchResultsCallback callback, Map<String, String> additionalParams) {

        }
    }

}
