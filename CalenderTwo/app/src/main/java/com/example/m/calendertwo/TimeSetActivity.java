package com.example.m.calendertwo;


import android.os.Build;
import android.support.annotation.RequiresApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import com.example.m.calendertwo.customView.DayPickerScrollView;
import com.example.m.calendertwo.customView.MonthPickerScrollView;
import com.example.m.calendertwo.customView.TimePickScrollView;
import com.example.m.calendertwo.customView.YearPickerScrollView;
import java.util.List;


public class TimeSetActivity extends AppCompatActivity implements YearPickerScrollView.YearChangeCallBack, MonthPickerScrollView.MonthChangeCallBack, DayPickerScrollView.DayChangeCallBack, TimePickScrollView.TimeChangeCallBack {
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private final int TYPE_HOUR=1;
    private final int TYPE_MINUTE=2;
    private YearPickerScrollView mYearView;
    private MonthPickerScrollView mMonthView;
    private DayPickerScrollView mDayView;
    private TimePickScrollView mHourView;
    private TimePickScrollView mMinuteView;
    private TextView mTimeText;
    private TimeInfo mTimeInfo;

    private static TimeSetListener mTimeSetListener;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_set);
        enterTransition();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Toolbar toolbar=findViewById(R.id.time_set_toolbar);
        TextView titleText=findViewById(R.id.time_set_title);
        mYearView=findViewById(R.id.time_set_year);
        mMonthView=findViewById(R.id.time_set_month);
        mDayView=findViewById(R.id.time_set_day);
        mTimeText=findViewById(R.id.time_set_time);
        mHourView=findViewById(R.id.time_set_hour);
        mMinuteView=findViewById(R.id.time_set_minute);
        mTimeInfo=getIntent().getParcelableExtra(getString(R.string.intent_time));
        if (mTimeInfo.getType()==TimeInfo.TYPE_BEGIN){
            titleText.setText(getString(R.string.begin));
        }else {
            titleText.setText(getString(R.string.end));
        }
        toolbar.inflateMenu(R.menu.newbuilding_tool_bar_menu);
        toolbar.setNavigationIcon(getDrawable(R.drawable.close));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mYear=mTimeInfo.getYear();
        mMonth=mTimeInfo.getMonth();
        mDay=mTimeInfo.getDay();
        mHour=mTimeInfo.getHour();
        mMinute=mTimeInfo.getMinute();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.new_building_toolbar_menu_check){
                    mTimeSetListener.timeSetChange(mYear,mMonth,mDay,mHour,mMinute,mTimeInfo.getType());
                    onBackPressed();
                }
                return false;
            }
        });
        mTimeText.setText(String.format(getString(R.string.time_set_time), mYear,mMonth,mDay,
                DateInfo.getWeekDay(mYear,mMonth,mDay)));
        mYearView.initData(DateInfo.arrayToList(getResources().getIntArray(R.array.scroll_selector_year)),
                mYear,this);
        mMonthView.initData(DateInfo.arrayToList(getResources().getIntArray(R.array.scroll_selector_month)),
                mMonth,this);
        mDayView.initData(getDays(mYear,mMonth),
                mDay,this);
        mHourView.initData(DateInfo.arrayToList(getResources().getIntArray(R.array.srcoll_selector_hour)),
                mHour,TYPE_HOUR,this);
        mMinuteView.initData(DateInfo.arrayToList(getResources().getIntArray(R.array.srcoll_selector_minute)),
                mMinute,TYPE_MINUTE,this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enterTransition(){
        Slide slide=new Slide();
        slide.setSlideEdge(Gravity.RIGHT);
        slide.setDuration(getResources().getInteger(R.integer.animation_time));
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.interpolator.fast_out_slow_in));
        getWindow().setEnterTransition(slide);
    }

    @Override
    public void yearChange(int year) {
        mYear=year;
        mDayView.refreshData(getDays(mYear,mMonth),mDay);
        mTimeText.setText(String.format(getString(R.string.time_set_time), mYear,mMonth,mDay,
                DateInfo.getWeekDay(mYear,mMonth,mDay)));
    }

    @Override
    public void monthChange(int month) {
        if (month==1&&mMonth==12){//+1年
            mYear++;
            mYearView.refreshData(mYear);
        }
        else if (month==12&&mMonth==1){
            mYear--;
            mYearView.refreshData(mYear);
        }
        mMonth=month;
        mDayView.refreshData(getDays(mYear,mMonth),mDay);
        mTimeText.setText(String.format(getString(R.string.time_set_time), mYear,mMonth,mDay,
                DateInfo.getWeekDay(mYear,mMonth,mDay)));
    }

    @Override
    public void dayChange(int day,int type) {
        mDay=day;
        if (type==1){
            mMonth++;
            if (mMonth==13){
                mMonth=1;
                mYear++;
                mYearView.refreshData(mYear);
            }
            mMonthView.refreshData(mMonth);
            mDayView.refreshData(getDays(mYear,mMonth),mDay);
        }else if (type==-1){
            mMonth--;
            if (mMonth==0){
                mMonth=12;
                mYear--;
                mYearView.refreshData(mYear);
            }

            mMonthView.refreshData(mMonth);
            mDayView.refreshData(getDays(mYear,mMonth),mDay);
        }
        mTimeText.setText(String.format(getString(R.string.time_set_time), mYear,mMonth,mDay,
                DateInfo.getWeekDay(mYear,mMonth,mDay)));
    }

    @Override
    public void updateTime(int day) {
        mDay=day;
        mTimeText.setText(String.format(getString(R.string.time_set_time), mYear,mMonth,mDay,
                DateInfo.getWeekDay(mYear,mMonth,mDay)));
    }

    public List<Integer> getDays(int year, int month){
        if (year%4==0){//闰年
            if (month==2){
                return DateInfo.arrayToList(getResources().getIntArray(R.array.scroll_selector_day_29));
            }
        }
        switch (month){
            case 2:
                return DateInfo.arrayToList(getResources().getIntArray(R.array.scroll_selector_day_28));
            case 4:
            case 6:
            case 9:
            case 11:
                return DateInfo.arrayToList(getResources().getIntArray(R.array.scroll_selector_day_30));
            default:
                return DateInfo.arrayToList(getResources().getIntArray(R.array.scroll_selector_day_31));
        }
    }
    @Override
    public void timeChange(int time, int type) {
        switch (type){
            case TYPE_HOUR:
                mHour=time;
                break;
            case TYPE_MINUTE:
                mMinute=time;
                break;
        }
    }
    public interface TimeSetListener{
        public void timeSetChange(int year, int month,int day,int hour,int minute,int type);
    }
    public static void setOnTimeSetListener(TimeSetListener listener){
        mTimeSetListener=listener;
    }
}
