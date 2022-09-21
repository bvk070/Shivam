package com.sadiwala.shivam.network;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sadiwala.shivam.inputfields.InputFieldValue;
import com.sadiwala.shivam.inputfields.SelectionInputField;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.models.User;
import com.sadiwala.shivam.models.common.CodeName;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.syncdata.WorkManagerUtils;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FirebaseDatabaseController {

    private static final String TAG = FirebaseDatabaseController.class.getName();
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_AREAS = "areas";

    public static void cacheCustomers(WorkManagerUtils.AaryaSyncListener aaryaSyncListener) {

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_CUSTOMERS);
        collectionReference.orderBy(Customer.TIMESTAMP, Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<Customer> customers = new ArrayList<>();
                if (queryDocumentSnapshots != null && !Util.isListEmpty(queryDocumentSnapshots.getDocuments())) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        customer.setId(documentSnapshot.getId());
                        customers.add(customer);
                    }
                }
                DataController.setPrefCustomers(customers);
                aaryaSyncListener.onSyncFinished();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                aaryaSyncListener.onSyncFinished();
            }
        });

    }

    public static void cacheOrders(WorkManagerUtils.AaryaSyncListener aaryaSyncListener) {

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_ORDERS);
        collectionReference.orderBy(Order.TIMESTAMP, Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<Order> orders = new ArrayList<>();
                if (queryDocumentSnapshots != null && !Util.isListEmpty(queryDocumentSnapshots.getDocuments())) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setId(documentSnapshot.getId());
                        orders.add(order);
                    }
                }
                DataController.setPrefOrders(orders);
                aaryaSyncListener.onSyncFinished();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                aaryaSyncListener.onSyncFinished();
            }
        });

    }

    public static void cacheAreas(WorkManagerUtils.AaryaSyncListener aaryaSyncListener) {

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_AREAS);
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<CodeName> codeNames = new ArrayList<>();
                if (queryDocumentSnapshots != null && !Util.isListEmpty(queryDocumentSnapshots.getDocuments())) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        CodeName codeName = documentSnapshot.toObject(CodeName.class);
                        codeName.setCode(documentSnapshot.getId());
                        codeNames.add(codeName);
                    }
                }
                DataController.setPrefAreas(codeNames);
                aaryaSyncListener.onSyncFinished();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                aaryaSyncListener.onSyncFinished();
            }
        });

    }

    public static ArrayList<Order> getOrdersInRange(long start, long end) {
        ArrayList<Order> results = new ArrayList<>();
        ArrayList<Order> orders = DataController.getPrefOrders();
        for (Order order : orders) {
            if (start <= order.getTimestamp() && order.getTimestamp() <= end) {
                results.add(order);
            }
        }
        return results;
    }

    public static ArrayList<Customer> getCustomersInRange(long start, long end) {
        ArrayList<Customer> results = new ArrayList<>();
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        for (Customer customer : customers) {
            if (start <= customer.getTimestamp() && customer.getTimestamp() <= end) {
                results.add(customer);
            }
        }
        return results;
    }

    public static int getUniqueCustomersCountFromOrders(ArrayList<Order> orders) {
        Set<String> codes = new HashSet<>();
        for (Order order : orders) {
            codes.add(SelectionInputField.getCodeFromJsonValue(order.getCustomer().getValue()));
        }
        return codes.size();
    }

    public static void deleteCustomerFromCache(Customer customer) {
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer1 = customers.get(i);
            if (customer1.getId().equals(customer.getId())) {
                customers.remove(i);
            }
        }
        DataController.setPrefCustomers(customers);
    }

    public static void deleteOrderFromCache(Order order) {
        ArrayList<Order> orders = DataController.getPrefOrders();
        for (int i = 0; i < orders.size(); i++) {
            Order order1 = orders.get(i);
            if (order1.getId().equals(order.getId())) {
                orders.remove(i);
            }
        }
        DataController.setPrefOrders(orders);
    }

    public static void addCustomerInCache(Customer customer) {
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        customers.add(0, customer);
        DataController.setPrefCustomers(customers);
    }

    public static void updateCustomerInCache(Customer customer) {
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer1 = customers.get(i);
            if (customer1.getId().equals(customer.getId())) {
                customers.set(i, customer);
            }
        }
        DataController.setPrefCustomers(customers);
    }

    public static void addOrderInCache(Order order) {
        ArrayList<Order> orders = DataController.getPrefOrders();
        orders.add(0, order);
        DataController.setPrefOrders(orders);
    }

    public static void updateOrderInCache(Order order) {
        ArrayList<Order> orders = DataController.getPrefOrders();
        for (int i = 0; i < orders.size(); i++) {
            Order order1 = orders.get(i);
            if (order1.getId().equals(order.getId())) {
                orders.set(i, order);
            }
        }
        DataController.setPrefOrders(orders);
    }

    public static ArrayList<Order> getOrdersByName(ArrayList<Order> orders, String query) {
        ArrayList<Order> results = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (SelectionInputField.getNameFromJsonValue(order.getCustomer().getValue()).toLowerCase().contains(query.toLowerCase())) {
                results.add(order);
            }
        }
        return results;
    }

    public static ArrayList<Customer> getCustomerByName(ArrayList<Customer> customers, String query) {
        ArrayList<Customer> results = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.getName().getValue().toLowerCase().contains(query.toLowerCase())) {
                results.add(customer);
            }
        }
        return results;
    }

    public static Customer getCustomerByCode(String code) {
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.getId().equals(code)) {
                return customer;
            }
        }
        return null;
    }

    public static User getUser(QuerySnapshot queryDocumentSnapshots, User currentUser) {
        if (queryDocumentSnapshots != null && !Util.isListEmpty(queryDocumentSnapshots.getDocuments())) {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                User user = documentSnapshot.toObject(User.class);
                if (user.getEmail().equals(currentUser.getEmail()) && user.getPassword().equals(currentUser.getPassword()))
                    return user;
            }
        }
        return null;
    }

    public static void updateLatestCustomerValue(ArrayList<InputFieldValue> inputFieldValues) {
        for (InputFieldValue inputFieldValue : inputFieldValues) {
            if (Order.CUSTOMER.equals(inputFieldValue.getCode())) {
                String json = inputFieldValue.getValue();
                String customerCode = SelectionInputField.getCodeFromJsonValue(json);
                Customer customer = getCustomerByCode(customerCode);
                if (customer != null) {
                    ArrayList<CodeName> codeNames = new ArrayList<>();
                    codeNames.add(new CodeName(customerCode, customer.getName().getValue()));
                    inputFieldValue.setValue(Gson.getInstance().toJson(codeNames));
                }
            }
        }
    }

}
