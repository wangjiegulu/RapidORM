package com.wangjie.rapidorm.core.delegate.openhelper;

import android.database.sqlite.SQLiteOpenHelper;
import com.wangjie.rapidorm.core.delegate.database.RapidORMDefaultSQLiteDatabaseDelegate;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/18/15.
 */
public class RapidORMDefaultSQLiteOpenHelperDelegate extends RapidORMDatabaseOpenHelperDelegate<SQLiteOpenHelper, RapidORMDefaultSQLiteDatabaseDelegate> {

    public RapidORMDefaultSQLiteOpenHelperDelegate(SQLiteOpenHelper rapidORMDatabaseOpenHelper) {
        super(rapidORMDatabaseOpenHelper);
    }

    @Override
    public RapidORMDefaultSQLiteDatabaseDelegate getReadableDatabase() {
        return new RapidORMDefaultSQLiteDatabaseDelegate(openHelper.getReadableDatabase());
    }

    @Override
    public RapidORMDefaultSQLiteDatabaseDelegate getWritableDatabase() {
        return new RapidORMDefaultSQLiteDatabaseDelegate(openHelper.getWritableDatabase());
    }

//    @Override
//    public void onCreate(RapidORMDefaultSQLiteDatabaseDelegate db) {
//    }
//
//    @Override
//    public void onUpgrade(RapidORMDefaultSQLiteDatabaseDelegate db, int oldVersion, int newVersion) {
//
//    }
}
