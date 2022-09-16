package com.sadiwala.shivam.base;

import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.util.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppData {

    public static ArrayList<InputFieldType> getCustomerForm() {
        String stringInputs = "[\n" +
                " {\n" +
                "\"type\": \"text\",\n" +
                "\"code\": " + Customer.NAME + ",\n" +
                "\"hint\": \"Name\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"phone\",\n" +
                "\"code\": " + Customer.MOBILE + ",\n" +
                "\"hint\": \"Mobile\",\n" +
                "\"required\": true,\n" +
                "\"min_chars\": 10,\n" +
                "\"max_chars\": 10\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"sentence\",\n" +
                "\"code\": " + Customer.ADDRESS + ",\n" +
                "\"hint\": \"Address\",\n" +
                "\"required\": false,\n" +
                "\"min_lines\": 3,\n" +
                "\"parent_code\": null,\n" +
                "\"read_only\": false\n" +
                "},\n" +
                " {\n" +
                "\"type\": \"text\",\n" +
                "\"code\": " + Customer.AREA + ",\n" +
                "\"hint\": \"Area\",\n" +
                "\"required\": false\n" +
                "},\n" +
                " {\n" +
                "\"type\": \"number\",\n" +
                "\"code\": " + Customer.PINCODE + ",\n" +
                "\"hint\": \"Pincode\",\n" +
                "\"required\": false\n" +
                "}\n" +
                "]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
    }

    public static HashMap<String, InputFieldValue> getCustomerEmptyInputFieldValues() {
        HashMap<String, InputFieldValue> hashMap = new HashMap<>();
        hashMap.put(Customer.NAME, new InputFieldValue());
        hashMap.put(Customer.MOBILE, new InputFieldValue());
        hashMap.put(Customer.ADDRESS, new InputFieldValue());
        hashMap.put(Customer.AREA, new InputFieldValue());
        hashMap.put(Customer.PINCODE, new InputFieldValue());
        return hashMap;
    }

}
