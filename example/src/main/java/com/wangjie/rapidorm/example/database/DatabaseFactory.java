package com.wangjie.rapidorm.example.database;

import android.support.annotation.NonNull;
import com.wangjie.rapidorm.core.RapidORMConnection;
import com.wangjie.rapidorm.core.delegate.openhelper.RapidORMDefaultSQLiteOpenHelperDelegate;
import com.wangjie.rapidorm.example.application.MyApplication;
import com.wangjie.rapidorm.example.database.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class DatabaseFactory extends RapidORMConnection<RapidORMDefaultSQLiteOpenHelperDelegate> {
    private static final int VERSION = 1;

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
    protected RapidORMDefaultSQLiteOpenHelperDelegate getRapidORMDatabaseOpenHelper(@NonNull String databaseName) {
        return new RapidORMDefaultSQLiteOpenHelperDelegate(new MyDatabaseOpenHelper(MyApplication.getInstance(), databaseName, VERSION));
    }

    @Override
    protected List<Class<?>> registerAllTableClass() {
        List<Class<?>> allTableClass = new ArrayList<>();
        allTableClass.add(Person.class);
        // all table class
        return allTableClass;
    }
}
