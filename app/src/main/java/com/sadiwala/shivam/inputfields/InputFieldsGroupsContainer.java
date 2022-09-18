package com.sadiwala.shivam.inputfields;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.sadiwala.shivam.util.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;


public class InputFieldsGroupsContainer extends ParentInputField {
    private Activity mLocalActivity;
    private List<InputFieldsGroup> mListOfInputFieldsGroups;
    private LinearLayout mGroupLayout;//this is the complete layout which will have sub-layouts with padding and margin between each other
    private final String TAG = "IFGC";

    public InputFieldsGroupsContainer(AppCompatActivity activity, List<InputFieldsGroup> groups, EventBus bus,
                                      EditMode mode, String startState) {
        super(activity, bus, mode, startState);
        mLocalActivity = activity;
        mGroupLayout = new LinearLayout(mLocalActivity);
        initialize(mode, groups);
    }

    public void initialize(EditMode mode, List<InputFieldsGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            return;
        }
        mListOfInputFieldsGroups = groups;
        mGroupLayout.setOrientation(LinearLayout.VERTICAL);
        for (InputFieldsGroup group : groups) {
            View view;
            if (EditMode.WRITE.equals(mode)) {
                view = group.getFormView();
            } else {
                //this is the case of read-only mode
                view = group.getDisplayView();
            }
            if (view != null) {
                LinearLayout layout = new LinearLayout(mLocalActivity);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(0, 0, 0, 0/*UiUtil.getDpToPixel((int) mLocalActivity.getResources().getDimension(R.dimen.tiny_padding))*/);
                layout.addView(view);
                if (View.VISIBLE == view.getVisibility()) {
                    // Adding the group in screen view only if the card is visible.
                    mGroupLayout.addView(layout);
                }
            }
        }
    }

    public void reset(List<InputFieldsGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            Log.e("AGIFs", "This is an empty group");
            return;
        }
        mGroupLayout.removeAllViews();
        initialize(EditMode.WRITE, groups);
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
        boolean valid = true;
        for (ParentInputField inputField : mListOfInputFieldsGroups) {
            valid = valid & inputField.validateInput();
        }
        return valid;
    }

    @Override
    public void addFilter(Map<String, String> filters) {
        for (ParentInputField inputField : mListOfInputFieldsGroups) {
            inputField.addFilter(filters);
        }
    }

    @Override
    public String getJsonValue() {
        try {
            List<InputFieldValue> inputFieldValues = new ArrayList<>();
            for (InputFieldsGroup inputField : mListOfInputFieldsGroups) {
                inputFieldValues.addAll(inputField.getInputFieldValue());
            }
            return Gson.getInstance().toJson(inputFieldValues);
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
        if (mListOfInputFieldsGroups == null) {
            return;
        }
        for (InputFieldsGroup inputField : mListOfInputFieldsGroups) {
            inputField.onSaveInstanceState(outState);
        }
    }

    @Override
    public void applyContextFilter(Map<String, Set<String>> filters) {

    }

    public List<InputFieldsGroup> getListOfInputFieldsGroups() {
        return mListOfInputFieldsGroups;
    }

}
