package com.wangjie.rapidorm.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.wangjie.rapidorm.core.RapidORMDatabaseOpenHelper;
import com.wangjie.rapidorm.core.dao.DatabaseProcessor;
import com.wangjie.rapidorm.example.database.model.Person;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class MyDatabaseOpenHelper extends RapidORMDatabaseOpenHelper {
    public MyDatabaseOpenHelper(Context context, String name, int version) {
        super(context, name, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        DatabaseProcessor.getInstance().createTable(db, Person.class, true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);


    }
}
