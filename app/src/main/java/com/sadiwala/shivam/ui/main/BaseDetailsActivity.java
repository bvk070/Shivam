package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_CUSTOMERS;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_ORDERS;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.deleteCustomerFromCache;
import static com.sadiwala.shivam.network.FirebaseDatabaseController.deleteOrderFromCache;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.inputfields.InputFieldsGroupsContainer;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.search.SearchActivity;

import de.greenrobot.event.EventBus;

public class BaseDetailsActivity extends BaseActivity {

    protected Customer customer;
    protected Order order;
    protected EventBus mBus;
    protected InputFieldsGroupsContainer groupView;
    protected boolean showDelete = true, showUpdate = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (showDelete) {
            menu.findItem(R.id.menu_delete).setVisible(true);
        } else {
            menu.findItem(R.id.menu_delete).setVisible(false);
        }
        if (showUpdate) {
            menu.findItem(R.id.menu_edit).setVisible(true);
        } else {
            menu.findItem(R.id.menu_edit).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                deleteClicked(this);
                return true;
            case R.id.menu_edit:
                SearchActivity.start(this, new Bundle());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteClicked(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.delete);
        builder.setMessage("Are you sure do you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (order == null) {
                    deleteCustomer();
                } else {
                    deleteOrder();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteCustomer() {
        if (customer == null) return;
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_CUSTOMERS);
        collectionReference.document(customer.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                deleteCustomerFromCache(customer);
                Toast.makeText(getApplicationContext(), getString(R.string.customer_deleted), Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.deleted_customer_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteOrder() {
        if (order == null) return;
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_ORDERS);
        collectionReference.document(order.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                deleteOrderFromCache(order);
                Toast.makeText(getApplicationContext(), getString(R.string.order_deleted), Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.delete_order_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

}
