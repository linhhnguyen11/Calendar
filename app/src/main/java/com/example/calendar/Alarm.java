package com.example.calendar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.number.NumberRangeFormatter;
import android.os.Build;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    final String CHANEL_ID = "201";
    String message;
    @Override
    public void onReceive(Context context, Intent intent) {

        //System.out.println("BÁO THỨC CHUẨN BỊ CHẠY");

     //   Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();


//        chuẩn bị thông báo
        int notificationId=intent.getIntExtra("notificationId",0);
        message=intent.getStringExtra("mess");
        //khi thông báo đc chọn gọi đến mainactivity
        Intent mainIntent=new Intent(context,MainActivity.class);



        PendingIntent contentIntent =PendingIntent.getActivities(context,0, new Intent[]{mainIntent},0);

        NotificationManager myNotificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //chuẩn bị thông báo
        Notification.Builder builder=new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Bạn có sự kiện đang diễn ra nè!")
                .setContentText(message).setShowWhen(true)
                .setAutoCancel(true).setContentIntent(contentIntent);
        //notify
        myNotificationManager.notify(notificationId,builder.build());
        System.out.println("giờ báo thức trang alarm: "+System.currentTimeMillis());
    }
}