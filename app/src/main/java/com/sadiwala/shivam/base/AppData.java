package com.sadiwala.shivam.base;

import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.util.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppData {

    public enum PRODUCT_TYPE {
        ALINE_GOWN, CHAPATTI_GOWN, NIGHT_DRESS
    }

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
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"add_campaign\",\n" +
                "\"hint\": \"Add Campaign\",\n" +
                "\"code_name_spinner_options\": [\n" +
                "{\n" +
                "\"code\": \"select\",\n" +
                "\"name\": \"Select\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"yes\",\n" +
                "\"name\": \"Yes\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"no\",\n" +
                "\"name\": \"No\"\n" +
                "}\n" +
                "],\n" +
                "\"required\": true\n" +
                "}"+
                "]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
    }

    public static HashMap<String, InputFieldValue> getEmptyInputFieldValues() {
        HashMap<String, InputFieldValue> hashMap = new HashMap<>();
        hashMap.put(Customer.NAME, new InputFieldValue());
        hashMap.put(Customer.MOBILE, new InputFieldValue());
        hashMap.put(Customer.ADDRESS, new InputFieldValue());
        hashMap.put(Customer.AREA, new InputFieldValue());
        hashMap.put(Customer.PINCODE, new InputFieldValue());

        hashMap.put(Order.SHOULDER, new InputFieldValue());
        hashMap.put(Order.CHEST, new InputFieldValue());
        hashMap.put(Order.WAIST, new InputFieldValue());
        hashMap.put(Order.SLEEVE, new InputFieldValue());
        hashMap.put(Order.LENGTH, new InputFieldValue());
        hashMap.put(Order.NECK_TYPE, new InputFieldValue());
        hashMap.put(Order.NECK_SIZE, new InputFieldValue());
        hashMap.put(Order.PATTERN, new InputFieldValue());
        hashMap.put(Order.POCKET_SIZE, new InputFieldValue());
        hashMap.put(Order.POCKET, new InputFieldValue());
        hashMap.put(Order.MUNDHO, new InputFieldValue());
        hashMap.put(Order.FITTING, new InputFieldValue());

        return hashMap;
    }

    public static ArrayList<InputFieldType> getAlineGownForm() {
        String stringInputs = "[\n" +
                " {\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"shoulder\",\n" +
                "\"hint\": \"Shoulder\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"chest\",\n" +
                "\"hint\": \"Chest\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"waist\",\n" +
                "\"hint\": \"Waist\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"sleeve\",\n" +
                "\"hint\": \"Sleeve\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"length\",\n" +
                "\"hint\": \"Length\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"neck_type\",\n" +
                "\"hint\": \"Neck Type\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"neck_size\",\n" +
                "\"hint\": \"Neck Size\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"pattern\",\n" +
                "\"hint\": \"Pattern\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"pocket\",\n" +
                "\"hint\": \"Pocket\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"pocket_size\",\n" +
                "\"hint\": \"Pocket Size\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"mundho\",\n" +
                "\"hint\": \"Mundho\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"fitting\",\n" +
                "\"hint\": \"Fitting\",\n" +
                "\"required\": false\n" +
                "}\n" +
                "]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
    }

}
