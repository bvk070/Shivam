package com.sadiwala.shivam.inputfields;

import android.text.Html;

import java.util.Map;


public class InputFieldValue {
    private String type;
    private String code;
    private String name;
    private String value;
    private Map<String, Object> meta;

    public InputFieldValue(String type, String code, String name, String value) {
        this.type = type;
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public InputFieldValue() {

    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        if (name != null && !name.isEmpty()) {
            return Html.fromHtml(name).toString();
        }
        return name;
    }

    public String getValue() {
        if (value != null && !value.isEmpty()) {
            return Html.fromHtml(value).toString();
        }
        return value;
    }

    public String getRawValue() {
        return value;
    }


    @Override
    public String toString() {
        return name;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
}
