package com.example.vintoday.utils;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.vintoday.R;
import com.example.vintoday.db.DBControllers;
import com.example.vintoday.models.News;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SyncWorker extends Worker {

    private static final String CHANNEL_ID = "SyncChannel";
    private Gson gson = new Gson();

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Sync Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder startBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.sync)
                .setContentTitle("Syncing Data")
                .setContentText("Data sync has started")
                .setPriority(NotificationCompat.PRIORITY_MAX);
        showToast("Data sync has started");

        notificationManager.notify(1, startBuilder.build());
        DBControllers dbControllers = new DBControllers(getApplicationContext());
        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
        List<News> sqliteNewsList = dbControllers.getAllNews();
        firestoreDb.collection("news").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<News> firestoreNewsList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    News news = document.toObject(News.class);
                    firestoreNewsList.add(news);
                }

                for (News sqliteNews : sqliteNewsList) {
                    boolean isNewsExist = false;
                    for (News firestoreNews : firestoreNewsList) {
                        if (sqliteNews.getTitle().equals(firestoreNews.getTitle())) {
                            isNewsExist = true;
                            break;
                        }
                    }
                    if (!isNewsExist) {
                        firestoreDb.collection("news").add(sqliteNews);
                    }
                }

                for (News firestoreNews : firestoreNewsList) {
                    boolean isNewsExist = false;
                    for (News sqliteNews : sqliteNewsList) {
                        if (firestoreNews.getTitle().equals(sqliteNews.getTitle())) {
                            isNewsExist = true;
                            break;
                        }
                    }
                    if (!isNewsExist) {
                        dbControllers.addNews(firestoreNews, null);
                    }
                }
            } else {
                // Handle error
            }

            NotificationCompat.Builder endBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.sync)
                    .setContentTitle("Syncing Data")
                    .setContentText("Data sync has completed")
                    .setPriority(NotificationCompat.PRIORITY_LOW);
            showToast("Data sync has completed");

            notificationManager.notify(2, endBuilder.build());
        });

        return Result.success();
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }
}