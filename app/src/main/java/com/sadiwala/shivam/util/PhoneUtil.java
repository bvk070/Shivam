package com.sadiwala.shivam.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.PermissionManager;
import com.sadiwala.shivam.base.ShivamApplication;

public class PhoneUtil {
    private static final String TAG = "PhoneUtil";
    public static final String INCOMING = "incoming";
    public static final String OUTGOING = "outgoing";
    public static final String MISSED = "missed";


    @SuppressLint("MissingPermission")
    public static void makeCall(Activity context, String mobile) {
        // log start
        if (isPhoneHasCallFacility() && PermissionManager.hasSelfPermission(context, PermissionManager.CALL_PHONE)) {
            Log.e(TAG, "make call no error");
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivityForResult(intent, AaryaConstants.REQUEST_CODE_ACTIVITY_UPDATE);
        } else if (!PermissionManager.hasSelfPermission(context, PermissionManager.CALL_PHONE)) {
            PermissionManager.requestPermission(context, new String[]{PermissionManager.CALL_PHONE}, AaryaConstants.PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            Log.e(TAG, "make call unknown_error");
            // log end
            //though this scenario should not occur but in case this happens we will show toast message.
            Toast.makeText(context, context.getString(R.string.no_call_facility), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Returns true if device can make calls
     */
    public static boolean isPhoneHasCallFacility() {
        return ShivamApplication.getAppContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

}