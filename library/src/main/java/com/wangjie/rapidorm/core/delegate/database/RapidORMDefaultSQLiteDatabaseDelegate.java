package com.wangjie.rapidorm.core.delegate.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 8/17/15.
 */
public class RapidORMDefaultSQLiteDatabaseDelegate extends RapidORMSQLiteDatabaseDelegate<SQLiteDatabase> {
    public RapidORMDefaultSQLiteDatabaseDelegate(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }

    @Override
    public void execSQL(String sql) throws SQLException {
        db.execSQL(sql);
    }

    @Override
    public boolean isDbLockedByCurrentThread() {
        return db.isDbLockedByCurrentThread();
    }

    @Override
    public void execSQL(String sql, Object[] bindArgs) throws Exception {
        db.execSQL(sql, bindArgs);
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    @Override
    public void beginTransaction() {
        db.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        db.endTransaction();
    }
}
