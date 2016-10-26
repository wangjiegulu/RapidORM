package com.wangjie.rapidorm.example.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.core.connection.RapidORMConnection;
import com.wangjie.rapidorm.core.dao.BaseDao;
import com.wangjie.rapidorm.core.delegate.openhelper.RapidORMDefaultSQLiteOpenHelperDelegate;
import com.wangjie.rapidorm.example.application.MyApplication;
import com.wangjie.rapidorm.example.database.model.Person;
import com.wangjie.rapidorm.example.database.model.Person_RORM;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class DatabaseFactory extends RapidORMConnection<RapidORMDefaultSQLiteOpenHelperDelegate> {
    private static final int VERSION = 1;
    private static final String TAG = DatabaseFactory.class.getSimpleName();

    private static DatabaseFactory instance;

    public synchronized static DatabaseFactory getInstance() {
        if (null == instance) {
            instance = new DatabaseFactory();
        }
        return instance;
    }

    private DatabaseFactory() {
        super();
    }

    @Override
    public boolean resetDatabase(@NonNull String databaseName) {
        return super.resetDatabase(databaseName);
    }

    @Override
    public boolean resetDatabaseIfCrash() {
        resetDatabase("hello_rapid_orm.db");
        return true;
    }

    @Override
    protected RapidORMDefaultSQLiteOpenHelperDelegate getRapidORMDatabaseOpenHelper(@NonNull String databaseName) {
        return new RapidORMDefaultSQLiteOpenHelperDelegate(new MyDatabaseOpenHelper(MyApplication.instance, databaseName, VERSION));
    }

    @Override
    protected void registerTableConfigMapper(HashMap<Class, TableConfig> tableConfigMapper) {
        tableConfigMapper.put(Person.class, new Person_RORM());
        // register all table config here...
    }

}
