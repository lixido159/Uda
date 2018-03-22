package com.example.m.calendertwo.adapter;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.m.calendertwo.DateInfo;
import com.example.m.calendertwo.R;
import com.example.m.calendertwo.database.MemoContract;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

/**
 * Created by m on 2018/3/13.
 */

public class MemorRecyclerAdapter extends RecyclerView.Adapter<MemorRecyclerAdapter.ViewHolder> {
    private Cursor mCursor;

    public MemorRecyclerAdapter(ContentResolver mResolver) {
        this.mResolver = mResolver;
    }

    private ContentResolver mResolver;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memor_recycler,
                parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        while (mCursor.moveToNext()){
            DecimalFormat format=new DecimalFormat("00");
            holder.timeText.setText(String.format(holder.timeText.getContext().getString(R.string.new_building_hour_minute),
                    format.format(mCursor.getInt(MemoContract.INDEX_BEGIN_TIME_HOUR)),
                    format.format(mCursor.getInt(MemoContract.INDEX_BEGIN_TIME_MINUTE))));
            holder.planText.setText(mCursor.getString(MemoContract.INDEX_PLAN));
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }
    public void swapCursor(Cursor cursor){
        mCursor=cursor;
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView planText;
        public TextView timeText;
        public ViewHolder(View itemView) {
            super(itemView);
            planText=itemView.findViewById(R.id.plan_memor_recycler_item);
            timeText=itemView.findViewById(R.id.time_memor_recycler_item);
        }
    }


}
