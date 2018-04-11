package com.example.m.calendertwo.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by m on 2018/3/14.
 */

public class MemoContract implements BaseColumns {
    public static final String TABLE_NAME="memos";
    public static final String COLUMN_PLAN="plan";

    public static final String COLUMN_BEGIN_TIME_YEAR="beginyear";
    public static final String COLUMN_BEGIN_TIME_MONTH="beginmonth";
    public static final String COLUMN_BEGIN_TIME_DAY="beginday";
    public static final String COLUMN_BEGIN_TIME_HOUR="beginhour";
    public static final String COLUMN_BEGIN_TIME_MINUTE="beginminute";

    public static final String COLUMN_END_TIME_YEAR="endyear";
    public static final String COLUMN_END_TIME_MONTH="endmonth";
    public static final String COLUMN_END_TIME_DAY="endday";
    public static final String COLUMN_END_TIME_HOUR="endhour";
    public static final String COLUMN_END_TIME_MINUTE="endminute";



    public static final String CONTENT_AUTHORITY="com.example.m.calendertwo";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String CONTENT_PATH_MEMO="memo";
    public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,CONTENT_PATH_MEMO);

    public static final int INDEX_ID=0;
    public static final int INDEX_PLAN=1;
    public static final int INDEX_BEGIN_TIME_YEAR=2;
    public static final int INDEX_BEGIN_TIME_MONTH=3;
    public static final int INDEX_BEGIN_TIME_DAY=4;
    public static final int INDEX_BEGIN_TIME_HOUR=5;
    public static final int INDEX_BEGIN_TIME_MINUTE=6;

    public static final int INDEX_END_TIME_YEAR=7;
    public static final int INDEX_END_TIME_MONTH=8;
    public static final int INDEX_END_TIME_DAY=9;
    public static final int INDEX_END_TIME_HOUR=10;
    public static final int INDEX_END_TIME_MINUTE=11;
}
