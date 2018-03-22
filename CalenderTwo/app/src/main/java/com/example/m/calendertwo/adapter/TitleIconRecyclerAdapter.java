package com.example.m.calendertwo.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.m.calendertwo.R;

/**
 * Created by m on 2018/3/18.
 */

public class TitleIconRecyclerAdapter extends RecyclerView.Adapter<TitleIconRecyclerAdapter.ViewHolder>{
    private int[]mImages;
    private int[]mBackgrounds;
    private ClickCallBack mClickCallBack;
    public TitleIconRecyclerAdapter(int[] mImages, int[] mBackgrounds,ClickCallBack mClickCallBack) {
        this.mImages = mImages;
        this.mBackgrounds = mBackgrounds;
        this.mClickCallBack=mClickCallBack;
    }

    @Override
    public TitleIconRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon,
                parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(TitleIconRecyclerAdapter.ViewHolder holder, final int position) {
        holder.imageView.setImageResource(mImages[position]);
        holder.imageView.setBackgroundResource(mBackgrounds[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickCallBack.clickIcon(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_icon_img);
        }
    }
    public interface ClickCallBack{
        void clickIcon(int position);
    }
}
