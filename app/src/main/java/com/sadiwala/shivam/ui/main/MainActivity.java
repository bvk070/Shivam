package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.syncdata.WorkManagerUtils.startSyncForeGroundService;
import static com.sadiwala.shivam.ui.Order.AddOrderActivity.PRODUCT_TYPE_DATA;
import static com.sadiwala.shivam.ui.search.SearchActivity.DATA_TYPE;
import static com.sadiwala.shivam.ui.search.SearchActivity.DATA_TYPE_CUSTOMERS;
import static com.sadiwala.shivam.ui.search.SearchActivity.DATA_TYPE_ORDERS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.Customer.AddCustomerActivity;
import com.sadiwala.shivam.ui.Order.AddOrderActivity;
import com.sadiwala.shivam.ui.search.SearchActivity;

public class MainActivity extends BaseActivity {

    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton addCustomer, addAlineGown, addChapattiGown, addLenghi, addNightDress;
    private Toolbar mToolbar;
    private NavController navController;
    private boolean showSearchMenu = false;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startSyncForeGroundService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (showSearchMenu) {
            menu.findItem(R.id.menu_search).setVisible(true);
        } else {
            menu.findItem(R.id.menu_search).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Bundle bundle = new Bundle();
                if (navController.getCurrentDestination().getLabel().equals(getString(R.string.customers))) {
                    bundle.putInt(DATA_TYPE, DATA_TYPE_CUSTOMERS);
                }
                if (navController.getCurrentDestination().getLabel().equals(getString(R.string.orders))) {
                    bundle.putInt(DATA_TYPE, DATA_TYPE_ORDERS);
                }
                SearchActivity.start(this, bundle);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void init() {
        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getLabel().equals(getString(R.string.customers)) || destination.getLabel().equals(getString(R.string.orders))) {
                    showSearchMenu = true;
                } else {
                    showSearchMenu = false;
                }
                invalidateOptionsMenu();
            }
        });

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Hello");

        floatingActionMenu = findViewById(R.id.menu);
        addCustomer = findViewById(R.id.addCustomer);
        addAlineGown = findViewById(R.id.addAlineGown);
        addChapattiGown = findViewById(R.id.addChapattiGown);
        addLenghi = findViewById(R.id.addLenghi);
        addNightDress = findViewById(R.id.addNightDress);

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                AddCustomerActivity.start(MainActivity.this, new Bundle());
            }
        });

        addAlineGown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.ALINE_GOWN.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

        addChapattiGown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.CHAPATTI_GOWN.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

        addNightDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.NIGHT_DRESS.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

        addLenghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.LENGHI.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

    }

}
