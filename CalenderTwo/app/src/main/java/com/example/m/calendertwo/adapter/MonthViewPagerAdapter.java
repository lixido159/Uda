package com.example.m.calendertwo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.fragment.MonthFragment;

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
