package com.example.m.calendertwo.fragment;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lunarlib.Lunar;
import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.NewBuildingActivity;
import com.example.m.calendertwo.R;
import com.example.m.calendertwo.adapter.DateRecyclerAdapter;
import com.example.m.calendertwo.adapter.MemorRecyclerAdapter;
import com.example.m.calendertwo.database.MemoContract;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment implements DateRecyclerAdapter.GapDaysCallBack, LoaderManager.LoaderCallbacks<Cursor>,MemorRecyclerAdapter.MemorRecyclerAdapterCallBack {

    private TextView mDaysText;
    private int mYear;
    private int mMonth;
    private int mDay;
    private MemorRecyclerAdapter memorRecyclerAdapter;
    private DateInfo mDateInfo;
    private RecyclerView mMemorRecycler;
    private DateRecyclerAdapter mDateAdapter;
    private boolean mIsCreated=false;

    public MonthFragment() {
        // Required empty public constructor
    }
    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_month, container, false);
        mDateInfo= getArguments().getParcelable("date");
        mYear=mDateInfo.getYear();
        mMonth=mDateInfo.getMonth();
        mDay=mDateInfo.getDay();
        Calendar calendar=DateInfo.getCalendar(mYear,mMonth,mDay);
        Lunar lunar=new Lunar(calendar);
        RecyclerView recyclerView=view.findViewById(R.id.month_fragment_recycler);
        mMemorRecycler=view.findViewById(R.id.month_fragment_memor_recycler);
        mMemorRecycler.setItemAnimator(new DefaultItemAnimator());
        mDaysText=view.findViewById(R.id.month_fragment_distance_days);
        int days=0;
        if (DateInfo.isEquale(DateInfo.getDate(),mDateInfo))
        {
            mDaysText.setText(String.format(getString(R.string.today),
                    DateInfo.getChinaMonthString(lunar.month),
                    DateInfo.getChinaDayString(lunar.day)));
        }else if ((days=DateInfo.getGapDays(DateInfo.getDate(),mDateInfo))>0){
            mDaysText.setText(String.format(getString(R.string.day_after), days,
                    DateInfo.getChinaMonthString(lunar.month),
                    DateInfo.getChinaDayString(lunar.day)));
        }else {
            mDaysText.setText(String.format(getString(R.string.day_before), -days,
                    DateInfo.getChinaMonthString(lunar.month),
                    DateInfo.getChinaDayString(lunar.day)));
        }
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),7);
        mDateAdapter=new DateRecyclerAdapter(mDateInfo,this,days,getContext().getContentResolver());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mDateAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL,false);
        mMemorRecycler.setLayoutManager(linearLayoutManager);
        memorRecyclerAdapter=new MemorRecyclerAdapter(getActivity(),getContext().getContentResolver(),this);
        mMemorRecycler.setAdapter(memorRecyclerAdapter);
        getLoaderManager().initLoader(0,null,this);
        if(getUserVisibleHint())
            NewBuildingActivity.setOnNewBuildingInsertListener(new NewBuildingActivity.NewBuildingInsertListener() {
                @Override
                public void insertComplete(int position) {
                    mDateAdapter.notifyData(position);
                }
            });
        mIsCreated=true;
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser&&mIsCreated){
            NewBuildingActivity.setOnNewBuildingInsertListener(new NewBuildingActivity.NewBuildingInsertListener() {
                @Override
                public void insertComplete(int position) {
                    mDateAdapter.notifyData(position);
                }
            });
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    //点击不同的日期
    @Override
    public void updatePlan(int year,int month,int day,int days,String lunarMonth,String lunarDay) {
        if ( days>0){
            mDaysText.setText(String.format(getString(R.string.day_after),days,lunarMonth,lunarDay));
        }else if(days<0){
            mDaysText.setText(String.format(getString(R.string.day_before),-days,lunarMonth,lunarDay));
        }else {
            mDaysText.setText(String.format(getString(R.string.today), lunarMonth,lunarDay));
        }
        mYear=year;
        mDay=day;
        mMonth=month;
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),MemoContract.CONTENT_URI,null,
                MemoContract.COLUMN_BEGIN_TIME_YEAR+"=? and "+
                        MemoContract.COLUMN_BEGIN_TIME_MONTH+"=? and "+
                        MemoContract.COLUMN_BEGIN_TIME_DAY+"=?",
                new String[]{String.valueOf(mYear),String.valueOf(mMonth),
                        String.valueOf(mDay)}, MemoContract.COLUMN_BEGIN_TIME_HOUR+","+
                MemoContract.COLUMN_BEGIN_TIME_MINUTE+" asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        memorRecyclerAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        memorRecyclerAdapter.swapCursor(null);
    }

    @Override
    public void clearDot(int day) {
        mDateAdapter.notifyData(day);
    }
}
