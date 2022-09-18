package com.sadiwala.shivam.base;

import static com.sadiwala.shivam.models.Customer.CUSTOMER_CODE;
import static com.sadiwala.shivam.models.Customer.GETADDRESS;
import static com.sadiwala.shivam.models.Customer.GETAREA;
import static com.sadiwala.shivam.models.Customer.GETMOBILE;
import static com.sadiwala.shivam.models.Customer.GETPINCODE;
import static com.sadiwala.shivam.models.Order.GETCHEST;
import static com.sadiwala.shivam.models.Order.GETDESIGN;
import static com.sadiwala.shivam.models.Order.GETFITTING;
import static com.sadiwala.shivam.models.Order.GETLENGTH;
import static com.sadiwala.shivam.models.Order.GETMUNDHO;
import static com.sadiwala.shivam.models.Order.GETNECKSIZE;
import static com.sadiwala.shivam.models.Order.GETNECKTYPE;
import static com.sadiwala.shivam.models.Order.GETPATTERN;
import static com.sadiwala.shivam.models.Order.GETPOCKET;
import static com.sadiwala.shivam.models.Order.GETPOCKETSIZE;
import static com.sadiwala.shivam.models.Order.GETSHOULDER;
import static com.sadiwala.shivam.models.Order.GETSLEEVE;
import static com.sadiwala.shivam.models.Order.GETWAIST;
import static com.sadiwala.shivam.preferences.DataController.getCachedCustomers;
import static com.sadiwala.shivam.util.Util.findGetters;
import static com.sadiwala.shivam.util.Util.getGetterMethodNameByFieldName;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.util.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                "}" +
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

        hashMap.put(CUSTOMER_CODE, new InputFieldValue());
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
        hashMap.put(Order.DESIGN, new InputFieldValue());

        return hashMap;
    }

    public static ArrayList<InputFieldType> getCustomerSectionInputs() {
        String stringInputs = "[{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + CUSTOMER_CODE + "\",\n" +
                "\"hint\": \"Customer\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getCachedCustomers()) + ",\n" +
                "\"required\": true\n" +
                "}]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
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
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"design\",\n" +
                "\"hint\": \"Design and color\",\n" +
                "\"required\": true\n" +
                "}" +
                "]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
    }

    public static boolean isExcludeField(String fieldName) {
//        if (fieldName.equals("customer")) return true;
        return false;
    }

    public static String getType(Activity Activity, String type) {
        switch (type) {
            case "ALINE_GOWN":
                return Activity.getString(R.string.aline_gown);
            case "CHAPATTI_GOWN":
                return Activity.getString(R.string.chapati_down);
            case "NIGHT_DRESS":
                return Activity.getString(R.string.night_dress);
        }
        return null;
    }

    public static void prepareOrderGroups(ArrayList<InputFieldType> inputFieldTypes, ArrayList<InputFieldValue> inputFieldValues, Order order) {
        if (order == null) return;

        ArrayList<Method> methods = findGetters(Order.class);
        Field[] fields = Order.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().getSimpleName().equals("InputFieldValue") && !AppData.isExcludeField(field.getName())) {
                InputFieldValue inputFieldValue = getOrderFieldValueByFieldName(methods, field.getName(), order);
                if (inputFieldValue != null) {
                    inputFieldValues.add(inputFieldValue);
                    InputFieldType inputFieldType = new InputFieldType(inputFieldValue.getType(), inputFieldValue.getCode(), false, inputFieldValue.getName());
                    inputFieldTypes.add(inputFieldType);
                }
            }
        }

    }

    public static void prepareCustomerGroups(ArrayList<InputFieldType> inputFieldTypes, ArrayList<InputFieldValue> inputFieldValues, Customer customer) {
        if (customer == null) return;

        ArrayList<Method> methods = findGetters(Customer.class);
        Field[] fields = Customer.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().getSimpleName().equals("InputFieldValue") && !AppData.isExcludeField(field.getName())) {
                InputFieldValue inputFieldValue = getCustomerFieldValueByFieldName(methods, field.getName(), customer);
                if (inputFieldValue != null) {
                    inputFieldValues.add(inputFieldValue);
                    InputFieldType inputFieldType = new InputFieldType(inputFieldValue.getType(), inputFieldValue.getCode(), false, inputFieldValue.getName());
                    inputFieldTypes.add(inputFieldType);
                }
            }
        }

    }

    public static InputFieldValue getOrderFieldValueByFieldName(ArrayList<Method> methods, String fieldName, Order order) {
        String getGetterMethod = getGetterMethodNameByFieldName(methods, fieldName);
        switch (getGetterMethod) {
            case GETSHOULDER:
                return order.getShoulder();
            case GETCHEST:
                return order.getChest();
            case GETWAIST:
                return order.getWaist();
            case GETSLEEVE:
                return order.getSleeve();
            case GETLENGTH:
                return order.getLength();
            case GETNECKTYPE:
                return order.getNeckType();
            case GETNECKSIZE:
                return order.getNeckSize();
            case GETPATTERN:
                return order.getPattern();
            case GETPOCKET:
                return order.getPocket();
            case GETPOCKETSIZE:
                return order.getPocketSize();
            case GETMUNDHO:
                return order.getMundho();
            case GETFITTING:
                return order.getFitting();
            case GETDESIGN:
                return order.getDesign();
            default:
                return null;
        }
    }

    public static InputFieldValue getCustomerFieldValueByFieldName(ArrayList<Method> methods, String fieldName, Customer customer) {
        String getGetterMethod = getGetterMethodNameByFieldName(methods, fieldName);
        switch (getGetterMethod) {
            case GETMOBILE:
                return customer.getMobile();
            case GETADDRESS:
                return customer.getAddress();
            case GETAREA:
                return customer.getArea();
            case GETPINCODE:
                return customer.getPincode();
            default:
                return null;
        }
    }

}
