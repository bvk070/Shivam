package com.sadiwala.shivam.base;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.util.AaryaConstants;
import com.sadiwala.shivam.util.Util;

import java.util.ArrayList;
import java.util.List;


public final class PermissionManager {
    private static final String TAG = PermissionManager.class.getName();

    // Call Log.
    public static final String CALL_LOG = Manifest.permission.READ_CALL_LOG;
    // Camera group.
    public static final String CAMERA = Manifest.permission.CAMERA;
    // Location group.
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // Phone group.
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    // Bluetooth group
    public static final String BLUETOOTH = Manifest.permission.BLUETOOTH;
    public static final String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;

    /**
     * Check that given permission have been granted.
     */
    public static boolean hasGranted(int grantResult) {
        return grantResult == PERMISSION_GRANTED;
    }

    /**
     * Returns true if the Context has access to a given permission.
     * Always returns true on platforms below M.
     */
    public static boolean hasSelfPermission(Context context, String permission) {
        if (isM()) {
            return permissionHasGranted(context, permission);
        }
        return true;
    }

    /**
     * Returns true if the Context has access to all given permissions.
     * Always returns true on platforms below M.
     */
    public static boolean hasSelfPermissions(Context context, String[] permissions) {
        if (!isM()) {
            return true;
        }

        for (String permission : permissions) {
            if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                return permissionHasGranted(context, permission);
            } else {
                if (!permissionHasGranted(context, permission)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<String> getSelfPermissionsNotGrantedList(Context context, String[] permissions) {
        List<String> permissionNotGranted = new ArrayList<>();
        if (!isM()) {
            return permissionNotGranted;
        }

        for (String permission : permissions) {
            if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                if (!permissionHasGranted(context, permission)) {
                    permissionNotGranted.add(permission);
                }
            } else {
                if (!permissionHasGranted(context, permission)) {
                    permissionNotGranted.add(permission);
                }
            }
        }
        return permissionNotGranted;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean permissionHasGranted(Context context, String permission) {
        return hasGranted(context.checkSelfPermission(permission));
    }

    private static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void retryRequestPermission(Activity activity, String[] permissions, int requestCode) {
        List<String> retryPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!permissionHasGranted(activity, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    //show open settings popup
                } else {
                    //ask for permission
                    retryPermissions.add(permission);
                }
            }
            if (!Util.isListEmpty(retryPermissions)) {
                requestPermission(activity, retryPermissions.toArray(new String[retryPermissions.size()]), requestCode);
            }
        }

    }

    public static void openSettingsForPermission(Activity activity, String toastMessage) {
        Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivity(intent);
    }

    public static void handlePermissionsNotAvailable(Activity activity) {
        // TODO: 12/06/18 below commented code is for reference for starting snackbar to start settings screen for the app.
        /*View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        Snackbar snackbar = Snackbar.make(rootView, R.string.error_required_permissions_not_available, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Grant", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        snackbar.show();
        */
        Toast.makeText(activity, R.string.error_required_permissions_not_available, Toast.LENGTH_SHORT).show();
        activity.finish();
    }

    public static void checkAndGetRequiredPermission(Activity activity, String[] requiredPermissions) {
        List<String> permissionsList;

        permissionsList = PermissionManager.getSelfPermissionsNotGrantedList(ShivamApplication.getAppContext(),
                requiredPermissions);
        if (!Util.isListEmpty(permissionsList)) {
            PermissionManager.requestPermission(activity, permissionsList.toArray(new String[permissionsList.size()]), AaryaConstants.REQUEST_CODE_USER_PERMISSIONS);
        }
    }

    /***
     *
     * @return = All the permission is granted or not !
     */
    public static boolean handleRequestPermissionResult(Activity activity, String[] requiredPermissions, String[] permissions, int[] grantResults, PermissionState state) {
        boolean failed = false;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                failed = true;
                break;
            }
        }

        if (failed) {
            int permissionRetryCount = state.incrementAndGetPermissionRetryCount();
            if (permissionRetryCount < AaryaConstants.USER_PERMISSION_REQUEST_RETRY_COUNT) {
                PermissionManager.checkAndGetRequiredPermission(activity, requiredPermissions);
            } else {
                PermissionManager.handlePermissionsNotAvailable(activity);
            }
        }
        return !failed;
    }

    public static class PermissionState {
        private int mPermissionRetryCount = 0;

        public int incrementAndGetPermissionRetryCount() {
            return ++mPermissionRetryCount;
        }
    }


}
