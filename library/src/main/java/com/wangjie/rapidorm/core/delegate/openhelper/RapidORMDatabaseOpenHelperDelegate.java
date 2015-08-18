package com.wangjie.rapidorm.core.delegate.openhelper;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public abstract class RapidORMDatabaseOpenHelperDelegate<RapidORMDatabaseOpenHelper, SQLiteDatabaseDelegate> {
    protected RapidORMDatabaseOpenHelper openHelper;

    public RapidORMDatabaseOpenHelperDelegate(RapidORMDatabaseOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    public abstract SQLiteDatabaseDelegate getReadableDatabase();

    public abstract SQLiteDatabaseDelegate getWritableDatabase();

//    public abstract void onCreate(SQLiteDatabase db);
//
//    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
