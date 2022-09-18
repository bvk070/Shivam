package com.sadiwala.shivam.base;


import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sadiwala.shivam.inputfields.InputField;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.models.common.IBottomSheetListener;
import com.sadiwala.shivam.ui.Customer.AddCustomerActivity;
import com.sadiwala.shivam.ui.Order.AddOrderActivity;
import com.sadiwala.shivam.ui.main.BaseAddActivity;
import com.sadiwala.shivam.ui.main.FragmentCustomers;
import com.sadiwala.shivam.ui.main.FragmentHome;
import com.sadiwala.shivam.ui.main.FragmentOrders;
import com.sadiwala.shivam.ui.main.MainActivity;


public class SourceRouteUtil {

    public static final String HOME = "home";
    public static final String ORDERS = "orders";
    public static final String CUSTOMERS = "customers";

    public static final String MAIN = "main";
    public static final String ADD_CUSTOMER = "add_customer";
    public static final String ADD_ORDER = "add_order";

    public static String getScreenName(Fragment fragment) {

        if (fragment instanceof FragmentHome) {
            return HOME;
        } else if (fragment instanceof FragmentOrders) {
            return ORDERS;
        } else if (fragment instanceof FragmentCustomers) {
            return CUSTOMERS;
        }
        return null;
    }

    public static String getScreenName(Activity activity) {
        if (activity instanceof MainActivity) {
            return MAIN;
        } else if (activity instanceof AddCustomerActivity) {
            return ADD_CUSTOMER;
        } else if (activity instanceof AddOrderActivity) {
            return ADD_ORDER;
        }
        return null;
    }


    public static IBottomSheetListener getBottomSheetListener(AppCompatActivity appCompatActivity) {
        IBottomSheetListener iBottomSheetListener = null;
        if (appCompatActivity instanceof BaseAddActivity) {
            iBottomSheetListener = ((BaseAddActivity) appCompatActivity).getBottomSheetListener();
        }
        return iBottomSheetListener;
    }

    public static void addInputFieldInRefreshMap(AppCompatActivity mActivity, InputFieldType inputFieldType, InputField inputField) {
        if (mActivity instanceof BaseAddActivity) {
            ((BaseAddActivity) mActivity).addInputFieldInRefreshMap(inputFieldType.getCode(), inputField);
        }
    }


}
