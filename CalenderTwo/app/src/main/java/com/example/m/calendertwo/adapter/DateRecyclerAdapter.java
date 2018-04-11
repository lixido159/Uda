package com.example.m.calendertwo.adapter;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lunarlib.Lunar;
import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.Festival;
import com.example.m.calendertwo.MainActivity;
import com.example.m.calendertwo.R;
import com.example.m.calendertwo.database.MemoContract;
import com.example.m.calendertwo.widget.LunarInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by m on 2018/3/11.
 */

public class DateRecyclerAdapter extends RecyclerView.Adapter<DateRecyclerAdapter.ViewHolder> {
    public DateInfo mDateInfo;
    private ViewHolder mOldViewHolder;
    private int mOldPosition;
    private int mRightPosition;
    private int mOriginalPosition;
    private int mUpdatePosition=-1;
    private boolean isFirst=true;
    private GapDaysCallBack callBack;
    private GapDaysCallBack mainCallBack;
    private int mGapDay;
    private int[]mScheduledDay;
    private ContentResolver mContentResolver;
    private List<LunarInfo>mLunarList=new ArrayList<>();
    public DateRecyclerAdapter(DateInfo mDateInfo, GapDaysCallBack callBack, int mGapDay,ContentResolver contentResolver) {
        this.mDateInfo = mDateInfo;
        this.callBack=callBack;
        this.mGapDay=mGapDay;
        mainCallBack=MainActivity.getInstance();
        mContentResolver=contentResolver;
        dotUpdate();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        if (position<mDateInfo.getFirstDayOfMonth()){
            return;
        }
        mRightPosition=position-mDateInfo.getFirstDayOfMonth()+1;
        Calendar calendar=DateInfo.getCalendar(mDateInfo.getYear(),mDateInfo.getMonth(),mRightPosition);
        Lunar lunar=new Lunar(calendar);
        mLunarList.add(mRightPosition-1,new LunarInfo(lunar.month, lunar.day));
        holder.textView.setText(mRightPosition+"");
        holder.lunarText.setText(Festival.getFestival(lunar.month,lunar.day,
                mDateInfo.getYear(),mDateInfo.getMonth(),mRightPosition));
        //如果正好是今天的日
        if (mRightPosition==mDateInfo.getDay()){
            mOldPosition=mRightPosition;
            holder.revealView.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(holder.parentView.getContext().getColor(R.color.white));
            holder.lunarText.setTextColor(holder.parentView.getContext().getColor(R.color.white));
            mOldViewHolder=holder;
            if (isFirst){
                mOriginalPosition=mRightPosition;
                isFirst=false;
            }
        }
        if (mScheduledDay!=null){
            if (holder.imageView.getVisibility()==View.GONE){
                for (int i:mScheduledDay)
                {
                    if (i==mRightPosition){
                        holder.imageView.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }else {
                boolean isEmpty=true;
                for (int i:mScheduledDay)
                {
                    Log.v(getClass().getSimpleName(),i+"");
                    if (i==mRightPosition)
                        isEmpty=false;
                }
                if (isEmpty)
                    holder.imageView.setVisibility(View.GONE);
            }
        }else {
            if (holder.imageView.getVisibility()!=View.GONE)
                holder.imageView.setVisibility(View.GONE);
        }
        holder.parentView.setOnClickListener(new RevealViewOnClickListener(mRightPosition,holder));
        if (mUpdatePosition==position){
            holder.revealView.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(holder.parentView.getContext().getColor(R.color.white));
            holder.lunarText.setTextColor(holder.parentView.getContext().getColor(R.color.white));
            mOldViewHolder=holder;
        }
    }

    @Override
    public int getItemCount() {
        return DateInfo.getDays(mDateInfo)+mDateInfo.getFirstDayOfMonth();
    }

    public void notifyData(int position){
        dotUpdate();
        mUpdatePosition=position+mDateInfo.getFirstDayOfMonth()-1;
        notifyItemChanged(mUpdatePosition);

    }
    public void dotUpdate(){
        Cursor cursor=mContentResolver.query(MemoContract.CONTENT_URI,
                new String[]{MemoContract.COLUMN_BEGIN_TIME_DAY},
                MemoContract.COLUMN_BEGIN_TIME_YEAR+"=? and "+
                        MemoContract.COLUMN_BEGIN_TIME_MONTH+"=?",
                new String[]{String.valueOf(mDateInfo.getYear()),String.valueOf(mDateInfo.getMonth())},
                MemoContract.COLUMN_BEGIN_TIME_DAY+" asc");
        int[]days;
        if (cursor.getCount()!=0){
            days=new int[cursor.getCount()];
            int index=cursor.getColumnIndex(MemoContract.COLUMN_BEGIN_TIME_DAY);
            while (cursor.moveToNext()){
                days[cursor.getPosition()]=cursor.getInt(index);
            }
            mScheduledDay=days;
        }else {
            mScheduledDay=null;
        }

    }



    public class RevealViewOnClickListener implements View.OnClickListener {
        private int mPosition;
        private ViewHolder mViewHolder;
        public RevealViewOnClickListener(int position,ViewHolder viewHolder) {
            mPosition=position;
            mViewHolder=viewHolder;
        }

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            //重复点击
            if (mPosition==mOldPosition){
                return;
            }
            mViewHolder.revealView.setVisibility(View.VISIBLE);
            if (mPosition==mDateInfo.getDay()&&mOldPosition!=mDateInfo.getDay()){
                mViewHolder.revealView.setBackground(mViewHolder.revealView.getResources().getDrawable(R.drawable.circle_background));
            }
            mViewHolder.textView.setTextColor(mViewHolder.parentView.getContext().getColor(R.color.white));
            mViewHolder.lunarText.setTextColor(mViewHolder.parentView.getContext().getColor(R.color.white));
            //动画
            Animator animator= ViewAnimationUtils.createCircularReveal(mViewHolder.revealView,
                    mViewHolder.revealView.getWidth()/2, mViewHolder.revealView.getHeight()/2,
                    0,mViewHolder.revealView.getWidth()/2);
            animator.setDuration(300);
            animator.start();
            //如果之前的位置是今天的位置,并且传入的日期与今天相等
            if (mOldPosition==mDateInfo.getDay()&&DateInfo.isEquale(mDateInfo,DateInfo.getDate())){
                mOldViewHolder.revealView.setBackground(mViewHolder.parentView.getResources().getDrawable(R.drawable.empty_circle));
            }else {
                mOldViewHolder.revealView.setVisibility(View.INVISIBLE);
            }
            mOldViewHolder.textView.setTextColor(mOldViewHolder.parentView.getContext().getColor(R.color.black));
            mOldViewHolder.lunarText.setTextColor(mOldViewHolder.parentView.getContext().getColor(R.color.text_color));
            callBack.updatePlan(mDateInfo.getYear(),mDateInfo.getMonth(),mPosition,
                    mGapDay+mPosition-mOriginalPosition,
                    DateInfo.getChinaMonthString( mLunarList.get(mPosition-1).getMonth()),
                    DateInfo.getChinaDayString(mLunarList.get(mPosition-1).getDay()));
            mainCallBack.updatePlan(mDateInfo.getYear(),mDateInfo.getMonth(),mPosition,
                    mGapDay+mPosition-mOriginalPosition,null,null);
            mOldPosition=mPosition;
            mOldViewHolder=mViewHolder;
        }
    }
    public interface GapDaysCallBack{
        void updatePlan(int year,int month,int day,int days,String lunarMonth,String lunarDay);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView lunarText;
        ImageView imageView;
        View parentView;
        View revealView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.date_recycler_item);
            parentView=itemView;
            revealView=itemView.findViewById(R.id.view_recycler_item);
            imageView=itemView.findViewById(R.id.date_recycler_img);
            lunarText=itemView.findViewById(R.id.lunar_date_recycler_item);
        }
    }


    }



