package com.example.m.calendertwo.adapter;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.R;

import org.w3c.dom.Text;

/**
 * Created by m on 2018/3/11.
 */

public class DateRecyclerAdapter extends RecyclerView.Adapter<DateRecyclerAdapter.ViewHolder> {
    public DateInfo mDateInfo;
    private ViewHolder mOldViewHolder;
    private int mOldPosition;
    private int mRightPosition;
    private int mOriginalPosition;
    private boolean isFirst=true;
    private GapDaysCallBack callBack;
    private int mGapDay;
    public DateRecyclerAdapter(DateInfo mDateInfo, GapDaysCallBack callBack,int mGapDay) {
        this.mDateInfo = mDateInfo;
        this.callBack=callBack;
        this.mGapDay=mGapDay;
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
        holder.textView.setText(mRightPosition+"");
        //如果正好是今天的日
        if (mRightPosition==mDateInfo.getDay()){
            mOldPosition=mRightPosition;
            holder.revealView.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(holder.parentView.getContext().getColor(R.color.white));
            mOldViewHolder=holder;
            if (isFirst){
                mOriginalPosition=mRightPosition;
                isFirst=false;
            }
        }
        holder.parentView.setOnClickListener(new RevealViewOnClickListener(mRightPosition,holder));
    }

    @Override
    public int getItemCount() {
        return DateInfo.getDays(mDateInfo)+mDateInfo.getFirstDayOfMonth();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        View parentView;
        View revealView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.date_recycler_item);
            parentView=itemView;
            revealView=itemView.findViewById(R.id.view_recycler_item);
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
            callBack.click(mGapDay+mPosition-mOriginalPosition);
            callBack.updatePlan(mDateInfo.getYear(),mDateInfo.getMonth(),mPosition);
            mOldPosition=mPosition;
            mOldViewHolder=mViewHolder;
        }
    }

    public interface GapDaysCallBack{
        void click(int days);
        void updatePlan(int year,int month,int day);
    }
}
