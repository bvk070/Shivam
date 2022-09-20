package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.syncdata.WorkManagerUtils.startSyncForeGroundService;
import static com.sadiwala.shivam.ui.Order.AddOrderActivity.PRODUCT_TYPE_DATA;

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
import com.sadiwala.shivam.base.AppData;
import com.sadiwala.shivam.ui.BaseActivity;
import com.sadiwala.shivam.ui.Customer.AddCustomerActivity;
import com.sadiwala.shivam.ui.Order.AddOrderActivity;

public class MainActivity extends BaseActivity {

    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton addCustomer, addAlineGown, addChapattiGown, addLenghi, addNightDress;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startSyncForeGroundService();
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
                Bundle bundle=new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.ALINE_GOWN.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

        addChapattiGown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Bundle bundle=new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.CHAPATTI_GOWN.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

        addNightDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Bundle bundle=new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.NIGHT_DRESS.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

        addLenghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Bundle bundle=new Bundle();
                bundle.putString(PRODUCT_TYPE_DATA, AppData.PRODUCT_TYPE.LENGHI.toString());
                AddOrderActivity.start(MainActivity.this, bundle);
            }
        });

    }

}
