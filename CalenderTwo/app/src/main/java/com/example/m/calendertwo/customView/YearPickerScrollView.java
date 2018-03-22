package com.example.m.calendertwo.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.m.calendertwo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by m on 2018/3/16.
 */

public class YearPickerScrollView extends View {
    private int mTextMaxSize=60;
    private int mTextMinSize=50;
    private int mTextColor;
    private int mTxtSelectedColor;
    private int mViewWidth;
    private int mViewHeight;
    private List<Integer> mData;
    private float mLastDownY;//手指落点位置
    private float mMoveLength;//手指移动的距离
    private int mDistance=200;  //数字上下的距离
    private Paint mPaint;
    private int mSelected;
    private int mSpead=8;
    private MyTimeTask myTimeTask;
    private Timer mTimer;
    private YearChangeCallBack mYearCallBack;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLength)<mSpead){
                mMoveLength=0;
                if (myTimeTask!=null){
                    myTimeTask.cancel();
                    myTimeTask=null;
                }
            }else {
                mMoveLength=mMoveLength-Math.abs(mMoveLength)/mMoveLength*mSpead;
            }

            invalidate();
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    public YearPickerScrollView(Context context) {
        super(context);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public YearPickerScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public YearPickerScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawData(canvas);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth=getMeasuredWidth();
        mViewHeight=getMeasuredHeight();
        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN://按下手指
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE://移动手指
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawData(Canvas canvas){
        mPaint.setTextSize(mTextMaxSize);
        mPaint.setAlpha(120);
        mPaint.setColor(mTxtSelectedColor);
        Paint.FontMetricsInt fmi=mPaint.getFontMetricsInt();
        float x= (float)(mViewWidth/2.0);
        float y=(float)(mViewHeight/2.0)+mMoveLength+mDistance/8;
        canvas.drawText(mData.get(mSelected)+"年",x,y,mPaint);
        //绘制下面的数字
        if (mSelected<mData.size()-1)
                drawOtherData(canvas,mSelected+1,mSelected,1);
        //绘制上面的
        if (mSelected>0)
                drawOtherData(canvas,mSelected-1,mSelected,-1);
        drawCircle(canvas);

    }
    private void drawOtherData(Canvas canvas,int position,int originalPosition,int type){
        mPaint.setTextSize(mTextMinSize);
        mPaint.setColor(mTextColor);
        Paint.FontMetricsInt fmi=mPaint.getFontMetricsInt();
        float x= (float)(mViewWidth/2.0);
        float y=(float)(mViewHeight/2.0)+mMoveLength+type*mDistance*Math.abs(originalPosition-position);
        canvas.drawText(mData.get(position)+"年",x,y,mPaint);
    }

    private void doDown(MotionEvent event){
        if (myTimeTask!=null){
            myTimeTask.cancel();
            myTimeTask=null;
        }
        mLastDownY=event.getY();
    }
    private void doMove(MotionEvent event){
        //>0,向下移动;<0,向上移动
        mMoveLength+=event.getY()-mLastDownY;
        //如果到达顶点还在往下滑
        if(mSelected==0&&mMoveLength>mDistance/3){
            mMoveLength=mDistance/3;
        }else if (mSelected==mData.size()-1&&mMoveLength<-mDistance/3){
            mMoveLength=-mDistance/3;
        }
        //如果下滑超过离开距离
        if (mMoveLength>mDistance/2){
            mSelected-=1;
            mMoveLength-=mDistance;
            mYearCallBack.yearChange(mData.get(mSelected));
        }
        if (mMoveLength<-mDistance/2){
            mSelected+=1;
            mMoveLength+=mDistance;
            mYearCallBack.yearChange(mData.get(mSelected));
        }
        if(mSelected<=0){
            mSelected=0;
        }else if (mSelected>=mData.size()-1){
            mSelected=mData.size()-1;
        }

        mLastDownY=event.getY();
        invalidate();


    }
    private void doUp(MotionEvent event){

        myTimeTask=new MyTimeTask(mHandler);
        mTimer.schedule(myTimeTask,0,10);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//是使位图抗锯齿的标志
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTimer=new Timer();
        mTxtSelectedColor=getContext().getColor(R.color.black);
        mTextColor=getContext().getColor(R.color.text_selected_color);
    }
    public void initData(List<Integer> list,int selected,YearChangeCallBack mYearCallBack){
        mData=list;
        mSelected=list.indexOf(selected);
        this.mYearCallBack=mYearCallBack;
        invalidate();
    }
    public void refreshData(int selected){
        mSelected=mData.indexOf(selected);
        invalidate();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawCircle(Canvas canvas){
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getContext().getColor(R.color.scroll_selector_circle_color));
        canvas.drawCircle(mViewWidth/2,mViewHeight/2,mDistance/2,mPaint);
    }
    public int getSelectedData(){
        return mData.get(mSelected);
    }

    public interface YearChangeCallBack{
        void yearChange(int year);
    }
    class MyTimeTask extends TimerTask{
        Handler handler;

        public MyTimeTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }
}
