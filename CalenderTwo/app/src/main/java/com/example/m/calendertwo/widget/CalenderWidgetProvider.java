package com.example.m.calendertwo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.MainActivity;
import com.example.m.calendertwo.R;

/**
 * Implementation of App Widget functionality.
 */
public class CalenderWidgetProvider extends AppWidgetProvider {

    public static final int LEFT=-1;
    public static final int INIT=0;
    public static final int RIGHT=1;

    public static int gap=0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,int type) {
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, getCalenderGridView(context,type,appWidgetManager,appWidgetId));

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        CalenderService.startActionUpdateWidgets(context);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    private static RemoteViews getCalenderGridView(Context context,int type,AppWidgetManager appWidgetManager,
                                                   int appWidgetId){

        RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget_grid_view);

        if (type==INIT){

        }
        else if (type==LEFT){
            gap--;
            RemoteViews switchView=new RemoteViews(context.getPackageName(),R.layout.switch_grid_left);
            views.removeAllViews(R.id.widget_grid_frame);
            views.addView(R.id.widget_grid_frame,switchView);

        }else if (type==RIGHT){
            gap++;
            RemoteViews switchView=new RemoteViews(context.getPackageName(),R.layout.switch_grid_right);
            views.removeAllViews(R.id.widget_grid_frame);
            views.addView(R.id.widget_grid_frame,switchView);

        }
        //就像recyclerView.setAdapter()
        Intent intent=new Intent(context,GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view,intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_grid_view);
        Intent appIntent=new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent=PendingIntent.getActivity(context,0,appIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view,appPendingIntent);
        DateInfo dateInfo=DateInfo.getSpecialDate(gap);
        views.setTextViewText(R.id.widget_grid_date, String.format(context.getString(R.string.date_title),
                dateInfo.getYear(),dateInfo.getMonth()));

        Intent intentLeft=new Intent(context,CalenderService.class);
        intentLeft.setAction(CalenderService.ACTION_CALENDER_LEFT);
        PendingIntent pendingIntentLeft=PendingIntent.getService(context,0,
                intentLeft,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_grid_img_left,pendingIntentLeft);

        Intent intentRight=new Intent(context,CalenderService.class);
        intentRight.setAction(CalenderService.ACTION_CALENDER_RIGHT);
        PendingIntent pendingIntentRight=PendingIntent.getService(context,1,
                intentRight,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_grid_img_right,pendingIntentRight);

        return views;
    }
    public static void updateCalenderWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds,int type) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,type);
        }
    }
}

