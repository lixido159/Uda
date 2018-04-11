package com.example.m.calendertwo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.example.m.calendertwo.database.MemoContract;

import java.text.DecimalFormat;

import static android.support.v4.app.NotificationCompat.FLAG_AUTO_CANCEL;


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
                    bundle.getInt(getString(R.string.bundle_minute)),
                    bundle.getInt(MemoContract._ID));
        }else if (intent.getAction().equals(ACTION_CANCLE_NOTIFICATION)){
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(intent.getIntExtra(MemoContract._ID,0));
        }

    }
    private void showNotification(String plan,int hour,int minute,int id){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            DecimalFormat format=new DecimalFormat("00");
            Intent intent=new Intent(this,MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,id,intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
                notification = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_calen)
                        .setContentTitle(plan)
                        .setContentText(String.format(getString(R.string.new_building_hour_minute),
                                format.format(hour),format.format(minute)))
                        .setContentIntent(pendingIntent)
                        .addAction(cancleNotification(this,id))
                        .build();
                notification.flags=FLAG_AUTO_CANCEL;
            }
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(id,notification);
        }
    }
    private static Notification.Action cancleNotification(Context context,int id){
        Intent intent=new Intent(context,NotificationService.class);
        intent.putExtra(MemoContract._ID,id);
        intent.setAction(ACTION_CANCLE_NOTIFICATION);
        PendingIntent pendingIntent=PendingIntent.getService(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Action action= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            action = new Notification.Action(R.mipmap.ic_launcher,context.getString(R.string.sure),pendingIntent);
        }
        return action;
    }
}
