package com.example.self_enrichment_app.view.mood;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.view.general.LoginActivity;

public class MoodAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationBuilder(context);
    }

    public void createNotificationBuilder(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NavDeepLinkBuilder navDeepLinkBuilder = new NavDeepLinkBuilder(context);
        navDeepLinkBuilder.setGraph(R.navigation.mobile_navigation);
        navDeepLinkBuilder.setDestination(R.id.destMood);
        PendingIntent pendingIntent = navDeepLinkBuilder.createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MoodDiary")
                .setSmallIcon(R.drawable.ic_health)
                .setContentTitle("Mental health notification")
                .setContentText("Are you feeling alright? Do you need help?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(2, builder.build());
    }

}
