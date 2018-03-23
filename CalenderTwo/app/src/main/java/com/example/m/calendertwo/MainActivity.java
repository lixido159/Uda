package com.example.m.calendertwo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.m.calendertwo.adapter.DateRecyclerAdapter;
import com.example.m.calendertwo.adapter.MonthViewPagerAdapter;
import com.example.m.calendertwo.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener,DateRecyclerAdapter.GapDaysCallBack{
    private int mCount;
    private ActivityMainBinding mBinding;
    private static MainActivity mInstance;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar mCalendar;
    private MainCallBack mCallBack;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ////设置statusbar的图标颜色高亮反转
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        clickNewBuilding(mBinding.mainNewBuildingText,this);
        mInstance=this;
        DateInfo mDateInfo=DateInfo.getDate();
        mBinding.mainToolBar.inflateMenu(R.menu.tool_bar_menu);
        mBinding.mainToolBar.setOnMenuItemClickListener(this);
        mBinding.mainNowMonth.setText(String.format(getString(R.string.date_title), mDateInfo.getYear(),mDateInfo.getMonth()));
        //ViewPager页数
        mCount=getResources().getInteger(R.integer.pager_count);
        MonthViewPagerAdapter adapter=new MonthViewPagerAdapter(getSupportFragmentManager(),mCount);
        mBinding.mainViewpager.setAdapter(adapter);
        mBinding.mainViewpager.setCurrentItem(mCount/2);
        mBinding.mainViewpager.setOffscreenPageLimit(1);
        mBinding.mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //fragment滑动完成,更改标题
                DateInfo dateInfo=DateInfo.getSpecialDate(position-mCount/2);
                mBinding.mainNowMonth.setText(String.format(getString(R.string.date_title), dateInfo.getYear(),dateInfo.getMonth()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mCalendar=Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT+08");
        mCalendar.setTimeZone(tz);
        mYear=mCalendar.get(Calendar.YEAR);
        mMonth=mCalendar.get(Calendar.MONTH)+1;
        mDay=mCalendar.get(Calendar.DAY_OF_MONTH);
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
                mBinding.mainViewpager.setCurrentItem(mCount/2,true);
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

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mCallBack= (MainCallBack) fragment;
    }

    public interface MainCallBack{
        void update(int year,int month,int day);
    }
}
