package com.example.m.calendertwo;


import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.m.calendertwo.adapter.TitleIconRecyclerAdapter;
import com.example.m.calendertwo.database.MemoContract;

import java.sql.Time;
import java.text.DecimalFormat;


public class NewBuildingActivity extends AppCompatActivity implements TitleIconRecyclerAdapter.ClickCallBack, View.OnClickListener {
    private AlertDialog mDialog;
    private int[]mImages;
    private int[]mBackgrounds;
    private int mPosition;
    private String[]mTitles;
    private RelativeLayout mBeginLayout;
    private RelativeLayout mEndLayout;
    private Toolbar mToolBar;
    private ImageView mTitleIcon;
    private EditText mTitleText;
    private Activity mActivity;
    private TextView mBeginText;
    private TextView mEndText;
    private TimeInfo mBeginTime;
    private TimeInfo mEndTime;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_building);
        enterTransiton();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
        mActivity=this;
        mBeginLayout.setTag(getString(R.string.begin));
        mBeginLayout.setOnClickListener(this);
        mEndLayout.setTag(getString(R.string.end));
        mEndLayout.setOnClickListener(this);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBar.setNavigationIcon(getDrawable(R.drawable.close));
        mToolBar.inflateMenu(R.menu.newbuilding_tool_bar_menu);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.new_building_toolbar_menu_check){
                    starInsert();
                    Intent intent=NavUtils.getParentActivityIntent(NewBuildingActivity.this);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable(getString(R.string.begin),mBeginTime);
                    intent.putExtra(getString(R.string.intent_time),bundle);
                    NavUtils.navigateUpTo(NewBuildingActivity.this,intent);
                }
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View view= View.inflate(this,R.layout.title_icon,null);
        RecyclerView recyclerView=view.findViewById(R.id.window_title_icon_recycler);
        AlertDialog.Builder builder=new AlertDialog.Builder(NewBuildingActivity.this);
        GridLayoutManager manager=new GridLayoutManager(this,5);
        TitleIconRecyclerAdapter adapter=new TitleIconRecyclerAdapter(mImages,mBackgrounds,this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        mDialog=builder.create();
        Window window=mDialog.getWindow();
        window.setWindowAnimations(R.style.dialogAnimation);
        mTitleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enterTransiton(){
        Slide slide=new Slide();
        slide.setSlideEdge(Gravity.BOTTOM);
        slide.setDuration(getResources().getInteger(R.integer.animation_time));
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.interpolator.fast_out_slow_in));
        getWindow().setEnterTransition(slide);
    }
    public void init(){
        mBeginLayout=findViewById(R.id.new_building_begin_layout);
        mEndLayout=findViewById(R.id.new_building_end_layout);
        mToolBar=findViewById(R.id.new_building_toolbar);
        mTitleIcon=findViewById(R.id.new_building_title_icon);
        mTitleText=findViewById(R.id.new_building_title_text);
        mBeginText=findViewById(R.id.new_building_begin_text);
        mEndText=findViewById(R.id.new_building_end_text);
        mBackgrounds=new int[]{0,R.drawable.icon_meeting_circle,R.drawable.icon_eating_circle,
                R.drawable.icon_plane_circle,R.drawable.icon_train_circle,R.drawable.icon_cafe_circle,
                R.drawable.icon_gift_circle,R.drawable.icon_shopping_circle,R.drawable.icon_movie_circle,
                R.drawable.icon_run_circle,R.drawable.icon_cut_circle,R.drawable.icon_car_circle,
                R.drawable.icon_money_circle,R.drawable.icon_bicycle_circle,R.drawable.icon_draw_circle};
        mImages=new int[]{R.drawable.plus_circle_unpressed,R.drawable.icon_meeting,R.drawable.icon_eating,
                R.drawable.icon_plane,R.drawable.icon_train,R.drawable.icon_cafe,
                R.drawable.icon_gift,R.drawable.icon_shopping,R.drawable.icon_movie,
                R.drawable.icon_run,R.drawable.icon_cut,R.drawable.icon_car,
                R.drawable.icon_money,R.drawable.icon_bicycle,R.drawable.icon_draw};
        mTitles= getResources().getStringArray(R.array.title_icons_text);
        Bundle bundle=getIntent().getBundleExtra(getString(R.string.intent_time));
        mBeginTime=bundle.getParcelable(getString(R.string.begin));
        mEndTime=bundle.getParcelable(getString(R.string.end));
        DecimalFormat format=new DecimalFormat("00");
        if (!TimeInfo.isAtSameDay(mBeginTime,mEndTime)){
            mEndText.setText(String.format(getString(R.string.new_building_time), mEndTime.getYear(),
                    mEndTime.getMonth(),mEndTime.getDay(),
                    format.format(mEndTime.getHour()), format.format(mEndTime.getMinute())));
        }else{
            mEndText.setText(String.format(getString(R.string.new_building_hour_minute),
                    format.format(mEndTime.getHour()), format.format(mEndTime.getMinute())));
        }
        mBeginText.setText(String.format(getString(R.string.new_building_time), mBeginTime.getYear(),
                mBeginTime.getMonth(),mBeginTime.getDay(),
                format.format(mBeginTime.getHour()), format.format(mBeginTime.getMinute())));
    }

    @Override
    public void clickIcon(int position) {
        mDialog.dismiss();
        mTitleIcon.setImageResource(mImages[position]);
        mTitleIcon.setBackgroundResource(mBackgrounds[position]);
        if (position==0){
             mTitleText.setHint(getString(R.string.title));
             mTitleText.setText("");
        }else {
             mTitleText.setText(mTitles[position-1]);
             mTitleText.setSelection(mTitles[position-1].length());
        }
        mPosition=position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Bundle bundle= ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity).toBundle();
        Intent intent=new Intent(v.getContext(),TimeSetActivity.class);
        if (String.valueOf(v.getTag()).equals(getString(R.string.begin))){
            bundle.putInt(getString(R.string.new_building_type), TimeInfo.TYPE_BEGIN);
        }else {
            bundle.putInt(getString(R.string.new_building_type), TimeInfo.TYPE_END);
        }
        bundle.putParcelable(getString(R.string.new_building_begin), mBeginTime);
        bundle.putParcelable(getString(R.string.new_building_end), mEndTime);
        intent.putExtra(getString(R.string.intent_time),bundle);
        startActivity(intent,bundle);
    }


    public void setIntent(Intent intent) {
        Bundle bundle=intent.getBundleExtra(getString(R.string.intent_time));
        mBeginTime = bundle.getParcelable(getString(R.string.new_building_begin));
        mEndTime = bundle.getParcelable(getString(R.string.new_building_end));
        //结束日期在开始日期之前
        if (TimeInfo.isSmaller(mBeginTime,mEndTime)) {
            mEndTime = new TimeInfo(mBeginTime.getYear(), mBeginTime.getMonth(),
                    mBeginTime.getDay(), mBeginTime.getHour(), mBeginTime.getMinute(),
                    TimeInfo.TYPE_END);
        }
        DecimalFormat format=new DecimalFormat("00");
        //不是同一天
        if (!TimeInfo.isAtSameDay(mBeginTime,mEndTime)){
            mEndText.setText(String.format(getString(R.string.new_building_time), mEndTime.getYear(),
                    mEndTime.getMonth(),mEndTime.getDay(),
                    format.format(mEndTime.getHour()), format.format(mEndTime.getMinute())));
        }else{//在同一天，结束时间只显示小时分钟
            mEndText.setText(String.format(getString(R.string.new_building_hour_minute),
                    format.format(mEndTime.getHour()), format.format(mEndTime.getMinute())));
        }
        mBeginText.setText(String.format(getString(R.string.new_building_time), mBeginTime.getYear(),
                mBeginTime.getMonth(),mBeginTime.getDay(),
                format.format(mBeginTime.getHour()), format.format(mBeginTime.getMinute())));

    }
    private void starInsert(){
        AsyncQueryHandler handler=new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected Handler createHandler(Looper looper) {
                return super.createHandler(looper);
            }
        };
        ContentValues values=new ContentValues();
        values.put(MemoContract.COLUMN_PLAN,mTitleText.getText().toString());
        values.put(MemoContract.COLUMN_ICON_IMG,mImages[mPosition]);
        values.put(MemoContract.COLUMN_ICON_BACK,mBackgrounds[mPosition]);

        values.put(MemoContract.COLUMN_BEGIN_TIME_YEAR,mBeginTime.getYear());
        values.put(MemoContract.COLUMN_BEGIN_TIME_MONTH,mBeginTime.getMonth());
        values.put(MemoContract.COLUMN_BEGIN_TIME_DAY,mBeginTime.getDay());
        values.put(MemoContract.COLUMN_BEGIN_TIME_HOUR,mBeginTime.getHour());
        values.put(MemoContract.COLUMN_BEGIN_TIME_MINUTE,mBeginTime.getMinute());

        values.put(MemoContract.COLUMN_END_TIME_YEAR,mEndTime.getYear());
        values.put(MemoContract.COLUMN_END_TIME_MONTH,mEndTime.getMonth());
        values.put(MemoContract.COLUMN_END_TIME_DAY,mEndTime.getDay());
        values.put(MemoContract.COLUMN_END_TIME_HOUR,mEndTime.getHour());
        values.put(MemoContract.COLUMN_END_TIME_MINUTE,mEndTime.getMinute());
        handler.startInsert(0,null,MemoContract.CONTENT_URI,values);
    }



}

