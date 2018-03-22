package com.example.m.calendertwo.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by m on 2018/3/14.
 */

public class MemoProvider extends ContentProvider {

    private static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    private static final int SINGLE_ROW=1;
    private static final int MULTI_ROW=2;

    private MemoDbHelper mHelper;

    static {
        matcher.addURI(MemoContract.CONTENT_AUTHORITY,MemoContract.CONTENT_PATH_MEMO,MULTI_ROW);
        matcher.addURI(MemoContract.CONTENT_AUTHORITY,MemoContract.CONTENT_PATH_MEMO+"/#",SINGLE_ROW);

    }
    @Override
    public boolean onCreate() {
        mHelper=new MemoDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int match=matcher.match(uri);
        if (match!=SINGLE_ROW&&match!=MULTI_ROW){
            throw new IllegalArgumentException("查询数据库,Uri参数有问题!");
        }
        SQLiteDatabase database=mHelper.getReadableDatabase();
        cursor=database.query(MemoContract.TABLE_NAME,projection,selection,selectionArgs,
                null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database=mHelper.getWritableDatabase();
        int match=matcher.match(uri);
        if (match!=SINGLE_ROW&&match!=MULTI_ROW){
            throw new IllegalArgumentException("插入数据库,Uri参数有问题!");
        }
        long id=database.insert(MemoContract.TABLE_NAME,null,values);
        if (id>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database=mHelper.getWritableDatabase();
        int match=matcher.match(uri);
        if (match!=SINGLE_ROW&&match!=MULTI_ROW){
            throw new IllegalArgumentException("删除数据库,Uri参数有问题!");
        }
        int count= database.delete(MemoContract.TABLE_NAME,selection,selectionArgs);
        if (count!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database=mHelper.getWritableDatabase();
        int match=matcher.match(uri);
        if (match!=SINGLE_ROW&&match!=MULTI_ROW){
            throw new IllegalArgumentException("更新数据库,Uri参数有问题!");
        }
        int count=database.update(MemoContract.TABLE_NAME,values,selection,selectionArgs);
        if (count!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }
}
