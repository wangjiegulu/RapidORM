package com.wangjie.rapidorm.core.delegate.database;

import android.database.Cursor;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 8/17/15.
 */
public abstract class RapidORMSQLiteDatabaseDelegate<SQLiteDatabase> {
    protected SQLiteDatabase db;

    public SQLiteDatabase getDb() {
        return db;
    }

    public RapidORMSQLiteDatabaseDelegate(SQLiteDatabase db) {
        this.db = db;
    }

    public abstract void execSQL(String sql) throws Exception;

    public abstract boolean isDbLockedByCurrentThread();

    public abstract void execSQL(String sql, Object[] bindArgs) throws Exception;

    public abstract Cursor rawQuery(String sql, String[] selectionArgs);

    public abstract void beginTransaction();

    public abstract void setTransactionSuccessful();

    public abstract void endTransaction();

    public abstract void close();

}

