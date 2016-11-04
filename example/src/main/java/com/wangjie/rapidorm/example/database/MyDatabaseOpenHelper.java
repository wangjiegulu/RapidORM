package com.wangjie.rapidorm.example.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.wangjie.rapidorm.core.connection.DatabaseProcessor;
import com.wangjie.rapidorm.core.delegate.database.RapidORMDefaultSQLiteDatabaseDelegate;
import com.wangjie.rapidorm.core.delegate.database.RapidORMSQLiteDatabaseDelegate;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public MyDatabaseOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    private RapidORMSQLiteDatabaseDelegate rapidORMSQLiteDatabaseDelegate;

    @Override
    public void onCreate(SQLiteDatabase db) {
        rapidORMSQLiteDatabaseDelegate = new RapidORMDefaultSQLiteDatabaseDelegate(db);
        DatabaseProcessor databaseProcessor = DatabaseProcessor.getInstance();
        databaseProcessor.initializeDatabase(rapidORMSQLiteDatabaseDelegate);

        DatabaseProcessor.getInstance().createAllTable(rapidORMSQLiteDatabaseDelegate, true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        rapidORMSQLiteDatabaseDelegate = new RapidORMDefaultSQLiteDatabaseDelegate(db);
        DatabaseProcessor databaseProcessor = DatabaseProcessor.getInstance();
        databaseProcessor.initializeDatabase(rapidORMSQLiteDatabaseDelegate);

    }
}
