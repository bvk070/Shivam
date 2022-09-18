package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.network.FirebaseDatabaseController.TABLE_CUSTOMERS;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.ui.Customer.CustomersRecycleviewAdapter;
import com.sadiwala.shivam.util.CustomDividerItemDecoration;
import com.sadiwala.shivam.util.Util;

import java.util.ArrayList;

public class FragmentCustomers extends Fragment {

    private static final String TAG = FragmentCustomers.class.getName();

    private ProgressBar progressBar;
    private TextView tvError;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.customers));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeViews();
        ArrayList<Customer> customers = DataController.getPrefCustomers();
        if (Util.isListEmpty(customers)) {
            refreshData();
        } else {
            setData(customers);
        }
    }

    private void initializeViews() {

        tvError = getView().findViewById(R.id.tvError);
        progressBar = getView().findViewById(R.id.progressbar);

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Add custom divider to recyclerview
        Drawable mDivider = ContextCompat.getDrawable(mRecyclerView.getContext(), R.drawable.divider);
        CustomDividerItemDecoration hItemDecoration = new CustomDividerItemDecoration(mDivider, false, false);
        mRecyclerView.addItemDecoration(hItemDecoration);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refreshData();
            }
        });
    }

    private void refreshData() {
        progressBar.setVisibility(View.VISIBLE);

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(TABLE_CUSTOMERS);
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                setData(customers);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), getString(R.string.error_network), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void setData(ArrayList<Customer> customers) {

        if (Util.isListEmpty(customers)) {
            tvError.setVisibility(View.VISIBLE);
        } else {
            tvError.setVisibility(View.GONE);
            CustomersRecycleviewAdapter customersRecycleviewAdapter = new CustomersRecycleviewAdapter(customers, getActivity());
            mRecyclerView.setAdapter(customersRecycleviewAdapter);
        }
        DataController.setPrefCustomers(customers);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

}
