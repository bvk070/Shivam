package com.sadiwala.shivam.models.common;

import java.util.List;


public class InputFieldValueChangeEvent {
    private String type;
    private String code;
    private Object value;

    public InputFieldValueChangeEvent(String code, List<ICodeName> value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
