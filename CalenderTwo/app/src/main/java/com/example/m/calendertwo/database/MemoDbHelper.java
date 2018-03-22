package com.example.m.calendertwo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by m on 2018/3/14.
 */

public class MemoDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="memo.db";

    public MemoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+MemoContract.TABLE_NAME+"("
                +MemoContract._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +MemoContract.COLUMN_PLAN+" TEXT,"
                +MemoContract.COLUMN_ICON_IMG+" INTEGER,"
                +MemoContract.COLUMN_ICON_BACK+" INTEGER,"
                +MemoContract.COLUMN_BEGIN_TIME_YEAR+" INTEGER,"
                +MemoContract.COLUMN_BEGIN_TIME_MONTH+" INTEGER,"
                +MemoContract.COLUMN_BEGIN_TIME_DAY+" INTEGER,"
                +MemoContract.COLUMN_BEGIN_TIME_HOUR+" INTEGER,"
                +MemoContract.COLUMN_BEGIN_TIME_MINUTE+" INTEGER,"
                +MemoContract.COLUMN_END_TIME_YEAR+" INTEGER,"
                +MemoContract.COLUMN_END_TIME_MONTH+" INTEGER,"
                +MemoContract.COLUMN_END_TIME_DAY+" INTEGER,"
                +MemoContract.COLUMN_END_TIME_HOUR+" INTEGER,"
                +MemoContract.COLUMN_END_TIME_MINUTE+" INTEGER);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
