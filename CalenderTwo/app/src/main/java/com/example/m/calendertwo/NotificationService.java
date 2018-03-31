package com.example.m.calendertwo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.DecimalFormat;


/**
 * Created by m on 2018/3/28.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class NotificationService extends IntentService {
    public static final String ACTION_SHOW_NOTIFICATION="com.example.m.calendertwo.show_notification";
    public static final String ACTION_CANCLE_NOTIFICATION="com.example.m.calendertwo.cancle_notification";
    private static final int ID_NOTIFICATION=0;
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals(ACTION_SHOW_NOTIFICATION)){
            Bundle bundle=intent.getBundleExtra(getString(R.string.intent_time));
            showNotification(bundle.getString(getString(R.string.bundle_plan)),
                    bundle.getInt(getString(R.string.bundle_hour)),
                    bundle.getInt(getString(R.string.bundle_minute)));
        }else if (intent.getAction().equals(ACTION_CANCLE_NOTIFICATION)){
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(ID_NOTIFICATION);
        }

    }
    private void showNotification(String plan,int hour,int minute){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            DecimalFormat format=new DecimalFormat("00");
            Intent intent=new Intent(this,MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
                notification = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_calen)
                        .setContentTitle(plan)
                        .setContentText(String.format(getString(R.string.new_building_hour_minute),
                                format.format(hour),format.format(minute)))
                        .setContentIntent(pendingIntent)
                        .addAction(cancleNotification(this))
                        .build();
            }
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(ID_NOTIFICATION,notification);
        }
    }
    private static Notification.Action cancleNotification(Context context){
        Intent intent=new Intent(context,NotificationService.class);
        intent.setAction(ACTION_CANCLE_NOTIFICATION);
        PendingIntent pendingIntent=PendingIntent.getService(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Action action= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            action = new Notification.Action(R.mipmap.ic_launcher,context.getString(R.string.sure),pendingIntent);
        }
        return action;
    }
}
