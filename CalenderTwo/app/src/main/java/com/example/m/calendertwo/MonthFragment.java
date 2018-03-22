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
public class MonthFragment extends Fragment implements DateRecyclerAdapter.GapDaysCallBack {

    private TextView mDaysText;
    private MemorRecyclerAdapter memorRecyclerAdapter;
    public MonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_month, container, false);
        DateInfo dateInfo= getArguments().getParcelable("date");
        RecyclerView recyclerView=view.findViewById(R.id.month_fragment_recycler);
        RecyclerView memorRecycler=view.findViewById(R.id.month_fragment_memor_recycler);
        mDaysText=view.findViewById(R.id.month_fragment_distance_days);
        int days=0;
        if (DateInfo.isEquale(DateInfo.getDate(),dateInfo))
        {
            mDaysText.setText(getString(R.string.today));
        }else if ((days=DateInfo.getGapDays(DateInfo.getDate(),dateInfo))>0){
            mDaysText.setText(String.format(getString(R.string.day_after), days));
        }else {
            mDaysText.setText(String.format(getString(R.string.day_before), -days));
        }
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),7);
        DateRecyclerAdapter adapter=new DateRecyclerAdapter(dateInfo,this,days);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL,false);
        memorRecycler.setLayoutManager(linearLayoutManager);
        memorRecyclerAdapter=new MemorRecyclerAdapter(getContext().getContentResolver());
        starQuery(dateInfo.getYear(),dateInfo.getMonth(),dateInfo.getDay(),getContext().getContentResolver());
        memorRecycler.setAdapter(memorRecyclerAdapter);
        return view;
    }
    private void starQuery(int year,int month,int day,ContentResolver contentResolver){
        MyQueryHandler queryHandler=new MyQueryHandler(contentResolver);
        queryHandler.startQuery(0,null,MemoContract.CONTENT_URI,null,
                MemoContract.COLUMN_BEGIN_TIME_YEAR+"=? and "+
                        MemoContract.COLUMN_BEGIN_TIME_MONTH+"=? and "+
                        MemoContract.COLUMN_BEGIN_TIME_DAY+"=?",
                new String[]{String.valueOf(year),String.valueOf(month),
                        String.valueOf(day)}, null);

    }
    public class MyQueryHandler extends AsyncQueryHandler{
        public MyQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            memorRecyclerAdapter.swapCursor(cursor);
            memorRecyclerAdapter.notifyDataSetChanged();


        }
    }
    @Override
    public void click(int days) {
        if ( days>0){
            mDaysText.setText(String.format(getString(R.string.day_after),days));
        }else if(days<0){
            mDaysText.setText(String.format(getString(R.string.day_before),-days));
        }else {
            mDaysText.setText(getString(R.string.today));
        }
    }

    @Override
    public void updatePlan(int year, int month, int day) {
        starQuery(year,month,day,getContext().getContentResolver());
    }


}
