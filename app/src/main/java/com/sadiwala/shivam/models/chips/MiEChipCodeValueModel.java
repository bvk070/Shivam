package com.sadiwala.shivam.models.chips;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sadiwala.shivam.models.common.CodeValue;

import java.util.List;


public class MiEChipCodeValueModel extends MiEChipModel {
    @SerializedName("chips")
    @Expose
    private List<CodeValue> chips;

    public MiEChipCodeValueModel(String fieldCode, List<CodeValue> chips) {
        this.chips = chips;
        this.fieldCode = fieldCode;
    }

    public List<CodeValue> getChips() {
        return chips;
    }

    public void setChips(List<CodeValue> chips) {
        this.chips = chips;
    }
}
