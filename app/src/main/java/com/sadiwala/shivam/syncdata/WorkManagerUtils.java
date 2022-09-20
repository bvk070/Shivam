package com.sadiwala.shivam.syncdata;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.sadiwala.shivam.base.ShivamApplication;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class WorkManagerUtils {

    public static void startSyncForeGroundService() {
        if (!isServiceRunning(SyncForegroundService.class, ShivamApplication.getAppContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent startIntent = new Intent(ShivamApplication.getAppContext(), SyncForegroundService.class);
                ShivamApplication.getAppContext().startForegroundService(startIntent);
            } else {
                Intent startIntent = new Intent(ShivamApplication.getAppContext(), SyncForegroundService.class);
                ShivamApplication.getAppContext().startService(startIntent);
            }
        }
    }

    public static void stopSyncForeGroundService() {
        if (isServiceRunning(SyncForegroundService.class, ShivamApplication.getAppContext())) {
            Intent stopIntent = new Intent(ShivamApplication.getAppContext(), SyncForegroundService.class);
            ShivamApplication.getAppContext().stopService(stopIntent);
        }
    }

    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForeGroundServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isMyWorkerRunning(String tag) {
        List<WorkInfo> workInfoList = null;
        try {
            workInfoList = WorkManager.getInstance(ShivamApplication.getAppContext()).getWorkInfosByTag(tag).get();
            for (WorkInfo workStatus : workInfoList) {
                if (workStatus.getState() == WorkInfo.State.RUNNING
                        || workStatus.getState() == WorkInfo.State.ENQUEUED) {
                    return true;
                }
            }
            return false;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Constraints getConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }

    public interface AaryaSyncListener {
        void onSyncTriggered();

        void onSyncFinished();
    }

}
