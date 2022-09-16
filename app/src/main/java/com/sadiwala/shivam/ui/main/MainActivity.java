package com.sadiwala.shivam.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.Customer.AddCustomerActivity;

public class MainActivity extends BaseActivity {

    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton addCustomer, addOrder;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Hello");

        floatingActionMenu = findViewById(R.id.menu);
        addCustomer = findViewById(R.id.addCustomer);
        addOrder = findViewById(R.id.addOrder);

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                AddCustomerActivity.start(MainActivity.this, new Bundle());
            }
        });

    }

}
