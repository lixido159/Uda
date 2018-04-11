package com.example.m.calendertwo.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by m on 2018/3/26.
 */

public class CalenderService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public static final String ACTION_CALENDER_INIT=
            "com.example.m.calendertwo.widget.action.calender_init";
    public static final String ACTION_CALENDER_LEFT=
            "com.example.m.calendertwo.widget.action.calender_left";
    public static final String ACTION_CALENDER_RIGHT=
            "com.example.m.calendertwo.widget.action.calender_right";
    public CalenderService() {
        super("CalenderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null){
            final String action=intent.getAction();
            int type=0;

            if (action.equals(ACTION_CALENDER_INIT)){

            }
            else if (action.equals(ACTION_CALENDER_LEFT)){
                type=CalenderWidgetProvider.LEFT;

            }else if (action.equals(ACTION_CALENDER_RIGHT)){
                type=CalenderWidgetProvider.RIGHT;

            }
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);
            int[]appWidgetIds=appWidgetManager.getAppWidgetIds(new ComponentName(this,CalenderWidgetProvider.class));
            CalenderWidgetProvider.updateCalenderWidgets(this,appWidgetManager,appWidgetIds,
                    type);
        }
    }
    public static void startActionUpdateWidgets(Context context){

        Intent intent=new Intent(context,CalenderService.class);
        intent.setAction(ACTION_CALENDER_INIT);
        context.startService(intent);
    }

}
