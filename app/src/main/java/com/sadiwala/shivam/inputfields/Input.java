package com.sadiwala.shivam.inputfields;

import android.os.Bundle;
import android.view.View;

import java.util.Map;
import java.util.Set;

/**
 * Created by ankit on 10/04/18.
 */

public interface Input {
    /**
     * This will return a view of an inputfield which can be used by any activity
     * or fragment to display it.
     */
    View getFormView();

    /**
     * This will return a read only view of an inputfield which can be used by any activity
     * or fragment to display it.
     */
    View getDisplayView();

    /**
     * This will return getFormView but should will make it not editable.
     */
    View getReadOnlyView();

    /**
     * This will return true if validation is successful for any input field
     */
    boolean validateInput();


    /**
     * This will return json value of each input field which can be
     * used to send back to server
     */
    String getJsonValue();

    /**
     * This will return json value of each input field which can be
     * used to send back to server for OIF
     */
    String getOnlineValidationValue();

    /**
     * This will get called when an activity or fragment recreates itself due to
     * some config change and each inputfield will save it's current state in it
     */
    void onSaveInstanceState(Bundle outState);

    void applyContextFilter(Map<String, Set<String>> filters);
}
