package com.example.m.calendertwo.adapter;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.MonthFragment;

/**
 * Created by m on 2018/3/12.
 */

public class MonthViewPagerAdapter extends FragmentStatePagerAdapter {
    private int mCount;
    public MonthViewPagerAdapter(FragmentManager fm,int count) {
        super(fm);
        mCount=count;
    }
    @Override
    public Fragment getItem(int position) {
        MonthFragment monthFragment=new MonthFragment();
        Bundle bundle=new Bundle();
        DateInfo dateInfo;
        if (position==mCount/2){
            dateInfo=DateInfo.getDate();
        }else {
            dateInfo=DateInfo.getSpecialDate(position-mCount/2);
        }
        bundle.putParcelable("date",dateInfo);
        monthFragment.setArguments(bundle);
        return monthFragment;
    }

    @Override
    public int getCount() {
        return mCount;
    }
}
