package com.sadiwala.shivam.base;

import static com.sadiwala.shivam.models.Customer.CUSTOMER_CODE;
import static com.sadiwala.shivam.models.Customer.GETADDRESS;
import static com.sadiwala.shivam.models.Customer.GETAREA;
import static com.sadiwala.shivam.models.Customer.GETMOBILE;
import static com.sadiwala.shivam.models.Customer.GETPINCODE;
import static com.sadiwala.shivam.models.Order.GETCHEST;
import static com.sadiwala.shivam.models.Order.GETCLOTHCOLOR;
import static com.sadiwala.shivam.models.Order.GETCLOTHDESIGN;
import static com.sadiwala.shivam.models.Order.GETCUT;
import static com.sadiwala.shivam.models.Order.GETFITTING;
import static com.sadiwala.shivam.models.Order.GETGHER;
import static com.sadiwala.shivam.models.Order.GETHIPS;
import static com.sadiwala.shivam.models.Order.GETILASTIC;
import static com.sadiwala.shivam.models.Order.GETKHRISTAK;
import static com.sadiwala.shivam.models.Order.GETKOTHO;
import static com.sadiwala.shivam.models.Order.GETLENGHAPOCKET;
import static com.sadiwala.shivam.models.Order.GETLENGTH;
import static com.sadiwala.shivam.models.Order.GETMORI;
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
import com.sadiwala.shivam.models.common.CodeName;
import com.sadiwala.shivam.util.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppData {

    public enum PRODUCT_TYPE {
        ALINE_GOWN, CHAPATTI_GOWN, NIGHT_DRESS, LENGHI
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
                "}\n" +
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
        hashMap.put(Order.KOTHO, new InputFieldValue());
        hashMap.put(Order.HIPS, new InputFieldValue());
        hashMap.put(Order.GHER, new InputFieldValue());
        hashMap.put(Order.CUT, new InputFieldValue());
        hashMap.put(Order.MORI, new InputFieldValue());
        hashMap.put(Order.LENGHA_POCKET, new InputFieldValue());
        hashMap.put(Order.KHRISTAK, new InputFieldValue());
        hashMap.put(Order.ILASTIC, new InputFieldValue());
        hashMap.put(Order.CLOTH_DESIGN, new InputFieldValue());
        hashMap.put(Order.CLOTH_COLOR, new InputFieldValue());

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
                "\"code\": \"" + Order.SHOULDER + "\",\n" +
                "\"hint\": \"ખભો\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.CHEST + "\",\n" +
                "\"hint\": \"છાતી\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.WAIST + "\",\n" +
                "\"hint\": \"કમર\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.SLEEVE + "\",\n" +
                "\"hint\": \"બાંય\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.LENGTH + "\",\n" +
                "\"hint\": \"લંબાઈ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.NECK_TYPE + "\",\n" +
                "\"hint\": \"ગળું\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getThroats()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.NECK_SIZE + "\",\n" +
                "\"hint\": \"ગળાનું માપ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.PATTERN + "\",\n" +
                "\"hint\": \"પેટર્ન\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getPatterns()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.POCKET + "\",\n" +
                "\"hint\": \"પોકેટ\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getPockets()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.POCKET_SIZE + "\",\n" +
                "\"hint\": \"પોકેટનું માપ\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.MUNDHO + "\",\n" +
                "\"hint\": \"મૂંઢો\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.FITTING + "\",\n" +
                "\"hint\": \"ફિટિંગ\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.CLOTH_DESIGN + "\",\n" +
                "\"hint\": \"કાપડ ડિઝાઇન\",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.CLOTH_COLOR + "\",\n" +
                "\"hint\": \"કાપડ રંગ\",\n" +
                "\"required\": true\n" +
                "}" +
                "]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
    }

    public static ArrayList<InputFieldType> getChapatiGownForm() {
        String stringInputs = "[\n" +
                " {\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.SHOULDER + "\",\n" +
                "\"hint\": \"ખભો\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.CHEST + "\",\n" +
                "\"hint\": \"છાતી\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.SLEEVE + "\",\n" +
                "\"hint\": \"સ્લીવ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.LENGTH + "\",\n" +
                "\"hint\": \"લંબાઈ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.KOTHO + "\",\n" +
                "\"hint\": \"કોથો\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.PATTERN + "\",\n" +
                "\"hint\": \"પેટર્ન\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getPatterns()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.POCKET + "\",\n" +
                "\"hint\": \"પોકેટ\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getPockets()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.POCKET_SIZE + "\",\n" +
                "\"hint\": \"પોકેટનું માપ\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.MUNDHO + "\",\n" +
                "\"hint\": \"મુંધો\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.NECK_TYPE + "\",\n" +
                "\"hint\": \"ગળું\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getThroats()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.CLOTH_DESIGN + "\",\n" +
                "\"hint\": \"કાપડ ડિઝાઇન\",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.CLOTH_COLOR + "\",\n" +
                "\"hint\": \"કાપડ રંગ\",\n" +
                "\"required\": true\n" +
                "}" +
                "]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
    }

    public static ArrayList<InputFieldType> getNightShootTopForm() {
        String stringInputs = "[\n" +
                " {\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.SHOULDER + "\",\n" +
                "\"hint\": \"ખભો\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.CHEST + "\",\n" +
                "\"hint\": \"છાતી\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.WAIST + "\",\n" +
                "\"hint\": \"કમર\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.HIPS + "\",\n" +
                "\"hint\": \"હીપ્સ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.SLEEVE + "\",\n" +
                "\"hint\": \"બાંય\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.LENGTH + "\",\n" +
                "\"hint\": \"લંબાઈ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.GHER + "\",\n" +
                "\"hint\": \"ઘેર\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.CUT + "\",\n" +
                "\"hint\": \"ક્ટ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.NECK_TYPE + "\",\n" +
                "\"hint\": \"ગળું\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getThroats()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.NECK_SIZE + "\",\n" +
                "\"hint\": \"ગળાનું માપ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.PATTERN + "\",\n" +
                "\"hint\": \"પેટર્ન\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getPatterns()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.POCKET + "\",\n" +
                "\"hint\": \"પોકેટ\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getPockets()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.POCKET_SIZE + "\",\n" +
                "\"hint\": \"પોકેટનું માપ\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.MUNDHO + "\",\n" +
                "\"hint\": \"મૂંઢો\",\n" +
                "\"required\": false\n" +
                "}" +
                "]";

        ArrayList<InputFieldType> inputFieldTypes = Gson.getInstance().fromJson(stringInputs, new TypeToken<List<InputFieldType>>() {
        }.getType());

        return inputFieldTypes;
    }

    public static ArrayList<InputFieldType> getNightShootBottomForm() {
        String stringInputs = "[\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.LENGTH + "\",\n" +
                "\"hint\": \"લંબાઈ\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.MORI + "\",\n" +
                "\"hint\": \"મોરી\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.GHER + "\",\n" +
                "\"hint\": \"ઘેર\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.KHRISTAK + "\",\n" +
                "\"hint\": \"ખ્રિસ્તક\",\n" +
                "\"required\": true\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"decimal\",\n" +
                "\"code\": \"" + Order.ILASTIC + "\",\n" +
                "\"hint\": \"ઈલાસ્ટીક\",\n" +
                "\"required\": false\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"code_name_spinner\",\n" +
                "\"code\": \"" + Order.LENGHA_POCKET + "\",\n" +
                "\"hint\": \"લેઘામાં પોકેટ\",\n" +
                "\"code_name_spinner_options\": " + Gson.getInstance().toJson(getPocketsYesNo()) + ",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.CLOTH_DESIGN + "\",\n" +
                "\"hint\": \"કાપડ ડિઝાઇન\",\n" +
                "\"required\": true\n" +
                "}," +
                "{\n" +
                "\"type\": \"text\",\n" +
                "\"code\": \"" + Order.CLOTH_COLOR + "\",\n" +
                "\"hint\": \"કાપડ રંગ\",\n" +
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
            case "LENGHI":
                return Activity.getString(R.string.lenghi);
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
            case GETLENGHAPOCKET:
                return order.getLenghaPocket();
            case GETPOCKETSIZE:
                return order.getPocketSize();
            case GETMUNDHO:
                return order.getMundho();
            case GETFITTING:
                return order.getFitting();
            case GETKOTHO:
                return order.getKotho();
            case GETHIPS:
                return order.getHips();
            case GETGHER:
                return order.getGher();
            case GETCUT:
                return order.getCut();
            case GETMORI:
                return order.getMori();
            case GETKHRISTAK:
                return order.getKhristak();
            case GETILASTIC:
                return order.getIlastic();
            case GETCLOTHDESIGN:
                return order.getClothDesign();
            case GETCLOTHCOLOR:
                return order.getClothColor();
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

    public static CodeName[] getPatterns() {
        CodeName[] codeNames = new CodeName[6];

        codeNames[0] = new CodeName("got", "ગોટ");
        codeNames[1] = new CodeName("dkajul", "ડબલ કાપડ જૂલ");
        codeNames[2] = new CodeName("motijul", "મોટી જૂલ");
        codeNames[3] = new CodeName("netwalu", "નેટવાળુ");
        codeNames[4] = new CodeName("plaingot", "પ્લેનગોટ");
        codeNames[5] = new CodeName("plaindkajul", "પ્લેન ડબલ કાપડ જૂલ");

        return codeNames;
    }

    public static CodeName[] getPockets() {
        CodeName[] codeNames = new CodeName[2];
        codeNames[0] = new CodeName("pocket", "પોકેટ");
        codeNames[1] = new CodeName("side_pocket", "સાઈડ પોકેટ");
        return codeNames;
    }

    public static CodeName[] getPocketsYesNo() {
        CodeName[] codeNames = new CodeName[2];
        codeNames[0] = new CodeName("yes", "Yes");
        codeNames[1] = new CodeName("no", "No");
        return codeNames;
    }

    public static CodeName[] getThroats() {
        CodeName[] codeNames = new CodeName[5];

        codeNames[0] = new CodeName("panchkon", "પંચકોણ");
        codeNames[1] = new CodeName("round", "ગોળ");
        codeNames[2] = new CodeName("square", "ચોરસ");
        codeNames[3] = new CodeName("vshape", "વિશેપ");
        codeNames[4] = new CodeName("panshape", "પાનશેપ");

        return codeNames;
    }

}
