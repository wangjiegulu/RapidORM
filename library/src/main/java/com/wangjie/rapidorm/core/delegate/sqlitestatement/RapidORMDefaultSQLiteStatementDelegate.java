package com.wangjie.rapidorm.core.delegate.sqlitestatement;

import android.annotation.TargetApi;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.ParcelFileDescriptor;

/**
 * Created by wangjie on 10/21/16.
 */

public class RapidORMDefaultSQLiteStatementDelegate extends RapidORMSQLiteStatementDelegate {
    private SQLiteStatement statement;

    public RapidORMDefaultSQLiteStatementDelegate(SQLiteStatement statement) {
        this.statement = statement;
    }

    @Override
    public void execute() {
        statement.execute();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public int executeUpdateDelete() {
        return statement.executeUpdateDelete();
    }

    @Override
    public long executeInsert() {
        return statement.executeInsert();
    }

    @Override
    public long simpleQueryForLong() {
        return statement.simpleQueryForLong();
    }

    @Override
    public String simpleQueryForString() {
        return statement.simpleQueryForString();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public ParcelFileDescriptor simpleQueryForBlobFileDescriptor() {
        return statement.simpleQueryForBlobFileDescriptor();
    }

    @Override
    public String toString() {
        return statement.toString();
    }

    @Override
    public void bindNull(int index) {
        statement.bindNull(index);
    }

    @Override
    public void bindLong(int index, long value) {
        statement.bindLong(index, value);
    }

    @Override
    public void bindDouble(int index, double value) {
        statement.bindDouble(index, value);
    }

    @Override
    public void bindString(int index, String value) {
        statement.bindString(index, value);
    }

    @Override
    public void bindBlob(int index, byte[] value) {
        statement.bindBlob(index, value);
    }

    @Override
    public void clearBindings() {
        statement.clearBindings();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void bindAllArgsAsStrings(String[] bindArgs) {
        statement.bindAllArgsAsStrings(bindArgs);
    }
}
