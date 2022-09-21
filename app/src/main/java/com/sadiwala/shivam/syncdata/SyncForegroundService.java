package com.sadiwala.shivam.syncdata;

import static com.sadiwala.shivam.syncdata.WorkManagerUtils.stopSyncForeGroundService;
import static com.sadiwala.shivam.util.AaryaConstants.AARYA_FOREGROUND_SERVICE_NOTIFICATION_ID;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.network.FirebaseDatabaseController;
import com.sadiwala.shivam.preferences.DataController;
import com.sadiwala.shivam.util.NotificationUtil;
import com.sadiwala.shivam.util.Util;

public class SyncForegroundService extends Service implements WorkManagerUtils.AaryaSyncListener {
    public static final String TAG = "SyncForegroundService";

    private int syncCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate called");
        startForeground(AARYA_FOREGROUND_SERVICE_NOTIFICATION_ID,
                NotificationUtil.getNotification(this,
                        getString(R.string.sync_service_title),
                        getString(R.string.sync_service_description), AARYA_FOREGROUND_SERVICE_NOTIFICATION_ID, 0));

        // cache customers
        syncCount++;
        FirebaseDatabaseController.cacheCustomers(this);

        // cache orders
        syncCount++;
        FirebaseDatabaseController.cacheOrders(this);

        // cache areas
        syncCount++;
        FirebaseDatabaseController.cacheAreas(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    public void onSyncTriggered() {

    }

    @Override
    public void onSyncFinished() {
        syncCount--;
        Log.e(TAG, "count decreasing: " + syncCount);
        // when all sync is done, stop the service
        if (syncCount <= 0) {
            stopSyncForeGroundService();
        }

    }


}
