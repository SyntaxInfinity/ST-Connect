package com.example.stconnect.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class NotificationScheduler {

    public static void scheduleNotification(Context context, String title, String message, long timeInMillis, int notificationId) {
        if (timeInMillis <= System.currentTimeMillis()) {
            Log.w("NotificationScheduler", "Ignored scheduling for past time: " + timeInMillis);
            return; 
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("id", notificationId);

        // Use notificationId as requestCode to ensure uniqueness
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
            Log.d("NotificationScheduler", "Scheduled notification (ID: " + notificationId + ") for: " + title + " at " + timeInMillis);
        } catch (SecurityException e) {
            Log.e("NotificationScheduler", "Permission error scheduling alarm", e);
        }
    }
}
