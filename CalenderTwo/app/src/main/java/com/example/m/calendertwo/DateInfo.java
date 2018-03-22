package com.example.m.calendertwo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by m on 2018/3/11.
 */

public class DateInfo implements Parcelable{


    private int mDay;//一个月的几号
    private int mMonth;//月
    private int mYear;//年
    private int mFirstDayOfMonth;//这个月开头是星期几
    private int mDayOfYear;//今天是这一年的多少天
    public DateInfo(int mDay, int mMonth, int mYear,int mFirstDayOfMonth,int mDayOfYear) {
        this.mDay = mDay;
        this.mMonth = mMonth;
        this.mYear = mYear;
        this.mFirstDayOfMonth=mFirstDayOfMonth;
        this.mDayOfYear=mDayOfYear;
    }

    protected DateInfo(Parcel in) {
        mDay = in.readInt();
        mMonth = in.readInt();
        mYear = in.readInt();
        mFirstDayOfMonth = in.readInt();
    }

    public static final Creator<DateInfo> CREATOR = new Creator<DateInfo>() {
        @Override
        public DateInfo createFromParcel(Parcel in) {
            return new DateInfo(in);
        }

        @Override
        public DateInfo[] newArray(int size) {
            return new DateInfo[size];
        }
    };

    public int getDay() {
        return mDay;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getFirstDayOfMonth() {
        return mFirstDayOfMonth;
    }

    public int getYear() {
        return mYear;
    }

    public int getDayOfYear() {
        return mDayOfYear;
    }

    //获取当前日期信息
    public static DateInfo getDate(){
        Calendar calendar=Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT+08");
        calendar.setTimeZone(tz);
        DateInfo info=new DateInfo(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.YEAR),
                getFistDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK)-1),
                calendar.get(Calendar.DAY_OF_YEAR));
        return info;
    }
    //这个月开头是星期几
    public static int getFistDayOfMonth(int nowDay,int nowDayOfWeek){
        nowDayOfWeek=nowDayOfWeek-nowDay+1;
        while (nowDayOfWeek<0){
            nowDayOfWeek+=7;
        }
        return nowDayOfWeek;
    }
    //获取与今天相差gap月的日期
    public static DateInfo getSpecialDate(int gap){
        Calendar calendar=Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT+08");
        calendar.setTimeZone(tz);
        calendar.add(Calendar.MONTH,gap);
        DateInfo info=new DateInfo(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.YEAR),
                getFistDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.DAY_OF_WEEK)-1),
                calendar.get(Calendar.DAY_OF_YEAR));
        return info;
    }
    public static String getWeekDay(int year,int month,int day){
        Calendar calendar=Calendar.getInstance();
        DateInfo dateInfo=getDate();
        calendar.add(Calendar.YEAR,year-dateInfo.getYear());
        calendar.add(Calendar.MONTH,month-dateInfo.getMonth());
        calendar.add(Calendar.DAY_OF_MONTH,day-dateInfo.getDay());
        Log.v("检测:",year+ " "+month+" "+day);
        switch (calendar.get(Calendar.DAY_OF_WEEK)){

            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "日";
        }
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDay);
        dest.writeInt(mMonth);
        dest.writeInt(mYear);
        dest.writeInt(mFirstDayOfMonth);
    }
    //判断两个日期是否相等
    public static boolean isEquale(DateInfo dateInfo1,DateInfo dateInfo2){
        if (dateInfo1.getYear()==dateInfo2.getYear()&&
                dateInfo1.getMonth()==dateInfo2.getMonth()&&
                dateInfo1.getDay()==dateInfo2.getDay()){
            return true;
        }
        return false;
    }
    //判断该月有几天
    public static int getDays(DateInfo dateInfo){
        if (dateInfo.getYear()%4==0){//闰年
            if(dateInfo.getMonth()==2)
            {
                return 29;
            }
        }else {//平年
            if(dateInfo.getMonth()==2)
            {
                return 28;
            }
        }
        switch (dateInfo.getMonth()){
            case 1:
                return 31;
            case 3:
                return 31;
            case 5:
                return 31;
            case 7:
                return 31;
            case 8:
                return 31;
            case 10:
                return 31;
            case 12:
                return 31;
            case 4:
                return 30;
            case 6:
                return 30;
            case 9:
                return 30;
            case 11:
                return 30;
            case 2:
                return 29;
        }
        return 0;
    }

    public static int getGapDays(DateInfo nowTime,DateInfo clickTime){
        if (nowTime.getYear()==clickTime.getYear()){
            return clickTime.getDayOfYear()-nowTime.getDayOfYear();
        }
        else if(clickTime.getYear()-nowTime.getYear()>0){//如果点击的年份大于当前的年
            int years=0;
            for (int i=nowTime.getYear();i<clickTime.getYear();i++){
                if(isLeapYear(i)){
                    years+=366;
                }
                else {
                    years+=365;
                }
            }
            return years+clickTime.getDayOfYear()-nowTime.getDayOfYear();
        }else{
            int years=0;
            for (int i=clickTime.getYear();i<nowTime.getYear();i++){
                if(isLeapYear(i)){
                    years+=366;
                }
                else {
                    years+=365;
                }
            }
            return -(years+nowTime.getDayOfYear()-clickTime.getDayOfYear());
        }
    }
    //判断是否是闰年
    public static boolean isLeapYear(int year){
        boolean isLeap;
        return  isLeap=year%4==0?true:false;
    }
    //将数据库内储存的时间转化为正确格式
    public static String parseTime(int time){
        String strTime=String.valueOf(time);
        return strTime.substring(0,2)+":"+strTime.substring(2);
    }
    //数据库查询
    public static String getDate(DateInfo dateInfo){
        String date=dateInfo.getYear()+dateInfo.getMonth()+dateInfo.getDay()+"";
        return date;
    }

    public static List<Integer> arrayToList(int[]array){
        List<Integer>list=new ArrayList<>();
        for (int i=0;i<array.length;i++){
            list.add(array[i]);
        }
        return list;
    }
}
