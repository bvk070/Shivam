package com.sadiwala.shivam.network;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sadiwala.shivam.inputfields.SelectionInputField;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.syncdata.WorkManagerUtils;
import com.sadiwala.shivam.util.Util;

import java.util.ArrayList;

public class FirebaseDatabaseController {

    private static final String TAG = FirebaseDatabaseController.class.getName();
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_ORDERS = "orders";

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

}
