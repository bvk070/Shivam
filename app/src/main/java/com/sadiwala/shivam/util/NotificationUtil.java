package com.sadiwala.shivam.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.ShivamApplication;

public class NotificationUtil {

    public static final String TAG = "NotificationUtil";

    public static Notification getNotification(Context context, String title, String description, int notificationId, int flag) {

        if (title == null) {
            title = context.getString(R.string.sync_service_title);
        }

        if (description == null) {
            description = context.getString(R.string.sync_service_description);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        String channelTitle = context.getString(R.string.aarya_notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String notificationChannelId = "1";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(notificationChannelId);
            if (mChannel == null) {
                mChannel = new NotificationChannel(notificationChannelId, channelTitle, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, notificationChannelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, flag);
        builder.setContentTitle(title)                            // required
                .setSmallIcon(getNotificationIcon())   // required
                .setContentText(description) // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(description));
        return builder.build();

    }

    /**
     * Returns icon for notification based on OS level
     */
    public static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_logo : getAppLauncherIcon(ShivamApplication.getAppContext());
    }

    /**
     * Returns App icon name from manifest file.
     */
    public static int getAppLauncherIcon(Context context) {
        int launcherIcon = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            launcherIcon = packageInfo.applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "exception " + e.getMessage());
        }

        return launcherIcon;
    }

}
