package com.sadiwala.shivam.models.chips;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MiEChipModel {
    @SerializedName("fieldCode")
    @Expose
    protected String fieldCode;

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
}
