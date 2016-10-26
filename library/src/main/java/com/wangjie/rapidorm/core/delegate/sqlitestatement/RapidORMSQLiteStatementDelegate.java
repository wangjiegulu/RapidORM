package com.wangjie.rapidorm.core.delegate.sqlitestatement;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.ParcelFileDescriptor;

/**
 * Created by wangjie on 10/21/16.
 */

public abstract class RapidORMSQLiteStatementDelegate {
    public abstract void execute();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public abstract int executeUpdateDelete();

    public abstract long executeInsert();

    public abstract long simpleQueryForLong();

    public abstract String simpleQueryForString();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public abstract ParcelFileDescriptor simpleQueryForBlobFileDescriptor();

    public abstract String toString();

    public abstract void bindNull(int index);

    public abstract void bindLong(int index, long value);

    public abstract void bindDouble(int index, double value);

    public abstract void bindString(int index, String value);

    public abstract void bindBlob(int index, byte[] value);

    public abstract void clearBindings();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public abstract void bindAllArgsAsStrings(String[] bindArgs);

}
