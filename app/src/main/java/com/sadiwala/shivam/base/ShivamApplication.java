package com.sadiwala.shivam.base;

import android.app.Application;
import android.content.Context;

public class ShivamApplication extends Application {

    private static final String TAG = "ShivamApplication";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    //Returns the application context
    public static Context getAppContext() {
        return mContext;
    }

}

