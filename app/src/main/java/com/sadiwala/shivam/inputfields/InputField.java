package com.sadiwala.shivam.inputfields;

import java.util.Map;


public interface InputField extends Input {
    /**
     * This is used for Photo and location input field based on this UI and some
     * behaviour may change
     */
    public enum EditMode {
        READ,
        WRITE,
        AUTO,
        PARTIAL // This is the one where we have to allow partial edit in any field like Check-in Location.
    }

    /**
     * This is used for sending values to server back from filters activity
     */
    void addFilter(Map<String, String> filters);

    /**
     * This will return json meta data of each input field which can be
     * used to send back to server
     * <p>
     * required to be implemented only if some extra meta is required, e.g. subType in location,
     * currency code in currency etc
     */
    Map<String, Object> getMeta();

    /**
     * This will be called by BaseAddActivity to refresh the view.
     */
    void updateViewPostSelection(String selectedValue);

    /**
     * This will be called by InputField to show error message
     */
    void updateErrorWithUserText(boolean showError, String errorText);

    /**
     * This will be called by AddActivity to add value to the view.
     */
    void refreshView(String value);

    /**
     * This will be called by InputFieldsGroup to refresh placeholder value.
     */
    void refreshPlaceholder(String value);

    /**
     * This will be overridden in the classes who needs the tags and values to populate child inputFields, ex: SIFG, Spinner input fields.
     */
    void updateInputFieldTagValuesMap(Map<String, String> scannedTagValues);

    String getInputFieldCode();

    /**
     * This will be called by BaseAddActivity to clear the view.
     */
    void clearView();

}
