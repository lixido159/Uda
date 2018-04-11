package com.example.m.calendertwo.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.m.calendertwo.NewBuildingActivity;
import com.example.m.calendertwo.R;
import com.example.m.calendertwo.TimeInfo;
import com.example.m.calendertwo.database.MemoContract;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 2018/3/13.
 */
//每个日期下面的安排事项
public class MemorRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cursor mCursor;
    private List<TimeInfo> mBeginList=new ArrayList<>();
    private List<TimeInfo> mEndList=new ArrayList<>();
    private List<String> mPlanList=new ArrayList<>();
    private List<Integer> mIdList=new ArrayList<>();
    private Activity mActivity;
    private float mOriginalX;
    private float mOriginalY;
    private boolean isFirst=true;
    private boolean isHorizontal;
    private ContentResolver mContentResolver;
    private MemorRecyclerAdapterCallBack mCallBack;
    private final int EMPTY_VIEW=45;
    public MemorRecyclerAdapter(Activity activity,ContentResolver contentResolver,
                                MemorRecyclerAdapterCallBack callBack) {
        mActivity=activity;
        mContentResolver=contentResolver;
        mCallBack=callBack;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==EMPTY_VIEW){
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memor_empty,
                    parent,false);
            return new EmptyViewHolder(view);
        }
        else{
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memor_recycler,
                    parent,false);
            return new ViewHolder(view);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder newHolder, final int position) {
        if (newHolder instanceof MemorRecyclerAdapter.EmptyViewHolder){
            return;
        }else {
            ViewHolder holder= (ViewHolder) newHolder;
            mCursor.moveToPosition(position);
            mBeginList.add(position,new TimeInfo(mCursor.getInt(MemoContract.INDEX_BEGIN_TIME_YEAR),
                    mCursor.getInt(MemoContract.INDEX_BEGIN_TIME_MONTH),
                    mCursor.getInt(MemoContract.INDEX_BEGIN_TIME_DAY),
                    mCursor.getInt(MemoContract.INDEX_BEGIN_TIME_HOUR),
                    mCursor.getInt(MemoContract.INDEX_BEGIN_TIME_MINUTE),
                    TimeInfo.TYPE_BEGIN));
            mEndList.add(position,new TimeInfo(mCursor.getInt(MemoContract.INDEX_END_TIME_YEAR),
                    mCursor.getInt(MemoContract.INDEX_END_TIME_MONTH),
                    mCursor.getInt(MemoContract.INDEX_END_TIME_DAY),
                    mCursor.getInt(MemoContract.INDEX_END_TIME_HOUR),
                    mCursor.getInt(MemoContract.INDEX_END_TIME_MINUTE),
                    TimeInfo.TYPE_BEGIN));
            mPlanList.add(position,mCursor.getString(MemoContract.INDEX_PLAN));
            mIdList.add(position,mCursor.getInt(MemoContract.INDEX_ID));
            DecimalFormat format=new DecimalFormat("00");
            holder.timeText.setText(String.format(holder.timeText.getContext().getString(R.string.new_building_hour_minute),
                    format.format(mBeginList.get(position).getHour()),
                    format.format(mBeginList.get(position).getMinute())));
            holder.planText.setText(mPlanList.get(position));
            holder.linearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((event.getEventTime()-event.getDownTime())<=300&&
                            event.getActionMasked()==MotionEvent.ACTION_UP&&
                            event.getRawX()==mOriginalX&&event.getRawY()==mOriginalY)
                    {
                        startActivity(v,position);
                        return true;
                    }

                    switch (event.getActionMasked()){
                        case MotionEvent.ACTION_DOWN:
                            mOriginalX=event.getRawX();
                            mOriginalY=event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isFirst){
                                //上下移动
                                if (Math.abs(event.getRawX()-mOriginalX)<= Math.abs(event.getRawY()-mOriginalY)){
                                    isHorizontal=false;
                                }else {
                                    isHorizontal=true;
                                }
                                isFirst=false;
                            }
                            if (isHorizontal)
                                move(event.getRawX()-mOriginalX,v);
                            break;
                        case MotionEvent.ACTION_UP:
                            up(event.getRawX()-mOriginalX,v,position);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            up(event.getRawX()-mOriginalX,v,position);
                            break;
                    }
                    return true;
                }
            });
            holder.linearLayout.requestDisallowInterceptTouchEvent(true);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void startActivity(View v, int position){
        Bundle animateBundle= ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity).toBundle();
        Bundle bundle1=new Bundle();
        bundle1.putParcelable(v.getResources().getString(R.string.begin),mBeginList.get(position));
        bundle1.putParcelable(v.getResources().getString(R.string.end),mEndList.get(position));
        bundle1.putString(v.getResources().getString(R.string.bundle_plan),mPlanList.get(position));
        Intent intent=new Intent(v.getContext(),NewBuildingActivity.class);
        intent.setAction(v.getResources().getString(R.string.memor));
        intent.putExtra(v.getResources().getString(R.string.intent_time),bundle1);
        v.getContext().startActivity(intent,animateBundle);
    }

    @Override
    public int getItemCount() {
        if (mCursor!=null&&mCursor.getCount()!=0){
            return mCursor.getCount();
        }
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCursor!=null&&mCursor.getCount()!=0){
            return super.getItemViewType(position);
        }else {
            return EMPTY_VIEW;
        }

    }

    public void swapCursor(Cursor cursor){
        mCursor=cursor;
        notifyDataSetChanged();
    }
    public void delete(Cursor cursor){
        if (mCursor!=null&&mCursor.getCount()!=0&&cursor.getCount()==0){
            mCallBack.clearDot(mBeginList.get(0).getDay());

        }
        mCursor=cursor;
        notifyDataSetChanged();
    }
    public void move(float moveLength,View view){
        if (moveLength>0){
            view.getParent().requestDisallowInterceptTouchEvent(true);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) view.getLayoutParams();
            params.leftMargin= (int) moveLength;
            view.setLayoutParams(params);
        }

    }
    public void up(float moveLength,View view,int position){
        if (moveLength>=view.getWidth()/2){
            mContentResolver.delete(MemoContract.CONTENT_URI,
                    MemoContract._ID+"=?  ",
                    new String[]{String.valueOf(mIdList.get(position))});
            delete(mContentResolver.query(MemoContract.CONTENT_URI,null,
                    MemoContract.COLUMN_BEGIN_TIME_YEAR+"=? and "+
                            MemoContract.COLUMN_BEGIN_TIME_MONTH+"=? and "+
                            MemoContract.COLUMN_BEGIN_TIME_DAY+"=?",
                    new String[]{String.valueOf(mBeginList.get(position).getYear()),
                            String.valueOf(mBeginList.get(position).getMonth()),
                            String.valueOf(mBeginList.get(position).getDay())},
                    MemoContract.COLUMN_BEGIN_TIME_HOUR+","+
                            MemoContract.COLUMN_BEGIN_TIME_MINUTE+" asc"));
        }
        RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) view.getLayoutParams();
        params.leftMargin=0;
        view.setLayoutParams(params);
        isFirst=true;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView planText;
        public TextView timeText;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            planText=itemView.findViewById(R.id.plan_memor_recycler_item);
            timeText=itemView.findViewById(R.id.time_memor_recycler_item);
            linearLayout=itemView.findViewById(R.id.time_memor_linear);
        }
    }
    public class EmptyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public EmptyViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.item_memor_empty_text);
        }
    }
    public interface MemorRecyclerAdapterCallBack{
        void clearDot(int day);
    }
}
