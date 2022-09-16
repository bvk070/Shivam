package com.sadiwala.shivam.inputfields;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ankit on 18/06/17.
 */
// FIXME: why is this class needed? can't the AttributeGroup class be used for this?
public class InputFieldGroupType {
    public static final String INPUT_FIELD_GROUP_TYPE_LOCATION = "location";
    public static final String INPUT_FIELD_GROUP_TYPE_MEETING = "meeting";
    public static final String INPUT_FIELD_GROUP_TYPE_GENERIC = "generic";

    private String code;
    private String name;
    private String type;

    public InputFieldGroupType(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public InputFieldsGroup toInputFieldGroups(AppCompatActivity activity, Bundle savedInstanceState,
                                               List<InputFieldValue> inputValues, List<InputFieldType> inputs,
                                               InputField.EditMode mode, boolean showDivider, EventBus bus, String startState) {
        InputFieldsGroup group;


        group = new InputFieldsGroup(activity, savedInstanceState, getCode(), getName(), inputValues, inputs, mode, showDivider, bus, startState, null);
        group.setType(getType());
        return group;
    }
}
