package com.example.m.calendertwo.widget;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.R;

/**
 * Created by m on 2018/3/25.
 */

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private DateInfo mDateInfo;
    public GridRemoteViewsFactory(Context context) {
        mContext=context;
        mDateInfo=DateInfo.getDate();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mDateInfo=DateInfo.getSpecialDate(CalenderWidgetProvider.gap);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return DateInfo.getDays(mDateInfo)+mDateInfo.getFirstDayOfMonth();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views=new RemoteViews(mContext.getPackageName(), R.layout.item_grid_widget);
        if (position<mDateInfo.getFirstDayOfMonth()){
            views.setTextViewText(R.id.widget_date,"");
        }else {
            int rightPosition=position-mDateInfo.getFirstDayOfMonth()+1;
            views.setTextViewText(R.id.widget_date,rightPosition+"");
        }
        if (mDateInfo.getDay()==position-mDateInfo.getFirstDayOfMonth()+1){
            views.setTextColor(R.id.widget_date,mContext.getColor(R.color.white));
            views.setImageViewResource(R.id.widget_reveal,R.drawable.circle_background);

        }
        Intent fillIntent=new Intent();
        views.setOnClickFillInIntent(R.id.widget_date,fillIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
