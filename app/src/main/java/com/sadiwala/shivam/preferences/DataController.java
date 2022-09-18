package com.sadiwala.shivam.preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.models.common.CodeName;

import java.util.ArrayList;
import java.util.List;


public class DataController {

    public static final String PREF_ORDERS = "orders";
    public static final String PREF_CUSTOMERS = "customers";

    public static String getModelObject(String key, String defaultValue) {
        return PreferenceUtil.getStringValue(key, defaultValue);
    }

    public static void setModelObject(String key, String value) {
        PreferenceUtil.setStringValue(key, value);
    }

    public static ArrayList<Order> getPrefOrders() {
        String strOrders = PreferenceUtil.getStringValue(PREF_ORDERS, null);
        ArrayList<Order> orders = new Gson().fromJson(strOrders, new TypeToken<List<Order>>() {
        }.getType());
        if (orders == null) {
            orders = new ArrayList<>();
        }
        return orders;
    }

    public static void setPrefOrders(ArrayList<Order> orders) {
        PreferenceUtil.setStringValue(PREF_ORDERS, new Gson().toJson(orders));
    }

    public static ArrayList<Customer> getPrefCustomers() {
        String strOrders = PreferenceUtil.getStringValue(PREF_CUSTOMERS, null);
        ArrayList<Customer> customers = new Gson().fromJson(strOrders, new TypeToken<List<Customer>>() {
        }.getType());
        if (customers == null) {
            customers = new ArrayList<>();
        }
        return customers;
    }

    public static void setPrefCustomers(ArrayList<Customer> customers) {
        PreferenceUtil.setStringValue(PREF_CUSTOMERS, new Gson().toJson(customers));
    }

    public static CodeName[] getCachedCustomers() {
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        CodeName[] codeNames = new CodeName[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            CodeName codeName = new CodeName(customers.get(i).getId(), customers.get(i).getName().getValue());
            codeNames[i] = codeName;
        }
        return codeNames;
    }

    public static Customer getCustomerById(String id) {
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) return customer;
        }
        return null;
    }

}
