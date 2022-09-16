package com.sadiwala.shivam.models.common;

import com.google.gson.annotations.SerializedName;

public class ComputationSpec {

    // "total,due,remaining" code of input fields
    private String requiredInputs;

    // total-due Expression
    @SerializedName("exp")
    private String expression;

    public String getRequiredInputs() {
        return requiredInputs;
    }

    public String getExpression() {
        return expression;
    }
}
