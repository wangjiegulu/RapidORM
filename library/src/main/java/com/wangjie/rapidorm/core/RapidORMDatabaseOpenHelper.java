package com.wangjie.rapidorm.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.wangjie.rapidorm.core.dao.DatabaseProcessor;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public abstract class RapidORMDatabaseOpenHelper extends SQLiteOpenHelper {

    public RapidORMDatabaseOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public RapidORMDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RapidORMDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseProcessor.getInstance().initializeDatabase(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatabaseProcessor.getInstance().initializeDatabase(db);


    }


}
