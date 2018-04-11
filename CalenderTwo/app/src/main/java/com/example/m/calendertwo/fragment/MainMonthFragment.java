package com.example.m.calendertwo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m.calendertwo.MainActivity;
import com.example.m.calendertwo.R;
import com.example.m.calendertwo.ViewPagerScroller;
import com.example.m.calendertwo.adapter.MonthViewPagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Field;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMonthFragment extends Fragment {
    private static MainFragmentCallBack mCallBack;
    private ViewPager mViewPager;
    public MainMonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_main, container, false);

        AdView adView=view.findViewById(R.id.main_fragment_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);


        mViewPager=view.findViewById(R.id.main_fragment_viewpager);
        try {
            ViewPagerScroller scroller=new ViewPagerScroller(getContext(),new FastOutSlowInInterpolator());
            Field field=ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(mViewPager,scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MonthViewPagerAdapter adapter=new MonthViewPagerAdapter(getFragmentManager(), MainActivity.mCount);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(MainActivity.mCount/2);
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //fragment滑动完成,更改标题
                mCallBack.selected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        MainActivity.setOnMainActivityItemListener(new MainActivity.MainItemCallBack() {
            @Override
            public void returnToday() {
                mViewPager.setCurrentItem(getResources().getInteger(R.integer.pager_count)/2,true);
            }
        });
        return view;
    }


    public interface MainFragmentCallBack{
        void selected(int position);
    }

    public static void setOnMainFragmentListener(MainFragmentCallBack callBack){
        mCallBack=callBack;
    }

}
