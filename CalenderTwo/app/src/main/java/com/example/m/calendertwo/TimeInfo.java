package com.example.m.calendertwo;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by m on 2018/3/19.
 */

public class TimeInfo implements Parcelable {
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mType;
    public static final int TYPE_BEGIN=1;
    public static final int TYPE_END=2;
    protected TimeInfo(Parcel in) {
        mYear = in.readInt();
        mMonth = in.readInt();
        mDay = in.readInt();
        mHour = in.readInt();
        mMinute = in.readInt();
        mType = in.readInt();
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }

    public int getType() {
        return mType;
    }

    public TimeInfo(int mYear, int mMonth, int mDay, int mHour, int mMinute, int mType) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mHour = mHour;
        this.mMinute = mMinute;
        this.mType = mType;
    }

    public void setYear(int mYear) {
        this.mYear = mYear;
    }

    public void setMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public void setDay(int mDay) {
        this.mDay = mDay;
    }

    public static TimeInfo getTimeInfo(int type){
        Calendar calendar=Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT+08");
        calendar.setTimeZone(tz);

        if (type==TYPE_BEGIN){
            return new TimeInfo(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),type);
        }else {
            int hour;
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            if ((hour=calendar.get(Calendar.HOUR_OF_DAY)+1)==24){
                hour=0;
                day++;
            }
            return new TimeInfo(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,
                    day,hour, calendar.get(Calendar.MINUTE),type);
        }
    }
    public static boolean isAtSameDay(TimeInfo timeInfo1,TimeInfo timeInfo2){
        if (timeInfo1.getDay()==timeInfo2.getDay()&&
                timeInfo1.getMonth()==timeInfo2.getMonth()&&
                timeInfo1.getYear()==timeInfo2.getYear()){
            return true;
        }
        return false;
    }
    public static boolean isSmaller(TimeInfo beginTime,TimeInfo endTime){
        DecimalFormat format=new DecimalFormat("00");
        long num1=Long.valueOf(beginTime.getYear()+format.format(beginTime.getMonth())+
                format.format(beginTime.getDay())+format.format(beginTime.getHour())+format.format(beginTime.getMinute()));
        long num2=Long.valueOf(endTime.getYear()+format.format(endTime.getMonth())+
                format.format(endTime.getDay())+format.format(endTime.getHour())+format.format(endTime.getMinute()));


        if (num2<num1){
            return true;
        }
        return false;
    }

    public static long dateToStamp(TimeInfo timeInfo, Context context)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time= String.format(context.getString(R.string.timeStamp),
                timeInfo.getYear(),timeInfo.getMonth(),timeInfo.getDay(),timeInfo.getHour(),timeInfo.getMinute());
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();

    }

    public static final Creator<TimeInfo> CREATOR = new Creator<TimeInfo>() {
        @Override
        public TimeInfo createFromParcel(Parcel in) {
            return new TimeInfo(in);
        }

        @Override
        public TimeInfo[] newArray(int size) {
            return new TimeInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mYear);
        dest.writeInt(mMonth);
        dest.writeInt(mDay);
        dest.writeInt(mHour);
        dest.writeInt(mMinute);
        dest.writeInt(mType);
    }
}
