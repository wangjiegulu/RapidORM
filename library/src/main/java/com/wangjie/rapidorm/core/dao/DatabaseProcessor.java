package com.wangjie.rapidorm.core.dao;

import android.database.sqlite.SQLiteDatabase;
import com.wangjie.rapidorm.core.RapidORMDatabaseOpenHelper;
import com.wangjie.rapidorm.core.config.TableConfig;
import com.wangjie.rapidorm.exception.RapidORMRuntimeException;

import java.util.HashMap;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class DatabaseProcessor {
    private static DatabaseProcessor instance;

    public static DatabaseProcessor getInstance() {
        if (null == instance) {
            instance = new DatabaseProcessor();
        }
        return instance;
    }

    private HashMap<Class<?>, TableConfig> tableConfigMapper = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void createTable(SQLiteDatabase db, Class<T> clazz, boolean ifNotExists) {
        TableConfig<T> tableConfig = tableConfigMapper.get(clazz);
        if (null == tableConfig) {
            throw new RapidORMRuntimeException("tableConfigMapper not initialized, had you invoke super() method in the sub class of RapidORMConnection ?");
        }
        db.execSQL(tableConfig.getTableCreateStatement().buildStatement(ifNotExists).toString());
    }


    private RapidORMDatabaseOpenHelper rapidORMDatabaseOpenHelper;
    private SQLiteDatabase db;
    protected List<Class<?>> allTableClass;

    private DatabaseProcessor() {
    }

    /**
     * RapidORMConnection构造的时候就初始化好所有的tableClass
     *
     * @param allTableClass
     */
    @SuppressWarnings("unchecked")
    public void initializeAllTableClass(List<Class<?>> allTableClass) {
        this.allTableClass = allTableClass;
        for (Class<?> clazz : allTableClass) {
            tableConfigMapper.put(clazz, new TableConfig(clazz));
        }
    }

    public void resetRapidORMDatabaseOpenHelper(RapidORMDatabaseOpenHelper rapidORMDatabaseOpenHelper) {
        this.rapidORMDatabaseOpenHelper = rapidORMDatabaseOpenHelper;
    }

    public void initializeDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    public DatabaseProcessor(List<Class<?>> allTableClass) {
        this.allTableClass = allTableClass;
    }

    public synchronized SQLiteDatabase getDb() {
        if (null == db) {
            db = this.rapidORMDatabaseOpenHelper.getWritableDatabase();
        }
        return db;
    }

    @SuppressWarnings("unchecked")
    public <T> TableConfig<T> getTableConfig(Class<T> clazz) {
        return (TableConfig<T>) tableConfigMapper.get(clazz);
    }
}
