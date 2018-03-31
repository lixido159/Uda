package com.example.m.calendertwo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import com.example.m.calendertwo.adapter.DateRecyclerAdapter;
import com.example.m.calendertwo.databinding.ActivityMainBinding;
import com.example.m.calendertwo.fragment.MainMonthFragment;


import java.util.Calendar;


import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener
        ,DateRecyclerAdapter.GapDaysCallBack
       {
    public static int mCount=100;
    private ActivityMainBinding mBinding;
    private static MainActivity mInstance;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar mCalendar;
    private static MainCallBack mCallBack;
    private static MainItemCallBack mItemCallBack;
    private int mCurrentSelected;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ////设置statusbar的图标颜色高亮反转
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        clickNewBuilding(mBinding.mainNewBuildingText,this);
        mBinding.mainMonthText.setSelected(true);
        mCurrentSelected=1;
        mBinding.mainMonthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentSelected){
                    case 2:
                        mBinding.mainWeekText.setSelected(false);
                        break;
                    case 3:
                        break;
                }
                mCurrentSelected=1;
                mBinding.mainMonthText.setSelected(true);

            }
        });
        mBinding.mainWeekText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentSelected){
                    case 1:
                        mBinding.mainMonthText.setSelected(false);
                        break;
                    case 3:
                        break;
                }
                mCurrentSelected=2;
                mBinding.mainWeekText.setSelected(true);

            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment,new MainMonthFragment())
                .commit();

        mInstance=this;
        DateInfo mDateInfo=DateInfo.getDate();
        mBinding.mainToolBar.inflateMenu(R.menu.tool_bar_menu);
        mBinding.mainToolBar.setOnMenuItemClickListener(this);
        mBinding.mainNowMonth.setText(String.format(getString(R.string.date_title), mDateInfo.getYear(),mDateInfo.getMonth()));
        //ViewPager页数
        mCalendar=Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT+08");
        mCalendar.setTimeZone(tz);
        mYear=mCalendar.get(Calendar.YEAR);
        mMonth=mCalendar.get(Calendar.MONTH)+1;
        mDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        MainMonthFragment.setOnMainFragmentListener(new MainMonthFragment.MainFragmentCallBack() {
            @Override
            public void selected(int position) {
                DateInfo dateInfo=DateInfo.getSpecialDate(position-MainActivity.mCount/2);
                mBinding.mainNowMonth.setText(String.format(getString(R.string.date_title), dateInfo.getYear(),dateInfo.getMonth()));
            }
        });
    }

           @Override
           protected void onNewIntent(Intent intent) {
               super.onNewIntent(intent);
               Bundle bundle=intent.getBundleExtra(getString(R.string.intent_time));
               TimeInfo info=bundle.getParcelable(getString(R.string.begin));
               mCallBack.update(info.getYear(),info.getMonth(),info.getDay());
           }
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               //点击右上角的今天,页面回到今天
               switch (item.getItemId()){
                   case R.id.menu_today:
                       mItemCallBack.returnToday();
                       Log.d("今天", "returnToday: ");
                       break;
               }
               return false;
           }
           public void clickNewBuilding(TextView textView, final Activity activity){
               textView.setOnClickListener(new View.OnClickListener() {
                   @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                   @Override
                   public void onClick(View v) {

                       TimeInfo begin=new TimeInfo(mYear,mMonth,mDay,mCalendar.get(Calendar.HOUR_OF_DAY),
                               mCalendar.get(Calendar.MINUTE),TimeInfo.TYPE_BEGIN);
                       TimeInfo end=new TimeInfo(mYear,mMonth,mDay,begin.getHour()+1,begin.getMinute(),
                               TimeInfo.TYPE_END);
                       Bundle bundle= ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
                       Bundle bundle1=new Bundle();
                       bundle1.putParcelable(getString(R.string.begin),begin);
                       bundle1.putParcelable(getString(R.string.end),end);
                       Intent intent=new Intent(v.getContext(),NewBuildingActivity.class);
                       intent.putExtra(getString(R.string.intent_time),bundle1);
                       startActivity(intent,bundle);
                   }
               });
           }

           @Override
           public void updatePlan(int year, int month, int day,int days) {
               mYear=year;
               mMonth=month;
               mDay=day;
           }
           public static DateRecyclerAdapter.GapDaysCallBack getInstance(){
               return mInstance;
           }





           public interface MainCallBack{
               void update(int year,int month,int day);

           }
           public interface MainItemCallBack{
               void returnToday();
           }
           public static void setOnMainActivityListener(MainCallBack callBack){
               mCallBack=callBack;
           }

           public static void setOnMainActivityItemListener(MainItemCallBack callBack){
               mItemCallBack=callBack;
           }

}
