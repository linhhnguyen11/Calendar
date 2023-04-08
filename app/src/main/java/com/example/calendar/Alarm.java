package com.example.calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    String message;
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("BÁO THỨC CHUẨN BỊ CHẠY");

        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();


        //chuẩn bị thông báo
        int notificationId=intent.getIntExtra("notificationId",0);

        //khi thông báo đc chọn gọi đến mainactivity
        Intent mainIntent=new Intent(context,MainActivity.class);
        message=intent.getStringExtra("mess");

        System.out.println(message);
        PendingIntent contentIntent =PendingIntent.getActivities(context,0, new Intent[]{mainIntent},0);

        NotificationManager myNotificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //chuẩn bị thông báo
        Notification.Builder builder=new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("It's time")
                .setContentText(message).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentIntent(contentIntent);
        //notify
        myNotificationManager.notify(notificationId,builder.build());
    }
}