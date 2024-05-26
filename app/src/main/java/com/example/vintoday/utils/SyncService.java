//package com.example.vintoday.utils;
//
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import androidx.core.app.NotificationCompat;
//
//public class SyncService extends IntentService {
//
//    private static final int NOTIFICATION_ID = 1;
//
//    public SyncService() {
//        super("SyncService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        syncData();
//    }
//
//    private void syncData() {
//        // Your code to sync SQLite and Firebase database
//
//        // Show notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_sync)
//                .setContentTitle("Syncing Data")
//                .setContentText("Your data is being synced")
//                .setPriority(NotificationCompat.PRIORITY_LOW);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
//}