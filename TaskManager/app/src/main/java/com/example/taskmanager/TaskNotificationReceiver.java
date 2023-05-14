package com.example.taskmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;

public class TaskNotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "task_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskDescription = intent.getStringExtra("task_description");
        int taskId = intent.getIntExtra("task_id", -1);

        createNotificationChannel(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_task_notification)
                .setContentTitle("Recordatorio de tarea")
                .setContentText(taskDescription)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // Cambia de NotificationManagerCompat.from(context) a (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)
        notificationManager.notify(taskId, notification);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tareas pendientes";
            String description = "Recordatorio de tareas pendientes";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
