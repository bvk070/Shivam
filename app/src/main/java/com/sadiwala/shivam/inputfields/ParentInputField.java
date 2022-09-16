package com.sadiwala.shivam.inputfields;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;

import java.util.Date;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public abstract class ParentInputField implements InputField {
    private static final String TAG = "Parent Input Field";
    protected InputFieldType mInputFieldType;
    protected OnRefreshListListener refreshListListener;
    protected Bundle mSavedInstance;
    protected EditMode mMode;
    private AppCompatActivity mActivity;
    private EventBus mBus;
    private String mStartState;
    private List<InputFieldValue> mPrepopulateValueList;
    private ImageView mFetchButton;
    protected boolean mIsInputFieldPopulated = false;
    protected RecyclerView mieChipsRecyclerView;

    public ParentInputField(AppCompatActivity activity, EventBus bus, EditMode mode, String startState, List<InputFieldValue> prepopulateValueList) {
        if (activity != null) {
            mActivity = activity;
        } else {
            Log.e(TAG, "Context is null");
        }

        if (bus != null) {
            mBus = bus;
            if (mBus.isRegistered(ParentInputField.this)) {
                mBus.unregister(ParentInputField.this);
            }
            mBus.register(ParentInputField.this);
        } else {
            Log.e(TAG, "Bus is null");
        }

        mMode = mode;
        mStartState = startState;
        mPrepopulateValueList = prepopulateValueList;
    }

    public ParentInputField(AppCompatActivity activity, EventBus bus, EditMode mode, String startState) {
        this(activity, bus, mode, startState, null);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }


    @Override
    protected void finalize() throws Throwable {
        //this will unregister bus whenever the object will be destroyed.
        // TODO: 16/09/16 need to check if it is calling for every object or not. if no then IFG should call to all child IFs.
        if (mBus != null) {
            mBus.unregister(ParentInputField.this);
        }
        super.finalize();
    }

    /**
     * This is the listener which some child classes will override who wanna handle the response.
     */
    public OnRefreshListListener getRefreshListListener() {
        return refreshListListener;
    }

    public void setRefreshListListener(OnRefreshListListener refreshListListener) {
        this.refreshListListener = refreshListListener;
    }

    @Override
    public void updateViewPostSelection(String selectedValue) {

    }

    @Override
    public void updateErrorWithUserText(boolean showError, String errorText) {

    }

    @Override
    public void clearView() {

    }

    protected EventBus getBus() {
        return mBus;
    }

    /**
     * This will return a linear layout which has two textViews in horizontal fashion and a horizontal
     * divider of 1dp height in last as divider
     */
    protected LinearLayout getReadOnlyView(String hint, String displayValueString) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        LinearLayout container = (LinearLayout) inflater.inflate(R.layout.horizontal_input_field_view_text_layout, null);

        TextView label = container.findViewById(R.id.label);
        label.setText(hint);

        TextView value = container.findViewById(R.id.value);
        value.setVisibility(View.VISIBLE);
        value.setTextIsSelectable(true);
        value.setText(displayValueString);
        if (TextUtils.isEmpty(displayValueString)) {
            container.setVisibility(View.GONE);
        }
        return container;
    }

    /**
     * This will return a layout which has one textView and another View in horizontal fashion
     */
    protected View getReadOnlyView(String hint, View displayValueView) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        LinearLayout container = (LinearLayout) inflater.inflate(R.layout.horizontal_input_field_view_linear_layout, null);
        if (displayValueView.getParent() != null) {
            ((ViewGroup) displayValueView.getParent()).removeView(displayValueView);
        }

        TextView label = container.findViewById(R.id.label);
        label.setText(hint);

        LinearLayout value = container.findViewById(R.id.ll_view_container);
        value.addView(displayValueView);

        return container;
    }

    @Override
    public Map<String, Object> getMeta() {
        return null;
    }

    public String getStartState() {
        return mStartState;
    }

    public boolean isInputFieldPopulated() {
        return mIsInputFieldPopulated;
    }

    public List<InputFieldValue> getPrepopulateInputValueList() {
        return mPrepopulateValueList;
    }

    public void refreshView(String value) {

    }

    public void refreshPlaceholder(String value) {

    }

    public void updateInputFieldTagValuesMap(Map<String, String> scannedValues) {

    }

    @Override
    public String getInputFieldCode() {
        return mInputFieldType.getCode();
    }

    //to get the inputHint
    public String getInputHint() {
        return mInputFieldType.getHint();
    }

    public interface OnRefreshListListener {
        /**
         * code = code of IFT which will be used to identify position of new input list.
         */
        void onRefresh(String parentCode, List<InputFieldType> list, EditMode mMode);

        void onFailure(String code, EditMode mode);
    }

    public InputFieldType getInputFieldType() {
        return mInputFieldType;
    }


}
