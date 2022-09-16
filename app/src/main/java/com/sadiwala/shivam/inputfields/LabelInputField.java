package com.sadiwala.shivam.inputfields;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.UiUtil;

import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class LabelInputField extends ParentInputField {
    private CustomTextView label;
    private static final String INTENT = "intent";
    private final String TAG = "LabelIF";

    public LabelInputField(AppCompatActivity activity, String prePopulate, InputFieldType inputField, EventBus bus, EditMode mode, String startState) {
        super(activity, bus, mode, startState);
        mInputFieldType = inputField;
        label = new CustomTextView(activity);

        label.setMovementMethod(LinkMovementMethod.getInstance());
        if (!TextUtils.isEmpty(prePopulate)) {
            setPrepopulateValue(prePopulate);
        } else if (!TextUtils.isEmpty(inputField.getValue())) {
            label.setText(Html.fromHtml(inputField.getValue()));
        }
    }


    private void setPrepopulateValue(String prePopulate) {
        // Set the default textColor in the CustomTextView
        label.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
        label.setText(Html.fromHtml(prePopulate), TextView.BufferType.SPANNABLE);

        int padding = (int) getActivity().getResources().getDimension(R.dimen.small_padding);
        label.setPadding(padding, 0, padding, 0);
        label.setTypeface(null, Typeface.BOLD);
    }

    private void openInAppViewer(InputFieldType inputField) {
        int padding = (int) getActivity().getResources().getDimension(R.dimen.small_padding);
        label.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
        label.setPadding(padding, 0, padding, 0);
        label.setTypeface(null, Typeface.BOLD);
        label.setText(inputField.getValue());


    }


    @Override
    public View getFormView() {
        return label;
    }

    @Override
    public View getDisplayView() {
        return label;
    }

    @Override
    public View getReadOnlyView() {
        //label input field does not have any effect be it in form or display so no changes are
        //required
        return label;
    }

    @Override
    public boolean validateInput() {
        return true;
    }

    @Override
    public void addFilter(Map<String, String> filters) {
        // No such thing as filtering by label.
    }

    @Override
    public String getJsonValue() {
        //note returning null because server does not need it back.
        return null;
    }

    @Override
    public String getOnlineValidationValue() {
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void applyContextFilter(Map<String, Set<String>> filters) {

    }

    @Override
    public void refreshView(String value) {
        label.setText(value);
    }

}
