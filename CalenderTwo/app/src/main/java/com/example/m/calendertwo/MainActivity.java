package com.example.m.calendertwo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.m.calendertwo.adapter.MonthViewPagerAdapter;
import com.example.m.calendertwo.databinding.ActivityMainBinding;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private int mCount;
    private ActivityMainBinding mBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////设置statusbar的图标颜色高亮反转
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        clickNewBuilding(mBinding.mainNewBuildingText,this);

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
                Bundle bundle= ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
                Intent intent=new Intent(v.getContext(),NewBuildingActivity.class);
                startActivity(intent,bundle);
                getWindow().setExitTransition(null);
            }
        });
    }
}
