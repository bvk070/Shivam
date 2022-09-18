package com.sadiwala.shivam.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AaryaProgressDialog {
    private static ProgressDialog mProgressDialog;
    private static Activity mCallerActivity;
    private static List<ProgressDialog> sProgressDialogList = new ArrayList<>();
    private static int mScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

    /**
     * This will show a progress dialog with given message
     */
    public static void show(Activity activity, String message) {
        mCallerActivity = activity;
        mScreenOrientation = mCallerActivity.getRequestedOrientation();
        //locking orientation while progress bar showing
        mCallerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mProgressDialog = new ProgressDialog(activity);
        //Setting caller activity so that we can use it while dismissing.
        if (mCallerActivity instanceof Activity) {
            mProgressDialog.setOwnerActivity(mCallerActivity);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
            sProgressDialogList.add(mProgressDialog);
        }
    }

    /**
     * This will show a progress dialog with given message
     */
    public static void show(Activity activity, String message, boolean isCancelable) {
        mCallerActivity = activity;
        mScreenOrientation = mCallerActivity.getRequestedOrientation();
        //locking orientation while progress bar showing
        mCallerActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mProgressDialog = new ProgressDialog(activity);
        //Setting caller activity so that we can use it while dismissing.
        if (mCallerActivity instanceof Activity) {
            mProgressDialog.setOwnerActivity(mCallerActivity);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.show();
        sProgressDialogList.add(mProgressDialog);
    }

    /**
     * This will hide a=the dialog if is visible
     */
    public static void hide() {
        // TODO: 25/10/17 Need to revisit this condition to check if we can do it in a better way.
        if (!isShowing()
                || mCallerActivity == null
                || mCallerActivity.isFinishing()
                || (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 && mCallerActivity.isDestroyed())) {
            return;
        }

        for (ProgressDialog progressDialog : sProgressDialogList) {
            if (progressDialog != null && progressDialog.isShowing()) {

                //Get owner activity if exists and check if its finished.
                Activity activity = progressDialog.getOwnerActivity();
                if (activity != null
                        && (activity.isFinishing()
                        || (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()))) {
                    continue;
                }

                try {
                    progressDialog.dismiss();
                } catch (RuntimeException e) {
                    Log.e("VPD", "Exception while dismissing the progress dialog.");
                }
            }
        }

        //Releasing orientation sensor
        if (mCallerActivity != null) {
            mCallerActivity.setRequestedOrientation(mScreenOrientation);
        }

        sProgressDialogList.clear();
    }

    public static boolean isShowing() {
        if (mProgressDialog != null) {
            return mProgressDialog.isShowing();
        } else {
            return false;
        }
    }
}