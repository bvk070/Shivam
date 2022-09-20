package com.sadiwala.shivam.syncdata;


import static com.sadiwala.shivam.syncdata.WorkManagerUtils.startSyncForeGroundService;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class AaryaWorkManager extends Worker {
    public static final String TAG = "AaryaWorkManager";

    public AaryaWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork starts");
        startSyncForeGroundService();
        Log.d(TAG, "doWork ends: success");
        return Result.success();
    }


}
