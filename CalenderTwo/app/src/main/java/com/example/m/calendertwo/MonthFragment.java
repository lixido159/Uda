package com.example.m.calendertwo;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m.calendertwo.adapter.DateRecyclerAdapter;
import com.example.m.calendertwo.adapter.MemorRecyclerAdapter;
import com.example.m.calendertwo.database.MemoContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment implements DateRecyclerAdapter.GapDaysCallBack,MainActivity.MainCallBack {

    private TextView mDaysText;
    private int mYear;
    private int mMonth;
    private int mDay;
    private MemorRecyclerAdapter memorRecyclerAdapter;
    DateInfo mDateInfo;
    public MonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_month, container, false);
        mDateInfo= getArguments().getParcelable("date");
        mYear=mDateInfo.getYear();
        mMonth=mDateInfo.getMonth();
        mDay=mDateInfo.getDay();
        RecyclerView recyclerView=view.findViewById(R.id.month_fragment_recycler);
        RecyclerView memorRecycler=view.findViewById(R.id.month_fragment_memor_recycler);
        mDaysText=view.findViewById(R.id.month_fragment_distance_days);
        int days=0;
        if (DateInfo.isEquale(DateInfo.getDate(),mDateInfo))
        {
            mDaysText.setText(getString(R.string.today));
        }else if ((days=DateInfo.getGapDays(DateInfo.getDate(),mDateInfo))>0){
            mDaysText.setText(String.format(getString(R.string.day_after), days));
        }else {
            mDaysText.setText(String.format(getString(R.string.day_before), -days));
        }
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),7);
        DateRecyclerAdapter adapter=new DateRecyclerAdapter(mDateInfo,this,days);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL,false);
        memorRecycler.setLayoutManager(linearLayoutManager);
        memorRecyclerAdapter=new MemorRecyclerAdapter(getContext().getContentResolver());
        starQuery(mYear,mMonth,mDay,getContext().getContentResolver());
        memorRecycler.setAdapter(memorRecyclerAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void starQuery(int year, int month, int day, ContentResolver contentResolver){
        MyQueryHandler queryHandler=new MyQueryHandler(contentResolver);
        queryHandler.startQuery(0,null,MemoContract.CONTENT_URI,null,
                MemoContract.COLUMN_BEGIN_TIME_YEAR+"=? and "+
                        MemoContract.COLUMN_BEGIN_TIME_MONTH+"=? and "+
                        MemoContract.COLUMN_BEGIN_TIME_DAY+"=?",
                new String[]{String.valueOf(year),String.valueOf(month),
                        String.valueOf(day)}, null);

    }

    //从新建页面点击确认回来
    @Override
    public void update(int year, int month, int day) {
        mYear=year;
        mMonth=month;
        mDay=day;
        starQuery(mYear,mMonth,mDay,getContext().getContentResolver());
    }

    public class MyQueryHandler extends AsyncQueryHandler{
        public MyQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            memorRecyclerAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    //点击不同的日期
    @Override
    public void updatePlan(int year, int month, int day,int days) {
        starQuery(year,month,day,getContext().getContentResolver());
        if ( days>0){
            mDaysText.setText(String.format(getString(R.string.day_after),days));
        }else if(days<0){
            mDaysText.setText(String.format(getString(R.string.day_before),-days));
        }else {
            mDaysText.setText(getString(R.string.today));
        }
    }


}
