package com.example.xyzreader.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by m on 2018/3/2.
 */

public class ThreeTwoImageView extends android.support.v7.widget.AppCompatImageView {
    private float mAspectRatio = 1.5f;
    public ThreeTwoImageView(Context context) {
        super(context);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        //width>=height
        if (width>=height) {
           height=width*2/3;
        }
        //width<=height
        else {
            width=height*2/3;
        }
        int widthSpec=MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        int heightSpc=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec,heightSpc);
    }
    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        requestLayout();
    }

}
