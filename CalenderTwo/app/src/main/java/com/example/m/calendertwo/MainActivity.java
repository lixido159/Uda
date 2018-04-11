package com.example.m.calendertwo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.m.calendertwo.adapter.DateRecyclerAdapter;
import com.example.m.calendertwo.databinding.ActivityMainBinding;
import com.example.m.calendertwo.fragment.MainMonthFragment;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener
        , DateRecyclerAdapter.GapDaysCallBack{
    public static int mCount = 100;
    private ActivityMainBinding mBinding;
    private static MainActivity mInstance;
    private int mYear;
    private int mMonth;
    private int mDay;
    private TimeInfo mBeginTime;
    private TimeInfo mEndTime;
    private static MainItemCallBack mItemCallBack;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ////设置statusbar的图标颜色高亮反转
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getLocation(this);
        clickNewBuilding(mBinding.mainNewBuildingText, this);
        mBinding.mainMonthText.setSelected(true);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment, new MainMonthFragment())
                .commit();

        mInstance = this;
        DateInfo mDateInfo = DateInfo.getDate();
        mBinding.mainToolBar.inflateMenu(R.menu.tool_bar_menu);
        mBinding.mainToolBar.setOnMenuItemClickListener(this);
        mBinding.mainNowMonth.setText(String.format(getString(R.string.date_title), mDateInfo.getYear(), mDateInfo.getMonth()));
        mBeginTime = TimeInfo.getTimeInfo(TimeInfo.TYPE_BEGIN);
        mEndTime = TimeInfo.getTimeInfo(TimeInfo.TYPE_END);
        mYear = mBeginTime.getYear();
        mMonth = mBeginTime.getMonth();
        mDay = mBeginTime.getDay();
        MainMonthFragment.setOnMainFragmentListener(new MainMonthFragment.MainFragmentCallBack() {
            @Override
            public void selected(int position) {
                DateInfo dateInfo = DateInfo.getSpecialDate(position - MainActivity.mCount / 2);
                mBinding.mainNowMonth.setText(String.format(getString(R.string.date_title), dateInfo.getYear(), dateInfo.getMonth()));
            }
        });


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //点击右上角的今天,页面回到今天
        switch (item.getItemId()) {
            case R.id.menu_today:
                mItemCallBack.returnToday();
                Log.d("今天", "returnToday: ");
                break;
        }
        return false;
    }

    public void clickNewBuilding(TextView textView, final Activity activity) {
        textView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(getString(R.string.begin), mBeginTime);
                bundle1.putParcelable(getString(R.string.end), mEndTime);
                Intent intent = new Intent(v.getContext(), NewBuildingActivity.class);
                intent.setAction(getString(R.string.main));
                intent.putExtra(getString(R.string.intent_time), bundle1);
                startActivity(intent, bundle);
            }
        });
    }

    @Override
    public void updatePlan(int year, int month, int day, int days, String lunarMonth, String lunarDay) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mBeginTime.setYear(mYear);
        mBeginTime.setMonth(mMonth);
        mBeginTime.setDay(mDay);
        mEndTime.setYear(mYear);
        mEndTime.setMonth(mMonth);
        mEndTime.setDay(mDay);
    }

    public static DateRecyclerAdapter.GapDaysCallBack getInstance() {
        return mInstance;
    }



    public String getLocation(Context context) {
        Log.v("到了","111111111111111");

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        //List<String> lp = lm.getAllProviders();
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        //设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = lm.getBestProvider(criteria, true);

        if (providerName != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location location = lm.getLastKnownLocation(providerName);
            if(location!=null){
                //获取维度信息
                double latitude = location.getLatitude();
                //获取经度信息
                double longitude = location.getLongitude();
                return latitude+","+longitude;
            }
        }
        return "";
    }

    public interface MainItemCallBack{
               void returnToday();
           }
           public static void setOnMainActivityItemListener(MainItemCallBack callBack){
               mItemCallBack=callBack;
           }

}
